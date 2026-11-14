"""Verify DPR6 method-call API and compatibility."""
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
PACK = ROOT / "DPR6-Method-Calls-Packs"
OUT = ROOT / "output/java-method-calls-contracts"
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
    event = dict(parentTask=os.environ.get("CLASSES_FAMILY_TASK_ID", "java-method-calls"),
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
    shutil.copytree(PACK / role, destination, ignore=shutil.ignore_patterns("out", "classes"))
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
    proof = dict(roles=[], regressions=[], isolated=[], intermediate=[],
                 jdk=success(run([JAVAC, "-version"], ROOT))["stdout"])
    demo = "140\n505\n645\n-1\nQuote[label=B, cents=505]\nnull\n"
    names = sorted(p.relative_to(PACK / "solution").as_posix()
                   for p in (PACK / "solution").rglob("*") if p.is_file() and "out" not in p.parts)
    assert len(names) == 18
    editable = {"src/dispatch/DispatchService.java", "src/dispatch/CompatibilityDesk.java",
                "src/dispatch/optional/QueueClient.java"}
    for name in set(names) - editable:
        assert (PACK / "starter" / name).read_bytes() == (PACK / "solution" / name).read_bytes(), name
    for role in ["starter", "solution"]:
        folder = copy_role(role, role)
        success(compile_role(folder))
        row = dict(role=role, hashes={n: hashlib.sha256((PACK/role/n).read_bytes()).hexdigest() for n in names})
        row["baseline"] = []
        for enabled in [True, False]:
            row["baseline"].append(success(execute(folder,"ContractChecks","baseline",enabled),
                "mode=baseline rows=576 checks=2319\n"))
            success(execute(folder,"dispatch.optional.OptionalChecks","baseline",enabled),
                "mode=baseline queues=21 checks=507\n")
        success(execute(folder, "Main", "baseline"), demo)
        if role == "starter":
            row["unfinished"] = [
                rejected(execute(folder, "ContractChecks", "core"), "TODO quote"),
                rejected(execute(folder, "dispatch.optional.OptionalChecks", "full"), "TODO printNext")]
        else:
            row["core"] = [success(execute(folder, "ContractChecks", "core", enabled),
                "mode=core rows=576 checks=9266\n") for enabled in [True, False]]
            row["optional"] = [success(execute(folder, "dispatch.optional.OptionalChecks", "full", enabled),
                "mode=full queues=21 checks=507\n") for enabled in [True, False]]
            success(execute(folder, "Main", "core"), demo)
        proof["roles"].append(row)

    # Required work compiles and executes without baseline, optional, tests or demo.
    folder = copy_role("solution", "isolated-core")
    selected = ["ClientCompatibility.java"] + [n for n in names if n.startswith("src/dispatch/")
                    and "/optional/" not in n and not n.endswith("LegacyDesk.java")]
    (folder/"IsolatedClient.java").write_text(
        'public final class IsolatedClient { public static void main(String[] args) { '
        'System.out.println(ClientCompatibility.run()); } }\n')
    success(compile_role(folder, selected + ["IsolatedClient.java"]))
    proof["isolated"].append(success(execute(folder, "IsolatedClient"), demo))

    # A completed required attempt does not depend on the unfinished optional method.
    folder = copy_role("starter", "required-only")
    for name in ["src/dispatch/DispatchService.java", "src/dispatch/CompatibilityDesk.java"]:
        shutil.copyfile(PACK/"solution"/name, folder/name)
    success(compile_role(folder))
    proof["intermediate"].append(success(execute(folder,"ContractChecks","core"),
        "mode=core rows=576 checks=9266\n"))
    rejected(execute(folder,"dispatch.optional.OptionalChecks","full"),"TODO printNext")

    mutations = [
        ("query-writes", "src/dispatch/DispatchService.java",
         "int bands = (parcel.grams() + 499) / 500;",
         'log.append(new Quote("unexpected", 0));\n        int bands = (parcel.grams() + 499) / 500;',
         "ContractChecks", "core", "query has no writes"),
        ("wrong-band", "src/dispatch/DispatchService.java",
         "(parcel.grams() + 499) / 500", "parcel.grams() / 500",
         "ContractChecks", "core", "pure quote"),
        ("adapter-query-only", "src/dispatch/CompatibilityDesk.java",
         "service.send(parcel,", "service.quote(parcel,",
         "ContractChecks", "core", "old client history"),
        ("setter-erases-history", "src/dispatch/CompatibilityDesk.java",
         "DispatchService.discounted(discountCents, log)",
         "DispatchService.discounted(discountCents, new DispatchLog())",
         "ContractChecks", "core", "old client history"),
        ("null-optional", "src/dispatch/optional/QueueClient.java",
         "if (!queue.hasNext()) return Optional.empty();", "if (!queue.hasNext()) return null;",
         "dispatch.optional.OptionalChecks", "full", "Optional.empty")
    ]
    for key, name, before, after, program, mode, token in mutations:
        folder = copy_role("solution", key)
        mutate(folder, name, before, after)
        success(compile_role(folder))
        proof["regressions"].append(dict(name=key, evidence=rejected(execute(folder, program, mode), token)))
    proof.update(filesPerRole=len(names), javaSources=14, coreRows=576, coreChecks=9266,
                 optionalQueues=21, optionalChecks=507, mutationControlsRejected=5,
                 fullGoalComplete=False)
    return proof

try:
    proof = verify()
    proof["processLog"] = str(LOG)
    (OUT/"proof.json").write_text(json.dumps(proof,indent=2)+"\n")
    print(json.dumps({k:proof[k] for k in ["filesPerRole","coreRows","coreChecks","optionalQueues",
                                          "optionalChecks","mutationControlsRejected","processLog"]}))
finally:
    # Retain process evidence and proof, remove only the generated compilation work.
    for folder in WORK.iterdir():
        if folder.is_dir():
            shutil.rmtree(folder)
