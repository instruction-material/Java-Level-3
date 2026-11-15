"""Verify DPR7 generalization, client contracts and optional independence."""
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
PACK = ROOT / "DPR7-Generalization-Packs"
OUT = ROOT / "output/java-generalization-contracts"
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
    event = dict(parentTask=os.environ.get("CLASSES_FAMILY_TASK_ID", "java-generalization"),
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
    match = re.fullmatch(r"mode=" + mode + r" checks=(\d+)\n", result["stdout"])
    assert match and int(match.group(1)) > 100, result
    return int(match.group(1))

def verify():
    version = success(run([JAVAC, "-version"], ROOT))["stdout"]
    assert version.startswith("javac 21."), version
    proof = dict(jdk=version.strip(), roles=[], independentProjects=[], rejectedRegressions=[], clients=[])
    demos = {
        "baseline": "Ada | volunteer | 6\nBo | mentor | 14\ntotal=20\nSupported by North Lab\n",
        "transfer-baseline": "Inventory\ntool, kit=2\nlamp=3\nitems=5\n---\nname,count\n\"tool, kit\",2\n\"lamp\",3\ntotal,5\n",
        "extension-baseline": "[B, C]\nB\n[C]\nvisits=7\nreset=0\n",
    }
    editable = {
        "src/roster/Roster.java", "src/roster/Volunteer.java", "src/roster/Mentor.java", "src/roster/SponsorBanner.java",
        "src/reports/ReportService.java", "src/events/EventBuffer.java", "src/events/EventFactory.java", "src/events/NamedCounter.java", "sources.txt",
    }
    role_names = {role: sorted(p.relative_to(PACK/role).as_posix() for p in (PACK/role).rglob("*") if p.is_file()) for role in ["starter", "solution"]}
    for name in set(role_names["starter"]) & set(role_names["solution"]) - editable:
        assert (PACK/"starter"/name).read_bytes() == (PACK/"solution"/name).read_bytes(), name
    for role in ["starter", "solution"]:
        folder = copy_role(role, role)
        names = sorted(p.relative_to(folder).as_posix() for p in folder.rglob("*.java"))
        assert (folder/"sources.txt").read_text().splitlines() == names
        success(compile_role(folder))
        row = dict(role=role, javaSources=len(names), files=len(role_names[role]),
                   hashes={n:hashlib.sha256((PACK/role/n).read_bytes()).hexdigest() for n in role_names[role]},
                   baseline=[], completed={})
        for enabled in [True, False]:
            row["baseline"].append(summary(execute(folder,"ContractChecks","baseline",enabled),"baseline"))
        for mode, expected in demos.items():
            success(execute(folder,"Main",mode), expected)
        for mode, marker in [("core","Missing refactoring class: roster.Member"),("transfer","Missing refactoring class: reports.Report"),("extension","EventBuffer still exposes List")]:
            if role=="starter":
                row["completed"][mode] = dict(expectedUnfinished=True, evidence=rejected(execute(folder,"ContractChecks",mode),marker))
            else:
                row["completed"][mode] = [summary(execute(folder,"ContractChecks",mode,enabled),mode) for enabled in [True,False]]
                success(execute(folder,"Main",mode), demos[{"core":"baseline","transfer":"transfer-baseline","extension":"extension-baseline"}[mode]])
        if role=="solution":
            row["full"] = summary(execute(folder,"ContractChecks","full"),"full")
        proof["roles"].append(row)
    groups = {
        "core": ["src/roster/Roster.java","src/roster/Volunteer.java","src/roster/Mentor.java","src/roster/SponsorBanner.java","src/roster/Member.java","src/roster/LineSource.java"],
        "transfer": ["src/reports/ReportService.java","src/reports/Report.java","src/reports/PlainReport.java","src/reports/CsvReport.java"],
        "extension": ["src/events/EventBuffer.java","src/events/EventFactory.java","src/events/NamedCounter.java"],
    }
    for mode, files in groups.items():
        folder = copy_role("starter","only-"+mode)
        for name in files:
            destination=folder/name;destination.parent.mkdir(parents=True,exist_ok=True)
            shutil.copy2(PACK/"solution"/name,destination)
        update_sources(folder)
        success(compile_role(folder))
        count=summary(execute(folder,"ContractChecks",mode),mode)
        unfinished=[]
        for other, marker in [("core","Missing refactoring class: roster.Member"),("transfer","Missing refactoring class: reports.Report"),("extension","EventBuffer still exposes List")]:
            if other != mode:
                unfinished.append(dict(mode=other,evidence=rejected(execute(folder,"ContractChecks",other),marker)))
        proof["independentProjects"].append(dict(completedProject=mode,checks=count,otherTwoRemainUnfinished=unfinished))
    # Compile real external clients against the reference's public types.
    folder=copy_role("solution","public-clients");success(compile_role(folder))
    external=r"""import roster.*;
import events.*;
import java.util.List;
public final class PublicClients {
    private static String text(LineSource source) { return source.line(); }
    private static long count(Counter counter) {
        counter.add(7); counter.reset(); counter.add(2); return counter.value();
    }
    public static void main(String[] args) {
        System.out.println(text(new Volunteer("Ada",3)));
        System.out.println(text(new SponsorBanner("North Lab")));
        System.out.println(count(new NamedCounter("visits")));
    }
}
"""
    (folder/"PublicClients.java").write_text(external)
    success(run([JAVAC,"--release","21","-encoding","UTF-8","-Xlint:all","-Werror","-proc:none","-cp","classes","-d","classes","PublicClients.java"],folder))
    success(execute(folder,"PublicClients"),"Ada | volunteer | 6\nSupported by North Lab\n2\n")
    proof["clients"].append("Unrelated LineSource values and actual NamedCounter-as-Counter client compiled and executed")
    probe='import events.EventBuffer; public class IndexedClient { void change() { new EventBuffer(2).add(0,"bypass"); } }\n'
    (folder/"IndexedClient.java").write_text(probe)
    rejected(run([JAVAC,"--release","21","-cp","classes","-d","classes","IndexedClient.java"],folder),"cannot find symbol")
    proof["clients"].append("Removed indexed List mutation does not compile against the reference")
    before=copy_role("starter","old-list-exposure");success(compile_role(before))
    (before/"IndexedClient.java").write_text(probe)
    success(run([JAVAC,"--release","21","-cp","classes","-d","classes","IndexedClient.java"],before))
    proof["clients"].append("The same unwanted indexed operation compiles against the original starter")
    controls = [
        ("mentor-credits","src/roster/Mentor.java","sessionCredits() + mentoringSlots * 3","sessionCredits() + mentoringSlots * 2","core","expected="),
        ("constructor-order","src/roster/Member.java",'Objects.requireNonNull(name, "name");','if (sessions < 0) throw new IllegalArgumentException("sessions");\n        Objects.requireNonNull(name, "name");',"core","Expected NullPointerException"),
        ("csv-delimiter","src/reports/CsvReport.java","private final char delimiter = ',';","private final char delimiter = ';';","transfer","expected="),
        ("template-order-owner","src/reports/Report.java","public final String render(","public String render(","transfer","Keep the template order"),
        ("fifo-eviction","src/events/EventBuffer.java","if (events.size() == capacity) events.removeFirst();","if (events.size() == capacity) events.removeLast();","extension","expected="),
        ("overflow-contract","src/events/Counter.java","value = Math.addExact(value, amount);","value += amount;","extension","Expected ArithmeticException"),
    ]
    for key,name,old,new,mode,token in controls:
        folder=copy_role("solution","rejected-"+key)
        mutate(folder,name,old,new)
        success(compile_role(folder))
        proof["rejectedRegressions"].append(dict(name=key,evidence=rejected(execute(folder,"ContractChecks",mode),token)))
    folder=copy_role("solution","wrong-but-compatible-relationship")
    shutil.copy2(PACK/"starter/src/events/NamedCounter.java",folder/"src/events/NamedCounter.java")
    success(compile_role(folder))
    proof["rejectedRegressions"].append(dict(name="delegation-retained-despite-equal-domain-results",evidence=rejected(execute(folder,"ContractChecks","extension"),"NamedCounter must inherit")))
    proof.update(techniques=12, sourceProjectGroups=3, fullGoalComplete=False)
    return proof

try:
    proof=verify()
    proof["processLog"]=str(LOG)
    (OUT/"proof.json").write_text(json.dumps(proof,indent=2)+"\n")
    print(json.dumps(dict(techniques=proof["techniques"],roles=[{k:r[k] for k in ["role","javaSources","files","baseline","full"] if k in r} for r in proof["roles"]],
                         independentProjects=len(proof["independentProjects"]),controlledRegressionsRejected=len(proof["rejectedRegressions"]),clients=proof["clients"],processLog=str(LOG))))
finally:
    for folder in WORK.iterdir():
        if folder.is_dir():shutil.rmtree(folder)
