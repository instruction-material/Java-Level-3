"""Verify the Java 21 AJ10 starter and reference contracts."""
import os
from pathlib import Path
import shutil
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
CASES = {
    "AJ10-Bakery-Packs": ("Read and retain the worker and product with the shared input",
                         ["Alex | Baker | Harbor Bakery", "Scone (Lemon) | flour, lemon"]),
    "AJ10-Anything-Array-Packs": ("Return a value with its node type",
                                 ["C", "hello world", "true", "100", "3.1415"]),
    "AJ10-Exceptions-Packs": ("Parse a signed integer and report missing data",
                             ["Quotient: 2.5", "Words: 3", "Apple starts with a vowel: true"]),
    "J3-21-aj10-sustainable-programming-supplemental-2": (
        "Convert and validate a single stock record", ["flour: 12"]
    ),
    "J3-22-aj10-sustainable-programming-supplemental-3": (
        "Collect successful values and continue after rejected rows", ["[12.5, -40.0, 60.0]", "[2, 3, 5]"]
    ),
}

def java(place, entry, *args, data=""):
    return subprocess.run([JAVA_BIN / "java", "-cp", place, entry, *args], cwd=place,
                          input=data, text=True, capture_output=True, timeout=20)

def output(result):
    assert result.returncode == 0, result.stderr
    return result.stdout.splitlines()

def verify_role(directory, folder, role):
    unfinished, examples = CASES[folder]
    result = java(directory, "ContractTests")
    if role == "starter":
        assert result.returncode and f"UnsupportedOperationException: {unfinished}" in result.stderr, result.stderr
        return
    assert output(result) == ["Sustainable-programming checks passed"]
    assert output(java(directory, "Main")) == examples
    if folder == "AJ10-Bakery-Packs":
        assert output(java(directory, "ContractTests", "batches")) == ["Batch checks passed"]
        assert output(java(directory, "Main", "batches")) == ["Batches: 2"]
        data = "Bay Bakery\n7 Pier\nJo\nPacker\n1700\nRoll\nPlain\nflour, water\n8\n"
        assert output(java(directory, "Main", "input", data=data)) == [
            "Jo | Packer | Bay Bakery", "Roll (Plain) | flour, water"
        ]
    if folder == "AJ10-Exceptions-Packs":
        assert output(java(directory, "Main", "bad", "2", "missing-course-file.txt")) == [
            "Invalid integer.", "Cannot read the file.", "Apple starts with a vowel: true"
        ]
        assert output(java(directory, "Main", "5", "0"))[0] == "Cannot divide by zero."

if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder in CASES:
        location = ROOT / folder
        assert (location / "starter/Main.java").read_bytes() != (location / "solution/Main.java").read_bytes()
        for role in ("starter", "solution"):
            with TemporaryDirectory(prefix="java-aj10-") as directory:
                source = location / role
                subprocess.run(
                    [JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror",
                     "-d", directory, *sorted(source.glob("*.java"))],
                    check=True, timeout=25,
                )
                for data in source.glob("*.txt"):
                    shutil.copyfile(data, Path(directory) / data.name)
                verify_role(directory, folder, role)
        print(f"Verified {folder}: both role contracts and runnable output", flush=True)
