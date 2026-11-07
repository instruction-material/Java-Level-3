package behavior;

public final class Reports {
    private Reports() {}
    public static final class Plain extends ReportTemplate {
        protected String heading() { return "Report"; }
        protected String formatLine(String text) { return text; }
    }
    public static final class Upper extends ReportTemplate {
        protected String heading() { return "UPPER"; }
        protected String formatLine(String text) { return new Rules.Upper().apply(text); }
    }
}
