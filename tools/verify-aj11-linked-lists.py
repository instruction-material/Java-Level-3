"""Compile and exercise both Java 21 roles of each linked-list assignment."""

import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ11-Singly-Linked-List-Packs": (
        "LinkedList.java", "Insert one node after the sentinel", ["[A, B, C]", "B", "0"],
    ),
    "AJ11-Doubly-Linked-List-Packs": (
        "DoublyLinkedList.java", "Walk backward through only the data nodes",
        ["[7, 12, 21]", "[21, 12, 7]", "12"],
    ),
    "J3-23-aj11-linked-lists-supplemental-2": (
        "Main.java", "Unlink only the earliest equal data node", ["true", "b", "a"],
    ),
    "J3-24-aj11-linked-lists-supplemental-3": (
        "Main.java", "Redirect the links one at a time", ["3", "2", "1"],
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


def verify_assignment(folder, implementation, starter_message, examples):
    assignment = ROOT / folder
    starter_file = assignment / "starter" / implementation
    solution_file = assignment / "solution" / implementation
    assert starter_file.read_bytes() != solution_file.read_bytes(), folder

    for role in ("starter", "solution"):
        with TemporaryDirectory(prefix="java-aj11-") as directory:
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
            assert checked_stdout(result) == ["List checks passed"]
            assert checked_stdout(run_java(directory, "Main")) == examples
            if implementation == "LinkedList.java":
                assert checked_stdout(run_java(directory, "ContractTests", "find")) == ["Find checks passed"]
                assert checked_stdout(run_java(directory, "Main", "find")) == ["1"]


if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder, contract in ASSIGNMENTS.items():
        verify_assignment(folder, *contract)
        print(f"Verified {folder}: starter and reference", flush=True)
