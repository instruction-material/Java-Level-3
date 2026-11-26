"""Compile and exercise both Java 21 roles of each collections checkpoint assignment."""

import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ-Check-In-3-Workbook-Packs": (
        "Read lines until an integer or end of input",
        ["A triple: Triple[first=oak, second=true, third=2]", "42", "1", "[elm]", "acdc",
         "[blue, red, green]", "[1, 2, 5, 5]", "{oak=5, elm=3}"],
    ),
    "AJ-Check-In-3-Collections-Practice-Packs": (
        "Reverse the queue through a stack",
        ["[Cora, Ada, Bea, Ada]", "[Ada, Bea, Ada, Cora]", "[Ada, Ada, Bea, Cora]"],
    ),
    "J3-27-check-in-3-supplemental-2": (
        "Count IDs and dispatch in first arrival order",
        ["[Dispatch[id=A2, count=3], Dispatch[id=B1, count=2], Dispatch[id=C3, count=1]]"],
    ),
    "J3-28-check-in-3-supplemental-3": (
        "Undo the latest addition and update the item counts", ["{bulb=1, fan=1, lamp=1}"],
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
        with TemporaryDirectory(prefix="java-checkin3-") as directory:
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
            assert checked_stdout(result) == ["Collections checkpoint checks passed"]
            assert checked_stdout(run_java(directory, "Main")) == examples

            if folder == "AJ-Check-In-3-Workbook-Packs":
                assert checked_stdout(run_java(directory, "ContractTests", "groups")) == ["Grouped collections checks passed"]
                assert checked_stdout(run_java(directory, "Main", "groups")) == ["{oak=[2, 5], elm=[3]}"]


if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder, contract in ASSIGNMENTS.items():
        verify_assignment(folder, *contract)
        print(f"Verified {folder}: starter and reference", flush=True)
