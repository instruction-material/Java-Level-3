import api.*;
import app.MessageService;
import legacy.LegacyMessages;
import transfer.RenderBatch;
import extension.PrefixRule;
import java.util.*;
import java.util.function.Supplier;
import static check.Checks.*;
public final class ContractChecks {
    private ContractChecks() {}
    private static void behavior(Supplier<Messages> make) {
        Messages value=make.get();
        for(String text:List.of("","go"," a ","é","a".repeat(24))) {
            same("text="+text+"\n",value.render("plain",text),"plain exact");
            same(text.length()+"|"+text+"\n",value.render("compact",text),"compact exact");
        }
        fails(NullPointerException.class,"mode",()->value.render(null,null));
        fails(NullPointerException.class,"message",()->value.render("missing",null));
        for(String bad:List.of("a\nb","a\rb","a|b","a".repeat(25)))
            fails(IllegalArgumentException.class,"message",()->value.render("missing",bad));
        for(String bad:List.of("missing","","PLAIN"))
            fails(IllegalArgumentException.class,"mode",()->value.render(bad,"go"));
    }
    private static void core() {
        behavior(MessageService::new);
        int[] calls={0};
        Map<String,RenderRule> rules=new LinkedHashMap<>();
        rules.put("caps",text->{calls[0]++;return text.toUpperCase(Locale.ROOT);});
        MessageService custom=new MessageService(rules);rules.clear();
        same("GO",custom.render("caps","go"),"caller supplies new variant");
        same(1,calls[0],"exactly one selected call");
        fails(NullPointerException.class,"message",()->custom.render("caps",null));
        same(1,calls[0],"invalid input before rule");
        fails(IllegalArgumentException.class,"mode",()->custom.render("plain","go"));
        same(1,calls[0],"unknown before rule");
        fails(NullPointerException.class,"rules",()->new MessageService(null));
        Map<String,RenderRule> bad=new LinkedHashMap<>();bad.put(null,text->text);
        fails(NullPointerException.class,"mode",()->new MessageService(bad));
        bad.clear();bad.put("BAD",text->text);
        fails(IllegalArgumentException.class,"mode",()->new MessageService(bad));
        bad.clear();bad.put("ok",null);
        fails(NullPointerException.class,"rule",()->new MessageService(bad));
        MessageService empty=new MessageService(Map.of());
        fails(IllegalArgumentException.class,"mode",()->empty.render("plain","go"));
    }
    private static void transfer() {
        int[] calls={0};
        Messages recorder=(mode,text)->{calls[0]++;return mode+":"+text+"\n";};
        List<String> input=new ArrayList<>(List.of("a",""," b "));
        same("plain:a\nplain:\nplain: b \n",RenderBatch.lines(recorder,"plain",input),"batch order and exact strings");
        same(List.of("a",""," b "),input,"batch retains input");same(3,calls[0],"one call per item");
        same("",RenderBatch.lines(recorder,"unknown",List.of()),"empty has no dispatch");same(3,calls[0],"empty calls");
        fails(NullPointerException.class,"renderer",()->RenderBatch.lines(null,null,null));
        fails(NullPointerException.class,"mode",()->RenderBatch.lines(recorder,null,null));
        fails(NullPointerException.class,"lines",()->RenderBatch.lines(recorder,"plain",null));
        fails(IllegalArgumentException.class,"message",()->RenderBatch.lines(recorder,"plain",List.of("ok","bad\n")));
        same(3,calls[0],"validate complete batch before calls");
        List<String> bad=new ArrayList<>();bad.add("ok");bad.add(null);
        fails(NullPointerException.class,"message",()->RenderBatch.lines(recorder,"plain",bad));
        same(3,calls[0],"null item before calls");
        Messages failure=(mode,text)->{throw new IllegalStateException("rule");};
        fails(IllegalStateException.class,"rule",()->RenderBatch.lines(failure,"plain",List.of("ok")));
    }
    private static void extension() {
        int[] calls={0};
        PrefixRule rule=new PrefixRule("!",text->{calls[0]++;return "<"+text+">\n";});
        same("!<go>\n",rule.render("go"),"prefix exact");same(1,calls[0],"delegate once");
        fails(NullPointerException.class,"message",()->rule.render(null));same(1,calls[0],"invalid no delegate");
        fails(NullPointerException.class,"prefix",()->new PrefixRule(null,null));
        fails(IllegalArgumentException.class,"prefix",()->new PrefixRule("x".repeat(13),null));
        fails(IllegalArgumentException.class,"prefix",()->new PrefixRule("bad\n",null));
        fails(NullPointerException.class,"delegate",()->new PrefixRule("!",null));
        PrefixRule missing=new PrefixRule("",text->null);
        fails(NullPointerException.class,"output",()->missing.render("ok"));
        PrefixRule nested=new PrefixRule("A",new PrefixRule("B",text->text));
        same("ABgo",nested.render("go"),"composition order");
    }
    public static void main(String[] args) {
        String mode=args.length==0?"all":args[0];
        switch(mode) {
            case "baseline" -> {behavior(LegacyMessages::new);behavior(MessageService::new);}
            case "core" -> core();
            case "transfer" -> transfer();
            case "extension" -> extension();
            case "all" -> {core();transfer();extension();}
            default -> throw new IllegalArgumentException("mode");
        }
        System.out.println(mode+": "+count()+" checks passed");
    }
}
