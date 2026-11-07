#!/usr/bin/env python3
"""Verify collaboration ownership, captured dispatch, undo, chain and visitor contracts."""
from __future__ import annotations
import hashlib
import importlib.util
import json
import os
from pathlib import Path
import re
import shutil
import tempfile

ROOT=Path(__file__).resolve().parents[1]
spec=importlib.util.spec_from_file_location("native_process",ROOT/"tools/verify-recursion.py")
assert spec and spec.loader
native=importlib.util.module_from_spec(spec);spec.loader.exec_module(native)
native.TASK="java-event-collaboration-contracts"
run=native.run

def verify():
    java_home=os.environ.get("JAVA_HOME")
    java=str(Path(java_home)/"bin/java") if java_home else shutil.which("java")
    javac=str(Path(java_home)/"bin/javac") if java_home else shutil.which("javac")
    assert java and javac
    version=run([javac,"-version"],ROOT).strip();assert version.startswith("javac 21."),version
    result={"jdk":version,"rolesCompiled":0,"unfinishedStartersRejected":0,"sourceHashes":{},
            "controlledRegressionsRejected":0,"compileBoundariesRejected":0}
    source=ROOT/"DPJ7-Editor-Collaboration-Packs"
    demo="replace:Ready->Checked\ntrue\nfalse\nundo:Checked->Ready\ntrue\nReady:0\n[a,[b,c]]\n3\n"
    def compile_at(folder):
        run([javac,"--release","21","-Xlint:all","-Werror","-d","classes","@sources.txt"],folder)
    def contracts(folder,core=False,success=True):
        return run([java,"-cp","classes","ContractTests",*(["core"] if core else [])],folder,success=success)
    optional=r"""Objects.requireNonNull(listener, "listener");
        boolean[] fired = {false};
        Subscription[] own = new Subscription[1];
        own[0] = subscribe(event -> {
            if (fired[0]) return;
            fired[0] = true;
            own[0].close();
            listener.accept(event);
        });
        return own[0];"""
    with tempfile.TemporaryDirectory(prefix="java-collaboration-") as temporary:
        work=Path(temporary)
        for role in ["starter","solution"]:
            folder=source/role;output=work/role;shutil.copytree(folder,output)
            listed=(output/"sources.txt").read_text().splitlines()
            assert len(listed)==len(set(listed))==11
            assert set(listed)=={str(p.relative_to(output)) for p in output.rglob("*.java")}
            files=sorted(p for p in folder.rglob("*") if p.is_file());assert len(files)==13
            for file in files:result["sourceHashes"][str(file.relative_to(ROOT))]=hashlib.sha256(file.read_bytes()).hexdigest()
            compile_at(output);result["rolesCompiled"]+=1
            if role=="starter":
                failure=contracts(output,core=True,success=False)
                assert "UnsupportedOperationException: Implement owner-bound snapshot restoration." in failure,failure
                result["unfinishedStartersRejected"]+=1
            else:
                core=contracts(output,core=True);full=contracts(output)
                one=re.fullmatch(r"Core collaboration contracts: (\d+) passed\n",core)
                two=re.fullmatch(r"Complete collaboration contracts: (\d+) passed\n",full)
                assert one and two,(core,full)
                assert int(one[1])>=110 and int(two[1])==int(one[1])+12
                result.update(coreContracts=core.strip(),completeContracts=full.strip())
                assert run([java,"-cp","classes","Main"],output)==demo
        core_only=work/"core-only";shutil.copytree(source/"solution",core_only)
        file=core_only/"src/editor/EventBus.java";text=file.read_text();assert text.count(optional)==1
        file.write_text(text.replace(optional,'throw new UnsupportedOperationException("Implement optional once-only subscription.");'))
        compile_at(core_only)
        assert contracts(core_only,core=True).strip()==result["coreContracts"]
        assert run([java,"-cp","classes","Main"],core_only)==demo
        assert "UnsupportedOperationException: Implement optional once-only subscription." in contracts(core_only,success=False)
        result["optionalIndependentOfCore"]=True
        for name,old,new,core_mode in [
            ("TextDocument.java",'if (snapshot.owner != this) throw new IllegalArgumentException("foreign snapshot");',"",True),
            ("EventBus.java","for (Entry entry : List.copyOf(entries))","for (Entry entry : List.copyOf(entries).reversed())",True),
            ("EventBus.java","entry.listener.accept(event);","if (entries.contains(entry)) entry.listener.accept(event);",True),
            ("ReplaceCommand.java","before = document.snapshot();\n        document.replace(replacement);","document.replace(replacement);\n        before = document.snapshot();",True),
            ("Validation.java","if (!result.accepted()) return result;","if (!result.accepted()) { }",True),
            ("EditorMediator.java","history.push(command);\n            events.publish(new EditEvent(EditEvent.Kind.REPLACE, before, document.text()));","events.publish(new EditEvent(EditEvent.Kind.REPLACE, before, document.text()));\n            history.push(command);",True),
            ("EditorMediator.java","history.pop();\n            events.publish(new EditEvent(EditEvent.Kind.UNDO, before, document.text()));","events.publish(new EditEvent(EditEvent.Kind.UNDO, before, document.text()));\n            history.pop();",True),
            ("TextVisitor.java","for (Nodes.Node child : node.children())","for (Nodes.Node child : node.children().reversed())",True),
            ("EventBus.java","if (fired[0]) return;","",False),
        ]:
            output=work/("regression-"+str(result["controlledRegressionsRejected"]));shutil.copytree(source/"solution",output)
            file=output/"src/editor"/name;text=file.read_text();assert text.count(old)==1,(name,old)
            file.write_text(text.replace(old,new));compile_at(output)
            failure=contracts(output,core=core_mode,success=False);assert "AssertionError" in failure,failure
            result["controlledRegressionsRejected"]+=1
        output=work/"solution"
        for code,marker in [
            ("import editor.*; class ForeignClient { Object reveal(TextDocument.Snapshot value) { return value.text; } }","private access"),
            ("import editor.*; class ForeignClient implements Nodes.Node { public <R> R accept(Nodes.Visitor<R> visitor) { return null; } }","sealed"),
        ]:
            (output/"ForeignClient.java").write_text(code)
            failure=run([javac,"--release","21","-Xlint:all","-Werror","-cp","classes","ForeignClient.java"],output,success=False)
            assert marker in failure,failure;result["compileBoundariesRejected"]+=1
        example_source=ROOT/"DPJ7-Collaboration-Worked-Example";example=work/"worked-example"
        shutil.copytree(example_source,example)
        assert {p.name for p in example_source.iterdir()}=={"Main.java","ContractTests.java","README.md"}
        for file in example_source.iterdir():result["sourceHashes"][str(file.relative_to(ROOT))]=hashlib.sha256(file.read_bytes()).hexdigest()
        run([javac,"--release","21","-Xlint:all","-Werror","Main.java","ContractTests.java"],example)
        worked=run([java,"ContractTests"],example)
        match=re.fullmatch(r"Collaboration worked contracts: (\d+) passed\n",worked)
        assert match and int(match[1])>=40,worked
        assert run([java,"Main"],example)=="0->2\ntrue\nfalse\n2->0\ntrue\n(2+3)\n5\n"
        result.update(workedExamplesCompiled=1,workedExampleContracts=worked.strip())
    result.update(accepted=True,temporaryBuildsCleaned=True,learnerPatternChoiceReasoningInferred=False)
    return result

if __name__=="__main__":
    print(json.dumps(verify(),sort_keys=True),flush=True)
