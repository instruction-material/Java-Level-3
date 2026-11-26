"""Compile and exercise both Java 21 roles of each priority queue and map assignment."""

import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ13-Class-Rank-Packs": (
        "Rank by descending GPA and ascending name",
        ["Mei: 4.00", "Ana: 3.90", "Zoe: 3.90", "Ray: 3.55"],
    ),
    "AJ13-Letter-Frequencies-Packs": (
        "Count normalized letter code points",
        ["a: 3", "b: 3", "c: 1", "f: 1", "é: 2", "Most frequent: a"],
    ),
    "J3-25-aj13-priority-queues-and-maps-supplemental-2": (
        "Schedule urgent jobs first and keep arrival order on ties",
        ["[Terminal, Router, Printer, Monitor]"],
    ),
    "J3-26-aj13-priority-queues-and-maps-supplemental-3": (
        "Record the latest count for each normalized SKU", ["LAMP: 0", "bulb: 7", "Cable: missing"],
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
    starter_file = assignment / "starter" / "Main.java"
    solution_file = assignment / "solution" / "Main.java"
    assert starter_file.read_bytes() != solution_file.read_bytes(), folder

    for role in ("starter", "solution"):
        with TemporaryDirectory(prefix="java-aj13-") as directory:
            sources = sorted((assignment / role).glob("*.java"))
            subprocess.run(
                [JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror",
                 "-d", directory, *sources],
                check=True, timeout=25,
            )
            result = run_java(directory, "ContractTests")
            if role == "starter":
                assert result.returncode != 0, folder
                assert f"UnsupportedOperationException: {starter_message}" in result.stderr, result.stderr
                continue
            assert checked_stdout(result) == ["Priority queue and map checks passed"]
            assert checked_stdout(run_java(directory, "Main")) == examples

            if folder == "AJ13-Class-Rank-Packs":
                assert checked_stdout(run_java(directory, "ContractTests", "cutoff")) == ["Cutoff checks passed"]
                assert checked_stdout(run_java(directory, "Main", "cutoff")) == examples[:3]
            if folder == "AJ13-Letter-Frequencies-Packs":
                assert checked_stdout(run_java(directory, "Main", "?!")) == ["No letters found."]


if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder, contract in ASSIGNMENTS.items():
        verify_assignment(folder, *contract)
        print(f"Verified {folder}: starter and reference", flush=True)
