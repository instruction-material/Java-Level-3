#!/usr/bin/env python3
"""Verify behavioral choice, state transitions, template ordering and iterator lifetimes."""
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
native.TASK = "java-patterns-behavioral-contracts"
run = native.run

def verify() -> dict:
    java_home = os.environ.get("JAVA_HOME")
    java = str(Path(java_home) / "bin/java") if java_home else shutil.which("java")
    javac = str(Path(java_home) / "bin/javac") if java_home else shutil.which("javac")
    assert java and javac
    version = run([javac, "-version"], ROOT).strip()
    assert version.startswith("javac 21."), version
    result = {"jdk":version, "rolesCompiled":0, "sourceHashes":{},
              "unfinishedStartersRejected":0, "controlledRegressionsRejected":0,
              "compileBoundariesRejected":0, "optionalIndependentOfCore":False}
    source = ROOT / "DPJ6-Behavioral-Document-Packs"
    demo = "PUBLISHED\nReview: Checked\nUPPER\nCHECKED\nDONE\nEnd\nfirst\nsecond\n"
    def compile_at(folder):
        run([javac,"--release","21","-Xlint:all","-Werror","-d","classes","@sources.txt"],folder)
    def contracts(folder, *, core=False, success=True):
        return run([java,"-cp","classes","ContractTests",*(["core"] if core else [])],folder,success=success)
    optional = r"""Objects.requireNonNull(predicate, "predicate");
        List<String> snapshot = List.copyOf(entries), result = new ArrayList<>();
        for (String entry : snapshot) if (predicate.test(entry)) result.add(entry);
        return List.copyOf(result);"""
    with tempfile.TemporaryDirectory(prefix="java-behavioral-") as temporary:
        work = Path(temporary)
        for role in ["starter","solution"]:
            folder = source / role; output = work / role
            shutil.copytree(folder,output)
            listed = (output / "sources.txt").read_text().splitlines()
            assert len(listed) == len(set(listed)) == 9
            assert set(listed) == {str(p.relative_to(output)) for p in output.rglob("*.java")}
            files = sorted(p for p in folder.rglob("*") if p.is_file())
            assert len(files) == 11
            for file in files:
                result["sourceHashes"][str(file.relative_to(ROOT))] = hashlib.sha256(file.read_bytes()).hexdigest()
            compile_at(output);result["rolesCompiled"] += 1
            if role == "starter":
                failure = contracts(output,core=True,success=False)
                assert "UnsupportedOperationException: Implement the selected text strategy." in failure,failure
                result["unfinishedStartersRejected"] += 1
            else:
                core = contracts(output,core=True); full = contracts(output)
                core_match = re.fullmatch(r"Core behavioral contracts: (\d+) passed\n",core)
                full_match = re.fullmatch(r"Complete behavioral contracts: (\d+) passed\n",full)
                assert core_match and full_match,(core,full)
                assert int(core_match[1]) >= 65 and int(full_match[1]) == int(core_match[1]) + 9
                result.update(coreContracts=core.strip(),completeContracts=full.strip())
                assert run([java,"-cp","classes","Main"],output) == demo
        core_only = work / "core-only";shutil.copytree(source/"solution",core_only)
        file = core_only / "src/behavior/ReviewQueue.java";text = file.read_text()
        assert text.count(optional) == 1
        file.write_text(text.replace(optional,'throw new UnsupportedOperationException("Implement optional snapshot selection.");'))
        compile_at(core_only)
        assert contracts(core_only,core=True).strip() == result["coreContracts"]
        assert run([java,"-cp","classes","Main"],core_only) == demo
        assert "UnsupportedOperationException: Implement optional snapshot selection." in contracts(core_only,success=False)
        result["optionalIndependentOfCore"] = True
        for filename,old,new,core_mode in [
            ("RuleContext.java","return TextRule.line(rule.apply(TextRule.line(text)));","return TextRule.line(text);",True),
            ("Document.java","document.phase = REVIEW;","document.phase = PUBLISHED;",True),
            ("Document.java","document.phase = DRAFT;","document.phase = PUBLISHED;",True),
            ("ReportTemplate.java","List<String> lines = List.copyOf(input);","List<String> lines = input;",True),
            ("ReviewQueue.java","List<String> snapshot = List.copyOf(entries);","List<String> snapshot = entries;",True),
            ("ReviewQueue.java","return index < snapshot.size();","return ++index < snapshot.size();",True),
            ("ReviewQueue.java","if (predicate.test(entry))","if (true)",False)
        ]:
            output = work / ("regression-" + str(result["controlledRegressionsRejected"]))
            shutil.copytree(source/"solution",output)
            file = output / "src/behavior" / filename;text = file.read_text()
            assert text.count(old) == 1,(filename,old)
            file.write_text(text.replace(old,new));compile_at(output)
            failure = contracts(output,core=core_mode,success=False)
            assert "AssertionError" in failure,failure
            result["controlledRegressionsRejected"] += 1
        output = work / "solution"
        for code,marker in [
            ("import behavior.*; class ForeignClient { Object state = Document.Phases.DRAFT; }","private access"),
            ("import behavior.*; class ForeignClient extends ReportTemplate { protected String heading(){return \"H\";} protected String formatLine(String s){return s;} public String render(java.util.List<String> s){return \"skip\";} }","final"),
        ]:
            (output/"ForeignClient.java").write_text(code)
            failure = run([javac,"--release","21","-Xlint:all","-Werror","-cp","classes","ForeignClient.java"],output,success=False)
            assert marker in failure,failure
            result["compileBoundariesRejected"] += 1
        example_source = ROOT / "DPJ6-Behavioral-Worked-Example"
        example = work / "worked-example";shutil.copytree(example_source,example)
        assert {p.name for p in example_source.iterdir()} == {"Main.java","ContractTests.java","README.md"}
        for file in example_source.iterdir():
            result["sourceHashes"][str(file.relative_to(ROOT))] = hashlib.sha256(file.read_bytes()).hexdigest()
        run([javac,"--release","21","-Xlint:all","-Werror","Main.java","ContractTests.java"],example)
        worked = run([java,"ContractTests"],example)
        match = re.fullmatch(r"Behavioral worked contracts: (\d+) passed\n",worked)
        assert match and int(match[1]) >= 30,worked
        assert run([java,"Main"],example) == "LOCKED\nREADY\n[-one|-two]\na\nb\n"
        result.update(workedExamplesCompiled=1,workedExampleContracts=worked.strip())
    result.update(accepted=True,temporaryBuildsCleaned=True,learnerPatternChoiceReasoningInferred=False)
    return result

if __name__ == "__main__":
    print(json.dumps(verify(),sort_keys=True),flush=True)
