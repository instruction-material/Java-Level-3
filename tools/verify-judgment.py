#!/usr/bin/env python3
"""Verify a preserved refactoring baseline, exact outputs and independent optional policy."""
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
native.TASK="java-refactoring-judgment-contracts"
run=native.run

def verify():
    java_home=os.environ.get("JAVA_HOME")
    java=str(Path(java_home)/"bin/java") if java_home else shutil.which("java")
    javac=str(Path(java_home)/"bin/javac") if java_home else shutil.which("javac")
    assert java and javac
    version=run([javac,"-version"],ROOT).strip();assert version.startswith("javac 21."),version
    result={"jdk":version,"rolesCompiled":0,"unfinishedStartersRejected":0,"sourceHashes":{},
            "controlledRegressionsRejected":0,"compileBoundariesRejected":0}
    source=ROOT/"DPJ9-Checkout-Judgment-Packs"
    baseline="REGULAR|STANDARD|10|2000|0|2000\n2700\n"
    demo="REGULAR|STANDARD|10|2000|0|2000\nREGULAR|STANDARD|10|2000|0|2000\n2700\n"
    def compile_at(folder):
        run([javac,"--release","21","-Xlint:all","-Werror","-d","classes","@sources.txt"],folder)
    def contracts(folder,mode="",success=True):
        return run([java,"-cp","classes","ContractTests",*([mode] if mode else [])],folder,success=success)
    with tempfile.TemporaryDirectory(prefix="java-judgment-") as temporary:
        work=Path(temporary)
        for role in ["starter","solution"]:
            folder=source/role;output=work/role;shutil.copytree(folder,output)
            listed=(output/"sources.txt").read_text().splitlines()
            assert len(listed)==len(set(listed))==6
            assert set(listed)=={str(p.relative_to(output)) for p in output.rglob("*.java")}
            files=sorted(p for p in folder.rglob("*") if p.is_file());assert len(files)==9
            for file in files:result["sourceHashes"][str(file.relative_to(ROOT))]=hashlib.sha256(file.read_bytes()).hexdigest()
            compile_at(output);result["rolesCompiled"]+=1
            base=contracts(output,"base");assert base=="Baseline judgment contracts: 1221 passed\n",base
            assert run([java,"-cp","classes","Main","base"],output)==baseline
            result["baselineContracts"]=base.strip()
            if role=="starter":
                failure=contracts(output,"core",success=False)
                assert "UnsupportedOperationException: Implement the behavior-preserving quote." in failure,failure
                result["unfinishedStartersRejected"]+=1
            else:
                core=contracts(output,"core");full=contracts(output)
                assert core=="Core judgment contracts: 2435 passed\n",core
                assert full=="Complete judgment contracts: 2445 passed\n",full
                result.update(coreContracts=core.strip(),completeContracts=full.strip())
                assert run([java,"-cp","classes","Main"],output)==demo
        identical=["Main.java","ContractTests.java","README.md","sources.txt","src/checkout/Order.java","src/checkout/Totals.java","src/checkout/LegacyCheckout.java"]
        for name in identical:assert (source/"starter"/name).read_bytes()==(source/"solution"/name).read_bytes()
        result["baselineAndHarnessIdenticalAcrossRoles"]=True
        core_only=work/"core-only";shutil.copytree(source/"solution",core_only)
        file=core_only/"src/checkout/Checkout.java";text=file.read_text()
        start=text.index("\n",text.index("public static Totals quoteWith("))+1;end=text.index("    }\n",start)
        file.write_text(text[:start]+'        throw new UnsupportedOperationException("Implement optional injected shipping.");\n'+text[end:])
        compile_at(core_only)
        assert contracts(core_only,"core").strip()==result["coreContracts"]
        assert run([java,"-cp","classes","Main"],core_only)==demo
        assert "UnsupportedOperationException: Implement optional injected shipping." in contracts(core_only,success=False)
        result["optionalIndependentOfCore"]=True
        mutations=[
            ("Checkout.java","subtotal>=2000?0:500","subtotal>2000?0:500","core"),
            ("Checkout.java","Order.Tier.MEMBER?180:200","Order.Tier.MEMBER?200:180","core"),
            ("Checkout.java",'totals.shipping()+"|"','0+"|"',"core"),
            ("LegacyCheckout.java","shipping=900;","shipping=901;","base"),
            ("Checkout.java","int fee=policy.applyAsInt(order);","int fee=policy.applyAsInt(order)+policy.applyAsInt(order);",""),
        ]
        for index,(name,old,new,mode) in enumerate(mutations):
            output=work/("regression-"+str(index));shutil.copytree(source/"solution",output)
            file=output/"src/checkout"/name;text=file.read_text();assert old in text,(name,old)
            file.write_text(text.replace(old,new,1));compile_at(output)
            failure=contracts(output,mode,success=False);assert "AssertionError" in failure,failure
            result["controlledRegressionsRejected"]+=1
        output=work/"solution"
        (output/"ForeignClient.java").write_text("import checkout.Order; class ForeignClient { int read(Order order){return order.units;} }")
        failure=run([javac,"--release","21","-Xlint:all","-Werror","-cp","classes","ForeignClient.java"],output,success=False)
        assert "private access" in failure,failure
        result["compileBoundariesRejected"]=1
        example_source=ROOT/"DPJ9-Judgment-Worked-Example";example=work/"worked-example";shutil.copytree(example_source,example)
        assert {p.name for p in example_source.iterdir()}=={"Main.java","ContractTests.java","README.md"}
        for file in example_source.iterdir():result["sourceHashes"][str(file.relative_to(ROOT))]=hashlib.sha256(file.read_bytes()).hexdigest()
        run([javac,"--release","21","-Xlint:all","-Werror","Main.java","ContractTests.java"],example)
        worked=run([java,"ContractTests"],example);assert worked=="Judgment worked contracts: 49 passed\n",worked
        assert run([java,"Main"],example)=="NOTICE: HELLO\nNOTICE: HELLO\nNOTICE: HELLO\n"
        result.update(workedExamplesCompiled=1,workedExampleContracts=worked.strip())
    result.update(accepted=True,temporaryBuildsCleaned=True,learnerRefactoringReasoningInferred=False)
    return result

if __name__=="__main__":print(json.dumps(verify(),sort_keys=True),flush=True)
