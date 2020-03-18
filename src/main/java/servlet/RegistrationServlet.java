package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    BankClientService service = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", req.getMethod());
        String name = req.getParameter("name");
        String pass = req.getParameter("password");
        Long money = Long.parseLong(req.getParameter("money"));
        BankClient client = new BankClient(name, pass, money);
        try {
            if(service.addClient(client)) {
                resp.getWriter().println("Add client successful");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.getWriter().println("Client not add");
            }
        } catch (SQLException | DBException e) {
            e.printStackTrace();
        }
        PageGenerator.getInstance().getPage("resultPage.html", pageVariables);
        resp.setContentType("text/html;charset=utf-8");
    }
}
