"""Compile and exercise both Java 21 roles of each hash table assignment."""

import os
import shutil
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ15-Hash-Map-Packs": (
        "Probe for a key without losing a collision chain", ["updated", "false", "1"],
    ),
    "AJ15-Search-Engine-Packs": (
        "Build tag lists from page and keyword lines",
        ["Java: [https://example.org/guide, https://example.org/lab]",
         "tools: [https://example.org/guide, https://example.org/editor]", "unknown: []"],
    ),
    "J3-31-aj15-hash-tables-supplemental-2": (
        "Update or append a key inside its collision chain", ["7", "9", "OptionalInt.empty"],
    ),
    "J3-32-aj15-hash-tables-supplemental-3": (
        "Create new buckets and reindex every entry", ["3", "[Entry[key=5, value=five]]"],
    ),
}


def run_java(directory, entry_point, *args):
    return subprocess.run(
        [JAVA_BIN / "java", "-cp", directory, entry_point, *args],
        cwd=directory, text=True, capture_output=True, timeout=20,
    )


def checked_stdout(result):
    assert result.returncode == 0, result.stderr
    return result.stdout.splitlines()


def verify_assignment(folder, starter_message, examples):
    assignment = ROOT / folder
    implementation = "HashMap.java" if folder == "AJ15-Hash-Map-Packs" else "Main.java"
    starter_file = assignment / "starter" / implementation
    solution_file = assignment / "solution" / implementation
    assert starter_file.read_bytes() != solution_file.read_bytes(), folder

    for role in ("starter", "solution"):
        with TemporaryDirectory(prefix="java-aj15-") as directory:
            sources = sorted((assignment / role).glob("*.java"))
            subprocess.run(
                [JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror",
                 "-d", directory, *sources],
                check=True, timeout=25,
            )
            for data in (assignment / role).glob("*.txt"):
                shutil.copyfile(data, Path(directory) / data.name)
            result = run_java(directory, "ContractTests")
            if role == "starter":
                assert result.returncode != 0, folder
                assert f"UnsupportedOperationException: {starter_message}" in result.stderr, result.stderr
                continue
            assert checked_stdout(result) == ["Hash table checks passed"]
            assert checked_stdout(run_java(directory, "Main")) == examples

            if folder == "AJ15-Hash-Map-Packs":
                assert checked_stdout(run_java(directory, "ContractTests", "compact")) == ["Compaction checks passed"]
                assert checked_stdout(run_java(directory, "Main", "compact")) == ["updated", "false", "Deleted slots: 0", "1"]


if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder, contract in ASSIGNMENTS.items():
        verify_assignment(folder, *contract)
        print(f"Verified {folder}: starter and reference", flush=True)
