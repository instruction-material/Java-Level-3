#!/usr/bin/env python3
"""Verify DPJ1 role packs with clean native builds and independent contracts."""
from __future__ import annotations

import hashlib
import importlib.util
import json
import os
from pathlib import Path
import shutil
import tempfile

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location("native_process", ROOT / "tools/verify-recursion.py")
assert spec and spec.loader
native = importlib.util.module_from_spec(spec)
spec.loader.exec_module(native)
native.TASK = "java-patterns-opening-contracts"
run = native.run


def verify() -> dict:
    java_home = os.environ.get("JAVA_HOME")
    java = str(Path(java_home) / "bin/java") if java_home else shutil.which("java")
    javac = str(Path(java_home) / "bin/javac") if java_home else shutil.which("javac")
    assert java and javac, "JDK 21 or newer is required; hosted acceptance uses JDK 21"
    version = run([javac, "-version"], ROOT).strip()
    assert int(version.split()[1].split(".")[0]) >= 21, version
    result = {"jdk": version, "languageRelease": 21, "sourceHashes": {},
              "rolesCompiled": 0, "unfinishedStartersRejected": 0,
              "baselineContracts": [], "referenceContracts": [],
              "controlledRegressionsRejected": 0, "consoleCases": 0}

    def compile_at(directory: Path) -> None:
        run([javac, "--release", "21", "-Xlint:all", "-Werror", "-d", "classes",
             *sorted(file.name for file in directory.glob("*.java"))], directory)

    with tempfile.TemporaryDirectory(prefix="java-patterns-") as temporary:
        work = Path(temporary)
        for folder, expected, reminder, demo in [
            ("DPJ1-Pricing-Contracts", "Pricing contracts: 10014 passed",
             "Implement the documented bulk-pricing tiers and input bounds.", "18.0\n"),
            ("DPJ1-Design-Comparison-Packs", "Complete design contracts: 28070 passed",
             "Implement the overnight policy: zero for an empty order, otherwise 1500 + 30 * units cents.", "1800\n2420\n1620\n2240\n"),
        ]:
            for role in ["starter", "solution"]:
                source = ROOT / folder / role
                output = work / (folder + "-" + role)
                output.mkdir()
                for file in sorted(source.iterdir()):
                    assert file.is_file() and file.suffix in [".java", ".md"], file
                    result["sourceHashes"][str(file.relative_to(ROOT))] = hashlib.sha256(file.read_bytes()).hexdigest()
                    shutil.copy2(file, output / file.name)
                compile_at(output)
                result["rolesCompiled"] += 1
                if folder.endswith("Comparison-Packs"):
                    baseline = run([java, "-cp", "classes", "ContractTests", "base"], output)
                    assert baseline == "Base design contracts: 20052 passed\n", baseline
                    result["baselineContracts"].append({"role": role, "output": baseline.strip()})
                    assert run([java, "-cp", "classes", "Main"], output) == demo
                    result["consoleCases"] += 1
                if role == "starter":
                    failure = run([java, "-cp", "classes", "ContractTests"], output, success=False)
                    assert "UnsupportedOperationException: " + reminder in failure, failure
                    result["unfinishedStartersRejected"] += 1
                else:
                    actual = run([java, "-cp", "classes", "ContractTests"], output)
                    assert actual == expected + "\n", actual
                    result["referenceContracts"].append(actual.strip())
                    if folder == "DPJ1-Pricing-Contracts":
                        assert run([java, "-cp", "classes", "Main"], output) == demo
                        result["consoleCases"] += 1

        # Reject deliberate behavior errors, independently of learner TODOs.
        for folder, filename, old, new in [
            ("DPJ1-Pricing-Contracts", "BulkPricingStrategy.java",
             "units >= 5 ? 3.0", "units > 5 ? 3.0"),
            ("DPJ1-Design-Comparison-Packs", "Main.java",
             "return pricing.subtotalCents(units) + shipping.feeCents(units);",
             "return grossCents(units) + shipping.feeCents(units);"),
            ("DPJ1-Design-Comparison-Packs", "Main.java",
             "return units == 0 ? 0 : 1500 + 30 * units;",
             "return 1500 + 30 * units;"),
        ]:
            output = work / (folder + "-solution")
            file = output / filename
            original = (ROOT / folder / "solution" / filename).read_text()
            assert original.count(old) == 1, (filename, old)
            file.write_text(original.replace(old, new))
            compile_at(output)
            failure = run([java, "-cp", "classes", "ContractTests"], output, success=False)
            assert "AssertionError" in failure, failure
            file.write_text(original)
            result["controlledRegressionsRejected"] += 1
    result.update(accepted=True, temporaryBuildsCleaned=True,
                  nativeJdk21Accepted=version.startswith("javac 21."),
                  learnerDesignReasoningInferred=False)
    return result


if __name__ == "__main__":
    print(json.dumps(verify(), sort_keys=True), flush=True)
