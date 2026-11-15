package trace;
import api.*;
public sealed interface Step permits Step.Open, Step.Join, Step.Leave, Step.Close, Step.Report {
    String run(DeskAPI desk);
    record Open(String session, int capacity) implements Step {
        public String run(DeskAPI desk) { desk.open(session, capacity); return "opened"; }
    }
    record Join(String session, String participant) implements Step {
        public String run(DeskAPI desk) { return desk.join(session, participant).name(); }
    }
    record Leave(String session, String participant) implements Step {
        public String run(DeskAPI desk) { return Boolean.toString(desk.leave(session, participant)); }
    }
    record Close(String session) implements Step {
        public String run(DeskAPI desk) { return Boolean.toString(desk.close(session)); }
    }
    record Report(String style) implements Step {
        public String run(DeskAPI desk) { return desk.report(style); }
    }
}
