#!/usr/bin/env python3
"""Verify composing-methods source, real intermediate states and behavioral regressions."""
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
native.TASK="java-composing-methods-contracts"
run=native.run

def verify():
    home=os.environ.get("JAVA_HOME")
    javac=str(Path(home)/"bin/javac") if home else shutil.which("javac")
    java=str(Path(home)/"bin/java") if home else shutil.which("java")
    assert javac and java
    version=run([javac,"-version"],ROOT).strip()
    assert version.startswith("javac 21."),version
    source=ROOT/"DPR2-Composing-Methods-Packs"
    proof={"jdk":version,"sourceHashes":{},"roles":{},"intermediates":[],
           "controlledRegressionsRejected":[],"learnerDesignReasoningInferred":False}
    def compile_at(folder):
        run([javac,"--release","21","-Xlint:all","-Werror","-d","classes","@sources.txt"],folder)
    def contract(folder,mode="all",success=True):
        return run([java,"-cp","classes","ContractTests",mode],folder,success=success)
    def count(text,label):
        match=re.fullmatch(re.escape(label)+r" composing-methods contracts: ([0-9]+) passed\n",text)
        assert match,text
        return int(match[1])
    demo="count=3|positive=2|total=30|mean=10\n3\n[read:0, bonus:0, read:1, read:2, bonus:2]\n"
    relative="src/reports/ComposedReport.java"
    with tempfile.TemporaryDirectory(prefix="java-composing-methods-") as temporary:
        work=Path(temporary)
        for role in ["starter","solution"]:
            original=source/role;folder=work/role;shutil.copytree(original,folder)
            listed=(original/"sources.txt").read_text().splitlines()
            assert len(listed)==len(set(listed))==6
            assert set(listed)=={str(path.relative_to(original)) for path in original.rglob("*.java")}
            files=[path for path in original.rglob("*") if path.is_file()]
            assert len(files)==10
            for path in files:
                proof["sourceHashes"][str(path.relative_to(ROOT))]=hashlib.sha256(path.read_bytes()).hexdigest()
            compile_at(folder)
            baseline=count(contract(folder,"base"),"Baseline")
            core=count(contract(folder,"core"),"Core")
            comparison=count(contract(folder,"object"),"Method-object")
            assert baseline>6500 and core==2*baseline and comparison==baseline
            for mode in [[],["base"],["object"]]:
                assert run([java,"-cp","classes","Main",*mode],folder)==demo
            record={"baseline":baseline,"core":core,"methodObject":comparison,
                    "runnableBeforeEdits":True,"exactThreeModeDemo":True}
            if role=="starter":
                assert "UnsupportedOperationException: Implement optional array adapter." in contract(folder,success=False)
                record["optionalExplicitlyUnfinished"]=True
            else:
                complete=count(contract(folder),"Complete");assert complete==core+8
                record["complete"]=complete
            proof["roles"][role]=record
        for path in (source/"starter").rglob("*"):
            if path.is_file() and str(path.relative_to(source/"starter"))!=relative:
                assert path.read_bytes()==(source/"solution"/path.relative_to(source/"starter")).read_bytes()
        proof["baselineOracleAndComparisonIdenticalAcrossRoles"]=True
        starter=(source/"starter"/relative).read_text()
        old="""            long temporary = next.getAsInt();
            if (temporary > 0) positive++;
            if (includeExtra && temporary > 0) {
                trace.accept("bonus:" + i);
                temporary = temporary + temporary / 10;
            }
            total = total + temporary;"""
        new="""            int sample = next.getAsInt();
            if (sample > 0) positive++;
            long adjusted = sample;
            if (includeExtra && sample > 0) {
                trace.accept("bonus:" + i);
                adjusted += sample / 10;
            }
            total += adjusted;"""
        assert starter.count(old)==1
        split=starter.replace(old,new)
        pure=split.replace("long mean = 0;\n        if (count > 0) mean = total / count;",
                           "long mean = mean(total, count);")
        pure=pure.replace("    public static Summary summarizeValues",
                         "    private static long mean(long total, int count) {\n"
                         "        return count == 0 ? 0 : total / count;\n    }\n"
                         "    public static Summary summarizeValues")
        for label,text in [("split-temporary",split),("pure-mean-query",pure)]:
            folder=work/label;shutil.copytree(source/"starter",folder)
            (folder/relative).write_text(text);compile_at(folder)
            assert count(contract(folder,"core"),"Core")==core
            proof["intermediates"].append(label)
        target=(source/"solution"/relative).read_text()
        folder=work/"core-only";shutil.copytree(source/"solution",folder)
        start=target.index("        int[] snapshot")
        end=target.index("    }\n",start)
        (folder/relative).write_text(target[:start]+
            '        throw new UnsupportedOperationException("Implement optional array adapter.");\n'+target[end:])
        compile_at(folder)
        assert count(contract(folder,"core"),"Core")==core
        assert "UnsupportedOperationException: Implement optional array adapter." in contract(folder,success=False)
        proof["optionalIndependentOfCore"]=True
        mutations=[
            ("int-overflow","long adjusted = sample;","int adjusted = sample;","core"),
            ("zero-is-positive","includeExtra && sample > 0","includeExtra && sample >= 0","core"),
            ("negative-division","total / count","Math.floorDiv(total, count)","core"),
            ("reordered-effects",'trace.accept("read:" + index);\n        return next.getAsInt();',
             'int value = next.getAsInt();\n        trace.accept("read:" + index);\n        return value;',"core"),
            ("wrong-call-count","return next.getAsInt();","next.getAsInt();\n        return next.getAsInt();","core"),
            ("array-adapter",'return summarize(snapshot.length, includeExtra, () -> snapshot[cursor[0]++], event -> {});',
             "return new Summary(0, 0, 0, 0);","all")
        ]
        for label,old,new,mode in mutations:
            folder=work/label;shutil.copytree(source/"solution",folder)
            assert target.count(old)==1,(label,old)
            (folder/relative).write_text(target.replace(old,new))
            compile_at(folder)
            failure=contract(folder,mode,success=False)
            assert "AssertionError" in failure or (label=="wrong-call-count" and "ArrayIndexOutOfBoundsException" in failure),failure
            proof["controlledRegressionsRejected"].append(label)
        proof.update(accepted=True,temporaryBuildsCleaned=True)
    return proof

if __name__=="__main__":
    proof=verify()
    output=ROOT/"output/java-composing-methods-contracts/proof.json"
    output.parent.mkdir(parents=True,exist_ok=True)
    output.write_text(json.dumps(proof,indent=2)+"\n")
    print(json.dumps(proof,indent=2))
