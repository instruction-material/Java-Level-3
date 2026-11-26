"""Compile and exercise both Java 21 roles of each stack and queue assignment."""

import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ12-Stack-Packs": ("Write a new top value", ["11", "11", "7", "0"]),
    "AJ12-Queue-Packs": ("Locate the free slot and append", ["10", "20", "30", "40"]),
    "AJ12-Valid-Parentheses-Packs": (
        "Match closing symbols to the most recent opener",
        ["{[()]} -> true", "([)] -> false", "] -> false", "(() -> false"],
    ),
    "AJ12-Calculator-with-Stacks-Packs": (
        "Apply one token without losing operands on failure",
        ["12 -> true [12]", "3 -> true [12, 3]", "- -> true [9]", "2 -> true [9, 2]",
         "* -> true [18]", "0 -> true [18, 0]", "/ -> false [18, 0]"],
    ),
    "AJ12-Deque-Packs": (
        "Move the front backward and write the value", ["30", "10", "20", "1"],
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
        with TemporaryDirectory(prefix="java-aj12-") as directory:
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
            assert checked_stdout(result) == ["Stack and queue checks passed"]
            assert checked_stdout(run_java(directory, "Main")) == examples


if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder, contract in ASSIGNMENTS.items():
        verify_assignment(folder, *contract)
        print(f"Verified {folder}: starter and reference", flush=True)
