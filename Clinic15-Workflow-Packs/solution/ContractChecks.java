import api.*;
import app.*;
import legacy.LegacyDocuments;
import transfer.WorkflowTrace;
import extension.NumberedRows;
import java.util.*;
import java.util.function.Supplier;
import static check.Checks.*;
public final class ContractChecks {
    private ContractChecks() {}
    private static void behavior(Supplier<Documents> make) {
        Documents docs=make.get();
        same("begin\na\nb\nend\n",docs.build("plain",List.of("a","b")),"plain");
        same("BEGIN\n* a\n* b\nEND\n",docs.build("marked",List.of("a","b")),"marked");
        same("begin\nend\n",docs.build("plain",List.of()),"empty skeleton");
        same("BEGIN\n* \nEND\n",docs.build("marked",List.of("")),"blank row retained");
        for(int n=0;n<=6;n++) {
            List<String> rows=new ArrayList<>();for(int k=0;k<n;k++)rows.add("x"+k);
            String plain="begin\n",marked="BEGIN\n";
            for(String row:rows){plain+=row+"\n";marked+="* "+row+"\n";}
            same(plain+"end\n",docs.build("plain",rows),"row table plain");
            same(marked+"END\n",docs.build("marked",rows),"row table marked");
            same(n,rows.size(),"input retained");
        }
        fails(NullPointerException.class,"kind",()->docs.build(null,null));
        fails(NullPointerException.class,"rows",()->docs.build("missing",null));
        fails(IllegalArgumentException.class,"row",()->docs.build("missing",List.of("bad\n")));
        fails(IllegalArgumentException.class,"kind",()->docs.build("missing",List.of()));
        fails(IllegalArgumentException.class,"rows",()->docs.build("plain",Collections.nCopies(7,"x")));
        List<String> bad=new ArrayList<>();bad.add(null);
        fails(NullPointerException.class,"row",()->docs.build("plain",bad));
    }
    private static void core() {
        behavior(DocumentService::new);List<String> calls=new ArrayList<>();
        DocumentTemplate custom=new DocumentTemplate() {
            protected String header(){calls.add("header");return "H\n";}
            protected String line(String row){calls.add("line:"+row);return row.toUpperCase(Locale.ROOT)+"\n";}
            protected String footer(){calls.add("footer");return "F\n";}
        };
        same("H\nA\n\nF\n",custom.build(List.of("a","")),"template output");
        same(List.of("header","line:a","line:","footer"),calls,"fixed algorithm order");
        calls.clear();
        fails(IllegalArgumentException.class,"row",()->custom.build(List.of("ok","bad\n")));
        same(List.of(),calls,"whole validation before hooks");
        DocumentTemplate noFooter=new DocumentTemplate() {
            protected String header(){return "H\n";}
            protected String line(String row){return row+"\n";}
        };
        same("H\na\n",noFooter.build(List.of("a")),"optional footer hook");
        DocumentTemplate nullHeader=new DocumentTemplate() {
            protected String header(){return null;}
            protected String line(String row){return row;}
        };
        fails(NullPointerException.class,"header",()->nullHeader.build(List.of()));
        DocumentTemplate nullLine=new DocumentTemplate() {
            protected String header(){return "";}
            protected String line(String row){return null;}
        };
        fails(NullPointerException.class,"line",()->nullLine.build(List.of("x")));
        DocumentTemplate nullFooter=new DocumentTemplate() {
            protected String header(){return "";}
            protected String line(String row){return row;}
            protected String footer(){return null;}
        };
        fails(NullPointerException.class,"footer",()->nullFooter.build(List.of()));

        List<String> failed=new ArrayList<>();
        DocumentTemplate error=new DocumentTemplate() {
            protected String header(){failed.add("header");return "";}
            protected String line(String row){failed.add(row);if(row.equals("bad"))throw new IllegalStateException("body");return row;}
            protected String footer(){failed.add("footer");return "";}
        };
        fails(IllegalStateException.class,"body",()->error.build(List.of("a","bad","c")));
        same(List.of("header","a","bad"),failed,"failure stops later hooks");
    }
    private static void transfer() {
        List<String> calls=new ArrayList<>();
        List<String> result=WorkflowTrace.stages(List.of("a",""),(phase,value)->calls.add(phase+":"+value));
        same(List.of("start","row:a","row:","end"),result,"stage result");
        same(List.of("start:","row:a","row:","end:"),calls,"hook trace");immutable(result);
        same(List.of("start","end"),WorkflowTrace.stages(List.of(),(phase,value)->{}),"empty stages");
        fails(NullPointerException.class,"rows",()->WorkflowTrace.stages(null,null));
        fails(NullPointerException.class,"hook",()->WorkflowTrace.stages(List.of(),null));
        int[] count={0};
        fails(IllegalArgumentException.class,"row",()->WorkflowTrace.stages(List.of("ok","bad\n"),(phase,value)->count[0]++));
        same(0,count[0],"invalid before hook");
        List<String> failed=new ArrayList<>();
        fails(IllegalStateException.class,"stop",()->WorkflowTrace.stages(List.of("a","b"),(phase,value)->{
            failed.add(phase+":"+value);if(phase.equals("row"))throw new IllegalStateException("stop");
        }));
        same(List.of("start:","row:a"),failed,"abort trace");
    }
    private static void extension() {
        List<String> input=new ArrayList<>(List.of("a",""));
        same("ROWS\n1:a\n2:\ntotal=2\n",NumberedRows.render(input,true),"numbered with footer");
        same("ROWS\n1:a\n2:\n",NumberedRows.render(input,false),"no footer");
        same("ROWS\ntotal=0\n",NumberedRows.render(List.of(),true),"empty counted");
        same(List.of("a",""),input,"numbering retains input");
        fails(NullPointerException.class,"rows",()->NumberedRows.render(null,true));
        fails(IllegalArgumentException.class,"row",()->NumberedRows.render(List.of("bad|"),false));
    }
    public static void main(String[] args) {
        String mode=args.length==0?"all":args[0];
        switch(mode) {
            case "baseline" -> {behavior(LegacyDocuments::new);behavior(DocumentService::new);}
            case "core" -> core();
            case "transfer" -> transfer();
            case "extension" -> extension();
            case "all" -> {core();transfer();extension();}
            default -> throw new IllegalArgumentException("mode");
        }
        System.out.println(mode+": "+count()+" checks passed");
    }
}
