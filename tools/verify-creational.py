#!/usr/bin/env python3
"""Compile DPJ3 roles, complete public examples and staged learner contracts."""
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
native.TASK = "java-patterns-creational-contracts"
run = native.run

FACTORIES = "Factory contracts: 635 passed\n"
CORE = "Core creation contracts: 2101 passed\n"
ALL = "Complete creation contracts: 2105 passed\n"
WORKED = "Creation worked contracts: 31 passed\n"
MAIN = "[Notes]\n| one |\n| two |\nLines: 2\nEnd\n"
WORKED_MAIN = "Ready\nReady\nReady\n[Ready]\n**Title**\n> Body\n[Title]\nBody\n"

def verify(*, require_jdk21: bool = True) -> dict:
    java_home = os.environ.get("JAVA_HOME")
    java = str(Path(java_home) / "bin/java") if java_home else shutil.which("java")
    javac = str(Path(java_home) / "bin/javac") if java_home else shutil.which("javac")
    assert java and javac
    version = run([javac, "-version"], ROOT).strip()
    if require_jdk21:
        assert version.startswith("javac 21."), version
    result = {
        "jdk": version, "java21RuntimeRequired": require_jdk21,
        "sourceHashes": {}, "rolesCompiled": 0, "unfinishedStartersRejected": 0,
        "controlledRegressionsRejected": 0, "compileBoundariesRejected": 0,
        "referenceContracts": [], "optionalExtensionIndependentOfCore": False,
        "factoryStageIndependentOfBuilder": False,
    }
    source = ROOT / "DPJ3-Report-Creation-Packs"
    def compile_at(directory: Path) -> None:
        run([javac, "--release", "21", "-Xlint:all", "-Werror", "-d", "classes", "@sources.txt"], directory)
    def test_at(directory: Path, mode: str | None = None, *, success: bool = True) -> str:
        return run([java, "-cp", "classes", "ContractTests", *([mode] if mode else [])], directory, success=success)
    with tempfile.TemporaryDirectory(prefix="java-creational-") as temporary:
        work = Path(temporary)
        for role in ["starter", "solution"]:
            folder = source / role
            output = work / role
            shutil.copytree(folder, output)
            listed = (output / "sources.txt").read_text().splitlines()
            assert len(listed) == len(set(listed)) == 12
            assert set(listed) == {str(p.relative_to(output)) for p in output.rglob("*.java")}
            files = sorted(p for p in folder.rglob("*") if p.is_file())
            assert len(files) == 14
            for file in files:
                result["sourceHashes"][str(file.relative_to(ROOT))] = hashlib.sha256(file.read_bytes()).hexdigest()
            compile_at(output)
            result["rolesCompiled"] += 1
            if role == "starter":
                failure = test_at(output, "core", success=False)
                assert "UnsupportedOperationException: Implement the boxed heading product." in failure, failure
                result["unfinishedStartersRejected"] += 1
            else:
                for mode, expected in [("factories", FACTORIES), ("core", CORE), (None, ALL)]:
                    actual = test_at(output, mode)
                    assert actual == expected, actual
                    result["referenceContracts"].append(actual.strip())
                assert run([java, "-cp", "classes", "Main"], output) == MAIN
                for args in [["wrong"], ["core", "extra"]]:
                    failure = run([java, "-cp", "classes", "ContractTests", *args], output, success=False)
                    assert "IllegalArgumentException: mode must be factories, core or all" in failure
        factory_only = work / "factory-stage"
        shutil.copytree(source / "starter", factory_only)
        for filename in ["Families.java", "Exporters.java"]:
            shutil.copy2(source / "solution/src/creation" / filename, factory_only / "src/creation" / filename)
        compile_at(factory_only)
        assert test_at(factory_only, "factories") == FACTORIES
        failure = test_at(factory_only, "core", success=False)
        assert "UnsupportedOperationException: Implement the style option." in failure, failure
        result["factoryStageIndependentOfBuilder"] = True
        core = work / "core-stage"
        shutil.copytree(source / "solution", core)
        job = core / "src/creation/ReportJob.java"
        old = "return new Builder(report).style(style).includeCount(includeCount).footer(footer);"
        text = job.read_text()
        assert text.count(old) == 1
        job.write_text(text.replace(old, 'throw new UnsupportedOperationException("Implement the optional independent toBuilder copy.");'))
        compile_at(core)
        assert test_at(core, "core") == CORE
        assert run([java, "-cp", "classes", "Main"], core) == MAIN
        failure = test_at(core, success=False)
        assert "UnsupportedOperationException: Implement the optional independent toBuilder copy." in failure, failure
        result["optionalExtensionIndependentOfCore"] = True
        for filename, old, new, mode in [
            ("Families.java", "public Heading heading() { return new BoxedHeading(); }",
             "public Heading heading() { return new PlainHeading(); }", "factories"),
            ("Exporters.java", 'Renderer product = Objects.requireNonNull(createRenderer(), "renderer");',
             'createRenderer(); Renderer product = Objects.requireNonNull(createRenderer(), "renderer");', "factories"),
            ("ReportJob.java", "includeCount = value; return this;",
             "includeCount = false; return this;", "core"),
            ("ReportJob.java", "return new Builder(report).style(style).includeCount(includeCount).footer(footer);",
             "return new Builder(report).style(style).footer(footer);", None),
        ]:
            output = work / ("regression-" + str(result["controlledRegressionsRejected"]))
            shutil.copytree(source / "solution", output)
            file = output / "src/creation" / filename
            text = file.read_text()
            assert text.count(old) == 1
            file.write_text(text.replace(old, new))
            compile_at(output)
            failure = test_at(output, mode, success=False)
            assert "AssertionError" in failure or "IllegalArgumentException: product families must match" in failure, failure
            result["controlledRegressionsRejected"] += 1
        output = work / "solution"
        for code, marker in [
            ("import creation.ReportJob; class ForeignClient { String leak(ReportJob.Builder builder) { return builder.footer; } }", "private access"),
            ("import creation.*; class ForeignClient extends Exporters.Exporter { protected Renderer createRenderer() { return RendererFactory.forStyle(Style.PLAIN); } public String export(Report report) { return \"bypass\"; } }", "final"),
        ]:
            (output / "ForeignClient.java").write_text(code)
            failure = run([javac, "--release", "21", "-Xlint:all", "-Werror", "-cp", "classes", "ForeignClient.java"], output, success=False)
            assert marker in failure, failure
            result["compileBoundariesRejected"] += 1
        example_source = ROOT / "DPJ3-Creation-Worked-Example"
        example = work / "worked-example"
        shutil.copytree(example_source, example)
        assert {p.name for p in example_source.iterdir()} == {"Main.java", "ContractTests.java", "README.md"}
        for file in example_source.iterdir():
            result["sourceHashes"][str(file.relative_to(ROOT))] = hashlib.sha256(file.read_bytes()).hexdigest()
        run([javac, "--release", "21", "-Xlint:all", "-Werror", "Main.java", "ContractTests.java"], example)
        assert run([java, "ContractTests"], example) == WORKED
        assert run([java, "Main"], example) == WORKED_MAIN
        result["workedExamplesCompiled"] = 1
        result["workedExampleContracts"] = WORKED.strip()
    result.update(accepted=True, temporaryBuildsCleaned=True, learnerDesignReasoningInferred=False)
    return result

if __name__ == "__main__":
    print(json.dumps(verify(), sort_keys=True), flush=True)
