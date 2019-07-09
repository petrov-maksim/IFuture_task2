package events;


import java.util.HashMap;
import java.util.Map;

public class EventHandler {
    private Map<Events, Handler> handlers = new HashMap<>();

    public EventHandler() {
        handlers.put(Events.ADD, new AddHandler());
        handlers.put(Events.GET, new GetHandler());
        handlers.put(Events.REMOVE, new RemoveHandler());
        handlers.put(Events.UPDATE, new UpdateHandler());
        handlers.put(Events.MOVE, new MoveHandler());
    }

    public String handle(Events event, String data) {
        return handlers.get(event).handle(data);
    }
}
