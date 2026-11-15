package app;
public final class MarkedDocument extends DocumentTemplate {
    public MarkedDocument() {}
    protected String header(){return "BEGIN\n";}
    protected String line(String row){return "* "+row+"\n";}
    protected String footer(){return "END\n";}
}
