"""Verify the working DPR10 capstone, safe extraction and independent optional projects."""
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
PACK = ROOT / "DPR10-Capstone-Refactoring-Packs"
OUT = ROOT / "output/java-capstone-refactoring-contracts"
OUT.mkdir(parents=True, exist_ok=True)
WORK = Path(tempfile.mkdtemp(prefix="run-", dir=OUT))
LOG = WORK / "processes.jsonl"
JDK = Path(os.environ["JAVA_HOME"])
JAVA, JAVAC = str(JDK / "bin/java"), str(JDK / "bin/javac")
RECORDS = []


def run(args, cwd, input_text=None):
    started = datetime.datetime.now(datetime.timezone.utc).isoformat()
    child = subprocess.Popen(args, cwd=cwd, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                             text=True, start_new_session=True, stdin=subprocess.PIPE if input_text is not None else subprocess.DEVNULL)
    event = dict(parentTask=os.environ.get("CLASSES_FAMILY_TASK_ID", "java-capstone-refactoring"),
                 cwd=str(cwd), command=args, pid=child.pid, parentPid=os.getpid(),
                 startedAt=started, timeoutSeconds=45)
    with LOG.open("a") as stream:
        stream.write(json.dumps(dict(event, event="start")) + "\n")
    try:
        stdout, stderr = child.communicate(input=input_text, timeout=45)
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
    assert match and int(match.group(1))>0,result
    return int(match.group(1))


def cli(folder, mode):
    return run([JAVA,"-Xmx128m","-cp","classes","Main",mode],folder,
               input_text=(folder/"sample-input.txt").read_text())


