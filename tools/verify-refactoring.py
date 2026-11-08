#!/usr/bin/env python3
"""Validate runnable refactoring sources and their protected behavioral baseline."""
from __future__ import annotations
import hashlib
import importlib.util
import json
import os
from pathlib import Path
import re
import shutil
import tempfile

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location("native_process", ROOT / "tools/verify-recursion.py")
assert spec and spec.loader
native = importlib.util.module_from_spec(spec)
spec.loader.exec_module(native)
native.TASK = "java-refactoring-contracts"
run = native.run

def count(output, label):
    found = re.fullmatch(re.escape(label) + r": ([0-9]+) passed\n", output)
    assert found, output
    return int(found[1])

def verify():
    home = os.environ.get("JAVA_HOME")
    javac = str(Path(home) / "bin/javac") if home else shutil.which("javac")
    java = str(Path(home) / "bin/java") if home else shutil.which("java")
    assert javac and java
    version = run([javac, "-version"], ROOT).strip()
    assert version.startswith("javac 21."), version
    result = {"jdk":version,"sourceHashes":{},"packs":[],"controlledRegressionsRejected":0,
              "rolesCompiled":0,"learnerDesignReasoningInferred":False}
    def compile_at(folder, success=True):
        return run([javac,"--release","21","-Xlint:all","-Werror","-d","classes","@sources.txt"],
                   folder,success=success)
    def contracts(folder, mode="all", success=True):
        return run([java,"-cp","classes","ContractTests",mode],folder,success=success)
    source = ROOT / "DPR1-Safe-Refactoring-Packs"
    proof = {"pack":source.name,"roles":{}}
    with tempfile.TemporaryDirectory(prefix="java-safe-refactoring-") as temporary:
        work = Path(temporary)
        expected_demo = "25\nTo Mira: Atlas | overdue=4 | charge=25\nMira|Atlas|4|25|READER\n0\n"
        for role in ["starter","solution"]:
            original = source / role
            folder = work / role
            shutil.copytree(original,folder)
            listed = (original/"sources.txt").read_text().splitlines()
            assert len(listed) == len(set(listed)) == 5
            assert set(listed) == {str(p.relative_to(original)) for p in original.rglob("*.java")}
            for path in sorted(original.rglob("*")):
                if path.is_file():
                    result["sourceHashes"][str(path.relative_to(ROOT))] = hashlib.sha256(path.read_bytes()).hexdigest()
            compile_at(folder)
            result["rolesCompiled"] += 1
            baseline = count(contracts(folder,"base"),"Baseline safe-refactoring contracts")
            core = count(contracts(folder,"core"),"Core safe-refactoring contracts")
            assert baseline >= 24000 and core == 2 * baseline, (baseline,core)
            for mode in [[],["base"]]:
                assert run([java,"-cp","classes","Main",*mode],folder) == expected_demo
            record = {"baselineChecks":baseline,"coreChecks":core,"runnableBeforeEdits":True}
            if role == "starter":
                assert "UnsupportedOperationException: Implement optional immutable notice batch." in contracts(folder,success=False)
                record["optionalExplicitlyUnfinished"] = True
            else:
                complete = count(contracts(folder),"Complete safe-refactoring contracts")
                assert complete > core + 5
                record["completeChecks"] = complete
            proof["roles"][role] = record
        for path in ["Main.java","ContractTests.java","README.md","DECISIONS.md","CHECKPOINTS.md",
                     "sources.txt","src/loans/Loan.java","src/loans/LegacyLoanDesk.java"]:
            assert (source/"starter"/path).read_bytes() == (source/"solution"/path).read_bytes(),path
        proof["protectedBaselineAndOracleIdenticalAcrossRoles"] = True
        relative = "src/loans/LoanDesk.java"
        starter = (source/"starter"/relative).read_text()
        renamed = work/"rename-only"
        shutil.copytree(source/"starter",renamed)
        (renamed/relative).write_text(re.sub(r"\bcost\b","charge",starter))
        compile_at(renamed)
        assert count(contracts(renamed,"core"),"Core safe-refactoring contracts") == core
        proof["actualRenameOnlyIntermediateAccepted"] = True
        one = work/"notice-only"
        shutil.copytree(source/"starter",one)
        before = ("        int cost = 0;\n        if (!loan.staff()) {\n"
                  "            if (loan.lateDays() > 3) cost = (loan.lateDays() - 3) * 25;\n        }")
        assert starter.count(before) == 2
        (one/relative).write_text(starter.replace(before,
            "        int cost = fee(loan.lateDays(), loan.staff());",1))
        compile_at(one)
        assert count(contracts(one,"core"),"Core safe-refactoring contracts") == core
        proof["actualOneEntrypointIntermediateAccepted"] = True
        broken = work/"failed-step-recovery"
        shutil.copytree(source/"starter",broken)
        (broken/relative).write_text(starter.replace("int cost = 0;","int changed = 0;",1))
        assert "cannot find symbol" in compile_at(broken,success=False)
        (broken/relative).write_text(starter)
        compile_at(broken)
        assert count(contracts(broken,"core"),"Core safe-refactoring contracts") == core
        proof["failedCompileStepRestoredFromExactGreenSource"] = True
        core_only = work/"core-only"
        shutil.copytree(source/"solution",core_only)
        text = (core_only/relative).read_text()
        begin = text.index("        List<Loan> snapshot")
        end = text.index("    }\n",begin)
        (core_only/relative).write_text(text[:begin]+'        throw new UnsupportedOperationException("Implement optional immutable notice batch.");\n'+text[end:])
        compile_at(core_only)
        assert count(contracts(core_only,"core"),"Core safe-refactoring contracts") == core
        assert "UnsupportedOperationException: Implement optional immutable notice batch." in contracts(core_only,success=False)
        proof["optionalIndependentOfCore"] = True
        mutations = [
            (relative,"FREE_DAYS = 3","FREE_DAYS = 4","core"),
            (relative,"DAILY_CENTS = 25","DAILY_CENTS = 26","core"),
            (relative,"staff ? 0 :","staff ? 1 :","core"),
            (relative,'"STAFF" : "READER"','"STAFF" : "MEMBER"',"core"),
            ("src/loans/Loan.java","lateDays > 365","lateDays > 366","core"),
            (relative,"return List.copyOf(notices);","return notices;","all"),
        ]
        for index,(name,old,new,mode) in enumerate(mutations):
            folder = work/("regression-"+str(index))
            shutil.copytree(source/"solution",folder)
            path = folder/name
            text = path.read_text()
            assert text.count(old) == 1,(name,old)
            path.write_text(text.replace(old,new))
            compile_at(folder)
            assert "AssertionError" in contracts(folder,mode,success=False)
            result["controlledRegressionsRejected"] += 1
        target = (source/"solution"/relative).read_text()
        assert target.count("fee(loan.lateDays(), loan.staff())") == 2
        proof["referenceBothFormattingEntrypointsUseTheFeeOperation"] = True
        proof["controlledRegressionsRejected"] = len(mutations)
        result["packs"].append(proof)
    result.update(accepted=True,temporaryBuildsCleaned=True)
    return result

if __name__ == "__main__":
    proof = verify()
    output = ROOT/"output/java-refactoring-contracts/proof.json"
    output.parent.mkdir(parents=True,exist_ok=True)
    output.write_text(json.dumps(proof,indent=2)+"\n")
    print(json.dumps(proof,indent=2))
