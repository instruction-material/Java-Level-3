package notices;
public final class TextCreator extends NoticeCreator {
    protected NoticeLine createLine() {
        return row -> row.label() + "=" + row.count() + "\n";
    }
}
