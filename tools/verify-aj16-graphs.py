"""Compile and exercise both Java 21 roles of each graph assignment."""

import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ16-Basic-Graph-Packs": ("Register an isolated vertex without replacing its existing neighbors", ["[Library, Museum]", "[]", "3"]),
    "AJ16-Shortest-Path-Packs": ("Relax weighted edges and reconstruct the shortest route", ["5", "[0, 2, 1, 3]", "true"]),
    "AJ16-Eulerian-Mail-Circuit-Packs": ("Check street connectivity and odd degrees", ["CIRCUIT", "TRAIL", "NEITHER"]),
    "J3-33-aj16-graphs-supplemental-2": ("Visit reachable vertices in breadth-first order", ["[A, B, C, D]"]),
    "J3-34-aj16-graphs-supplemental-3": ("Count the unique incoming and outgoing arcs of each vertex", ["A: 1 1", "B: 2 2", "C: 0 0", "D: 0 0"]),
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
        with TemporaryDirectory(prefix="java-aj16-") as directory:
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
            assert checked_stdout(result) == ["Graph checks passed"]
            assert checked_stdout(run_java(directory, "Main")) == examples



if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder, contract in ASSIGNMENTS.items():
        verify_assignment(folder, *contract)
        print(f"Verified {folder}: starter and reference", flush=True)
