#!/usr/bin/env python3
"""Verify DI, real provider discovery, repository/service direction and view snapshots."""
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
native.TASK="java-notebook-architecture-contracts"
run=native.run

def verify():
    java_home=os.environ.get("JAVA_HOME")
    def executable(name):
        value=str(Path(java_home)/"bin"/name) if java_home else shutil.which(name)
        assert value
        return value
    java,javac,jar=[executable(name) for name in ["java","javac","jar"]]
    version=run([javac,"-version"],ROOT).strip()
    assert version.startswith("javac 21."),version
    source=ROOT/"DPJ8-Notebook-Architecture-Packs"
    descriptor="META-INF/services/notes.plugins.TextPlugin"
    result={"jdk":version,"rolesCompiled":0,"unfinishedStartersRejected":0,"sourceHashes":{},
            "controlledRegressionsRejected":0,"descriptorBoundaryChecks":0}
    demo="[bracket, upper]\na:[HI]\n[a=[HI]]\na:[Ready]\n[a=[Ready]]\n1\n"
    def build(folder):
        run([javac,"--release","21","-Xlint:all","-Werror","-d","classes","@sources.txt"],folder)
        run([jar,"--create","--file","notebook.jar","-C","classes",".","-C",".","META-INF"],folder)
    def contracts(folder,core=False,success=True):
        return run([java,"-cp","notebook.jar","ContractTests",*(["core"] if core else [])],folder,success=success)
    with tempfile.TemporaryDirectory(prefix="java-architecture-") as temporary:
        work=Path(temporary)
        for role in ["starter","solution"]:
            folder=source/role;output=work/role;shutil.copytree(folder,output)
            files=sorted(p for p in folder.rglob("*") if p.is_file())
            assert len(files)==15
            listed=(output/"sources.txt").read_text().splitlines()
            assert len(listed)==len(set(listed))==12
            assert set(listed)=={str(p.relative_to(output)) for p in output.rglob("*.java")}
            assert (output/descriptor).read_text()=="notes.infra.UppercasePlugin\nnotes.infra.BracketPlugin\n"
            for file in files:result["sourceHashes"][str(file.relative_to(ROOT))]=hashlib.sha256(file.read_bytes()).hexdigest()
            build(output);result["rolesCompiled"]+=1
            if role=="starter":
                failure=contracts(output,core=True,success=False)
                assert "UnsupportedOperationException: Implement repository lookup." in failure,failure
                result["unfinishedStartersRejected"]+=1
            else:
                core=contracts(output,core=True);full=contracts(output)
                one=re.fullmatch(r"Core architecture contracts: (\d+) passed\n",core)
                two=re.fullmatch(r"Complete architecture contracts: (\d+) passed\n",full)
                assert one and two and int(one[1])>=90 and int(two[1])==int(one[1])+10,(core,full)
                result.update(coreContracts=core.strip(),completeContracts=full.strip())
                assert run([java,"-cp","notebook.jar","Main"],output)==demo
        # Architecture fitness: compile the service and its ports without either infrastructure or UI.
        isolated=work/"ports-only";isolated.mkdir()
        (isolated/"empty-source").mkdir()
        paths=[str(p) for p in (source/"solution"/"src/notes").rglob("*.java")
               if "/infra/" not in str(p) and "/ui/" not in str(p)]
        run([javac,"--release","21","-Xlint:all","-Werror","-sourcepath","empty-source","-d","classes",*paths],isolated)
        result["serviceCompilesWithoutInfrastructureOrUI"]=True
        core_only=work/"core-only";shutil.copytree(source/"solution",core_only)
        file=core_only/"src/notes/application/NoteService.java"
        text=file.read_text();start=text.index("        enter();",text.index("public Note copy("));end=text.index("    }\n",start)
        text=text[:start]+'        throw new UnsupportedOperationException("Implement optional note copying.");\n'+text[end:]
        file.write_text(text);build(core_only)
        assert contracts(core_only,core=True).strip()==result["coreContracts"]
        assert run([java,"-cp","notebook.jar","Main"],core_only)==demo
        assert "UnsupportedOperationException: Implement optional note copying." in contracts(core_only,success=False)
        result["optionalIndependentOfCore"]=True
        mutations=[
            ("infra/InMemoryNotes.java","return List.copyOf(notes.values());","return new java.util.ArrayList<>(notes.values());",True),
            ("plugins/PluginCatalog.java","for (TextPlugin plugin : selected)","for (TextPlugin plugin : selected.reversed())",True),
            ("plugins/PluginCatalog.java","selected.add(plugin);","selected.add(plugin); plugin.apply(result);",True),
            ("application/NoteService.java","repository.save(note);\n            sink.saved(note);","sink.saved(note);\n            repository.save(note);",True),
            ("ui/NoteViewModel.java","state=new State(rows);","state=new State(List.of());",True),
            ("plugins/PluginCatalog.java","return new PluginCatalog(ServiceLoader.load(TextPlugin.class,loader));","return new PluginCatalog(List.of());",True),
            ("application/NoteService.java","new Note(targetId,plugins.apply(source.text(),pipeline))","new Note(sourceId,plugins.apply(source.text(),pipeline))",False),
        ]
        for index,(path,old,new,core_mode) in enumerate(mutations):
            output=work/("regression-"+str(index));shutil.copytree(source/"solution",output)
            file=output/"src/notes"/path;text=file.read_text();assert old in text,(path,old)
            file.write_text(text.replace(old,new,1));build(output)
            failure=contracts(output,core=core_mode,success=False)
            assert "AssertionError" in failure,failure
            result["controlledRegressionsRejected"]+=1
        for index,data in enumerate(["","missing.Provider\n"]):
            output=work/("descriptor-"+str(index));shutil.copytree(source/"solution",output)
            (output/descriptor).write_text(data);build(output)
            failure=run([java,"-cp","notebook.jar","Main"],output,success=False)
            assert ("unknown plugin" if not data else "ServiceConfigurationError") in failure,failure
            result["descriptorBoundaryChecks"]+=1
        # An empty discovery is explicit and does not inject a fallback provider.
        output=work/"empty-discovery";shutil.copytree(source/"solution",output)
        (output/descriptor).write_text("")
        (output/"EmptyDiscovery.java").write_text('import notes.plugins.*; class EmptyDiscovery { public static void main(String[] x) { System.out.println(PluginCatalog.discover(EmptyDiscovery.class.getClassLoader()).availableIds()); } }')
        with (output/"sources.txt").open("a") as stream:stream.write("EmptyDiscovery.java\n")
        build(output)
        assert run([java,"-cp","notebook.jar","EmptyDiscovery"],output)=="[]\n"
        result["emptyDiscoveryExplicit"]=True
        example_source=ROOT/"DPJ8-Architecture-Worked-Example";example=work/"worked-example";shutil.copytree(example_source,example)
        assert {p.name for p in example_source.iterdir()}=={"Main.java","ContractTests.java","README.md"}
        for file in example_source.iterdir():result["sourceHashes"][str(file.relative_to(ROOT))]=hashlib.sha256(file.read_bytes()).hexdigest()
        run([javac,"--release","21","-Xlint:all","-Werror","Main.java","ContractTests.java"],example)
        worked=run([java,"ContractTests"],example)
        match=re.fullmatch(r"Architecture worked contracts: (\d+) passed\n",worked);assert match and int(match[1])>=35,worked
        assert run([java,"Main"],example)=="1:HI\n[1=HI]\n2:READY\n[1=HI]\n[1=HI, 2=READY]\n"
        result.update(workedExamplesCompiled=1,workedExampleContracts=worked.strip())
    result.update(accepted=True,temporaryBuildsCleaned=True,learnerArchitectureReasoningInferred=False)
    return result

if __name__=="__main__":print(json.dumps(verify(),sort_keys=True),flush=True)
