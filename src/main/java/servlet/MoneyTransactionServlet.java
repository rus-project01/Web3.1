package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", req.getMethod());
        String name = req.getParameter("senderName");
        String pass = req.getParameter("senderPass");
        Long count = Long.parseLong(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo");
        BankClient client = null;
            client = new BankClient(name, pass, count);
            if(bankClientService.sendMoneyToClient(client, nameTo, count)) {
                resp.getWriter().println("The transaction was successful");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.getWriter().println("transaction rejected");
            }
        PageGenerator.getInstance().getPage("resultPage.html", pageVariables);
        resp.setContentType("text/html;charset=utf-8");
    }
}
