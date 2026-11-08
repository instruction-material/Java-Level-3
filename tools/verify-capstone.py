#!/usr/bin/env python3
"""Compile the actual runnable capstone roles, an intermediate refactor and independent contracts."""
from __future__ import annotations
import hashlib
import importlib.util
import json
import os
from pathlib import Path
import shutil
import tempfile

ROOT=Path(__file__).resolve().parents[1]
spec=importlib.util.spec_from_file_location("native_process",ROOT/"tools/verify-recursion.py")
assert spec and spec.loader
native=importlib.util.module_from_spec(spec);spec.loader.exec_module(native)
native.TASK="java-dispatch-capstone-contracts"
run=native.run

def verify():
    home=os.environ.get("JAVA_HOME")
    java=str(Path(home)/"bin/java") if home else shutil.which("java")
    javac=str(Path(home)/"bin/javac") if home else shutil.which("javac")
    assert java and javac
    version=run([javac,"-version"],ROOT).strip();assert version.startswith("javac 21."),version
    source=ROOT/"DPJ10-Dispatch-Refactor-Packs"
    result={"jdk":version,"sourceHashes":{},"rolesCompiled":0,"controlledRegressionsRejected":0}
    demo="EMAIL: Hi\nsent:a\n200\nsent:b\n[a, b]\n"
    def compile_at(folder):
        run([javac,"--release","21","-Xlint:all","-Werror","-d","classes","@sources.txt"],folder)
    def contracts(folder,mode="",success=True):
        return run([java,"-cp","classes","ContractTests",*([mode] if mode else [])],folder,success=success)
    with tempfile.TemporaryDirectory(prefix="java-dispatch-capstone-") as temporary:
        work=Path(temporary)
        for role,count in [("starter",9),("solution",14)]:
            folder=source/role;output=work/role;shutil.copytree(folder,output)
            listed=(folder/"sources.txt").read_text().splitlines()
            assert len(listed)==len(set(listed))==count,(role,listed)
            assert set(listed)=={str(p.relative_to(folder)) for p in folder.rglob("*.java")}
            for path in sorted(folder.rglob("*")):
                if path.is_file():result["sourceHashes"][str(path.relative_to(ROOT))]=hashlib.sha256(path.read_bytes()).hexdigest()
            compile_at(output);result["rolesCompiled"]+=1
            base=contracts(output,"base");core=contracts(output,"core")
            assert base=="Baseline capstone contracts: 2433 passed\n",base
            assert core=="Core capstone contracts: 4854 passed\n",core
            assert run([java,"-cp","classes","Main","base"],output)==demo
            assert run([java,"-cp","classes","Main"],output)==demo
            if role=="starter":
                assert "UnsupportedOperationException: Implement optional prefix-commit batch." in contracts(output,success=False)
            else:
                full=contracts(output);assert full=="Complete capstone contracts: 4869 passed\n",full
                architecture=run([java,"-cp","classes","ArchitectureTests"],output)
                assert architecture=="Architecture capstone contracts: 30 passed\n",architecture
                result.update(completeContracts=full.strip(),architectureContracts=architecture.strip())
            result.update(baselineContracts=base.strip(),coreContracts=core.strip())
        for name in ["Main.java","ContractTests.java","README.md","CHECKPOINTS.md","src/dispatch/domain/Request.java","src/dispatch/domain/Dispatch.java","src/dispatch/ports/DeskApi.java","src/dispatch/ports/NoticeSink.java","src/dispatch/legacy/LegacyDesk.java"]:
            assert (source/"starter"/name).read_bytes()==(source/"solution"/name).read_bytes(),name
        result["baselineAndOraclePreservedAcrossRoles"]=True
        # Actual runnable intermediate: centralize the repeated rules before adding ports.
        intermediate=work/"computation-only";shutil.copytree(source/"starter",intermediate)
        path=intermediate/"src/dispatch/app/Desk.java";text=path.read_text()
        start=text.index("            String rendered;",text.index("public Dispatch send"))
        end=text.index("\n            sent.put",start)
        path.write_text(text[:start]+"            Dispatch result=preview(request);"+text[end:])
        compile_at(intermediate)
        assert contracts(intermediate,"core").strip()==result["coreContracts"]
        assert run([java,"-cp","classes","Main"],intermediate)==demo
        result["computationOnlyIntermediateAccepted"]=True
        # Core behavior remains available before the optional batch.
        core_only=work/"core-only";shutil.copytree(source/"solution",core_only)
        path=core_only/"src/dispatch/app/Desk.java";text=path.read_text()
        start=text.index("\n",text.index("public List<Dispatch> sendBatch"))+1
        end=text.index("    }\n",start)
        path.write_text(text[:start]+'        throw new UnsupportedOperationException("Implement optional prefix-commit batch.");\n'+text[end:])
        compile_at(core_only)
        assert contracts(core_only,"core").strip()==result["coreContracts"]
        assert "UnsupportedOperationException: Implement optional prefix-commit batch." in contracts(core_only,success=False)
        result["optionalIndependentOfCore"]=True
        # Compile application/domain/ports alone to prove dependency direction.
        selected=[str(p.relative_to(source/"solution")) for folder in ["src/dispatch/domain","src/dispatch/ports","src/dispatch/app"] for p in (source/"solution"/folder).rglob("*.java")]
        isolated=work/"isolated";isolated.mkdir()
        for name in selected:
            path=isolated/name;path.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(source/"solution"/name,path)
        run([javac,"--release","21","-Xlint:all","-Werror","-d","classes",*sorted(selected)],isolated)
        result["applicationCompilesWithoutInfrastructureOrLegacy"]=True
        mutations=[
            ("src/dispatch/infra/StandardRenderer.java","case EMAIL -> 100;","case EMAIL -> 101;","core","ContractTests"),
            ("src/dispatch/infra/StandardRenderer.java",'"EMAIL: "','"MAIL: "',"core","ContractTests"),
            ("src/dispatch/app/Desk.java","repository.insert(result);\n            sink.sent(result);","sink.sent(result);\n            repository.insert(result);","core","ContractTests"),
            ("src/dispatch/app/Desk.java","return List.copyOf(repository.all());","return new ArrayList<>(repository.all());","core","ContractTests"),
            ("src/dispatch/app/Desk.java","finally { busy=false; }","finally { busy=true; }","core","ContractTests"),
            ("src/dispatch/app/Desk.java","List<Request> selected=List.copyOf(Objects.requireNonNull(requests,\"requests\"));","List<Request> selected=Objects.requireNonNull(requests,\"requests\");","","ContractTests"),
            ("src/dispatch/app/Desk.java","renderer.render(request)","new dispatch.infra.StandardRenderer().render(request)","","ArchitectureTests"),
        ]
        # Mutants are derivative temporary sources, never replacements for the accepted artifact.
        for index,(name,old,new,mode,entry) in enumerate(mutations):
            folder=work/("regression-"+str(index));shutil.copytree(source/"solution",folder)
            path=folder/name;text=path.read_text();assert text.count(old)==1,(name,old)
            path.write_text(text.replace(old,new,1));compile_at(folder)
            failure=run([java,"-cp","classes",entry,*([mode] if mode else [])],folder,success=False)
            assert "AssertionError" in failure,failure
            result["controlledRegressionsRejected"]+=1
    result.update(accepted=True,temporaryBuildsCleaned=True,learnerDesignReasoningInferred=False)
    return result

if __name__=="__main__":print(json.dumps(verify(),sort_keys=True),flush=True)
