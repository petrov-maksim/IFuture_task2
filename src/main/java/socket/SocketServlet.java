package socket;

import events.EventHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "Dictionary Socket Servlet", urlPatterns = "/ws")
public class SocketServlet extends WebSocketServlet {
    private EventHandler eventHandler;

    public SocketServlet(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(new SocketCreator(eventHandler));
    }
}
