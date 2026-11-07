#!/usr/bin/env python3
"""Compile clean AJ4 role packs and check their numerical and console contracts."""
from __future__ import annotations
import hashlib
import json
import os
from pathlib import Path
import shutil
import signal
import subprocess
import tempfile
import time

ROOT = Path(__file__).resolve().parents[1]
TASK = "java3-recursion-contracts"


def run(args: list[str], directory: Path, *, data: str = "", success: bool = True) -> str:
    started = time.time()
    process = subprocess.Popen(args, cwd=directory, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                               stderr=subprocess.PIPE, text=True, start_new_session=True)
    print(json.dumps({"event": "start", "parentTaskId": TASK, "cwd": str(directory),
                      "command": args, "pid": process.pid, "parentPid": os.getpid(),
                      "startTime": started, "timeoutSeconds": 30}), flush=True)
    try:
        output, error = process.communicate(data, timeout=30)
    except BaseException:
        os.killpg(process.pid, signal.SIGKILL)
        process.communicate()
        print(json.dumps({"event": "end", "parentTaskId": TASK, "pid": process.pid,
                          "endTime": time.time(), "exitCode": process.returncode,
                          "childProcessGroupCleanup": "owned group killed and reaped"}), flush=True)
        raise
    print(json.dumps({"event": "end", "parentTaskId": TASK, "pid": process.pid,
                      "endTime": time.time(), "exitCode": process.returncode,
                      "childProcessGroupCleanup": "owned process exited and reaped"}), flush=True)
    if success and process.returncode != 0:
        raise AssertionError(f"{args}: {output}\n{error}")
    if not success:
        assert process.returncode != 0, (args, "unfinished or regressed implementation passed")
    return output + error


def compile_pack(source: Path, destination: Path, javac: str) -> None:
    destination.mkdir()
    for name in ["Main.java", "ContractTests.java"]:
        shutil.copy2(source / name, destination / name)
    run([javac, "-Xlint:all", "-Werror", "--release", "21", "-d", "classes",
         "Main.java", "ContractTests.java"], destination)


def verify() -> dict:
    javac = str(Path(os.environ["JAVA_HOME"]) / "bin/javac") if os.environ.get("JAVA_HOME") else shutil.which("javac")
    java = str(Path(os.environ["JAVA_HOME"]) / "bin/java") if os.environ.get("JAVA_HOME") else shutil.which("java")
    assert javac and java, "JDK 21 is required"
    version = run([javac, "-version"], ROOT)
    assert version.startswith("javac 21."), version
    packs = [
        ("AJ4-Recursion-Practice", "Practice contracts: 368 passed", "Implement factorial"),
        ("AJ4-Divisible-by-7", "Divisibility contracts: 220005 passed", "Implement isDivBySeven"),
    ]
    result = {"sourceHashes": {}, "jdk": version.strip(), "rolesCompiled": 0,
              "unfinishedStartersRejected": 0, "referenceContracts": [],
              "controlledRegressionsRejected": 0, "consoleCases": 0}
    with tempfile.TemporaryDirectory(prefix="java3-aj4-") as temporary:
        work = Path(temporary)
        example = ROOT / "AJ4-Call-Trace"
        example_output = work / "call-trace"
        compile_pack(example, example_output, javac)
        result["workedExamplesCompiled"] = 1
        assert "Call-trace contracts: 105 passed" in run([java, "-cp", "classes", "ContractTests"], example_output)
        assert run([java, "-cp", "classes", "Main"], example_output) == "10" + chr(10)
        for name in ["Main.java", "ContractTests.java", "README.md"]:
            result["sourceHashes"][str((example / name).relative_to(ROOT))] = hashlib.sha256((example / name).read_bytes()).hexdigest()
        for folder, expected, reminder in packs:
            packs_root = ROOT / (folder + "-Packs")
            assert (ROOT / folder / "Main.java").read_bytes() == (packs_root / "solution/Main.java").read_bytes()
            for role in ["starter", "solution"]:
                src = packs_root / role
                for name in ["Main.java", "ContractTests.java", "README.md"]:
                    result["sourceHashes"][str((src / name).relative_to(ROOT))] = hashlib.sha256((src / name).read_bytes()).hexdigest()
                output = work / (folder + "-" + role)
                compile_pack(src, output, javac)
                result["rolesCompiled"] += 1
                if role == "starter":
                    text = run([java, "-cp", "classes", "ContractTests"], output, success=False)
                    assert "UnsupportedOperationException: " + reminder in text, text
                    result["unfinishedStartersRejected"] += 1
                    continue
                text = run([java, "-cp", "classes", "ContractTests"], output)
                assert expected in text, text
                result["referenceContracts"].append(expected)
                if folder == "AJ4-Recursion-Practice":
                    expected_driver = ('Factorial of 5: 120\n2^3: 8\nSum of the digits 1234: 10\n'
                                       '5th Fibonacci number (term 1 is 0): 3\n'
                                       'Is "racecar" a palindrome? true\n')
                    assert run([java, "-cp", "classes", "Main"], output) == expected_driver
                    result["consoleCases"] += 1
                    old, new = "if (n <= 1) return 1;", "if (n == 1) return 1;"
                else:
                    tokens = "0 -7 14 15 203 -203 -2147483648 bad 2147483648 quit 7"
                    expected_driver = ('0 is divisible by 7\n-7 is divisible by 7\n'
                                       '14 is divisible by 7\n15 is NOT divisible by 7\n'
                                       '203 is divisible by 7\n-203 is divisible by 7\n'
                                       '-2147483648 is NOT divisible by 7\n'
                                       'Invalid integer.\nInvalid integer.\n')
                    for data in [tokens, tokens.replace(" ", "\r\n")]:
                        assert run([java, "-cp", "classes", "Main"], output, data=data) == expected_driver
                        result["consoleCases"] += 1
                    assert run([java, "-cp", "classes", "Main"], output, data="") == ""
                    result["consoleCases"] += 1
                    old, new = "return magnitude == 0 || magnitude == 7;", "return magnitude == 0;"
                source = output / "Main.java"
                text = source.read_text()
                assert text.count(old) == 1
                source.write_text(text.replace(old, new))
                run([javac, "-Xlint:all", "-Werror", "--release", "21", "-d", "classes",
                     "Main.java", "ContractTests.java"], output)
                run([java, "-cp", "classes", "ContractTests"], output, success=False)
                result["controlledRegressionsRejected"] += 1
    result.update(accepted=True, temporaryBuildsCleaned=True, learnerRecursiveTraceInferred=False)
    return result


if __name__ == "__main__":
    print(json.dumps(verify(), sort_keys=True), flush=True)
