"""Verify the Java 21 packs for selection, insertion and merge sorting."""
import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
CASES = {
    "AJ7-Selection-Sort-Packs": ("Select the smallest remaining value for each position",
                               ["[1, 1, 3, 4]", "Comparisons: 6"]),
    "AJ7-Insertion-Sort-Packs": ("Insert each next value into the sorted prefix",
                               ["[1, 1, 3, 4]", "Shifts: 4"]),
    "AJ7-Bookshelf-Packs": ("Select the largest remaining book from a copy",
                           ["[Drift, Reef, Current, Jetty]"]),
    "J3-13-aj7-selection-and-insertion-sort-supplemental-2":
        ("Insert tasks by ascending priority and preserve ties", ["[review, upload, archive]"]),
    "J3-14-aj7-selection-and-insertion-sort-supplemental-3":
        ("Count earlier-larger pairs without sorting", ["4", "0"]),
    "AJ9-Merge-Packs": ("Merge both sorted arrays without changing them", ["[-2, 1, 1, 2, 5, 7, 9]"]),
    "AJ9-Split-Packs": ("Divide each range until it gives singleton leaves", ["[[-1], [8], [3], [8], [0]]"]),
    "AJ9-Merge-Sort-Packs": ("Sort the two smaller halves and merge the results", ["[-3, 2, 4, 4, 8]"]),
    "J3-17-aj9-merge-sort-supplemental-2":
        ("Record the range before recursing into its halves", ["[0,3) [0,1) [1,3) [1,2) [2,3)"]),
    "J3-18-aj9-merge-sort-supplemental-3":
        ("Merge attendance IDs and emit each distinct ID once", ["[1, 2, 4, 8, 9]"]),
}

def java(place, entry, *args):
    return subprocess.run([JAVA_BIN / "java", "-cp", place, entry, *args],
                          text=True, capture_output=True, timeout=20)

def output(result):
    assert result.returncode == 0, result.stderr
    return result.stdout.splitlines()

def verify_role(directory, folder, role):
    unfinished, examples = CASES[folder]
    result = java(directory, "ContractTests")
    if role == "starter":
        assert result.returncode and f"UnsupportedOperationException: {unfinished}" in result.stderr, result.stderr
        return
    assert output(result) == ["Sorting checks passed"]
    assert output(java(directory, "Main")) == examples
    if folder == "AJ9-Merge-Packs":
        assert output(java(directory, "Main", "stable")) == ["[left, right]"]
        assert output(java(directory, "ContractTests", "stable")) == ["Stable merge checks passed"]

if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder in CASES:
        location = ROOT / folder
        assert (location / "starter/Main.java").read_bytes() != (location / "solution/Main.java").read_bytes()
        for role in ("starter", "solution"):
            with TemporaryDirectory(prefix="java-sorting-") as directory:
                source = location / role
                subprocess.run(
                    [JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror",
                     "-d", directory, source / "Main.java", source / "ContractTests.java"],
                    check=True, timeout=25,
                )
                verify_role(directory, folder, role)
        print(f"Verified {folder}: both role contracts and runnable output", flush=True)
