package transfer;
import api.*;
import java.util.*;
public final class RenderBatch {
    private RenderBatch() {}
    public static String lines(Messages renderer,String mode,List<String> lines) {
        Objects.requireNonNull(renderer,"renderer");Objects.requireNonNull(mode,"mode");
        List<String> copy=new ArrayList<>(Objects.requireNonNull(lines,"lines"));
        for(String line:copy)Rules.text(line);
        StringBuilder output=new StringBuilder();
        for(String line:copy)output.append(renderer.render(mode,line));
        return output.toString();
    }
}
