"""Verify the Java 21 packs for optional bubble sorting and Check-In 2."""
import os
from pathlib import Path
import re
import shutil
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
CASES = {
    "AJ8-Bubble-Sort-Packs": ("Sort adjacent values with an optional no-swap stop",
                           ["Early-stop comparisons: 7", "[1, 2, 3, 4, 5]"]),
    "AJ8-Alphabetical-Order-Packs": ("Sort words by adjacent Java string comparison", None),
    "J3-15-aj8-bubble-sort-supplemental-2": ("Capture the array at the end of each shrinking pass",
                                          ["[2, 3, 1, 4]", "[2, 1, 3, 4]", "[1, 2, 3, 4]"]),
    "J3-16-aj8-bubble-sort-supplemental-3": ("Move the largest and smallest items toward opposite ends",
                                          ["[-1, 2, 2, 3, 4, 8]"]),
    "AJ-Check-In-2-Workbook-Packs": ("Trace the next minimum exchanges", [
        "[14, 23, 76, 75, 34, 25]", "[13, 15, 27, 99, 8, 1]",
        "[1, 2, 3, 4, 5, 7, 10, 11]"
    ]),
    "AJ-Check-In-2-Benchmark-Packs": ("Time a copy and verify the sorted result", None),
    "J3-19-check-in-2-supplemental-2": ("Verify both order and the multiset of values", ["true", "false"]),
    "J3-20-check-in-2-supplemental-3": ("Select the sorted prefix and stop after k positions", ["[-3, -3, 2]"]),
}

def java(place, entry, *args):
    return subprocess.run([JAVA_BIN / "java", "-cp", place, entry, *args], cwd=place,
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
    lines = output(java(directory, "Main"))
    if examples is not None:
        assert lines == examples, (folder, lines)
    if folder == "AJ8-Alphabetical-Order-Packs":
        words = (Path(directory) / "words.txt").read_text().split()
        assert words and lines == ["[" + ", ".join(sorted(words)) + "]"]
    if folder == "AJ-Check-In-2-Benchmark-Packs":
        assert lines[0] == "300 values per trial, 4 trials per input order"
        assert len(lines) == 16 and lines[1::5] == ["random", "ascending", "descending"]
        for group in range(3):
            values = lines[2 + group * 5:6 + group * 5]
            for row, name in zip(values, ["Selection", "Insertion", "Bubble", "Merge"], strict=True):
                assert re.fullmatch(rf"{name} sort mean: [0-9]+\.[0-9]{{3}} ms", row)
    if folder == "AJ8-Bubble-Sort-Packs":
        assert output(java(directory, "Main", "last")) == ["Last-exchange comparisons: 4", "[1, 2, 3, 4, 5]"]
        assert output(java(directory, "ContractTests", "last")) == ["Last-exchange checks passed"]
    if folder == "AJ-Check-In-2-Workbook-Packs":
        assert output(java(directory, "Main", "bubble")) == ["[12, 23, 12, 20, 43, 50]"]
        assert output(java(directory, "ContractTests", "bubble")) == ["Optional bubble checks passed"]
        assert output(java(directory, "Main", "range")) == ["[0, 3]"]
        assert output(java(directory, "ContractTests", "range")) == ["Equal-range checks passed"]

if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder in CASES:
        location = ROOT / folder
        assert (location / "starter/Main.java").read_bytes() != (location / "solution/Main.java").read_bytes()
        for role in ("starter", "solution"):
            with TemporaryDirectory(prefix="java-bubble-check-in-2-") as directory:
                source = location / role
                subprocess.run(
                    [JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror",
                     "-d", directory, *sorted(source.glob("*.java"))],
                    check=True, timeout=25,
                )
                word_file = source / "words.txt"
                if word_file.exists():
                    assert word_file.read_bytes().replace(b"\r\n", b"\n") == (
                        ROOT / "AJ8-Alphabetical-Order/words.txt"
                    ).read_bytes().replace(b"\r\n", b"\n")
                    shutil.copyfile(word_file, Path(directory) / "words.txt")
                verify_role(directory, folder, role)
        print(f"Verified {folder}: both role contracts and runnable output", flush=True)
