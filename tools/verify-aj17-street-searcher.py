"""Exercise the staged street-searcher workbook and both Java 21 practice packs."""

import os
from pathlib import Path
from shutil import copyfile
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
CAPSTONE = "AJ17-Street-Searcher-Packs"
ASSIGNMENTS = {
    CAPSTONE: ("Store a directed road with its validated weight", ["Harbor -> Market -> Station", "5.00 miles"]),
    "J3-37-aj17-master-project-google-maps-supplemental-2": (
        "Accumulate mileage through connected legs",
        ["[Stop[name=Harbor, totalMiles=0], Stop[name=Market, totalMiles=2], Stop[name=Station, totalMiles=5]]"],
    ),
    "J3-38-aj17-master-project-google-maps-supplemental-3": (
        "Classify each directed road by its reverse",
        ["A -> B different reverse cost", "B -> A different reverse cost", "B -> C one-way"],
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
    unfinished, example = ASSIGNMENTS[folder]
    tests = run_java(directory, "ContractTests")
    if role == "starter":
        expect_unfinished(tests, unfinished)
        return
    assert output(tests) == ["core checks passed" if folder == CAPSTONE else "Practice checks passed"]
    assert output(run_java(directory, "Main")) == example
    if folder == CAPSTONE:
        for stage in ("graph", "load", "route", "avoid"):
            assert output(run_java(directory, "ContractTests", stage)) == [f"{stage} checks passed"]
        assert output(run_java(directory, "Main", "roads.txt", "Harbor", "Station", "Market")) == [
            "Harbor -> Station", "8.00 miles",
        ]
        assert output(run_java(directory, "Main", "roads.txt", "Station", "Harbor")) == [
            "No route from Station to Harbor",
        ]
        assert output(run_java(directory, "Main", "roads.txt", "Harbor", "Grove")) == [
            "No route from Harbor to Grove",
        ]

def verify_stage_order(directory, solution):
    for completed, stage, unfinished in (
        ("StreetGraph.java", "load", "Parse and validate one local road record"),
        ("RoadFile.java", "route", "Find the cheapest route through open stops"),
    ):
        copyfile(solution / completed, directory / completed)
        compile_pack(directory)
        expect_unfinished(run_java(directory, "ContractTests", stage), unfinished)
    expect_unfinished(run_java(directory, "ContractTests", "avoid"), "Reuse the route search while excluding closed stops")

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
                if folder == CAPSTONE and role == "starter":
                    verify_stage_order(directory, roles / "solution")
        print(f"Verified {folder}: starter and reference", flush=True)
