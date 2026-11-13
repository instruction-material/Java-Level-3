"""Compile exact DPR4 roles, preserve the baseline and reject controlled regressions."""
from pathlib import Path
import datetime
import hashlib
import json
import os
import shutil
import signal
import subprocess
import time

ROOT = Path(__file__).resolve().parents[1]
PACK = ROOT / "DPR4-Value-Data-Packs"
OUT = ROOT / "output/java-value-data-contracts"
OUT.mkdir(parents=True, exist_ok=True)
LOG = OUT / "processes.jsonl"
JDK = Path(os.environ["JAVA_HOME"])
JAVA, JAVAC = str(JDK / "bin/java"), str(JDK / "bin/javac")
DEMO = ("LAB|1|PEN:2;PAD:1;PEN:3\nLAB dispatch=60 lines=3 units=6\n"
        "equal=true\nhash-set=true\nunchanged=LAB|1|PEN:2;PAD:1;PEN:3\n")

def run(args, cwd):
    started = datetime.datetime.now(datetime.timezone.utc).isoformat()
    child = subprocess.Popen(args, cwd=cwd, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                             text=True, start_new_session=True)
    event = dict(parentTask=os.environ.get("CLASSES_FAMILY_TASK_ID", "java-value-data-contracts"),
                 cwd=str(cwd), command=args, pid=child.pid, parentPid=os.getpid(),
                 startedAt=started, timeoutSeconds=45)
    try:
        stdout, stderr = child.communicate(timeout=45)
    except BaseException:
        os.killpg(child.pid, signal.SIGKILL)
        stdout, stderr = child.communicate()
        event.update(endedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),
                     exitCode=child.returncode, timedOut=True, childProcessGroupCleanup="killed")
        with LOG.open("a") as f:
            f.write(json.dumps(event) + "\n")
        raise
    event.update(endedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),
                 exitCode=child.returncode, timedOut=False,
                 childProcessGroupCleanup="process group exited", stdout=stdout, stderr=stderr)
    with LOG.open("a") as f:
        f.write(json.dumps(event) + "\n")
    return subprocess.CompletedProcess(args, child.returncode, stdout, stderr)

def copy_role(role, name):
    destination = OUT / name
    assert not destination.exists(), destination
    shutil.copytree(PACK / role, destination)
    return destination

def compile_role(folder):
    result = run([JAVAC, "--release", "21", "-Xlint:all", "-Werror", "-d", "classes", "@sources.txt"], folder)
    assert result.returncode == 0, result

def contracts(folder, mode, expected=None, failure=None):
    result = run([JAVA, "-cp", "classes", "ContractTests", mode], folder)
    if expected is not None:
        assert result.returncode == 0 and result.stdout == expected, result
    else:
        assert result.returncode != 0 and failure in result.stderr, result
    return dict(mode=mode, returncode=result.returncode, stdout=result.stdout, stderr=result.stderr)

def verify():
    expected_files = sorted(str(p.relative_to(PACK / "solution"))
                            for p in (PACK / "solution").rglob("*") if p.is_file())
    assert len(expected_files) == 13
    assert sorted(str(p.relative_to(PACK / "starter"))
                  for p in (PACK / "starter").rglob("*") if p.is_file()) == expected_files
    for name in ["ClientCompatibility.java", "src/supplies/LegacySupplyRequest.java"]:
        assert (PACK / "starter" / name).read_bytes() == (PACK / "solution" / name).read_bytes()
    proof = {"roles": [], "regressions": [], "jdk": run([JAVAC, "-version"], ROOT).stdout}
    for role in ["starter", "solution"]:
        folder = copy_role(role, role)
        compile_role(folder)
        row = {"role": role, "sourceHashes": {
            name: hashlib.sha256((PACK / role / name).read_bytes()).hexdigest() for name in expected_files
        }}
        row["base"] = contracts(folder, "base", "base data contracts: 1673 passed\n")
        if role == "starter":
            row["core"] = contracts(folder, "core", failure="Expected IllegalArgumentException: code")
        else:
            row["core"] = contracts(folder, "core", "core data contracts: 1704 passed\n")
            row["all"] = contracts(folder, "all", "all data contracts: 1709 passed\n")
        for args in [[], ["base"]]:
            result = run([JAVA, "-cp", "classes", "Main", *args], folder)
            assert result.returncode == 0 and result.stdout == DEMO, result
        proof["roles"].append(row)
    intermediate = copy_role("starter", "core-complete-optional-open")
    for name in ["ItemCode.java", "RequestLine.java", "SupplyRequest.java", "RequestCodec.java"]:
        text = (PACK / "solution/src/supplies" / name).read_text()
        if name == "SupplyRequest.java":
            start = text.index("    public SupplyRequest merged() {")
            text = text[:start] + ('    public SupplyRequest merged() {\n'
                '        throw new UnsupportedOperationException("Implement optional merge.");\n'
                '    }\n}\n')
        (intermediate / "src/supplies" / name).write_text(text)
    compile_role(intermediate)
    contracts(intermediate, "base", "base data contracts: 1673 passed\n")
    contracts(intermediate, "core", "core data contracts: 1704 passed\n")
    contracts(intermediate, "all", failure="Implement optional merge.")
    mutations = [
        ("live-list", "SupplyRequest.java", "this.lines = List.copyOf(lines);",
         "this.lines = lines;", "snapshot versus live view"),
        ("unordered-value", "SupplyRequest.java", "lines.equals(request.lines)",
         "new java.util.HashSet<>(lines).equals(new java.util.HashSet<>(request.lines))",
         "order is part of value"),
        ("empty-wire-body", "RequestCodec.java", 'row.split("\\\\|", -1)',
         'row.split("\\\\|")', "IllegalArgumentException: row")
    ]
    for name, filename, before, after, failure in mutations:
        folder = copy_role("solution", "regression-" + name)
        file = folder / "src/supplies" / filename
        text = file.read_text()
        assert text.count(before) == 1, (name, before)
        file.write_text(text.replace(before, after))
        compile_role(folder)
        result = contracts(folder, "core", failure=failure)
        proof["regressions"].append({"name": name, "expectedRejection": result})
    proof["accepted"] = True
    proof["scope"] = "DPR4 exact native roles, unchanged client/baseline, core before optional and three rejected regressions"
    (OUT / "native-proof.json").write_text(json.dumps(proof, indent=2) + "\n")
    print("DPR4 native roles, independent core and three controlled regressions passed")

if __name__ == "__main__":
    verify()
