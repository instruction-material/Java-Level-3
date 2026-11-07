#!/usr/bin/env python3
"""Compile packaged DPJ2 roles and check behavior, boundaries and optional scope."""
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
native.TASK = "java-patterns-foundations-contracts"
run = native.run

def verify() -> dict:
    java_home = os.environ.get("JAVA_HOME")
    java = str(Path(java_home) / "bin/java") if java_home else shutil.which("java")
    javac = str(Path(java_home) / "bin/javac") if java_home else shutil.which("javac")
    assert java and javac
    version = run([javac, "-version"], ROOT).strip()
    assert version.startswith("javac 21."), version
    result = {"jdk":version, "sourceHashes":{}, "rolesCompiled":0,
              "unfinishedStartersRejected":0, "controlledRegressionsRejected":0,
              "compileBoundariesRejected":0, "referenceContracts":[],
              "optionalExtensionIndependentOfCore":False}
    source = ROOT / "DPJ2-Report-Foundations-Packs"
    def compile_at(directory: Path) -> None:
        run([javac, "--release", "21", "-Xlint:all", "-Werror", "-d", "classes", "@sources.txt"], directory)
    with tempfile.TemporaryDirectory(prefix="java-foundations-") as temporary:
        work = Path(temporary)
        for role in ["starter","solution"]:
            folder = source / role
            output = work / role
            shutil.copytree(folder, output)
            listed = (output / "sources.txt").read_text().splitlines()
            assert len(listed) == len(set(listed)) == 11
            assert set(listed) == {str(p.relative_to(output)) for p in output.rglob("*.java")}
            for file in folder.rglob("*"):
                if file.is_file():
                    result["sourceHashes"][str(file.relative_to(ROOT))] = hashlib.sha256(file.read_bytes()).hexdigest()
            compile_at(output)
            result["rolesCompiled"] += 1
            if role == "starter":
                failure = run([java,"-cp","classes","ContractTests","core"], output, success=False)
                assert "UnsupportedOperationException: Implement Report validation and immutable snapshot." in failure, failure
                result["unfinishedStartersRejected"] += 1
            else:
                for args, expected in [
                    (["core"],"Core foundations contracts: 649 passed\n"),
                    ([],"Complete foundations contracts: 654 passed\n"),
                ]:
                    actual = run([java,"-cp","classes","ContractTests",*args], output)
                    assert actual == expected, actual
                    result["referenceContracts"].append(actual.strip())
                assert run([java,"-cp","classes","Main"], output) == "Weekly report\nReady\nChecked\n"
        example_source = ROOT / "DPJ2-Seam-Worked-Example"
        example = work / "worked-example"
        shutil.copytree(example_source, example)
        for file in example_source.iterdir():
            assert file.is_file()
            result["sourceHashes"][str(file.relative_to(ROOT))] = hashlib.sha256(file.read_bytes()).hexdigest()
        run([javac,"--release","21","-Xlint:all","-Werror","Main.java","ContractTests.java"], example)
        assert run([java,"ContractTests"], example) == "Seam worked contracts: 14 passed\\n"
        assert run([java,"Main"], example) == "Ready\\n"
        result["workedExamplesCompiled"] = 1
        result["workedExampleContracts"] = "Seam worked contracts: 14 passed"
        # Complete core behavior must remain usable before the optional fourth task.
        core = work / "core-only"
        shutil.copytree(source / "solution", core)
        extension = Path("src/report/adapters/UppercaseTitleFormatter.java")
        shutil.copy2(source / "starter" / extension, core / extension)
        compile_at(core)
        assert run([java,"-cp","classes","ContractTests","core"], core) == "Core foundations contracts: 649 passed\n"
        assert run([java,"-cp","classes","Main"], core) == "Weekly report\nReady\nChecked\n"
        failure = run([java,"-cp","classes","ContractTests"], core, success=False)
        assert "UnsupportedOperationException: Implement the locale-independent uppercase-title extension." in failure, failure
        result["optionalExtensionIndependentOfCore"] = True
        for filename, old, new in [
            ("src/report/domain/Report.java","lines = List.copyOf(lines);","lines = java.util.Collections.unmodifiableList(lines);"),
            ("src/report/application/ReportPublisher.java","sink.write(text);","sink.write(text); sink.write(text);"),
            ("src/report/adapters/UppercaseTitleFormatter.java","toUpperCase(Locale.ROOT)","toUpperCase()"),
        ]:
            output = work / ("regression-" + str(result["controlledRegressionsRejected"]))
            shutil.copytree(source / "solution", output)
            file = output / filename
            text = file.read_text()
            assert text.count(old) == 1
            file.write_text(text.replace(old,new))
            compile_at(output)
            failure = run([java,"-cp","classes","ContractTests"], output, success=False)
            assert "AssertionError" in failure, failure
            result["controlledRegressionsRejected"] += 1
        output = work / "solution"
        for code, marker in [
            ("import report.domain.Report; class ForeignClient { String leak(Report report) { return report.title; } }","private access"),
            ("import report.examples.LanguageChoices; final class ForeignClient implements LanguageChoices.Outcome {}","sealed"),
        ]:
            (output / "ForeignClient.java").write_text(code)
            failure = run([javac,"--release","21","-Xlint:all","-Werror","-cp","classes","ForeignClient.java"], output, success=False)
            assert marker in failure, failure
            result["compileBoundariesRejected"] += 1
        # Verify the actual declared imports at the architectural boundaries.
        allowed = {"domain":set(), "ports":{"domain"}, "application":{"domain","ports"}}
        import re
        for package, dependencies in allowed.items():
            for file in (source / "solution/src/report" / package).glob("*.java"):
                imported = set(re.findall(r"^import report\.([a-z]+)\.",file.read_text(),re.M))
                assert imported <= dependencies, (file, imported, dependencies)
    result.update(accepted=True, temporaryBuildsCleaned=True, learnerRefactoringReasoningInferred=False)
    return result

if __name__ == "__main__":
    print(json.dumps(verify(),sort_keys=True),flush=True)
