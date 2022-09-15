package model;

import java.util.Date;

public class Event {
    private String source;
    private String date;
    private String destination;
    private Action action;
    private ActionState state;

    public Event(String source, String date, String destination, Action action, ActionState state) {
        this.source = source;
        this.date = date;
        this.destination = destination;
        this.action = action;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Event{" +
                "source='" + source + '\'' +
                ", date='" + date + '\'' +
                ", destination='" + destination + '\'' +
                ", action=" + action +
                ", state=" + state +
                '}';
    }
}




