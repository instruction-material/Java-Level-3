package reports;
public final class CsvReport extends Report {
    private final char delimiter = ',';
    private String quoteLabel(String label) {
        return "\"" + label.replace("\"", "\"\"") + "\"";
    }
    protected String header() { return "name" + delimiter + "count\n"; }
    protected String rowText(Row row) {
        return quoteLabel(row.label()) + delimiter + row.count() + "\n";
    }
    protected String footer(long total) { return "total" + delimiter + total + "\n"; }
}
