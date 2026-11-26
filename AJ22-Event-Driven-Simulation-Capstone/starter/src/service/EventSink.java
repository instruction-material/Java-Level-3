package service;
import java.io.IOException;

@FunctionalInterface
public interface EventSink {
    void write(String outcome) throws IOException;
}
