"""Exercise the paired Java 21 reference-library labs."""

import os
from pathlib import Path
from shutil import copyfile
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ18-Checked-Reading-Boundary-Packs": (
        "Classify invalid measurements at the parser boundary", ["[8, 12, 0]", "[2, 4]"],
    ),
    "AJ18-Nested-Room-Packs": (
        "Read the current owner name and desk number", ["North-3", "Maple-3"],
    ),
    "AJ18-Chaining-Resize-Packs": (
        "Unlink a single matching entry", ["false", "collision", "negative"],
    ),
}

def run_java(directory, entry_point, *args):
    return subprocess.run([JAVA_BIN / "java", "-cp", directory, entry_point, *args], cwd=directory,
                          text=True, capture_output=True, timeout=15)

def output(result):
    assert result.returncode == 0, result.stderr
    return result.stdout.splitlines()

def compile_pack(directory):
    subprocess.run([JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror",
                    *sorted(directory.glob("*.java"))], cwd=directory, check=True, timeout=25)

def expect_unfinished(result, message):
    assert result.returncode != 0
    assert f"UnsupportedOperationException: {message}" in result.stderr, result.stderr

def verify_program(directory, folder, role):
    unfinished, demonstration = ASSIGNMENTS[folder]
    result = run_java(directory, "ContractTests")
    if role == "starter":
        expect_unfinished(result, unfinished)
        return
    assert output(result) == ["Reference lab checks passed"]
    assert output(run_java(directory, "Main")) == demonstration

if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder in ASSIGNMENTS:
        roles = ROOT / folder
        for role in ("starter", "solution"):
            with TemporaryDirectory(prefix="java-aj17-") as temporary:
                directory = Path(temporary)
                for entry in (roles / role).iterdir():
                    copyfile(entry, directory / entry.name)
                compile_pack(directory)
                verify_program(directory, folder, role)
        print(f"Verified {folder}: starter and reference", flush=True)
