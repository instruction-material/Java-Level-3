import java.util.ArrayList;
import java.util.List;
import creation.BodyLine;
import creation.Exporters;
import creation.Families;
import creation.FamilyRenderer;
import creation.Heading;
import creation.Renderer;
import creation.RendererFactory;
import creation.Report;
import creation.ReportJob;
import creation.Style;

public class ContractTests {
    private static int checks;
    private static void equal(Object actual, Object expected) {
        if (!actual.equals(expected)) throw new AssertionError("expected " + expected + ", got " + actual);
        checks++;
    }
    private static void yes(boolean value) {
        if (!value) throw new AssertionError("expected true");
        checks++;
    }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("wrong failure", error);
            checks++; return;
        }
        throw new AssertionError("missing " + type.getSimpleName());
    }
    private static String oracle(Report report, Style style) {
        String result = style == Style.PLAIN ? report.title() + "\n" : "[" + report.title() + "]\n";
        for (String line : report.lines())
            result += style == Style.PLAIN ? line + "\n" : "| " + line + " |\n";
        return result;
    }
    private static void factories() {
        for (int count = 0; count <= 50; count++) {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < count; i++) lines.add(i % 3 == 0 ? "" : "line " + i);
            Report report = new Report("Title", lines);
            for (Style style : Style.values()) {
                String expected = oracle(report, style);
                Renderer selected = RendererFactory.forStyle(style);
                equal(selected.style(), style); equal(selected.render(report), expected);
                Families.Factory family = style == Style.PLAIN ? new Families.PlainFactory() : new Families.BoxedFactory();
                equal(family.heading().style(), style); equal(family.bodyLine().style(), style);
                equal(RendererFactory.from(family).render(report), expected);
                Exporters.Exporter creator = style == Style.PLAIN ? new Exporters.Plain() : new Exporters.Boxed();
                equal(creator.export(report), expected);
            }
        }
        Report report = new Report("T", List.of("one"));
        int[] calls = {0};
        Exporters.Exporter custom = new Exporters.Exporter() {
            protected Renderer createRenderer() { calls[0]++; return RendererFactory.forStyle(Style.PLAIN); }
        };
        equal(custom.export(report), "T\none\n"); equal(calls[0], 1);
        fails(NullPointerException.class, () -> custom.export(null)); equal(calls[0], 1);
        fails(NullPointerException.class, () -> new Exporters.Exporter() { protected Renderer createRenderer() { return null; } }.export(report));
        fails(NullPointerException.class, () -> new Exporters.Exporter() {
            protected Renderer createRenderer() { return new Renderer() {
                public Style style() { return Style.PLAIN; }
                public String render(Report value) { return null; }
            }; }
        }.export(report));
        fails(IllegalStateException.class, () -> new Exporters.Exporter() {
            protected Renderer createRenderer() { throw new IllegalStateException("creation failed"); }
        }.export(report));
        fails(IllegalArgumentException.class, () -> new FamilyRenderer(new Families.PlainHeading(), new Families.BoxedLine()));
        fails(IllegalArgumentException.class, () -> new FamilyRenderer(new Families.BoxedHeading(), new Families.PlainLine()));
        fails(NullPointerException.class, () -> RendererFactory.forStyle(null));
        fails(NullPointerException.class, () -> RendererFactory.from(null));
        fails(NullPointerException.class, () -> new FamilyRenderer(null, new Families.PlainLine()));
        fails(NullPointerException.class, () -> new FamilyRenderer(new Families.PlainHeading(), null));
        fails(NullPointerException.class, () -> RendererFactory.from(new Families.Factory() {
            public Heading heading() { return null; }
            public BodyLine bodyLine() { return new Families.PlainLine(); }
        }));
        fails(NullPointerException.class, () -> new FamilyRenderer(new Heading() {
            public Style style() { return null; }
            public String render(String text) { return text; }
        }, new Families.PlainLine()));
        fails(NullPointerException.class, () -> new FamilyRenderer(new Families.PlainHeading(), new BodyLine() {
            public Style style() { return null; }
            public String render(String text) { return text; }
        }));

        fails(NullPointerException.class, () -> RendererFactory.forStyle(Style.PLAIN).render(null));
        fails(NullPointerException.class, () -> new Families.PlainHeading().render(null));
        fails(NullPointerException.class, () -> new Families.BoxedLine().render(null));
        fails(NullPointerException.class, () -> new FamilyRenderer(new Heading() {
            public Style style() { return Style.PLAIN; }
            public String render(String text) { return null; }
        }, new Families.PlainLine()).render(report));
        fails(NullPointerException.class, () -> new FamilyRenderer(new Families.PlainHeading(), new BodyLine() {
            public Style style() { return Style.PLAIN; }
            public String render(String text) { return null; }
        }).render(report));
        List<String> caller = new ArrayList<>(List.of("one"));
        Report frozen = new Report("T", caller); caller.add("two");
        equal(frozen.lines(), List.of("one"));
        fails(UnsupportedOperationException.class, () -> frozen.lines().add("change"));
    }
    private static void builders() {
        for (int count = 0; count <= 25; count++) {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < count; i++) lines.add("item " + i);
            Report report = new Report("T", lines);
            for (Style style : Style.values()) for (boolean include : new boolean[]{false, true})
                for (String footer : List.of("", "End")) {
                    ReportJob.Builder builder = new ReportJob.Builder(report);
                    yes(builder.style(style) == builder); yes(builder.includeCount(include) == builder); yes(builder.footer(footer) == builder);
                    ReportJob built = builder.build();
                    yes(built != builder.build());
                    String expected = oracle(report, style) + (include ? "Lines: " + count + "\n" : "") + (footer.isEmpty() ? "" : footer + "\n");
                    equal(built.render(), expected);
                    equal(built, new ReportJob(report, style, include, footer));
                    builder.style(style == Style.PLAIN ? Style.BOXED : Style.PLAIN).includeCount(!include).footer("Changed");
                    equal(built.render(), expected);
                }
        }
        Report report = new Report("T", List.of());
        equal(new ReportJob.Builder(report).build(), new ReportJob(report, Style.PLAIN, false, ""));
        fails(NullPointerException.class, () -> new ReportJob.Builder(null));
        fails(NullPointerException.class, () -> new ReportJob.Builder(report).style(null));
        fails(NullPointerException.class, () -> new ReportJob.Builder(report).footer(null));
        fails(IllegalArgumentException.class, () -> new ReportJob.Builder(report).footer("a\nb"));
        fails(IllegalArgumentException.class, () -> new ReportJob.Builder(report).footer("a\rb"));
        fails(NullPointerException.class, () -> new ReportJob(null, Style.PLAIN, false, ""));
        fails(NullPointerException.class, () -> new ReportJob(report, null, false, ""));
        fails(NullPointerException.class, () -> new ReportJob(report, Style.PLAIN, false, null));
        fails(IllegalArgumentException.class, () -> new ReportJob(report, Style.PLAIN, false, "bad\nfooter"));
    }
    private static void extension() {
        Report report = new Report("T", List.of("one"));
        ReportJob original = new ReportJob(report, Style.BOXED, true, "End");
        ReportJob.Builder copy = original.toBuilder();
        equal(copy.build(), original);
        ReportJob changed = copy.style(Style.PLAIN).footer("Next").build();
        equal(original.render(), "[T]\n| one |\nLines: 1\nEnd\n");
        equal(changed.render(), "T\none\nLines: 1\nNext\n");
        yes(original.toBuilder() != copy);
    }
    public static void main(String[] args) {
        String mode = args.length == 0 ? "all" : args.length == 1 ? args[0] : "";
        if (!List.of("factories", "core", "all").contains(mode)) throw new IllegalArgumentException("mode must be factories, core or all");
        factories();
        if (!mode.equals("factories")) builders();
        if (mode.equals("all")) extension();
        System.out.println((mode.equals("factories") ? "Factory" : mode.equals("core") ? "Core creation" : "Complete creation") + " contracts: " + checks + " passed");
    }
}
