#!/usr/bin/env python3
"""Compile runnable optional studios and independently verify each published contract."""
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
native.TASK = "java-applied-studio-contracts"
run = native.run

def method_body(text, signature, replacement):
    start = text.index("{", text.index(signature))
    level = 1
    end = start + 1
    while level:
        if text[end] == "{":
            level += 1
        elif text[end] == "}":
            level -= 1
        end += 1
    return text[:start + 1] + "\n" + replacement + "\n    " + text[end - 1:]

def verify():
    home = os.environ.get("JAVA_HOME")
    javac = str(Path(home) / "bin/javac") if home else shutil.which("javac")
    java = str(Path(home) / "bin/java") if home else shutil.which("java")
    assert javac and java
    version = run([javac, "-version"], ROOT).strip()
    assert version.startswith("javac 21."), version
    result = {"jdk": version, "sourceHashes": {}, "packs": [], "rolesCompiled": 0,
              "controlledRegressionsRejected": 0, "learnerDesignReasoningInferred": False}
    def compile_at(folder):
        run([javac, "--release", "21", "-Xlint:all", "-Werror", "-d", "classes", "@sources.txt"], folder)
    def contracts(folder, mode="all", success=True):
        return run([java, "-cp", "classes", "ContractTests", mode], folder, success=success)
    def accepted_count(text, label):
        match = re.fullmatch(re.escape(label) + r": ([0-9]+) passed\n", text)
        assert match, text
        return int(match[1])
    with tempfile.TemporaryDirectory(prefix="java-applied-studios-") as temporary:
        work = Path(temporary)
        name = "DPJ-Studio-Strategy-Selection-Packs"
        source = ROOT / name
        results = {"pack": name, "roles": {}, "optionalIndependentOfCore": False}
        demo = "1800\n2400\n1620\nBULK|6|1800\n"
        for role, count in [("starter", 6), ("solution", 9)]:
            folder = source / role
            output = work / ("strategy-" + role)
            shutil.copytree(folder, output)
            listed = (folder / "sources.txt").read_text().splitlines()
            assert len(listed) == len(set(listed)) == count
            assert set(listed) == {str(p.relative_to(folder)) for p in folder.rglob("*.java")}
            for path in sorted(folder.rglob("*")):
                if path.is_file():
                    result["sourceHashes"][str(path.relative_to(ROOT))] = hashlib.sha256(path.read_bytes()).hexdigest()
            compile_at(output)
            result["rolesCompiled"] += 1
            base = accepted_count(contracts(output, "base"), "Baseline strategy studio contracts")
            core = accepted_count(contracts(output, "core"), "Core strategy studio contracts")
            assert base == 6028 and core == 12056, (base, core)
            assert run([java, "-cp", "classes", "Main", "base"], output) == demo
            assert run([java, "-cp", "classes", "Main"], output) == demo
            record = {"baselineChecks": base, "coreChecks": core, "runnableBeforeEdits": True}
            if role == "starter":
                assert "UnsupportedOperationException: Implement optional rule injection." in contracts(output, success=False)
                record["optionalExplicitlyUnfinished"] = True
            else:
                full = accepted_count(contracts(output), "Complete strategy studio contracts")
                assert full == 12067, full
                record["completeChecks"] = full
            results["roles"][role] = record
        for path in ["Main.java", "ContractTests.java", "README.md", "DECISIONS.md",
                     "src/pricing/Plan.java", "src/pricing/PricingRule.java",
                     "src/pricing/legacy/LegacyPricer.java"]:
            assert (source / "starter" / path).read_bytes() == (source / "solution" / path).read_bytes(), path
        results["baselineAndOracleIdenticalAcrossRoles"] = True
        intermediate = work / "strategy-helper-only"
        shutil.copytree(source / "starter", intermediate)
        path = intermediate / "src/pricing/CartPricer.java"
        path.write_text(method_body(path.read_text(), "public String receipt(",
                        '        Objects.requireNonNull(plan, "plan");\n'
                        '        return plan.name() + "|" + units + "|" + quote(plan, units);'))
        compile_at(intermediate)
        assert accepted_count(contracts(intermediate, "core"), "Core strategy studio contracts") == 12056
        results["helperOnlyIntermediateAccepted"] = True
        core_only = work / "strategy-core-only"
        shutil.copytree(source / "solution", core_only)
        path = core_only / "src/pricing/CartPricer.java"
        path.write_text(method_body(path.read_text(), "public long priceUsing(",
                        '        throw new UnsupportedOperationException("Implement optional rule injection.");'))
        compile_at(core_only)
        assert accepted_count(contracts(core_only, "core"), "Core strategy studio contracts") == 12056
        assert "UnsupportedOperationException: Implement optional rule injection." in contracts(core_only, success=False)
        results["optionalIndependentOfCore"] = True
        mutations = [
            ("src/pricing/rules/BulkRule.java", "units >= 10", "units > 10", "core"),
            ("src/pricing/rules/FlatRule.java", "units * 400L", "units * 401L", "core"),
            ("src/pricing/rules/MemberRule.java", "* 90 / 100", "* 91 / 100", "core"),
            ("src/pricing/CartPricer.java", '+ quote(plan, units);', '+ (quote(plan, units) + 1);', "core"),
            ("src/pricing/CartPricer.java", "if (result < 0)", "if (result < -1)", "all"),
        ]
        for index, (name, old, new, mode) in enumerate(mutations):
            folder = work / ("strategy-regression-" + str(index))
            shutil.copytree(source / "solution", folder)
            path = folder / name
            text = path.read_text()
            assert text.count(old) == 1, (name, old)
            path.write_text(text.replace(old, new, 1))
            compile_at(folder)
            failed = contracts(folder, mode, success=False)
            assert "AssertionError" in failed, failed
            result["controlledRegressionsRejected"] += 1
        results["controlledRegressionsRejected"] = 5
        result["packs"].append(results)

        name = "DPJ-Studio-Structural-Wrapper-Packs"
        source = ROOT / name
        results = {"pack": name, "roles": {}, "optionalIndependentOfCore": False}
        demo = "tag:HI\ntag:HI\n2\n1\ntag:HI\n2\n"
        for role, count in [("starter", 5), ("solution", 9)]:
            folder = source / role
            output = work / ("wrapper-" + role)
            shutil.copytree(folder, output)
            listed = (folder / "sources.txt").read_text().splitlines()
            assert len(listed) == len(set(listed)) == count
            assert set(listed) == {str(p.relative_to(folder)) for p in folder.rglob("*.java")}
            for path in sorted(folder.rglob("*")):
                if path.is_file():
                    result["sourceHashes"][str(path.relative_to(ROOT))] = hashlib.sha256(path.read_bytes()).hexdigest()
            compile_at(output)
            result["rolesCompiled"] += 1
            base = accepted_count(contracts(output, "base"), "Baseline wrapper studio contracts")
            core = accepted_count(contracts(output, "core"), "Core wrapper studio contracts")
            assert base == 410 and core == 820, (base, core)
            assert run([java, "-cp", "classes", "Main", "base"], output) == demo
            assert run([java, "-cp", "classes", "Main"], output) == demo
            record = {"baselineChecks": base, "coreChecks": core, "runnableBeforeEdits": True}
            if role == "starter":
                assert "UnsupportedOperationException: Implement optional selective invalidation." in contracts(output, success=False)
                record["optionalExplicitlyUnfinished"] = True
            else:
                full = accepted_count(contracts(output), "Complete wrapper studio contracts")
                assert full == 839, full
                record["completeChecks"] = full
            results["roles"][role] = record
        for path in ["Main.java", "ContractTests.java", "README.md", "DECISIONS.md",
                     "src/wrapping/Source.java", "src/wrapping/legacy/LegacyReader.java"]:
            assert (source / "starter" / path).read_bytes() == (source / "solution" / path).read_bytes(), path
        results["baselineAndOracleIdenticalAcrossRoles"] = True
        intermediate = work / "wrapper-helper-only"
        shutil.copytree(source / "starter", intermediate)
        path = intermediate / "src/wrapping/ReadDesk.java"
        text = path.read_text()
        old = ('        String value = Objects.requireNonNull(source.load(key), "source result");\n'
               '        if (upper) value = value.toUpperCase(Locale.ROOT);\n'
               '        value = prefix + value;')
        assert text.count(old) == 1
        text = text.replace(old, "        String value = transformed(key);")
        helper = ('\n    private String transformed(String key) {\n'
                  '        String value = Objects.requireNonNull(source.load(key), "source result");\n'
                  '        if (upper) value = value.toUpperCase(Locale.ROOT);\n'
                  '        return prefix + value;\n    }\n')
        path.write_text(text[:-2] + helper + text[-2:])
        compile_at(intermediate)
        assert accepted_count(contracts(intermediate, "core"), "Core wrapper studio contracts") == 820
        results["helperOnlyIntermediateAccepted"] = True
        core_only = work / "wrapper-core-only"
        shutil.copytree(source / "solution", core_only)
        path = core_only / "src/wrapping/ReadDesk.java"
        path.write_text(method_body(path.read_text(), "public boolean invalidate(",
                        '        throw new UnsupportedOperationException("Implement optional selective invalidation.");'))
        compile_at(core_only)
        assert accepted_count(contracts(core_only, "core"), "Core wrapper studio contracts") == 820
        assert "UnsupportedOperationException: Implement optional selective invalidation." in contracts(core_only, success=False)
        results["optionalIndependentOfCore"] = True
        right_order = ('        Source pipeline = source;\n'
                       '        if (upper) pipeline = new UpperSource(pipeline);\n'
                       '        pipeline = new PrefixSource(pipeline, prefix);')
        wrong_order = ('        Source pipeline = new PrefixSource(source, prefix);\n'
                       '        if (upper) pipeline = new UpperSource(pipeline);')
        mutations = [
            ("src/wrapping/layers/CacheSource.java", "values.put(key, value);", "// Cache deliberately bypassed.", "core"),
            ("src/wrapping/layers/CountingSource.java", "attempts++;", "attempts += 2;", "core"),
            ("src/wrapping/ReadDesk.java", right_order, wrong_order, "core"),
            ("src/wrapping/Source.java", "{0,15}", "{0,16}", "core"),
            ("src/wrapping/ReadDesk.java", "return cache != null && cache.invalidate(key);", "return false;", "all"),
        ]
        for index, (name, old, new, mode) in enumerate(mutations):
            folder = work / ("wrapper-regression-" + str(index))
            shutil.copytree(source / "solution", folder)
            path = folder / name
            text = path.read_text()
            assert text.count(old) == 1, (name, old)
            path.write_text(text.replace(old, new, 1))
            compile_at(folder)
            assert "AssertionError" in contracts(folder, mode, success=False)
            result["controlledRegressionsRejected"] += 1
        results["controlledRegressionsRejected"] = 5
        result["packs"].append(results)
    result["temporaryBuildsCleaned"] = True
    result["accepted"] = True
    return result

if __name__ == "__main__":
    result = verify()
    output = ROOT / "output/java-studio-contracts/proof.json"
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(result, indent=2) + "\n")
    print(json.dumps(result, indent=2))
