"""Compile and verify AJ5 starter, completed and reference roles on Java 21."""
from pathlib import Path
import os
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]
JAVA_HOME = Path(os.environ["JAVA_HOME"])
CORE = "AJ5-Linear-Search-Packs"
GROWTH = "AJ5-Big-O-Practice-Packs"
TRANSFER = "J3-07-aj5-linear-search-and-big-o-notation-supplemental-2"
COMPARE = "J3-08-aj5-linear-search-and-big-o-notation-supplemental-3"


def java(work, entry, args=(), stdin=""):
    return subprocess.run(
        [JAVA_HOME / "bin/java", "-cp", work, entry, *args],
        input=stdin, text=True, capture_output=True, timeout=20,
    )


def compile_java(work, *sources):
    subprocess.run(
        [JAVA_HOME / "bin/javac", "--release", "21", "-Xlint:all", "-Werror",
         "-d", work, *sources],
        check=True, timeout=30,
    )


def output(result):
    assert result.returncode == 0, result.stderr
    return result.stdout.splitlines()


def main():
    version = subprocess.check_output([JAVA_HOME / "bin/javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    with tempfile.TemporaryDirectory(prefix="aj5-contract-") as tmp:
        work = Path(tmp)
        legacy = work / "legacy"
        legacy.mkdir()
        compile_java(legacy, ROOT / "AJ5-Linear-Search/Main.java")
        assert output(java(legacy, "Main", stdin="1\n"))[-1].endswith("1 found at index 0")
        assert output(java(legacy, "Main", stdin="42\n"))[-1].endswith("42 is not in the array!")
        for pack in (CORE, GROWTH, TRANSFER, COMPARE):
            for role in ("starter", "solution"):
                owner = ROOT / pack / role
                place = work / pack / role
                place.mkdir(parents=True)
                compile_java(place, owner / "Main.java", owner / "ContractTests.java")
                check = java(place, "ContractTests")
                if role == "starter":
                    assert check.returncode != 0
                    expected = "Complete row 1" if pack == GROWTH else "UnsupportedOperationException"
                    assert expected in check.stderr, check.stderr
                    if pack == GROWTH:
                        assert all(row.endswith("-> ?") for row in output(java(place, "Main")))
                    continue
                assert check.returncode == 0, check.stderr
                lines = output(java(place, "Main"))
                if pack == CORE:
                    assert lines == ["7 found at index 0"]
                    assert output(java(place, "Main", ("all",))) == ["[0, 2]"]
                    assert output(java(place, "ContractTests", ("all",))) == ["Search contracts passed."]
                elif pack == GROWTH:
                    labels = [row.rsplit(" -> ", 1)[1] for row in lines]
                    assert labels == ["n^2", "n", "n", "n", "1", "log(n)", "n", "n^2",
                                      "log(n) + m^2", "log(n)"]
                elif pack == TRANSFER:
                    assert lines == ["0", "1", "-1"]
                else:
                    assert lines == ["8 1 8 8", "16 1 16 16", "32 1 32 32"]
            print(f"Verified {pack}: both roles and exact outputs", flush=True)


if __name__ == "__main__":
    main()
