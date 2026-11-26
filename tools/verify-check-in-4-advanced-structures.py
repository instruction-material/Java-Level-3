"""Compile and exercise both Java 21 roles of each advanced structures checkpoint assignment."""

import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ-Check-In-4-Workbook-Packs": (
        "Check every tree key against its inherited bounds",
        ["true", "Probe[index=3, found=true]", "Probe[index=2, found=false]", "5", "OptionalLong.empty"],
    ),
    "AJ-Check-In-4-Tree-BFS-Packs": (
        "Visit a tree level by level with a queue",
        ["[12, 3, 20, 7]", "[12, 3, 7, 20]"],
    ),
    "J3-35-check-in-4-supplemental-2": (
        "Verify the new bucket positions and preserve each key and value", ["true", "false"],
    ),
    "J3-36-check-in-4-supplemental-3": (
        "Check each proposed route and choose the cheapest valid candidate", ["5", "[A, B, C]"],
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
        with TemporaryDirectory(prefix="java-checkin4-") as directory:
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
            assert checked_stdout(result) == ["Advanced structures checks passed"]
            assert checked_stdout(run_java(directory, "Main")) == examples

            if folder == "AJ-Check-In-4-Workbook-Packs":
                assert checked_stdout(run_java(directory, "ContractTests", "hops")) == ["Hop checks passed"]
                assert checked_stdout(run_java(directory, "Main", "hops")) == ["1"]


if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder, contract in ASSIGNMENTS.items():
        verify_assignment(folder, *contract)
        print(f"Verified {folder}: starter and reference", flush=True)
