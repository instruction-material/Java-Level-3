"""Verify DPR8 pattern journeys, working baselines and independent optional completions."""
from pathlib import Path
import datetime
import hashlib
import json
import os
import shutil
import signal
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]
PACK = ROOT / "DPR8-Pattern-Journeys-Packs"
OUT = ROOT / "output/java-pattern-journeys-contracts"
OUT.mkdir(parents=True, exist_ok=True)
WORK = Path(tempfile.mkdtemp(prefix="run-", dir=OUT))
LOG = WORK / "processes.jsonl"
JDK = Path(os.environ["JAVA_HOME"])
JAVA, JAVAC = str(JDK / "bin/java"), str(JDK / "bin/javac")
RECORDS = []


def run(args, cwd):
    started = datetime.datetime.now(datetime.timezone.utc).isoformat()
    child = subprocess.Popen(args, cwd=cwd, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                             text=True, start_new_session=True)
    event = dict(parentTask=os.environ.get("CLASSES_FAMILY_TASK_ID", "java-pattern-journeys"),
                 cwd=str(cwd), command=args, pid=child.pid, parentPid=os.getpid(),
                 startedAt=started, timeoutSeconds=45)
    with LOG.open("a") as stream:
        stream.write(json.dumps(dict(event, event="start")) + "\n")
    try:
        stdout, stderr = child.communicate(timeout=45)
    except BaseException:
        try:
            os.killpg(child.pid, signal.SIGKILL)
        except ProcessLookupError:
            pass
        stdout, stderr = child.communicate()
        event.update(event="end", endedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),
                     exitCode=child.returncode, timedOut=True,
                     childProcessGroupCleanup="terminated owned group",
                     stdout=stdout, stderr=stderr)
        with LOG.open("a") as stream:
            stream.write(json.dumps(event) + "\n")
        raise
    event.update(event="end", endedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),
                 exitCode=child.returncode, timedOut=False,
                 childProcessGroupCleanup="owned command exited", stdout=stdout, stderr=stderr)
    with LOG.open("a") as stream:
        stream.write(json.dumps(event) + "\n")
    result = dict(command=args, returncode=child.returncode, stdout=stdout, stderr=stderr)
    RECORDS.append(result)
    return result


def success(result, expected=None):
    assert result["returncode"] == 0, result
    if expected is not None:
        assert result["stdout"] == expected, result
    return result


def rejected(result, token):
    assert result["returncode"] != 0 and token in result["stderr"], result
    return result


def copy_role(role, name):
    destination = WORK / name
    shutil.copytree(PACK / role, destination, ignore=shutil.ignore_patterns("out", "classes"))
    return destination


def compile_role(folder, isolated=None):
    empty = folder / "empty-source-and-class-path"
    empty.mkdir(exist_ok=True)
    names = (folder / "sources.txt").read_text().splitlines() if isolated is None else isolated
    args = [JAVAC, "--release", "21", "-encoding", "UTF-8", "-Xlint:all", "-Werror",
            "-proc:none", "--source-path", str(empty), "--class-path", str(empty),
            "-d", "classes", *names]
    return run(args, folder)


def execute(folder, class_name, mode=None, assertions=True):
    return run([JAVA, "-Xmx128m", "-ea" if assertions else "-da", "-cp", "classes",
                class_name, *([] if mode is None else [mode])], folder)


def mutate(folder, name, before, after):
    file = folder / name
    text = file.read_text()
    assert text.count(before) == 1, (name, before)
    file.write_text(text.replace(before, after))




def update_sources(folder):
    names = sorted(p.relative_to(folder).as_posix() for p in folder.rglob("*.java"))
    (folder / "sources.txt").write_text("\n".join(names) + "\n")


def summary(result, mode):
    import re
    success(result)
    match = re.fullmatch(mode + r": (\d+) checks passed\n", result["stdout"])
    assert match and int(match.group(1)) > 100, result
    return int(match.group(1))


