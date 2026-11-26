"""Verify bounded task execution and both stages of the Java library simulation."""

import os
from pathlib import Path
from shutil import copytree
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
CAPSTONE = "AJ22-Event-Driven-Simulation-Capstone"
ASSIGNMENTS = {
    "AJ22-Concurrent-Task-Runner": (
        "Run bounded batches and return results in input order",
        "Runner checks passed",
        ["oak 5", "cedar Negative value", "elm 0"],
    ),
    CAPSTONE: (
        "Apply the next circulation event with FIFO handoff",
        "Simulation checks passed",
        ["loan Ada oak", "waiting Bea oak", "waiting Cy oak",
         "handoff Ada Bea oak", "cancelled Cy oak", "returned Bea oak"],
    ),
}

def run(directory, output, entry, *args):
    return subprocess.run([JAVA_BIN / "java", "-cp", output, entry, *args], cwd=directory,
                          text=True, capture_output=True, timeout=15)

def successful(result, expected):
    assert result.returncode == 0 and not result.stderr, result.stderr
    assert result.stdout.splitlines() == expected, result.stdout

def compile_pack(project, output):
    sources = (project / "sources.txt").read_text().splitlines()
    assert len(sources) == len(set(sources))
    assert set(sources) == {file.relative_to(project).as_posix() for file in project.rglob("*.java")}
    subprocess.run([JAVA_BIN / "javac", "--release", "21", "-Xlint:all", "-Werror",
                    "-d", output, "@sources.txt"], cwd=project, check=True, timeout=25)

def verify_program(directory, output, folder, role):
    unfinished, suite, demonstration = ASSIGNMENTS[folder]
    result = run(directory, output, "checks.ContractTests")
    if role == "starter":
        assert result.returncode != 0
        assert f"UnsupportedOperationException: {unfinished}" in result.stderr, result.stderr
        return

    successful(result, [suite])
    successful(run(directory, output, "runner.Main"), demonstration)
    if folder == CAPSTONE:
        successful(run(directory, output, "checks.ContractTests", "extension"), ["Snapshot checks passed"])
        report = directory / "test-report.txt"
        successful(run(directory, output, "runner.Main", "requests.txt", report.name), [])
        assert report.read_text().splitlines() == demonstration
        collision = run(directory, output, "runner.Main", "requests.txt", report.name)
        assert collision.returncode != 0 and "FileAlreadyExistsException" in collision.stderr
        assert report.read_text().splitlines() == demonstration

if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder in ASSIGNMENTS:
        for role in ("starter", "solution"):
            with TemporaryDirectory(prefix="java-aj22-") as scratch:
                directory = Path(scratch)
                project = directory / "project"
                output = directory / "classes"
                copytree(ROOT / folder / role, project)
                compile_pack(project, output)
                verify_program(project, output, folder, role)
                if folder == CAPSTONE and role == "solution":
                    service = project / "src/service/Circulation.java"
                    code = service.read_text()
                    before = "return List.copyOf(shelf(title).waiting());"
                    assert code.count(before) == 1
                    service.write_text(code.replace(before, "return shelf(title).waiting();"))
                    compile_pack(project, output)
                    successful(run(project, output, "checks.ContractTests"), ["Simulation checks passed"])
                    result = run(project, output, "checks.ContractTests", "extension")
                    assert result.returncode != 0 and "Returned waiting list permits mutation" in result.stderr
        print(f"Verified {folder}: starter and reference", flush=True)
