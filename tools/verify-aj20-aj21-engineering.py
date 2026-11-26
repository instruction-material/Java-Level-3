"""Verify both Java 21 roles for the AJ20 and AJ21 engineering workbooks."""

import os
from pathlib import Path
from shutil import copyfile
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {'AJ20-Generic-Repository': ('Filter records into an immutable snapshot',
                             ['Java Level 3',
                              'Active large courses: [CourseRecord[id=java-3',
                              'Experienced students: [StudentRecord[id=s-100',
                              'Rejected duplicate: duplicate id: java-3']),
 'AJ20-Comparator-Priority-Scheduler': ('Select and stabilize a task ordering policy',
                                        ['DEADLINE [A, B, C]', 'IMPORTANCE [B, A, C]', 'DURATION [C, B, A]']),
 'AJ20-Type-Safe-Plugin-Registry': ('Register one typed command without replacing an existing name',
                                    ['LEAF', 'fael', 'leaf', '4']),
 'AJ21-Log-Analyzer-Pipeline': ('Parse one typed event from a log row',
                                ['{CHECKOUT=2, RETURN=1}', '08:05 to 10:20', 'CHECKOUT', '[2, 5]']),
 'AJ21-CSV-Import-Validator': ('Validate the fields of a single input row',
                               ['[existing, oak, cedar]',
                                '[Rejection[line=2, reason=negative count], Rejection[line=3, '
                                'reason=duplicate id], Rejection[line=5, reason=duplicate id], '
                                'Rejection[line=6, reason=invalid status]]']),
 'AJ21-Report-Builder-Refactor': ('Parse an isolated report row', ['elm=4', 'oak=5', 'skipped=1'])}

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
        if folder == "AJ21-Report-Builder-Refactor":
            assert output(run_java(directory, "Main")) == demonstration
        return
    assert output(result) == ["Engineering checks passed"]
    actual = output(run_java(directory, "Main"))
    if folder == "AJ20-Generic-Repository":
        assert all(any(line.startswith(item) for line in actual) for item in demonstration), actual
    else:
        assert actual == demonstration, actual

if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder in ASSIGNMENTS:
        roles = ROOT / folder
        for role in ("starter", "solution"):
            with TemporaryDirectory(prefix="java-aj20-aj21-") as temporary:
                directory = Path(temporary)
                for entry in (roles / role).iterdir():
                    copyfile(entry, directory / entry.name)
                compile_pack(directory)
                verify_program(directory, folder, role)
        print(f"Verified {folder}: starter and reference", flush=True)
