
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.MoneyTransactionServlet;
import servlet.RegistrationServlet;
import servlet.ResultServlet;

public class Main {
    public static void main(String[] args) throws Exception{
        RegistrationServlet registration = new RegistrationServlet();
        MoneyTransactionServlet transaction = new MoneyTransactionServlet();
        ResultServlet result = new ResultServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(registration), "/registration");
        context.addServlet(new ServletHolder(transaction), "/transaction");
        context.addServlet(new ServletHolder(result), "/result");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
