"""Verify DPR9 testability seams and independent characterization/failure exercises."""
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
PACK = ROOT / "DPR9-Testability-Packs"
OUT = ROOT / "output/java-testability-contracts"
OUT.mkdir(parents=True, exist_ok=True)
WORK = Path(tempfile.mkdtemp(prefix="run-", dir=OUT))
LOG = WORK / "processes.jsonl"
JDK = Path(os.environ["JAVA_HOME"])
JAVA, JAVAC = str(JDK / "bin/java"), str(JDK / "bin/javac")
RECORDS = []


def run(args, cwd):
    started = datetime.datetime.now(datetime.timezone.utc).isoformat()
    child = subprocess.Popen(args, cwd=cwd, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                             text=True, start_new_session=True)
    event = dict(parentTask=os.environ.get("CLASSES_FAMILY_TASK_ID", "java-testability"),
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




def update_sources(folder):
    names = sorted(p.relative_to(folder).as_posix() for p in folder.rglob("*.java"))
    (folder / "sources.txt").write_text("\n".join(names) + "\n")


def summary(result, mode):
    import re
    success(result)
    match=re.fullmatch(mode+r": (\d+) checks passed\n",result["stdout"])
    assert match and int(match.group(1))>100,result
    return int(match.group(1))


def verify():
    import re
    version=success(run([JAVAC,"-version"],ROOT))["stdout"]
    assert version.startswith("javac 21."),version
    head=success(run(["git","rev-parse","HEAD"],ROOT))["stdout"].strip()
    assert os.environ.get("GITHUB_SHA",head)==head
    proof=dict(sourceCommit=head,jdk=version.strip(),roles=[],independentProjects=[],rejectedRegressions=[],clients=[],fullGoalComplete=False)
    unfinished={"core":"Missing injection constructor","transfer":"AssertionError","extension":"java.io.IOException: first"}
    editable={"src/journal/DispatchJournal.java","src/journal/CaptureNormalizer.java","src/journal/RetryingSink.java"}
    names={role:sorted(p.relative_to(PACK/role).as_posix() for p in (PACK/role).rglob("*") if p.is_file()) for role in ["starter","solution"]}
    assert names["starter"]==names["solution"]
    for name in set(names["starter"])-editable:
        assert (PACK/"starter"/name).read_bytes()==(PACK/"solution"/name).read_bytes(),name
    for role in ["starter","solution"]:
        folder=copy_role(role,role)
        assert (folder/"sources.txt").read_text().splitlines()==sorted(p.relative_to(folder).as_posix() for p in folder.rglob("*.java"))
        success(compile_role(folder))
        row=dict(role=role,files=len(names[role]),hashes={name:hashlib.sha256((PACK/role/name).read_bytes()).hexdigest() for name in names[role]},
                 baseline=[summary(execute(folder,"ContractChecks","baseline",enabled),"baseline") for enabled in [True,False]],completed={})
        for mode,marker in unfinished.items():
            row["completed"][mode]=([summary(execute(folder,"ContractChecks",mode,enabled),mode) for enabled in [True,False]]
                                    if role=="solution" else dict(expectedUnfinished=True,evidence=rejected(execute(folder,"ContractChecks",mode),marker)))
        for mode in ["baseline","core"]:
            result=success(execute(folder,"Main",mode))
            assert re.fullmatch(r"[^|\n]+\|(?:0|[1-9][0-9]{0,5})\|lamp\|2\n",result["stdout"]),result
            datetime.datetime.fromisoformat(result["stdout"].split("|",1)[0].replace("Z","+00:00"))
        if role=="solution":row["full"]=[summary(execute(folder,"ContractChecks","full",enabled),"full") for enabled in [True,False]]
        proof["roles"].append(row)
    groups={"core":"src/journal/DispatchJournal.java","transfer":"src/journal/CaptureNormalizer.java","extension":"src/journal/RetryingSink.java"}
    for mode,file in groups.items():
        folder=copy_role("starter","only-"+mode)
        shutil.copy2(PACK/"solution"/file,folder/file)
        success(compile_role(folder))
        baseline=summary(execute(folder,"ContractChecks","baseline"),"baseline")
        completed=summary(execute(folder,"ContractChecks",mode),mode)
        other=[dict(mode=key,evidence=rejected(execute(folder,"ContractChecks",key),marker)) for key,marker in unfinished.items() if key!=mode]
        proof["independentProjects"].append(dict(completed=mode,checks=completed,baseline=baseline,otherTwoRemainUnfinished=other))
    folder=copy_role("solution","actual-external-client");success(compile_role(folder))
    probe='''import journal.*;
import java.time.*;
import java.util.*;
public final class Probe {
    private Probe() {}
    public static void main(String[] args) {
        List<Entry> written = new ArrayList<>();
        JournalAPI journal = new DispatchJournal(
            Clock.fixed(Instant.parse("2026-11-15T12:00:00Z"), ZoneOffset.UTC),
            () -> 42, written::add);
        Entry value = journal.record("kit", 0);
        if (!value.line().equals("2026-11-15T12:00:00Z|42|kit|0\\n")) throw new AssertionError("line");
        if (!written.equals(List.of(value))) throw new AssertionError("sink");
        System.out.println("probe passed");
    }
}
'''
    (folder/"Probe.java").write_text(probe)
    success(run([JAVAC,"--release","21","-Xlint:all","-Werror","-proc:none","-cp","classes","-d","classes","Probe.java"],folder))
    success(execute(folder,"Probe"),"probe passed\n")
    proof["clients"].append("Separate client supplies fixed time, ID and recording sink through the real public constructor")
    controls=[
        ("clock-bypassed","src/journal/DispatchJournal.java","new Entry(clock.instant(), ids.getAsInt(), label, count)","new Entry(java.time.Instant.now(), ids.getAsInt(), label, count)","core"),
        ("id-bypassed","src/journal/DispatchJournal.java","ids.getAsInt()","0","core"),
        ("invalid-input-dependency","src/journal/DispatchJournal.java","Entry.validate(label, count);","clock.instant(); Entry.validate(label, count);","core"),
        ("duplicate-output","src/journal/DispatchJournal.java","sink.write(entry);","sink.write(entry); sink.write(entry);","core"),
        ("swallowed-output-error","src/journal/DispatchJournal.java",'throw new UncheckedIOException("journal", error);',"/* lost failure */","core"),
        ("approval-masks-count","src/journal/CaptureNormalizer.java",'parts[3] + "\\n"','"0\\n"',"transfer"),
        ("third-attempt","src/journal/RetryingSink.java","if (second != first) second.addSuppressed(first);","if (second != first) second.addSuppressed(first); delegate.write(entry);","extension"),
    ]
    for name,file,old,new,mode in controls:
        folder=copy_role("solution","rejected-"+name);mutate(folder,file,old,new)
        success(compile_role(folder))
        proof["rejectedRegressions"].append(dict(name=name,evidence=rejected(execute(folder,"ContractChecks",mode),"AssertionError")))
    return proof


try:
    proof=verify();proof["processLog"]=str(LOG)
    (OUT/"proof.json").write_text(json.dumps(proof,indent=2)+"\n")
    print(json.dumps(dict(sourceCommit=proof["sourceCommit"],roles=[{k:r[k] for k in ["role","files","baseline","full"] if k in r} for r in proof["roles"]],independentProjects=len(proof["independentProjects"]),controlledRegressionsRejected=len(proof["rejectedRegressions"]),clients=proof["clients"],processLog=str(LOG))))
finally:
    for folder in WORK.iterdir():
        if folder.is_dir():shutil.rmtree(folder)
