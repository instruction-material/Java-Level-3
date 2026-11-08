package reports;

public record Summary(int count, int positive, long total, long mean) {
    public String format() {
        return "count=" + count + "|positive=" + positive + "|total=" + total + "|mean=" + mean;
    }
}
