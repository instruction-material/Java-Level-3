"""Verify native Java 21 readiness and output for Check-In 1."""
import os
from pathlib import Path
import re
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
CASES = {
    "AJ-Check-In-1-Workbook-Packs": (
        "Workbook checks passed", "Count the characters recursively", ["12", "6", "true", "3", "-1"]
    ),
    "AJ-Check-In-1-Benchmark-Packs": (
        "Benchmark checks passed", "Time and count the identical target list", None
    ),
    "J3-11-check-in-1-supplemental-2": (
        "Boundary checks passed", "Find the monotone false-to-true boundary", ["2", "-1"]
    ),
    "J3-12-check-in-1-supplemental-3": (
        "Loop-count checks passed", "Derive the exact number of inner iterations", ["0", "8", "32"]
    ),
}

def java(place, entry, *args):
    return subprocess.run([JAVA_BIN / "java", "-cp", place, entry, *args],
                          text=True, capture_output=True, timeout=20)

def output(result):
    assert result.returncode == 0, result.stderr
    return result.stdout.splitlines()

def verify_role(directory, folder, role):
    message, unfinished, examples = CASES[folder]
    result = java(directory, "ContractTests")
    if role == "starter":
        assert result.returncode != 0 and f"UnsupportedOperationException: {unfinished}" in result.stderr
        return
    assert output(result) == [message]
    lines = output(java(directory, "Main"))
    if examples is not None:
        assert lines == examples
    else:
        assert len(lines) == 3
        assert lines[0] == "2000 queries per round, 1000 hits and 1000 misses, 4 rounds"
        for line, kind in zip(lines[1:], ("Linear", "Binary")):
            assert re.fullmatch(rf"{kind} search mean: [0-9]+\.[0-9]{{3}} ms", line), line
    if folder == "AJ-Check-In-1-Workbook-Packs":
        assert output(java(directory, "Main", "trace")) == ["[5, 4, 3, 2, 1, 2, 3, 2, 1]"]
        assert output(java(directory, "Main", "memo")) == ["252"]
        assert output(java(directory, "ContractTests", "memo")) == ["Memo checks passed"]

if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder in CASES:
        location = ROOT / folder
        assert (location / "starter/Main.java").read_bytes() != (location / "solution/Main.java").read_bytes()
        for role in ("starter", "solution"):
            with TemporaryDirectory(prefix="check-in-1-") as directory:
                owner = location / role
                subprocess.run(
                    [JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror",
                     "-d", directory, owner / "Main.java", owner / "ContractTests.java"],
                    check=True, timeout=25,
                )
                verify_role(directory, folder, role)
        print(f"Verified {folder}: both role contracts and runnable output", flush=True)
