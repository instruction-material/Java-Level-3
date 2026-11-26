"""Compile and replay each Java Level 3 review role with Java 21."""

import os
from pathlib import Path
from shutil import copyfile
import subprocess
from tempfile import TemporaryDirectory

ROOT = Path(__file__).resolve().parents[1]
JAVA_BIN = Path(os.environ["JAVA_HOME"]) / "bin"
ASSIGNMENTS = {'AJ1-Mad-Lib-Packs': ('Assemble the story from four lines',
                       'red fox\ndance\ncurious\na lantern\n',
                       ['A curious red fox likes to dance with a lantern.']),
 'AJ1-Chat-Bot-Packs': ('Choose a reply from the activity',
                        'Mira\nDRAWING\n',
                        ['Hello, Mira! Try sketching a tree.']),
 'AJ2-Array-Practice-Packs': ('Build an independent doubled array', None, ['[6, -4, 10]', '2', '6']),
 'AJ2-Grid-Practice-Packs': ('Fill the grid with row plus column', None, ['[[0, 1, 2], [1, 2, 3]]', '3']),
 'AJ2-ArrayList-Practice-Packs': ('Keep even values in their original order', None, ['[2, -4, 2]']),
 'AJ2-Test-Scores-Packs': ('Calculate a nontruncated score mean', None, ['89.5', 'B']),
 'AJ2-Matrix-Addition-Packs': ('Add matching rectangular matrices', None, ['[[5, 5], [5, 5]]']),
 'AJ2-Sandwich-Orders-Packs': ('Encode selected ingredients in order', None, ['lc']),
 'AJ3-Student-Class-Packs': ('Initialize and count a valid student',
                             None,
                             ['Cedar #7 present', 'Rowan #9 absent', '2']),
 'AJ3-Baked-Good-Packs': ('Compare baked goods by ingredient count', None, ['350 cents']),
 'J3-01-aj1-review-variables-strings-and-input-supplemental-2': ('Calculate the fare in tokens', None, ['14']),
 'J3-02-aj1-review-variables-strings-and-input-supplemental-3': ('Read through invalid lines to the first '
                                                                 'count',
                                                                 None,
                                                                 ['7']),
 'J3-03-aj3-review-objects-and-classes-supplemental-2': ('Preserve stock when checking out a copy',
                                                         None,
                                                         ['true', 'false', '0']),
 'J3-04-aj3-review-objects-and-classes-supplemental-3': ('Price a standard parcel', None, ['150', '200'])}

def run_java(directory, entry_point, *args, stdin=None):
    return subprocess.run([JAVA_BIN / "java", "-cp", directory, entry_point, *args],
                          cwd=directory, text=True, input=stdin, capture_output=True, timeout=15)

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
    unfinished, stdin, expected = ASSIGNMENTS[folder]
    checks = run_java(directory, "ContractTests")
    if role == "starter":
        expect_unfinished(checks, unfinished)
        return
    assert output(checks) == ["Review checks passed"]
    assert output(run_java(directory, "Main", stdin=stdin)) == expected
    if folder == "AJ1-Mad-Lib-Packs":
        assert output(run_java(directory, "ContractTests", "headline")) == ["Review checks passed"]
        assert output(run_java(directory, "Main", "headline", stdin=stdin)) == [*expected, "CURIOUS RED FOX ADVENTURE"]
        assert output(run_java(directory, "Main", stdin="red fox")) == [
            "Enter an animal, a verb, an adjective, and a noun on separate lines."
        ]
    if folder == "AJ3-Student-Class-Packs":
        assert output(run_java(directory, "ContractTests", "attendance")) == ["Review checks passed"]
        assert output(run_java(directory, "Main", "attendance")) == [*expected, "1"]
    if folder == "AJ1-Chat-Bot-Packs":
        assert output(run_java(directory, "Main", stdin="Mira")) == []

if __name__ == "__main__":
    version = subprocess.check_output([JAVA_BIN / "javac", "-version"], text=True).strip()
    assert version == "javac 21" or version.startswith("javac 21."), version
    print(version, flush=True)
    for folder in ASSIGNMENTS:
        for role in ("starter", "solution"):
            with TemporaryDirectory(prefix="java3-review-") as temporary:
                directory = Path(temporary)
                for entry in (ROOT / folder / role).iterdir():
                    copyfile(entry, directory / entry.name)
                compile_pack(directory)
                verify_program(directory, folder, role)
        print(f"Verified {folder}: starter and reference", flush=True)
