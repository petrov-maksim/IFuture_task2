package socket;



import events.EventHandler;
import events.Events;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;


@WebSocket
public class Socket {
    private Session session;
    private EventHandler eventHandler;

    public Socket(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @OnWebSocketConnect
    public void onOpen(Session session){
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String data) throws IOException {
        String reply;
        Events event;
        if (data.startsWith("get"))
            event = Events.GET;
        else if (data.startsWith("add"))
            event = Events.ADD;
        else if (data.startsWith("remove"))
            event = Events.REMOVE;
        else if (data.startsWith("update"))
            event = Events.UPDATE;
        else if (data.startsWith("move"))
            event = Events.MOVE;
        else
            return;

        reply = eventHandler.handle(event,data);

        if (reply != null)
            session.getRemote().sendString(reply);
    }
}




