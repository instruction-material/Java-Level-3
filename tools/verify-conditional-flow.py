"""Verify DPR5 roles, optional separation and behavior-preserving refactoring."""
from pathlib import Path
import datetime
import hashlib
import json
import os
import shutil
import signal
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]
PACK = ROOT / "DPR5-Conditional-Flow-Packs"
OUT = ROOT / "output/java-conditional-flow-contracts"
OUT.mkdir(parents=True, exist_ok=True)
WORK = Path(tempfile.mkdtemp(prefix="run-", dir=OUT))
LOG = WORK / "processes.jsonl"
JDK = Path(os.environ["JAVA_HOME"])
JAVA, JAVAC = str(JDK / "bin/java"), str(JDK / "bin/javac")
DEMO = ("approved:60\n[training:2>=2, availability:busy=false, decision:approved]\n"
        "busy:0\n[availability:busy=true, decision:busy]\n")
CORE = "rows=144000 checks=576017\n"
OPTIONAL = "workflow-rows=720 checks=2899\n"
RECORDS = []


def run(args, cwd):
    started = datetime.datetime.now(datetime.timezone.utc).isoformat()
    child = subprocess.Popen(args, cwd=cwd, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                             text=True, start_new_session=True)
    event = dict(parentTask=os.environ.get("CLASSES_FAMILY_TASK_ID", "java-conditional-flow"),
                 cwd=str(cwd), command=args, pid=child.pid, parentPid=os.getpid(),
                 startedAt=started, timeoutSeconds=45)
    with LOG.open("a") as stream:
        stream.write(json.dumps(dict(event, event="start")) + "\n")
    try:
        stdout, stderr = child.communicate(timeout=45)
    except BaseException:
        try:
            os.killpg(child.pid, signal.SIGKILL)
        except ProcessLookupError:
            pass
        stdout, stderr = child.communicate()
        event.update(event="end", endedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),
                     exitCode=child.returncode, timedOut=True,
                     childProcessGroupCleanup="terminated owned group",
                     stdout=stdout, stderr=stderr)
        with LOG.open("a") as stream:
            stream.write(json.dumps(event) + "\n")
        raise
    event.update(event="end", endedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),
                 exitCode=child.returncode, timedOut=False,
                 childProcessGroupCleanup="owned command exited", stdout=stdout, stderr=stderr)
    with LOG.open("a") as stream:
        stream.write(json.dumps(event) + "\n")
    result = dict(command=args, returncode=child.returncode, stdout=stdout, stderr=stderr)
    RECORDS.append(result)
    return result


def success(result, expected=None):
    assert result["returncode"] == 0, result
    if expected is not None:
        assert result["stdout"] == expected, result
    return result


def rejected(result, token):
    assert result["returncode"] != 0 and token in result["stderr"], result
    return result


def copy_role(role, name):
    destination = WORK / name
    shutil.copytree(PACK / role, destination)
    return destination


def compile_role(folder, isolated=None):
    empty = folder / "empty-source-and-class-path"
    empty.mkdir(exist_ok=True)
    names = (folder / "sources.txt").read_text().splitlines() if isolated is None else isolated
    args = [JAVAC, "--release", "21", "-encoding", "UTF-8", "-Xlint:all", "-Werror",
            "-proc:none", "--source-path", str(empty), "--class-path", str(empty),
            "-d", "classes", *names]
    return run(args, folder)


def execute(folder, class_name, mode=None, assertions=True):
    return run([JAVA, "-Xmx128m", "-ea" if assertions else "-da", "-cp", "classes",
                class_name, *([] if mode is None else [mode])], folder)


def mutate(folder, name, before, after):
    file = folder / name
    text = file.read_text()
    assert text.count(before) == 1, (name, before)
    file.write_text(text.replace(before, after))


