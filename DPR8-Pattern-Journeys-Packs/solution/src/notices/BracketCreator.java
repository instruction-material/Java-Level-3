package notices;
public final class BracketCreator extends NoticeCreator {
    protected NoticeLine createLine() {
        return row -> "[" + row.label() + ":" + row.count() + "]\n";
    }
}
