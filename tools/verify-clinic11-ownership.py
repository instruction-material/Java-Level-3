"""Verify Clinic 11 ownership moves, preserved clients and independent optional projects."""
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
PACK = ROOT / "Clinic11-Ownership-Packs"
OUT = ROOT / "output/java-clinic11-ownership-contracts"
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
    event = dict(parentTask=os.environ.get("CLASSES_FAMILY_TASK_ID", "java-clinic11-ownership"),
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


def verify():
    version=success(run([JAVAC,"-version"],ROOT))["stdout"]
    assert version.startswith("javac 21."),version
    head=success(run(["git","rev-parse","HEAD"],ROOT))["stdout"].strip()
    assert os.environ.get("GITHUB_SHA",head)==head
    proof=dict(sourceCommit=head,jdk=version.strip(),roles=[],independentProjects=[],
        safeIntermediateSteps=[],rejectedRegressions=[],clients=[],
        starterCoreBehaviorAlreadyWorks=True,manualOwnershipReviewRequired=True,fullGoalComplete=False)
    unfinished={"transfer":"Complete longest leg","extension":"Complete waypoint preview"}
    common=["Main.java","ContractChecks.java","CompatibilityClient.java","README.md","SOURCES.md",
        "src/api/Point.java","src/api/RouteData.java","src/api/PlannerAPI.java","src/legacy/LegacyPlanner.java"]
    for name in common:
        assert (PACK/"starter"/name).read_bytes()==(PACK/"solution"/name).read_bytes(),name
    for role,total in [("starter",14),("solution",15)]:
        folder=copy_role(role,role)
        names=sorted(f.relative_to(PACK/role).as_posix() for f in (PACK/role).rglob("*") if f.is_file())
        assert len(names)==total,names
        assert (folder/"sources.txt").read_text().splitlines()==sorted(f.relative_to(folder).as_posix() for f in folder.rglob("*.java"))
        success(compile_role(folder))
        row=dict(role=role,files=total,hashes={n:hashlib.sha256((PACK/role/n).read_bytes()).hexdigest() for n in names},
            baseline=[summary(execute(folder,"ContractChecks","baseline",flag),"baseline") for flag in [True,False]],
            core=[summary(execute(folder,"ContractChecks","core",flag),"core") for flag in [True,False]],completed={})
        for mode,token in unfinished.items():
            row["completed"][mode]=([summary(execute(folder,"ContractChecks",mode,flag),mode) for flag in [True,False]]
                if role=="solution" else dict(expectedUnfinished=True,evidence=rejected(execute(folder,"ContractChecks",mode),token)))
        if role=="solution":
            row["full"]=[summary(execute(folder,"ContractChecks","all",flag),"all") for flag in [True,False]]
            success(execute(folder,"Main","transfer"),"3\n")
            success(execute(folder,"Main","extension"),"walk|stops=4|distance=15\n")
        for mode in ["baseline","core"]:
            success(execute(folder,"Main",mode),"walk|stops=4|distance=7\n")
        success(execute(folder,"CompatibilityClient"),"compatibility passed\n")
        proof["roles"].append(row)
    groups={"core":["src/app/TripPlanner.java","src/app/RoutePlan.java"],
        "transfer":["src/transfer/LongestLeg.java"],"extension":["src/extension/WaypointEdit.java"]}
    for mode,files in groups.items():
        folder=copy_role("starter","only-"+mode)
        for file in files:shutil.copy2(PACK/"solution"/file,folder/file)
        update_sources(folder);success(compile_role(folder))
        row=dict(completed=mode,baseline=summary(execute(folder,"ContractChecks","baseline"),"baseline"),
            checks=[summary(execute(folder,"ContractChecks",mode,flag),mode) for flag in [True,False]],
            coreDesign="refactored" if mode=="core" else "working before",optionalProjectsStillUnfinished=[])
        for other,token in unfinished.items():
            if other!=mode:row["optionalProjectsStillUnfinished"].append(dict(mode=other,
                evidence=rejected(execute(folder,"ContractChecks",other),token)))
        success(execute(folder,"Main","core"),"walk|stops=4|distance=7\n")
        proof["independentProjects"].append(row)
    folder=copy_role("starter","intermediate-extract-leg")
    file="src/app/TripPlanner.java"
    text=(folder/file).read_text()
    before="Math.abs(after.x() - before.x()) + Math.abs(after.y() - before.y())"
    assert text.count(before)==1
    text=text.replace(before,"leg(before, after)")
    position=text.rfind("}")
    text=text[:position]+"""
    private int leg(Point before, Point after) {
        return Math.abs(after.x() - before.x()) + Math.abs(after.y() - before.y());
    }
""" +text[position:]
    (folder/file).write_text(text)
    success(compile_role(folder))
    proof["safeIntermediateSteps"].append(dict(technique="Extract Method",file=file,
        sourceHash=hashlib.sha256(text.encode()).hexdigest(),
        baseline=summary(execute(folder,"ContractChecks","baseline"),"baseline"),
        core=summary(execute(folder,"ContractChecks","core"),"core"),
        patch=dict(before=before,after="leg(before, after)")))
    folder=copy_role("solution","exact-readme-client");success(compile_role(folder))
    text=(folder/"README.md").read_text()
    block=text.split("    import api.*;\n",1)[1].split("\nCompile with javac",1)[0]
    probe="import api.*;\n"+"\n".join(line[4:] if line.startswith("    ") else line for line in block.splitlines())+"\n"
    (folder/"Probe.java").write_text(probe)
    success(run([JAVAC,"--release","21","-Xlint:all","-Werror","-proc:none","-cp","classes","-d","classes","Probe.java"],folder))
    for flag in [True,False]:success(execute(folder,"Probe",assertions=flag),"probe passed\n")
    proof["clients"].append("Exact separately compiled README client using the unchanged public facade")
    controls=[
      ("sign-cancellation","src/app/RoutePlan.java",
       "Math.abs(after.x() - before.x()) + Math.abs(after.y() - before.y())",
       "Math.abs(after.x() - before.x() + after.y() - before.y())","core"),
      ("last-leg-lost","src/app/RoutePlan.java","index < stops.size()","index < stops.size()-1","core"),
      ("late-null-validation","src/app/TripPlanner.java",'Objects.requireNonNull(route, "route");',"/* lost explicit boundary */","core"),
      ("wrong-stop-count","src/app/RoutePlan.java",'name+"|stops="+stops.size()','name+"|stops="+(stops.size()+1)',"core"),
      ("longest-becomes-total","src/transfer/LongestLeg.java","longest = Math.max(longest, leg);","longest += leg;","transfer"),
      ("replacement-moves-position","src/extension/WaypointEdit.java","changed.set(index, replacement);",
       "changed.remove(index); changed.add(replacement);","extension")
    ]
    for name,file,before,after,mode in controls:
        folder=copy_role("solution","rejected-"+name);mutate(folder,file,before,after)
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
