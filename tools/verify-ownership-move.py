#!/usr/bin/env python3
"""Check actual ownership moves, unchanged clients, package direction and public behavior."""
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
native.TASK="java-ownership-move-contracts"
run=native.run

def verify():
    home=os.environ.get("JAVA_HOME")
    javac=str(Path(home)/"bin/javac") if home else shutil.which("javac")
    java=str(Path(home)/"bin/java") if home else shutil.which("java")
    assert javac and java
    version=run([javac,"-version"],ROOT).strip()
    assert version.startswith("javac 21."),version
    source=ROOT/"DPR3-Contact-Ownership-Packs"
    proof={"jdk":version,"sourceHashes":{},"roles":{},"controlledRegressionsRejected":[],
           "learnerDesignReasoningInferred":False}
    def compile_at(folder):
        return run([javac,"--release","21","-Xlint:all","-Werror","-d","classes","@sources.txt"],folder)
    def contracts(folder,mode="all",success=True):
        return run([java,"-cp","classes","ContractTests",mode],folder,success=success)
    def count(text,label):
        match=re.fullmatch(re.escape(label)+r" ownership-move contracts: ([0-9]+) passed\n",text)
        assert match,text
        return int(match[1])
    demo="(212) 5550100\nMira <(212) 5550100>\nMira <(020) 70001234>\n020|70001234\n2024-01-08\n2024-01-08\n"
    member="src/contacts/Member.java";printer="src/contacts/ContactPrinter.java"
    with tempfile.TemporaryDirectory(prefix="java-ownership-move-") as temporary:
        work=Path(temporary)
        for role in ["starter","solution"]:
            original=source/role;folder=work/role;shutil.copytree(original,folder)
            files=[path for path in original.rglob("*") if path.is_file()]
            assert len(files)==(13 if role=="starter" else 14)
            listed=(original/"sources.txt").read_text().splitlines()
            assert len(listed)==len(set(listed))==(9 if role=="starter" else 10)
            assert set(listed)=={str(path.relative_to(original)) for path in original.rglob("*.java")}
            for path in files:
                proof["sourceHashes"][str(path.relative_to(ROOT))]=hashlib.sha256(path.read_bytes()).hexdigest()
            compile_at(folder)
            baseline=count(contracts(folder,"base"),"Baseline")
            core=count(contracts(folder,"core"),"Core")
            assert baseline>3000 and core>baseline
            for mode in [[],["base"]]:
                assert run([java,"-cp","classes","Main",*mode],folder)==demo
            record={"baseline":baseline,"core":core,"runnableBeforeEdits":True,
                    "unchangedExistingClientCompiled":True,"exactTwoModeDemo":True}
            if role=="starter":
                assert "UnsupportedOperationException: Implement optional masked format." in contracts(folder,success=False)
                record["optionalExplicitlyUnfinished"]=True
            else:
                complete=count(contracts(folder),"Complete");assert complete==core+80
                record["complete"]=complete
            proof["roles"][role]=record
        for name in ["Main.java","ContractTests.java","ClientCompatibility.java","README.md","DECISIONS.md",
                     "CHECKPOINTS.md","src/contacts/LegacyMember.java","src/contacts/LegacyPrinter.java",
                     "src/dates/ForeignDateMethods.java","src/dates/BusinessDate.java"]:
            assert (source/"starter"/name).read_bytes()==(source/"solution"/name).read_bytes()
        proof["baselineOracleClientAndDateComparisonsIdenticalAcrossRoles"]=True
        folder=work/"method-only";shutil.copytree(source/"starter",folder)
        path=folder/member;text=path.read_text()
        old="return ContactPrinter.telephone(this);"
        assert text.count(old)==1
        path.write_text(text.replace(old,'return "(" + area + ") " + number;'))
        path=folder/printer;text=path.read_text()
        old='Objects.requireNonNull(member, "member");\n        return "(" + member.areaCode() + ") " + member.localNumber();'
        assert text.count(old)==1
        path.write_text(text.replace(old,'return Objects.requireNonNull(member, "member").telephone();'))
        compile_at(folder);assert count(contracts(folder,"core"),"Core")==core
        proof["actualMethodMoveAndTemporaryBridgeIntermediateAccepted"]=True
        folder=work/"owner-isolation";shutil.copytree(source/"solution",folder)
        run([javac,"--release","21","-Xlint:all","-Werror","-d","owner-classes",
             "src/contacts/Member.java","src/contacts/Phone.java"],folder)
        proof["referenceOwnerCompilesWithoutPrinterClientOrLegacy"]=True
        target=(source/"solution"/member).read_text()
        assert "private Phone phone;" in target
        assert "phone.areaCode()" in target and "phone.localNumber()" in target
        assert "member.areaCode()" not in (source/"solution"/printer).read_text()
        proof["fieldsAndFormattingMovedToPhoneOldGetterBridgesRetained"]=True
        folder=work/"optional-independent";shutil.copytree(source/"solution",folder)
        text=(folder/member).read_text()
        (folder/member).write_text(text.replace("return phone.masked();",
            'throw new UnsupportedOperationException("Implement optional masked format.");'))
        compile_at(folder);assert count(contracts(folder,"core"),"Core")==core
        assert "UnsupportedOperationException: Implement optional masked format." in contracts(folder,success=False)
        proof["optionalIndependentOfCore"]=True
        mutations=[
            ("area-range","src/contacts/Phone.java","[0-9]{2,4}","[0-9]{2,5}","core"),
            ("format-rule","src/contacts/Phone.java",'return "(" + areaCode + ") " + localNumber;',
             'return areaCode + ") " + localNumber;',"core"),
            ("partial-update",member,"phone = new Phone(nextArea, nextNumber);",
             'phone = new Phone(nextArea, "0000");\n        phone = new Phone(nextArea, nextNumber);',"core"),
            ("name-identity",member,"this.name = name;","this.name = name.strip();","core"),
            ("strict-monday","src/dates/ForeignDateMethods.java","8 - date.getDayOfWeek().getValue()",
             "(8 - date.getDayOfWeek().getValue()) % 7","core"),
            ("mask-rule","src/contacts/Phone.java",'"*".repeat(localNumber.length() - 2)',
             '"*".repeat(localNumber.length() - 1)',"all")
        ]
        for label,name,old,new,mode in mutations:
            folder=work/label;shutil.copytree(source/"solution",folder)
            path=folder/name;text=path.read_text();assert text.count(old)==1,(label,old)
            path.write_text(text.replace(old,new));compile_at(folder)
            assert "AssertionError" in contracts(folder,mode,success=False)
            proof["controlledRegressionsRejected"].append(label)
        folder=work/"breaking-old-client";shutil.copytree(source/"solution",folder)
        text=(folder/member).read_text().replace("public String areaCode()","public String renamedAreaCode()")
        (folder/member).write_text(text)
        failure=run([javac,"--release","21","-d","classes","@sources.txt"],folder,success=False)
        assert "cannot find symbol" in failure and "areaCode" in failure
        proof["oldClientBreakingChangeRejectedAtCompile"]=True
        proof.update(accepted=True,temporaryBuildsCleaned=True)
    return proof

if __name__=="__main__":
    proof=verify()
    output=ROOT/"output/java-ownership-move-contracts/proof.json"
    output.parent.mkdir(parents=True,exist_ok=True)
    output.write_text(json.dumps(proof,indent=2)+"\n")
    print(json.dumps(proof,indent=2))
