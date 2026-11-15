package reports;
public final class PlainReport extends Report {
    protected String header() { return "Inventory\n"; }
    protected String rowText(Row row) { return row.label() + "=" + row.count() + "\n"; }
    protected String footer(long total) { return "items=" + total + "\n"; }
}
