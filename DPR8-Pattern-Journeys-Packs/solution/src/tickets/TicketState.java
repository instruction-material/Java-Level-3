package tickets;
/** State: each constant owns its behavior and chooses the next state. */
enum TicketState {
    DRAFT { TicketState next(String action) { return action.equals("approve") ? READY : this; } },
    READY { TicketState next(String action) { return action.equals("finish") ? DONE : this; } },
    DONE { TicketState next(String action) { return action.equals("reset") ? DRAFT : this; } };
    abstract TicketState next(String action);
}
