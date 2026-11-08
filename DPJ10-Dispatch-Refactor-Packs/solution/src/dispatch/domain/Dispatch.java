package dispatch.domain;
/** Stored outcome; immutable strings and primitive values have no mutable aliases. */
public record Dispatch(String id,String rendered,int cents) {
    public Dispatch {
        id=Request.key(id);rendered=Request.line(rendered);
        if(cents<0)throw new IllegalArgumentException("nonnegative cents");
    }
}
