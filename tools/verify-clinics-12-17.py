"""Verify six distinct refactoring clinics, independent work and safe intermediate source states."""
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
PACK = None
OUT = ROOT / "output/java-clinics-12-17-contracts"
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
    event = dict(parentTask=os.environ.get("CLASSES_FAMILY_TASK_ID", "java-clinics-12-17"),
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
    destination = WORK / (PACK.name + '-' + name)
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


def checked(folder,mode):
    return [summary(execute(folder,"ContractChecks",mode,flag),mode) for flag in [True,False]]


def verify():
    global PACK
    import textwrap
    version=success(run([JAVAC,"-version"],ROOT))["stdout"]
    assert version.startswith("javac 21."),version
    head=success(run(["git","rev-parse","HEAD"],ROOT))["stdout"].strip()
    assert os.environ.get("GITHUB_SHA",head)==head
    units=json.loads((ROOT/"tools/clinics-12-17-contracts.json").read_text())
    assert set(units)=={"12","13","14","15","16","17"}
    proof=dict(sourceCommit=head,jdk=version.strip(),families=[],manualDesignReviewRequired=True,
               siteTeachingAccepted=False,actualSiteIDEAccepted=False,fullGoalComplete=False)
    for number,spec in units.items():
        PACK=ROOT/spec["pack"]
        family=dict(clinic=int(number),pack=spec["pack"],roles=[],independentProjects=[],
                    safeIntermediateSteps=[],rejectedRegressions=[],clients=[],rollbackChecks=[])
        unfinished=dict(spec["unfinished"])
        if spec["coreUnfinished"]:unfinished["core"]=spec["coreUnfinished"]
        for name in spec["common"]+["README.md","SOURCES.md"]:
            assert (PACK/"starter"/name).read_bytes()==(PACK/"solution"/name).read_bytes(),name
        for role in ["starter","solution"]:
            folder=copy_role(role,role)
            names=spec["roleFiles"][role]
            assert names==sorted(f.relative_to(PACK/role).as_posix() for f in (PACK/role).rglob("*") if f.is_file()),(number,role)
            assert (folder/"sources.txt").read_text().splitlines()==sorted(f.relative_to(folder).as_posix() for f in folder.rglob("*.java"))
            success(compile_role(folder))
            row=dict(role=role,files=len(names),hashes={n:hashlib.sha256((PACK/role/n).read_bytes()).hexdigest() for n in names},
                     baseline=checked(folder,"baseline"),completed={})
            for mode in ["core","transfer","extension"]:
                row["completed"][mode]=(dict(expectedUnfinished=True,evidence=rejected(execute(folder,"ContractChecks",mode),unfinished[mode]))
                    if role=="starter" and mode in unfinished else checked(folder,mode))
            if role=="solution":row["full"]=checked(folder,"all")
            for mode,expected in spec["mainOutputs"].items():
                if role=="starter" and mode in ["transfer","extension"]:
                    rejected(execute(folder,"Main",mode),unfinished[mode])
                else:success(execute(folder,"Main",mode),expected)
            success(execute(folder,"CompatibilityClient"),"compatibility passed\n")
            family["roles"].append(row)
        for mode,files in spec["completions"].items():
            folder=copy_role("starter","only-"+mode)
            for file in files:
                (folder/file).parent.mkdir(parents=True,exist_ok=True)
                shutil.copy2(PACK/"solution"/file,folder/file)
            update_sources(folder);success(compile_role(folder))
            row=dict(completed=mode,baseline=checked(folder,"baseline"),checks=checked(folder,mode),
                     stillUnfinished=[])
            for other,token in unfinished.items():
                if other!=mode:row["stillUnfinished"].append(dict(mode=other,evidence=rejected(execute(folder,"ContractChecks",other),token)))
            success(execute(folder,"Main","core"),spec["mainOutputs"]["core"])
            family["independentProjects"].append(row)
        for index,step in enumerate(spec["intermediates"],1):
            folder=copy_role("starter","checkpoint-"+str(index))
            for name,content in step["files"].items():
                file=folder/name;file.parent.mkdir(parents=True,exist_ok=True);file.write_text(content)
            update_sources(folder);success(compile_role(folder))
            family["safeIntermediateSteps"].append(dict(label=step["label"],files={n:hashlib.sha256(c.encode()).hexdigest() for n,c in step["files"].items()},
                baseline=checked(folder,step["mode"]),exactOriginalClient=success(execute(folder,"CompatibilityClient"),"compatibility passed\n")))
            if number=="17" and index==2:
                # Restore the actual earlier checkpoint in this owned temporary tree.
                for name in step["files"]:
                    if name not in spec["roleFiles"]["starter"]:(folder/name).unlink()
                for name in spec["roleFiles"]["starter"]:
                    (folder/name).parent.mkdir(parents=True,exist_ok=True)
                    shutil.copy2(PACK/"starter"/name,folder/name)
                first=spec["intermediates"][0]
                for name,content in first["files"].items():(folder/name).write_text(content)
                update_sources(folder)
                shutil.rmtree(folder/"classes")
                success(compile_role(folder))
                family["rollbackChecks"].append(dict(restored=first["label"],baseline=checked(folder,"baseline"),
                    originalSourceUntouched=True))
        folder=copy_role("solution","exact-external-probe");success(compile_role(folder))
        readme=(folder/"README.md").read_text()
        block=readme.split("Write this separately compiled Probe.java after completing core:\n\n",1)[1].split("\nCompile with javac",1)[0]
        probe=textwrap.dedent(block).strip()+"\n"
        assert probe==spec["probe"].strip()+"\n",number
        (folder/"Probe.java").write_text(probe)
        success(run([JAVAC,"--release","21","-Xlint:all","-Werror","-proc:none","-cp","classes","-d","classes","Probe.java"],folder))
        for flag in [True,False]:success(execute(folder,"Probe",assertions=flag),"probe passed\n")
        family["clients"].append("Exact separately compiled README probe")
        for control in spec["controls"]:
            folder=copy_role("solution","rejected-"+control["name"])
            mutate(folder,control["file"],control["before"],control["after"])
            success(compile_role(folder))
            family["rejectedRegressions"].append(dict(name=control["name"],mode=control["mode"],
                expectedMarker=control.get("marker","AssertionError"),
                evidence=rejected(execute(folder,"ContractChecks",control["mode"]),control.get("marker","AssertionError"))))
        proof["families"].append(family)
    return proof


try:
    proof=verify();proof["processLog"]=str(LOG)
    (OUT/"proof.json").write_text(json.dumps(proof,indent=2)+"\n")
    print(json.dumps(dict(sourceCommit=proof["sourceCommit"],families=[
        dict(clinic=f["clinic"],files=sum(r["files"] for r in f["roles"]),
             baseline=f["roles"][0]["baseline"],complete=f["roles"][1]["full"],
             independentProjects=len(f["independentProjects"]),safeSteps=len(f["safeIntermediateSteps"]),
             rejectedRegressions=len(f["rejectedRegressions"]),rollbackChecks=len(f["rollbackChecks"]))
        for f in proof["families"]],processLog=str(LOG))))
finally:
    for folder in WORK.iterdir():
        if folder.is_dir():shutil.rmtree(folder)
