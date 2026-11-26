"""Compile AJ19 package directories and verify both source roles."""

import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {
    "AJ19-Tested-Gradebook-Service": ("Compute the weighted mean of submitted scores", ["87.50 B"]),
    "AJ19-Command-Parser-Test-Harness": ("Parse a full command into a typed value", ["Add[item=pencils, count=3]", "Print[]"]),
    "AJ19-Characterization-Test-Bug-Fix": ("Expected 0, got 250", ["0 cents"]),
}

def replay(project, output, entry, *arguments):
    return subprocess.run(
        [JAVA_BIN / "java", "-cp", output, entry, *arguments],
        cwd=project, text=True, capture_output=True, timeout=10,
    )

def successful(result, expected):
    assert result.returncode == 0 and not result.stderr, result.stderr
    assert result.stdout.splitlines() == expected, result.stdout

if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    for folder, (boundary, output_lines) in ASSIGNMENTS.items():
        for role in ("starter", "solution"):
            project = ROOT / folder / role
            sources = (project / "sources.txt").read_text().splitlines()
            assert len(sources) == len(set(sources)) == 3
            assert set(sources) == {file.relative_to(project).as_posix() for file in project.rglob("*.java")}
            with TemporaryDirectory(prefix="java3-aj19-packages-") as output:
                subprocess.run(
                    [JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror", "-d", output, "@sources.txt"],
                    cwd=project, check=True, timeout=20,
                )
                result = replay(project, output, "checks.ContractTests")
                if role == "solution":
                    successful(result, ["AJ19 checks passed"])
                    successful(replay(project, output, "runner.Main"), output_lines)
                else:
                    assert result.returncode != 0 and boundary in result.stderr, result.stderr
                    if folder == "AJ19-Characterization-Test-Bug-Fix":
                        successful(replay(project, output, "checks.ContractTests", "characterize"),
                                   ["Original threshold behavior captured"])
                        successful(replay(project, output, "runner.Main"), ["250 cents"])
        print(f"{folder}: starter and reference verified", flush=True)