def verify():
    version=success(run([JAVAC,"-version"],ROOT))["stdout"]
    assert version.startswith("javac 21."),version
    head=success(run(["git","rev-parse","HEAD"],ROOT))["stdout"].strip()
    assert os.environ.get("GITHUB_SHA",head)==head
    proof=dict(sourceCommit=head,jdk=version.strip(),roles=[],independentProjects=[],
               rejectedRegressions=[],clients=[],safeIntermediateSteps=[],
               starterCoreBehaviorAlreadyWorks=True,
               manualPortfolioRequired=True,fullGoalComplete=False)
    unfinished={"transfer":"Complete trace comparison","extension":"Complete rename preview"}
    common=["Main.java","CompatibilityClient.java","ContractChecks.java","sample-input.txt","README.md","SOURCES.md",
            "src/api/DeskAPI.java","src/api/JoinStatus.java","src/api/Rules.java","src/api/SessionView.java",
            "src/legacy/LegacyWorkshopDesk.java","src/app/AppScript.java","src/trace/Step.java","src/trace/Observation.java"]
    for name in common:
        assert (PACK/"starter"/name).read_bytes()==(PACK/"solution"/name).read_bytes(),name
    demo=("Workshops 2\nroom capacity=2 open=true\nenrolled=bo,cy\nwaiting=-\n"
          "quiet capacity=1 open=false\nenrolled=-\nwaiting=-\n2\nroom|2|open|bo,cy|-\nquiet|1|closed|-|-\n"
          "[open, join:ana:enrolled, join:bo:enrolled, join:cy:waiting, leave:ana:enrolled, promote:cy]\n")
    sample="opened\nENROLLED\nWAITLISTED\ntrue\n1\nroom|1|open|bo|-\ntrue\nerror:IllegalStateException:closed\n"
    for role,expected_count in [("starter",19),("solution",21)]:
        folder=copy_role(role,role)
        names=sorted(p.relative_to(PACK/role).as_posix() for p in (PACK/role).rglob("*") if p.is_file())
        assert len(names)==expected_count,names
        assert (folder/"sources.txt").read_text().splitlines()==sorted(p.relative_to(folder).as_posix() for p in folder.rglob("*.java"))
        success(compile_role(folder))
        row=dict(role=role,files=len(names),hashes={name:hashlib.sha256((PACK/role/name).read_bytes()).hexdigest() for name in names},
                 baseline=[summary(execute(folder,"ContractChecks","baseline",flag),"baseline") for flag in [True,False]],
                 core=[summary(execute(folder,"ContractChecks","core",flag),"core") for flag in [True,False]],
                 completed={})
        for mode,marker in unfinished.items():
            row["completed"][mode]=([summary(execute(folder,"ContractChecks",mode,flag),mode) for flag in [True,False]]
                if role=="solution" else dict(expectedUnfinished=True,evidence=rejected(execute(folder,"ContractChecks",mode),marker)))
        if role=="solution":
            row["full"]=[summary(execute(folder,"ContractChecks","all",flag),"all") for flag in [True,False]]
        for mode in ["baseline","core"]:
            success(execute(folder,"Main",mode),demo)
        for mode in ["script-baseline","script-core"]:
            success(cli(folder,mode),sample)
        success(execute(folder,"CompatibilityClient"),"compatibility passed\n")
        proof["roles"].append(row)
    groups={"core":["src/app/WorkshopDesk.java","src/app/SessionBook.java","src/app/DeskReports.java"],
            "transfer":["src/trace/Equivalence.java"],"extension":["src/preview/RenamePreview.java"]}
    for mode,files in groups.items():
        folder=copy_role("starter","only-"+mode)
        for file in files:
            shutil.copy2(PACK/"solution"/file,folder/file)
        update_sources(folder)
        success(compile_role(folder))
        baseline=summary(execute(folder,"ContractChecks","baseline"),"baseline")
        completed=[summary(execute(folder,"ContractChecks",mode,flag),mode) for flag in [True,False]]
        other=[dict(mode=key,evidence=rejected(execute(folder,"ContractChecks",key),marker))
               for key,marker in unfinished.items() if key!=mode]
        # Core remains working at the before design for independent transfer/extension attempts.
        success(execute(folder,"Main","core"),demo)
        proof["independentProjects"].append(dict(completed=mode,checks=completed,baseline=baseline,
            optionalProjectsStillUnfinished=other,coreDesign="refactored" if mode=="core" else "working before"))
    folder=copy_role("starter","intermediate-extract-method")
    file="src/app/WorkshopDesk.java"
    text=(folder/file).read_text()
    before='enrolled.get(session).contains(participant) || waiting.get(session).contains(participant)'
    assert before in text,text
    text=text.replace(before,'containsParticipant(session, participant)')
    position=text.rfind("}")
    text=text[:position]+"""
    private boolean containsParticipant(String session, String participant) {
        return enrolled.get(session).contains(participant) || waiting.get(session).contains(participant);
    }
""" +text[position:]
    (folder/file).write_text(text)
    success(compile_role(folder))
    proof["safeIntermediateSteps"].append(dict(technique="Extract Method",
        sourceHash=hashlib.sha256(text.encode()).hexdigest(),
        baseline=summary(execute(folder,"ContractChecks","baseline"),"baseline"),
        core=summary(execute(folder,"ContractChecks","core"),"core"),
        patch=dict(file=file,before=before,after="containsParticipant(session, participant)")))
    folder=copy_role("solution","actual-external-clients");success(compile_role(folder))
    probe=r"""import api.*;
import app.WorkshopDesk;
import legacy.LegacyWorkshopDesk;
import preview.RenamePreview;
import trace.*;
import java.util.List;
public final class Probe {
    private Probe() {}
    public static void main(String[] args) {
        DeskAPI before=new LegacyWorkshopDesk(),after=new WorkshopDesk();
        List<Step> plan=List.of(new Step.Open("room",1),new Step.Join("room","ana"),
            new Step.Join("room","cy"),new Step.Join("room","bo"));
        if(!Equivalence.same(before,after,plan))throw new AssertionError("trace");
        SessionView input=after.view("room"),preview=RenamePreview.rename(input,"cy","dee");
        if(!preview.waiting().equals(List.of("dee","bo")))throw new AssertionError("queue");
        if(!input.equals(after.view("room")))throw new AssertionError("mutation");
        if(!after.leave("room","ana")||!after.view("room").enrolled().equals(List.of("cy")))
            throw new AssertionError("FIFO");
        System.out.println("probe passed");
    }
}
"""
    (folder/"Probe.java").write_text(probe)
    success(run([JAVAC,"--release","21","-Xlint:all","-Werror","-proc:none","-cp","classes","-d","classes","Probe.java"],folder))
    for flag in [True,False]:success(execute(folder,"Probe",assertions=flag),"probe passed\n")
    # Compile the exact README standalone preview, not an illustrative fragment.
    readme=(folder/"README.md").read_text()
    block=readme.split("    import api.*;\n",1)[1].split("\nCompile with javac",1)[0]
    preview="import api.*;\n"+"\n".join(line[4:] if line.startswith("    ") else line for line in block.splitlines())+"\n"
    (folder/"PreviewProbe.java").write_text(preview)
    success(run([JAVAC,"--release","21","-Xlint:all","-Werror","-proc:none","-cp","classes","-d","classes","PreviewProbe.java"],folder))
    success(execute(folder,"PreviewProbe"),"preview passed\n")
    proof["clients"]=["Separately compiled API/trace/preview/FIFO client","Exact standalone README PreviewProbe"]
    # Each controlled regression compiles. A frozen behavioral check must then reject it.
    final_only="""package trace;
import api.DeskAPI;
import java.util.*;
public final class Equivalence {
    private Equivalence() {}
    public static boolean same(DeskAPI before,DeskAPI after,List<Step> steps) {
        Objects.requireNonNull(before,"before");Objects.requireNonNull(after,"after");
        List<Step> plan=List.copyOf(Objects.requireNonNull(steps,"steps"));
        if(before==after)throw new IllegalArgumentException("independent desks");
        for(Step step:plan){Observation.take(before,step);Observation.take(after,step);}
        return before.all().equals(after.all());
    }
}
"""
    controls=[
        ("FIFO-order","src/app/SessionBook.java","waiting.remove(0)","waiting.remove(waiting.size()-1)","core"),
        ("query-side-effect","src/app/SessionBook.java","SessionView view() { return","SessionView view() { history.add(\"read\"); return","core"),
        ("closed-session","src/app/SessionBook.java","JoinStatus join(String participant) {\n        requireOpen();","JoinStatus join(String participant) {","core"),
        ("final-state-only","src/trace/Equivalence.java",(PACK/"solution/src/trace/Equivalence.java").read_text(),final_only,"transfer"),
        ("rename-moves-position","src/preview/RenamePreview.java","selected.set(selected.indexOf(oldID), newID);","selected.remove(oldID); selected.add(newID);","extension"),
        ("rename-rewrites-history","src/preview/RenamePreview.java","history.add(\"rename:\"+oldID+\":\"+newID);",
         "history.replaceAll(value -> value.replace(oldID, newID)); history.add(\"rename:\"+oldID+\":\"+newID);","extension")]
    for name,file,old,new,mode in controls:
        folder=copy_role("solution","rejected-"+name);mutate(folder,file,old,new)
        success(compile_role(folder))
        proof["rejectedRegressions"].append(dict(name=name,evidence=rejected(execute(folder,"ContractChecks",mode),"AssertionError")))
    return proof


try:
    proof=verify();proof["processLog"]=str(LOG)
    (OUT/"proof.json").write_text(json.dumps(proof,indent=2)+"\n")
    print(json.dumps(dict(sourceCommit=proof["sourceCommit"],
        roles=[{k:r[k] for k in ["role","files","baseline","core","full"] if k in r} for r in proof["roles"]],
        independentProjects=len(proof["independentProjects"]),safeIntermediateSteps=len(proof["safeIntermediateSteps"]),
        controlledRegressionsRejected=len(proof["rejectedRegressions"]),clients=proof["clients"],processLog=str(LOG))))
finally:
    for folder in WORK.iterdir():
        if folder.is_dir():shutil.rmtree(folder)
