#!/usr/bin/env python3
"""Verify actual prototype, adapter and facade role packs with Java 21."""
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
native.TASK = "java-patterns-boundary-contracts"
run = native.run

def verify() -> dict:
    java_home = os.environ.get("JAVA_HOME")
    java = str(Path(java_home) / "bin/java") if java_home else shutil.which("java")
    javac = str(Path(java_home) / "bin/javac") if java_home else shutil.which("javac")
    assert java and javac
    version = run([javac, "-version"], ROOT).strip()
    assert version.startswith("javac 21."), version
    result = {"jdk": version, "rolesCompiled": 0, "sourceHashes": {},
              "unfinishedStartersRejected": 0, "controlledRegressionsRejected": 0,
              "optionalIndependentOfCore": False}
    source = ROOT / "DPJ4-Legacy-Quote-Packs"
    def compile_at(folder):
        run([javac, "--release", "21", "-Xlint:all", "-Werror", "-d", "classes", "@sources.txt"], folder)
    def contracts(folder, *, core=False, success=True):
        return run([java, "-cp", "classes", "ContractTests", *(["core"] if core else [])], folder, success=success)
    with tempfile.TemporaryDirectory(prefix="java-boundary-") as temporary:
        work = Path(temporary)
        for role in ["starter", "solution"]:
            folder = source / role
            output = work / role
            shutil.copytree(folder, output)
            listed = (output / "sources.txt").read_text().splitlines()
            assert len(listed) == len(set(listed)) == 8
            assert set(listed) == {str(p.relative_to(output)) for p in output.rglob("*.java")}
            files = sorted(p for p in folder.rglob("*") if p.is_file())
            assert len(files) == 10
            for file in files:
                result["sourceHashes"][str(file.relative_to(ROOT))] = hashlib.sha256(file.read_bytes()).hexdigest()
            compile_at(output); result["rolesCompiled"] += 1
            if role == "starter":
                failure = contracts(output, core=True, success=False)
                assert "UnsupportedOperationException: Implement the independent prototype copy." in failure, failure
                result["unfinishedStartersRejected"] += 1
            else:
                core = contracts(output, core=True)
                full = contracts(output)
                core_match = re.fullmatch(r"Core boundary contracts: (\d+) passed\n", core)
                full_match = re.fullmatch(r"Complete boundary contracts: (\d+) passed\n", full)
                assert core_match and full_match, (core, full)
                core_count, full_count = int(core_match[1]), int(full_match[1])
                assert core_count >= 4000 and full_count == core_count + 7
                result.update(coreContracts=core.strip(), completeContracts=full.strip())
                assert run([java, "-cp", "classes", "Main"], output) == "Class order: 6 units = USD 18.00\n"
        core_only = work / "core-only"
        shutil.copytree(source / "solution", core_only)
        file = core_only / "src/boundary/OrderDraft.java"
        text = file.read_text()
        old = "return new OrderDraft(value, lines);"
        assert text.count(old) == 1
        file.write_text(text.replace(old, 'throw new UnsupportedOperationException("Implement the optional titled prototype copy.");'))
        compile_at(core_only)
        assert contracts(core_only, core=True).strip() == result["coreContracts"]
        assert run([java, "-cp", "classes", "Main"], core_only) == "Class order: 6 units = USD 18.00\n"
        failure = contracts(core_only, success=False)
        assert "UnsupportedOperationException: Implement the optional titled prototype copy." in failure
        result["optionalIndependentOfCore"] = True
        for filename, old, new, core_mode in [
            ("OrderDraft.java", "lines.add(new Line(line.name(), line.units()));", "lines.add(line);", True),
            ("PriceAdapter.java", "USD [0-9]+", "(?:USD|EUR) [0-9]+", True),
            ("QuoteFacade.java", "return new Quote(snapshot.title(), units, prices.total(units));",
             "prices.total(units); return new Quote(snapshot.title(), units, prices.total(units));", True),
            ("OrderDraft.java", "return new OrderDraft(value, lines);", "return new OrderDraft(title, lines);", False),
        ]:
            output = work / ("regression-" + str(result["controlledRegressionsRejected"]))
            shutil.copytree(source / "solution", output)
            file = output / "src/boundary" / filename
            text = file.read_text()
            assert text.count(old) == 1
            file.write_text(text.replace(old, new))
            compile_at(output)
            failure = contracts(output, core=core_mode, success=False)
            assert "AssertionError" in failure, failure
            result["controlledRegressionsRejected"] += 1
    result.update(accepted=True, temporaryBuildsCleaned=True,
                  learnerPatternChoiceReasoningInferred=False)
    return result

if __name__ == "__main__":
    print(json.dumps(verify(), sort_keys=True), flush=True)
