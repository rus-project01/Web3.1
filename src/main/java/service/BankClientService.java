package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) throws SQLException {
        return getBankClientDAO().getClientByName(name);
    }

    public List<BankClient> getAllClient() throws SQLException {
        return getBankClientDAO().getAllBankClient();
    }

    public boolean deleteClient(String name) {
        return false;
    }

    public boolean addClient(BankClient client) throws DBException, SQLException {
        return getBankClientDAO().addClient(client);
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
        try {

            if(!getBankClientDAO().validateClient(sender.getName(), sender.getPassword())) {
                return false;
            }
            sender.setMoney(getBankClientDAO().getClientByName(sender.getName()).getMoney());
            if(!getBankClientDAO().isClientHasSum(sender.getName(), value)) {
                return false;
            }
            getBankClientDAO().updateClientsMoney(getBankClientDAO().getClientByName(name).getName(), getBankClientDAO().getClientByName(name).getPassword(), getBankClientDAO().getClientByName(name).getMoney() + value);
            getBankClientDAO().updateClientsMoney(sender.getName(), sender.getPassword(), sender.getMoney() - value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public void createTable() throws DBException{
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("web3?").          //db name
                    append("user=root&").          //login
                    append("password=Qwerty12").       //password
                    append("&serverTimezone=UTC").
                    append("&useSSL=false");

            System.out.println("URL: " + url + "\n");


            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