def verify():
    version = success(run([JAVAC, "-version"], ROOT))["stdout"]
    assert version.startswith("javac 21."), version
    head = success(run(["git", "rev-parse", "HEAD"], ROOT))["stdout"].strip()
    expected = os.environ.get("GITHUB_SHA")
    assert expected is None or expected == head
    proof = dict(jdk=version.strip(), sourceCommit=head, roles=[], independentProjects=[],
                 rejectedRegressions=[], clients=[], fullGoalComplete=False)
    demos = {
        "baseline": "Notes\n[kit:3]\n[lamp:2]\nend=2\n",
        "transfer-baseline": "finish=false:DRAFT\napprove=true:READY\napprove=false:READY\nfinish=true:DONE\nreset=true:DRAFT\n[DRAFT->READY, READY->DONE, DONE->DRAFT]\n",
        "extension-baseline": "2026-11-15 | lamp\n2026-11-16 | kit\nundo=true\n2026-11-15 | lamp\nredo=true\n2026-11-15 | lamp\n2026-11-16 | kit\n",
    }
    modes = {"core": "notices.NoticeCreator", "transfer": "tickets.TicketState", "extension": "editor.TextBuffer"}
    names_by_role = {role: sorted(p.relative_to(PACK/role).as_posix() for p in (PACK/role).rglob("*") if p.is_file()) for role in ["starter", "solution"]}
    editable = {"src/notices/NoticeService.java", "src/tickets/Ticket.java", "src/editor/Editor.java", "sources.txt"}
    for name in (set(names_by_role["starter"]) & set(names_by_role["solution"])) - editable:
        assert (PACK/"starter"/name).read_bytes() == (PACK/"solution"/name).read_bytes(), name
    for role in ["starter", "solution"]:
        folder = copy_role(role, role)
        java_names = sorted(p.relative_to(folder).as_posix() for p in folder.rglob("*.java"))
        assert (folder/"sources.txt").read_text().splitlines() == java_names
        success(compile_role(folder))
        row = dict(role=role, files=len(names_by_role[role]), javaSources=len(java_names),
                   hashes={name: hashlib.sha256((PACK/role/name).read_bytes()).hexdigest() for name in names_by_role[role]},
                   baseline=[summary(execute(folder, "ContractChecks", "baseline", enabled), "baseline") for enabled in [True, False]],
                   completed={})
        for mode, expected_output in demos.items():
            success(execute(folder, "Main", mode), expected_output)
        for mode, marker in modes.items():
            if role == "starter":
                row["completed"][mode] = dict(expectedUnfinished=True, evidence=rejected(execute(folder, "ContractChecks", mode), "Unfinished extraction: " + marker))
            else:
                row["completed"][mode] = [summary(execute(folder, "ContractChecks", mode, enabled), mode) for enabled in [True, False]]
                success(execute(folder, "Main", mode), demos[{"core":"baseline", "transfer":"transfer-baseline", "extension":"extension-baseline"}[mode]])
        if role == "solution":
            row["full"] = [summary(execute(folder, "ContractChecks", "full", enabled), "full") for enabled in [True, False]]
        proof["roles"].append(row)
    for mode, package in [("core", "notices"), ("transfer", "tickets"), ("extension", "editor")]:
        folder = copy_role("starter", "only-" + mode)
        for source in (PACK/"solution/src"/package).glob("*.java"):
            shutil.copy2(source, folder/"src"/package/source.name)
        update_sources(folder)
        success(compile_role(folder))
        count = summary(execute(folder, "ContractChecks", mode), mode)
        other = [dict(mode=key, evidence=rejected(execute(folder, "ContractChecks", key), "Unfinished extraction: " + marker)) for key, marker in modes.items() if key != mode]
        proof["independentProjects"].append(dict(completed=mode, checks=count, otherTwoRemainUnfinished=other))
    folder = copy_role("solution", "custom-client")
    success(compile_role(folder))
    (folder/"ExternalClient.java").write_text('''import notices.*;
import java.util.List;
public final class ExternalClient {
    private ExternalClient() {}
    private static final class PipeCreator extends NoticeCreator {
        protected NoticeLine createLine() { return row -> row.label() + "|" + row.count() + "\\n"; }
    }
    public static void main(String[] args) {
        Ordering byLabel = rows -> rows.stream().sorted(java.util.Comparator.comparing(Notice::label)).toList();
        System.out.print(new PipeCreator().render(List.of(new Notice("z", 1), new Notice("a", 2)), byLabel));
    }
}
''')
    success(run([JAVAC, "--release", "21", "-encoding", "UTF-8", "-Xlint:all", "-Werror", "-proc:none", "-cp", "classes", "-d", "classes", "ExternalClient.java"], folder))
    success(execute(folder, "ExternalClient"), "Notes\na|2\nz|1\nend=2\n")
    proof["clients"].append("A real third creator overrides the Factory Method; an independently supplied ordering Strategy runs through the unchanged template")
    controls = [
        ("format-count", "src/notices/BracketCreator.java", 'row.count() + "]\\n"', '(row.count() + 1) + "]\\n"', "core", "AssertionError"),
        ("reversal", "src/notices/ReversedOrder.java", "Collections.reverse(copy);", "// reversal lost", "core", "AssertionError"),
        ("template-order-owner", "src/notices/NoticeCreator.java", "public final String render(", "public String render(", "core", "AssertionError"),
        ("state-transition", "src/tickets/TicketState.java", 'action.equals("approve") ? READY', 'action.equals("approve") ? DONE', "transfer", "AssertionError"),
        ("redo-branch", "src/editor/CommandHistory.java", "done.push(command); undone.clear();", "done.push(command);", "extension", "AssertionError"),
        ("invalid-append-atomicity", "src/editor/Editor.java", "NoteDate date =", "history.undo(); NoteDate date =", "extension", "AssertionError"),
        ("snapshot-opacity", "src/editor/TextBuffer.java", "private final String text;", "public final String text;", "extension", "AssertionError"),
    ]
    for name, file, old, new, mode, marker in controls:
        folder = copy_role("solution", "rejected-" + name)
        mutate(folder, file, old, new)
        success(compile_role(folder))
        proof["rejectedRegressions"].append(dict(name=name, evidence=rejected(execute(folder, "ContractChecks", mode), marker)))
    proof.update(projectGroups=3, smallerAlternativeImplementations=2, verifiedTerminologySource="SOURCES.md")
    return proof


try:
    proof = verify()
    proof["processLog"] = str(LOG)
    (OUT/"proof.json").write_text(json.dumps(proof, indent=2) + "\n")
    print(json.dumps(dict(sourceCommit=proof["sourceCommit"], roles=[{k:r[k] for k in ["role","files","javaSources","baseline","full"] if k in r} for r in proof["roles"]], independentProjects=len(proof["independentProjects"]), controlledRegressionsRejected=len(proof["rejectedRegressions"]), clients=proof["clients"], processLog=str(LOG))))
finally:
    for folder in WORK.iterdir():
        if folder.is_dir(): shutil.rmtree(folder)
