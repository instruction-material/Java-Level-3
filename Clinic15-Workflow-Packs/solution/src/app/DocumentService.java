package app;
import api.*;
import java.util.*;
public final class DocumentService implements Documents {
    private final Map<String,DocumentTemplate> formats=Map.of("plain",new PlainDocument(),"marked",new MarkedDocument());
    public DocumentService() {}
    public String build(String kind,List<String> rows) {
        Objects.requireNonNull(kind,"kind");List<String> input=Rows.copy(rows);
        DocumentTemplate format=formats.get(kind);
        if(format==null)throw new IllegalArgumentException("kind");
        return format.build(input);
    }
}