def verify():
    files = sorted(p.relative_to(PACK / "solution").as_posix()
                   for p in (PACK / "solution").rglob("*") if p.is_file())
    assert len(files) == 18
    assert sum(name.endswith(".java") for name in files) == 15
    assert sorted(p.relative_to(PACK / "starter").as_posix()
                  for p in (PACK / "starter").rglob("*") if p.is_file()) == files
    editable = {"src/lab/GatePolicy.java", "src/lab/optional/FeePolicies.java",
                "src/lab/optional/OptionalWorkflow.java", "REFACTORING.md"}
    for name in set(files) - editable:
        assert (PACK / "starter" / name).read_bytes() == (PACK / "solution" / name).read_bytes(), name
    for role in ["starter", "solution"]:
        source_names = (PACK / role / "sources.txt").read_text().splitlines()
        assert len(source_names) == len(set(source_names)) == 15
        assert set(source_names) == {name for name in files if name.endswith(".java")}
    proof = dict(roles=[], intermediate={}, regressions=[], isolated=[],
                 jdk=success(run([JAVAC, "-version"], ROOT))["stdout"],
                 sourceCommit=success(run(["git", "rev-parse", "HEAD"], ROOT))["stdout"].strip())
    for role in ["starter", "solution"]:
        folder = copy_role(role, role)
        success(compile_role(folder))
        row = dict(role=role, sourceHashes={
            name: hashlib.sha256((PACK / role / name).read_bytes()).hexdigest()
            for name in files})
        row["baseline"] = success(execute(folder, "lab.CoreChecks", "baseline"), "baseline " + CORE)
        row["baselineOptional"] = success(
            execute(folder, "lab.optional.OptionalChecks", "baseline"), "baseline " + OPTIONAL)
        for mode in [None, "baseline"]:
            success(execute(folder, "lab.CoreDemo", mode), DEMO)
        row["assertions"] = []
        for enabled in [True, False]:
            row["assertions"].append(success(
                execute(folder, "lab.CoreChecks", "assertions", enabled),
                "assertions=" + ("on" if enabled else "off") + " public-validation=active\n"))
        if role == "starter":
            row["core"] = rejected(execute(folder, "lab.CoreChecks", "core"), "TODO evaluate")
            row["optional"] = rejected(execute(folder, "lab.optional.OptionalChecks", "full"), "TODO")
        else:
            row["core"] = [success(execute(folder, "lab.CoreChecks", "core", enabled),
                                   "core " + CORE) for enabled in [True, False]]
            row["optional"] = success(execute(folder, "lab.optional.OptionalChecks", "full"), "full " + OPTIONAL)
            success(execute(folder, "lab.CoreDemo", "core"), DEMO)
            success(execute(folder, "lab.optional.OptionalDemo"), "approved credits=126\n")
        proof["roles"].append(row)
    folder = copy_role("starter", "core-complete-optional-open")
    target = "src/lab/GatePolicy.java"
    shutil.copyfile(PACK / "solution" / target, folder / target)
    success(compile_role(folder))
    proof["intermediate"]["core"] = success(execute(folder, "lab.CoreChecks", "core"), "core " + CORE)
    proof["intermediate"]["optional"] = rejected(
        execute(folder, "lab.optional.OptionalChecks", "full"), "TODO")
    core_sources = ["src/lab/" + name + ".java" for name in [
        "BookingData", "AdmissionPolicy", "AuditTrail", "Inputs", "GatePolicy", "BookingClient"]]
    optional_sources = core_sources + ["src/lab/optional/" + name + ".java" for name in [
        "StatusReporter", "FeePolicies", "OptionalWorkflow"]]
    for name, sources in [("required-only", core_sources), ("optional-production-only", optional_sources)]:
        folder = copy_role("solution", name)
        proof["isolated"].append(dict(name=name, result=success(compile_role(folder, sources))))
    mutations = [
        ("inactive-or-to-and", "src/lab/GatePolicy.java",
         "member.suspended() || member.revoked()", "member.suspended() && member.revoked()",
         "decision row"),
        ("training-before-supervision", "src/lab/GatePolicy.java",
         "request.supervised() || audit.training(request.member().trainingLevel(),\n                request.machine().requiredTraining())",
         "audit.training(request.member().trainingLevel(),\n                request.machine().requiredTraining()) || request.supervised()",
         "probe/decision trace row"),
        ("duplicate-final-event", "src/lab/GatePolicy.java", "audit.decision(result);",
         "audit.decision(result); audit.decision(result);", "probe/decision trace row"),
    ]
    for name, path, before, after, reason in mutations:
        folder = copy_role("solution", "regression-" + name)
        mutate(folder, path, before, after)
        success(compile_role(folder))
        result = rejected(execute(folder, "lab.CoreChecks", "core"), reason)
        proof["regressions"].append(dict(name=name, result=result))
    folder = copy_role("solution", "regression-absent-reporter")
    mutate(folder, "src/lab/optional/OptionalWorkflow.java",
           "reporter == null ? StatusReporter.none() : reporter", "reporter")
    success(compile_role(folder))
    success(execute(folder, "lab.CoreChecks", "core"), "core " + CORE)
    result = rejected(execute(folder, "lab.optional.OptionalChecks", "full"), "NullPointerException")
    proof["regressions"].append(dict(name="absent-reporter", result=result))
    folder = copy_role("solution", "regression-baseline-delegation")
    (folder / "src/lab/GatePolicy.java").write_text(
        'package lab; import lab.BookingData.*;\n'
        'public final class GatePolicy implements AdmissionPolicy {\n'
        ' public Decision evaluate(Request request, AuditTrail audit) {\n'
        '  return new LegacyGate().evaluate(request,audit);\n }\n}\n')
    result = rejected(compile_role(folder, core_sources), "LegacyGate")
    proof["regressions"].append(dict(name="baseline-delegation-is-not-independent", result=result))
    proof.update(accepted=True, commands=RECORDS, requiredRows=144000,
                 optionalFeePairs=720, optionalWorkflowRows=720)
    (WORK / "proof.json").write_text(json.dumps(proof, indent=2) + "\n")
    print(json.dumps(dict(accepted=True, sourceCommit=proof["sourceCommit"],
                         roles=2, filesPerRole=len(files), coreRows=144000,
                         optionalWorkflowRows=720, regressions=len(proof["regressions"]),
                         proof=str(WORK / "proof.json"))))


if __name__ == "__main__":
    verify()
