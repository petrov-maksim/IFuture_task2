package socket;

import events.EventHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;



public class SocketCreator implements WebSocketCreator {
    EventHandler eventHandler;
    public SocketCreator(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        return new Socket(eventHandler);
    }
}
