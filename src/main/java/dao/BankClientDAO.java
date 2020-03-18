package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }



    public boolean validateClient(String name, String password) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        BankClient banks = new BankClient();
        while(result.next()) {
            banks.setName(result.getString("name"));
            banks.setPassword(result.getString("password"));
        }
        if(name.equals(banks.getName()) && password.equals(banks.getPassword())){
            return true;
        } else return false;
    }



    public BankClient getClientById(long id) throws SQLException {
        String url = "SELECT * FROM bank_client WHERE id=?";
        BankClient banks = new BankClient();
        PreparedStatement result = connection.prepareStatement(url);
            result.setLong(1, id);
            ResultSet resultSet = result.executeQuery();
            banks.setId(resultSet.getLong("id"));
            banks.setName(resultSet.getString("name"));
            banks.setPassword(resultSet.getString("password"));
            banks.setMoney(resultSet.getLong("money"));
            result.executeUpdate();
            result.close();
            connection.close();
            return banks;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        if(result.getLong("money") >= expectedSum) {
            return true;
        } else return false;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet resultSet = stmt.getResultSet();
        BankClient banks = new BankClient(null, null, null);
        while(resultSet.next()) {
            banks.setId(resultSet.getLong("id"));
            banks.setName(resultSet.getString("name"));
            banks.setPassword(resultSet.getString("password"));
            banks.setMoney(resultSet.getLong("money"));
        }
        return banks;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> list = new ArrayList<>();
        PreparedStatement result = null;
        String url = "SELECT id, name, password, money FROM bank_client";
        try {
            result = connection.prepareStatement(url);
            ResultSet resultSet = result.executeQuery(url);
            while(resultSet.next()) {
                BankClient client = new BankClient();
                client.setId(resultSet.getLong("id"));
                client.setName(resultSet.getString("name"));
                client.setPassword(resultSet.getString("password"));
                client.setMoney(resultSet.getLong("money"));
                list.add(client);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        PreparedStatement result = null;
        String url = "UPDATE bank_client SET name=?, password=?, money=? WHERE name=?";
        try {
            result = connection.prepareStatement(url);
            result.setString(1, name);
            result.setString(2, password);
            result.setLong(3, transactValue);
            result.setString(4, name);

            result.executeUpdate();

        } catch (SQLException e){}
        finally {
            if(result != null){
                result.close();
            }
            if(connection != null){
                connection.close();
            }
        }
    }

    public boolean addClient(BankClient client) throws SQLException {
        List<BankClient> list = new ArrayList<>(getAllBankClient());
        PreparedStatement result = null;
        String url = "INSERT INTO bank_client (id, name, password, money) values(?, ?, ?, ?)";
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getName().equals(client.getName())){
                    return false;
                }
            }
            result = connection.prepareStatement(url);
            result.setLong(1, client.getId());
            result.setString(2, client.getName());
            result.setString(3, client.getPassword());
            result.setLong(4, client.getMoney());
            result.executeUpdate();
        return true;
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
