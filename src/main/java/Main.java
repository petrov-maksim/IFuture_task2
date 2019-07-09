import events.EventHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import socket.SocketServlet;




public class Main {
    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((t, e)->{
            e.printStackTrace();
        });
        Server server = new Server(8080);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("src/static");
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(new ServletHolder(new SocketServlet(new EventHandler())), "/ws");

        contextHandler.setSessionHandler(new SessionHandler());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler,contextHandler});

        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
