"""Compile and check the five AJ6 packs on Java 21."""
import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory


ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
CASES = {
    "AJ6-Binary-Search-Iteration-Packs": (
        ["0", "-1"], "Iteration checks passed", "Search the inclusive range"
    ),
    "AJ6-Binary-Search-Recursion-Packs": (
        ["4", "-1"], "Recursion checks passed", "Search a smaller range recursively"
    ),
    "AJ6-Precision-Square-Root-Packs": (
        ["0.5"], "Square-root checks passed", "Bisect the interval containing the square root"
    ),
    "J3-09-aj6-binary-search-supplemental-2": (
        ["0:2:4 3:3:4 4:4:4"], "Trace checks passed", "Record the midpoint probes in order"
    ),
    "J3-10-aj6-binary-search-supplemental-3": (
        ["1", "4", "5"], "Insertion checks passed", "Find the leftmost valid insertion position"
    ),
}


def run(directory, entry, *args):
    return subprocess.run(
        [JAVA_BIN / "java", "-cp", directory, entry, *args],
        capture_output=True, text=True, timeout=15,
    )


def verify(directory, expected, name):
    result = run(directory, name[0], *name[1:])
    assert result.returncode == 0, result.stderr
    assert result.stdout.splitlines() == expected, (name, result.stdout)


def verify_pack(pack, expected):
    console, check, task = expected
    starter = ROOT / pack / "starter"
    solution = ROOT / pack / "solution"
    assert (starter / "Main.java").read_bytes() != (solution / "Main.java").read_bytes()
    for role in (starter, solution):
        with TemporaryDirectory(prefix="aj6-contract-") as directory:
            subprocess.run(
                [JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror", "-d", directory,
                 role / "Main.java", role / "ContractTests.java"],
                check=True, timeout=20,
            )
            if role == starter:
                result = run(directory, "ContractTests")
                assert result.returncode != 0
                assert "UnsupportedOperationException: " + task in result.stderr, result.stderr
                continue
            verify(directory, [check], ("ContractTests",))
            verify(directory, console, ("Main",))
            if pack == "AJ6-Binary-Search-Iteration-Packs":
                verify(directory, ["Measurement checks passed"], ("ContractTests", "count"))
                verify(directory, [
                    "SearchResult[index=0, probes=2]", "SearchResult[index=-1, probes=3]"
                ], ("Main", "count"))
    print(f"Verified {pack}: compilation, both roles and exact outputs", flush=True)


if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for directory, expected in CASES.items():
        verify_pack(directory, expected)
