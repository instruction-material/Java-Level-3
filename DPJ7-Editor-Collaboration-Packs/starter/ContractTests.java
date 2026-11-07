import editor.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ContractTests {
    private static int checks;
    private static void equal(Object actual, Object expected) {
        checks++;
        if (!Objects.equals(actual, expected)) throw new AssertionError(actual + " != " + expected);
    }
    private static void yes(boolean condition) { equal(condition, true); }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable error) {
            if (type.isInstance(error)) return;
            throw new AssertionError("Expected " + type.getName() + ", got " + error, error);
        }
        throw new AssertionError("Expected " + type.getName());
    }
    private static EditEvent event(String text) { return new EditEvent(EditEvent.Kind.REPLACE, text, "after"); }
    private static void core() {
        TextDocument document = new TextDocument("a");
        TextDocument.Snapshot saved = document.snapshot();
        document.replace("b"); document.restore(saved); equal(document.text(),"a");
        document.replace(""); document.restore(saved); equal(document.text(),"a");
        TextDocument other = new TextDocument("other");
        fails(IllegalArgumentException.class, () -> document.restore(other.snapshot())); equal(document.text(),"a");
        fails(NullPointerException.class, () -> document.restore(null)); equal(document.text(),"a");
        fails(NullPointerException.class, () -> new TextDocument(null));
        fails(IllegalArgumentException.class, () -> new TextDocument("bad\n"));
        fails(IllegalArgumentException.class, () -> document.replace("bad\r")); equal(document.text(),"a");
        fails(NullPointerException.class, () -> new EditEvent(null,"a","b"));
        fails(NullPointerException.class, () -> new EditEvent(EditEvent.Kind.UNDO,null,"b"));
        fails(IllegalArgumentException.class, () -> new EditEvent(EditEvent.Kind.UNDO,"a","bad\n"));
        List<String> seen = new ArrayList<>();
        EventBus duplicate = new EventBus(); Consumer<EditEvent> listener = e -> seen.add(e.before());
        EventBus.Subscription one = duplicate.subscribe(listener), two = duplicate.subscribe(listener);
        equal(duplicate.size(),2); duplicate.publish(event("x")); equal(seen,List.of("x","x"));
        one.close(); one.close(); equal(duplicate.size(),1); seen.clear();
        duplicate.publish(event("y")); equal(seen,List.of("y")); two.close(); equal(duplicate.size(),0);
        fails(NullPointerException.class, () -> duplicate.subscribe(null)); equal(duplicate.size(),0);
        fails(NullPointerException.class, () -> duplicate.publish(null));
        EventBus changing = new EventBus(); seen.clear();
        EventBus.Subscription[] second = new EventBus.Subscription[1]; boolean[] added = {false};
        changing.subscribe(e -> { seen.add("A"); second[0].close(); if (!added[0]) { added[0]=true; changing.subscribe(x -> seen.add("C")); } });
        second[0] = changing.subscribe(e -> seen.add("B"));
        changing.publish(event("x")); equal(seen,List.of("A","B")); equal(changing.size(),2);
        seen.clear(); changing.publish(event("y")); equal(seen,List.of("A","C"));
        EventBus nested = new EventBus(); boolean[] entered={false}; seen.clear();
        nested.subscribe(e -> { seen.add("A:"+e.before()); if (!entered[0]) { entered[0]=true; nested.publish(event("inner")); } });
        nested.subscribe(e -> seen.add("B:"+e.before()));
        nested.publish(event("outer")); equal(seen,List.of("A:outer","A:inner","B:inner","B:outer"));
        EventBus failing = new EventBus(); seen.clear();
        failing.subscribe(e -> seen.add("first"));
        EventBus.Subscription failure = failing.subscribe(e -> { throw new UnsupportedOperationException("listener"); });
        failing.subscribe(e -> seen.add("last"));
        fails(UnsupportedOperationException.class, () -> failing.publish(event("x"))); equal(seen,List.of("first"));
        failure.close(); seen.clear(); failing.publish(event("x")); equal(seen,List.of("first","last"));
        TextDocument target = new TextDocument("A"); ReplaceCommand command = new ReplaceCommand(target,"B");
        equal(command.phase(),"NEW"); fails(IllegalStateException.class,command::undo); equal(target.text(),"A");
        target.replace("C"); command.execute(); equal(target.text(),"B"); equal(command.phase(),"DONE");
        fails(IllegalStateException.class,command::execute); equal(target.text(),"B");
        target.replace("external"); fails(IllegalStateException.class,command::undo);
        equal(target.text(),"external"); equal(command.phase(),"DONE");
        target.replace("B"); command.undo(); equal(target.text(),"C"); equal(command.phase(),"UNDONE");
        fails(IllegalStateException.class,command::undo); fails(IllegalStateException.class,command::execute); equal(target.text(),"C");
        fails(NullPointerException.class, () -> new ReplaceCommand(null,"x"));
        fails(NullPointerException.class, () -> new ReplaceCommand(target,null));
        fails(IllegalArgumentException.class, () -> new ReplaceCommand(target,"bad\n"));
        List<String> calls=new ArrayList<>();
        Validation.Verdict rejection=Validation.Verdict.fail("stop");
        List<Validation.Check> stages=new ArrayList<>(List.of(
            value -> { calls.add("first:"+value); return Validation.Verdict.pass(); },
            value -> { calls.add("second:"+value); return rejection; },
            value -> { throw new AssertionError("late stage must not run"); }));
        Validation chain=new Validation(stages); stages.clear();
        equal(chain.check("x"),rejection); equal(calls,List.of("first:x","second:x"));
        calls.clear(); fails(NullPointerException.class, () -> chain.check(null)); yes(calls.isEmpty());
        fails(IllegalArgumentException.class, () -> chain.check("bad\r")); yes(calls.isEmpty());
        yes(new Validation(List.of()).check("").accepted());
        yes(new Validation(List.of(Validation.nonBlank(),Validation.maxLength(3))).check("abc").accepted());
        equal(new Validation(List.of(Validation.nonBlank(),Validation.maxLength(3))).check("abcd").reason(),"too long");
        equal(new Validation(List.of(Validation.nonBlank())).check("  ").reason(),"blank");
        yes(Validation.maxLength(0).check("").accepted()); yes(!Validation.maxLength(0).check("x").accepted());
        yes(!Validation.maxLength(1).check("\uD83D\uDE00").accepted());
        fails(IllegalArgumentException.class, () -> Validation.maxLength(-1));
        fails(NullPointerException.class, () -> new Validation(null));
        fails(NullPointerException.class, () -> new Validation(Arrays.asList((Validation.Check)null)));
        fails(NullPointerException.class, () -> new Validation(List.of(v -> null)).check("x"));
        fails(UnsupportedOperationException.class, () -> new Validation(List.of(v -> { throw new UnsupportedOperationException(); })).check("x"));
        fails(IllegalArgumentException.class, () -> new Validation.Verdict(true,"reason"));
        fails(IllegalArgumentException.class, () -> Validation.Verdict.fail(" "));
        fails(NullPointerException.class, () -> Validation.Verdict.fail(null));
        EventBus events=new EventBus(); List<EditEvent> notices=new ArrayList<>(); events.subscribe(notices::add);
        EditorMediator editor=new EditorMediator("Ready",new Validation(List.of(Validation.nonBlank())),events);
        equal(editor.text(),"Ready"); equal(editor.undoDepth(),0); yes(!editor.undo());
        yes(editor.replace("A")); yes(editor.replace("B")); equal(editor.undoDepth(),2);
        equal(notices,List.of(new EditEvent(EditEvent.Kind.REPLACE,"Ready","A"),new EditEvent(EditEvent.Kind.REPLACE,"A","B")));
        yes(editor.undo()); equal(editor.text(),"A"); equal(editor.undoDepth(),1);
        yes(editor.undo()); equal(editor.text(),"Ready"); equal(editor.undoDepth(),0);
        yes(!editor.undo()); yes(!editor.replace(" ")); equal(notices.size(),4);
        fails(NullPointerException.class, () -> editor.replace(null)); equal(editor.text(),"Ready"); equal(editor.undoDepth(),0);
        yes(editor.replace("C")); equal(editor.text(),"C");
        EventBus failedNotices=new EventBus();
        EditorMediator committed=new EditorMediator("a",new Validation(List.of()),failedNotices);
        EventBus.Subscription bad=failedNotices.subscribe(e -> { throw new UnsupportedOperationException("notify"); });
        fails(UnsupportedOperationException.class, () -> committed.replace("b"));
        equal(committed.text(),"b"); equal(committed.undoDepth(),1);
        bad.close(); yes(committed.undo()); equal(committed.text(),"a"); equal(committed.undoDepth(),0);
        yes(committed.replace("c"));
        EventBus.Subscription badUndo=failedNotices.subscribe(e -> { if (e.kind()==EditEvent.Kind.UNDO) throw new UnsupportedOperationException("undo notify"); });
        fails(UnsupportedOperationException.class,committed::undo);
        equal(committed.text(),"a"); equal(committed.undoDepth(),0); yes(!committed.undo()); badUndo.close();
        EventBus guardedBus=new EventBus();
        EditorMediator guarded=new EditorMediator("a",new Validation(List.of()),guardedBus);
        List<String> committedReads=new ArrayList<>();
        guardedBus.subscribe(e -> {
            committedReads.add(guarded.text()+":"+guarded.undoDepth());
            fails(IllegalStateException.class, () -> guarded.replace("nested"));
            fails(IllegalStateException.class,guarded::undo);
        });
        yes(guarded.replace("b")); yes(guarded.undo()); equal(committedReads,List.of("b:1","a:0"));
        EditorMediator[] holder=new EditorMediator[1];
        holder[0]=new EditorMediator("a",new Validation(List.of(v -> {
            fails(IllegalStateException.class, () -> holder[0].replace("nested"));
            return Validation.Verdict.pass();
        })),new EventBus());
        yes(holder[0].replace("b")); equal(holder[0].undoDepth(),1);
        fails(NullPointerException.class, () -> new EditorMediator(null,new Validation(List.of()),new EventBus()));
        fails(NullPointerException.class, () -> new EditorMediator("a",null,new EventBus()));
        fails(NullPointerException.class, () -> new EditorMediator("a",new Validation(List.of()),null));
        Nodes.Text a=new Nodes.Text("a"), b=new Nodes.Text("b");
        List<Nodes.Node> children=new ArrayList<>(List.of(a,new Nodes.Group(List.of(b,new Nodes.Text("c"))),a));
        Nodes.Group tree=new Nodes.Group(children); children.clear();
        equal(tree.accept(new TextVisitor()),"[a,[b,c],a]"); equal(tree.accept(new Nodes.LeafCount()),4);
        yes(tree.children().get(0)==a); equal(tree.children().size(),3);
        fails(UnsupportedOperationException.class, () -> tree.children().clear());
        equal(new Nodes.Group(List.of()).accept(new TextVisitor()),"[]");
        equal(new Nodes.Group(List.of()).accept(new Nodes.LeafCount()),0);
        equal(new Nodes.Text("").accept(new Nodes.LeafCount()),1);
        Nodes.Visitor<String> tags=new Nodes.Visitor<>() {
            public String text(Nodes.Text node) { return "T:"+node.value(); }
            public String group(Nodes.Group node) { return "G:"+node.children().size(); }
        };
        equal(a.accept(tags),"T:a"); equal(tree.accept(tags),"G:3");
        fails(NullPointerException.class, () -> a.accept(null));
        fails(NullPointerException.class, () -> tree.accept(null));
        Nodes.Visitor<String> nullResults=new Nodes.Visitor<>() {
            public String text(Nodes.Text node) { return null; }
            public String group(Nodes.Group node) { return null; }
        };
        fails(NullPointerException.class, () -> a.accept(nullResults));
        fails(NullPointerException.class, () -> tree.accept(nullResults));
        fails(NullPointerException.class, () -> new Nodes.Text(null));
        fails(IllegalArgumentException.class, () -> new Nodes.Text("bad\n"));
        fails(NullPointerException.class, () -> new Nodes.Group(null));
        fails(NullPointerException.class, () -> new Nodes.Group(Arrays.asList(a,null)));
    }
    private static void extension() {
        EventBus bus=new EventBus(); List<String> seen=new ArrayList<>(); boolean[] nested={false};
        bus.subscribe(e -> { seen.add("lead:"+e.before()); if (!nested[0]) { nested[0]=true; bus.publish(event("inner")); } });
        EventBus.Subscription once=bus.subscribeOnce(e -> seen.add("once:"+e.before()));
        bus.subscribe(e -> seen.add("tail:"+e.before()));
        bus.publish(event("outer"));
        equal(seen,List.of("lead:outer","lead:inner","once:inner","tail:inner","tail:outer"));
        equal(bus.size(),2); once.close(); once.close(); seen.clear();
        bus.publish(event("next")); equal(seen,List.of("lead:next","tail:next"));
        EventBus failing=new EventBus(); int[] calls={0};
        failing.subscribeOnce(e -> { calls[0]++; throw new UnsupportedOperationException(); });
        fails(UnsupportedOperationException.class, () -> failing.publish(event("x")));
        equal(calls[0],1); equal(failing.size(),0); failing.publish(event("y")); equal(calls[0],1);
        fails(NullPointerException.class, () -> failing.subscribeOnce(null)); equal(failing.size(),0);
        EventBus closed=new EventBus(); EventBus.Subscription token=closed.subscribeOnce(e -> calls[0]++);
        token.close(); token.close(); closed.publish(event("x")); equal(calls[0],1);
        EventBus captured=new EventBus(); EventBus.Subscription[] later=new EventBus.Subscription[1];
        captured.subscribe(e -> later[0].close());
        later[0]=captured.subscribeOnce(e -> calls[0]++);
        captured.publish(event("x")); equal(calls[0],2);
        captured.publish(event("y")); equal(calls[0],2);
    }
    public static void main(String[] args) {
        boolean coreOnly=args.length==1 && args[0].equals("core");
        if (!(coreOnly || args.length==0)) throw new IllegalArgumentException("mode must be core or empty");
        core(); if (!coreOnly) extension();
        System.out.println((coreOnly ? "Core collaboration" : "Complete collaboration")+" contracts: "+checks+" passed");
    }
}
