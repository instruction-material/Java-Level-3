"""Compile and exercise both Java 21 roles of each binary search tree assignment."""

import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ14-BST-Basic-Packs": ("Insert a distinct key into the ordered tree", ["[3, 7, 12, 20]", "true", "false"]),
    "AJ14-BST-Traversal-Packs": (
        "Visit each key before, between, or after its subtrees",
        ["[12, 3, -4, 7, 20, 16, 23]", "[-4, 3, 7, 12, 16, 20, 23]", "[-4, 7, 3, 16, 23, 20, 12]"],
    ),
    "AJ14-BST-Removal-Packs": (
        "Find the key and reconnect the replacement subtree", ["[3, 7, 16, 20]", "false", "0"],
    ),
    "J3-29-aj14-binary-search-trees-supplemental-2": (
        "Visit only branches that can contain an in-range value", ["[7, 12, 18, 20]"],
    ),
    "J3-30-aj14-binary-search-trees-supplemental-3": (
        "Return the in-range subtree using the surviving nodes", ["[18, 20, 23]"],
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
        with TemporaryDirectory(prefix="java-aj14-") as directory:
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
            assert checked_stdout(result) == ["Binary search tree checks passed"]
            assert checked_stdout(run_java(directory, "Main")) == examples

            if folder == "AJ14-BST-Basic-Packs":
                assert checked_stdout(run_java(directory, "ContractTests", "ceiling")) == ["Ceiling checks passed"]
                assert checked_stdout(run_java(directory, "Main", "ceiling")) == ["12"]


if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder, contract in ASSIGNMENTS.items():
        verify_assignment(folder, *contract)
        print(f"Verified {folder}: starter and reference", flush=True)
