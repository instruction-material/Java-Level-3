package app;
public final class PlainDocument extends DocumentTemplate {
    public PlainDocument() {}
    protected String header(){return "begin\n";}
    protected String line(String row){return row+"\n";}
    protected String footer(){return "end\n";}
}
