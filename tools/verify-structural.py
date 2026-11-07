#!/usr/bin/env python3
"""Compile structural role packs and check dispatch, ownership, lifecycle and sharing."""
from __future__ import annotations
import hashlib
import importlib.util
import json
import os
from pathlib import Path
import re
import shutil
import tempfile

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location("native_process", ROOT / "tools/verify-recursion.py")
assert spec and spec.loader
native = importlib.util.module_from_spec(spec)
spec.loader.exec_module(native)
native.TASK = "java-patterns-structural-contracts"
run = native.run

def verify() -> dict:
    java_home = os.environ.get("JAVA_HOME")
    java = str(Path(java_home) / "bin/java") if java_home else shutil.which("java")
    javac = str(Path(java_home) / "bin/javac") if java_home else shutil.which("javac")
    assert java and javac
    version = run([javac, "-version"], ROOT).strip()
    assert version.startswith("javac 21."), version
    result = {"jdk":version, "rolesCompiled":0, "sourceHashes":{},
              "unfinishedStartersRejected":0, "controlledRegressionsRejected":0,
              "compileBoundariesRejected":0, "optionalIndependentOfCore":False}
    source = ROOT / "DPJ5-Structural-Scene-Packs"
    demo = "group[circle(moon,2);[box(tile,3x4)];C:sun:1]\nA:INK@1,2\nA:INK@5,6\ntrue\n"
    def compile_at(folder):
        run([javac,"--release","21","-Xlint:all","-Werror","-d","classes","@sources.txt"],folder)
    def contracts(folder, *, core=False, success=True):
        return run([java,"-cp","classes","ContractTests",*(["core"] if core else [])],folder,success=success)
    with tempfile.TemporaryDirectory(prefix="java-structural-") as temporary:
        work = Path(temporary)
        for role in ["starter","solution"]:
            folder = source / role; output = work / role
            shutil.copytree(folder,output)
            listed = (output / "sources.txt").read_text().splitlines()
            assert len(listed) == len(set(listed)) == 12
            assert set(listed) == {str(p.relative_to(output)) for p in output.rglob("*.java")}
            files = sorted(p for p in folder.rglob("*") if p.is_file())
            assert len(files) == 14
            for file in files:
                result["sourceHashes"][str(file.relative_to(ROOT))] = hashlib.sha256(file.read_bytes()).hexdigest()
            compile_at(output);result["rolesCompiled"] += 1
            if role == "starter":
                failure = contracts(output,core=True,success=False)
                assert "UnsupportedOperationException: Implement the circle rendering bridge." in failure,failure
                result["unfinishedStartersRejected"] += 1
            else:
                core = contracts(output,core=True); full = contracts(output)
                core_match = re.fullmatch(r"Core structural contracts: (\d+) passed\n",core)
                full_match = re.fullmatch(r"Complete structural contracts: (\d+) passed\n",full)
                assert core_match and full_match,(core,full)
                assert int(core_match[1]) >= 500 and int(full_match[1]) == int(core_match[1]) + 7
                result.update(coreContracts=core.strip(),completeContracts=full.strip())
                assert run([java,"-cp","classes","Main"],output) == demo
        core_only = work / "core-only";shutil.copytree(source/"solution",core_only)
        file = core_only / "src/scene/Placement.java";text = file.read_text()
        old = "return new Placement(glyph, Math.addExact(x, dx), Math.addExact(y, dy));"
        assert text.count(old) == 1
        file.write_text(text.replace(old,'throw new UnsupportedOperationException("Implement optional checked placement shifting.");'))
        compile_at(core_only)
        assert contracts(core_only,core=True).strip() == result["coreContracts"]
        assert run([java,"-cp","classes","Main"],core_only) == demo
        failure = contracts(core_only,success=False)
        assert "UnsupportedOperationException: Implement optional checked placement shifting." in failure
        result["optionalIndependentOfCore"] = True
        for filename,old,new,core_mode in [
            ("Circle.java","renderer.circle(name, radius)","new Formats.Plain().circle(name, radius)",True),
            ("Group.java","for (Graphic child : children)","for (Graphic child : children.reversed())",True),
            ("Group.java",'List.copyOf(Objects.requireNonNull(children, "children"))','java.util.Collections.unmodifiableList(Objects.requireNonNull(children, "children"))',True),
            ("Frame.java",'return "[" + Objects.requireNonNull(wrapped.render(), "wrapped text") + "]";','wrapped.render(); return "[" + Objects.requireNonNull(wrapped.render(), "wrapped text") + "]";',True),
            ("LazyGraphic.java",'if (loaded == null) loaded = Objects.requireNonNull(loader.get(), "loaded graphic");','loaded = Objects.requireNonNull(loader.get(), "loaded graphic");',True),
            ("GlyphPool.java","return pool.computeIfAbsent(new Key(symbol, tone), key -> new Glyph(key.symbol(), key.tone()));","return new Glyph(symbol, tone);",True),
            ("Placement.java","return new Placement(glyph, Math.addExact(x, dx), Math.addExact(y, dy));","return new Placement(glyph, Math.addExact(x, dx), y);",False),
        ]:
            output = work / ("regression-" + str(result["controlledRegressionsRejected"]))
            shutil.copytree(source/"solution",output)
            file = output / "src/scene" / filename;text = file.read_text()
            assert text.count(old) == 1
            file.write_text(text.replace(old,new));compile_at(output)
            failure = contracts(output,core=core_mode,success=False)
            assert "AssertionError" in failure,failure
            result["controlledRegressionsRejected"] += 1
        output = work / "solution"
        for code,marker in [
            ("import scene.*; class ForeignClient { void change(Placement value) { value.x = 4; } }","private access"),
            ("import scene.*; class ForeignClient { void change(GlyphPool.Glyph value) { value.symbol = 'B'; } }","private access"),
        ]:
            (output/"ForeignClient.java").write_text(code)
            failure = run([javac,"--release","21","-Xlint:all","-Werror","-cp","classes","ForeignClient.java"],output,success=False)
            assert marker in failure,failure
            result["compileBoundariesRejected"] += 1
    result.update(accepted=True,temporaryBuildsCleaned=True,learnerPatternChoiceReasoningInferred=False)
    return result

if __name__ == "__main__":
    print(json.dumps(verify(),sort_keys=True),flush=True)
