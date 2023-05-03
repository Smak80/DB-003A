package ru.smak.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbHelper {
    private final Connection conn;
    public DbHelper(String host,
                    int port,
                    String user,
                    String password) throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port, user, password);
    }

    public void createDb() throws SQLException {
        var sql1 = "CREATE SCHEMA IF NOT EXISTS `db003a`";
        var sql2 = "USE `db003a`";
        var st = conn.createStatement();
        st.addBatch(sql1);
        st.addBatch(sql2);
        st.executeBatch();
    }

    public void createDbStructure() throws SQLException {
        var sql = "CREATE TABLE IF NOT EXISTS `test1` (" +
                "`phone` varchar(15) not null primary key," +
                "`login` varchar(50) not null," +
                "`password` varchar(512) not null)";
        conn.createStatement().execute(sql);
    }

    public boolean addUser(Customer newUser) throws SQLException {
        var sql = "INSERT INTO `test1` (phone, login, password) VALUES (?, ?, ?)";
        var st = conn.prepareStatement(sql);
        try {
            st.setString(1, newUser.getPhone());
            st.setString(2, newUser.getLogin());
            st.setString(3, newUser.getPassword());
            st.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public ArrayList<Customer> getUsers(String name) throws SQLException {
        var sql = "SELECT * FROM `test1` WHERE login Like ?";
        var pst = conn.prepareStatement(sql);
        pst.setString(1, "%"+name+"%");
        var rs = pst.executeQuery();
        var users = new ArrayList<Customer>();
        while (rs.next()){
            var user = new Customer();
            user.setPhone(rs.getString("phone"));
            user.setLogin(rs.getString("login"));
            try {
                user.setHashedPassword(rs.getString("password"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            users.add(user);
        }
        rs.close();
        return users;
    }
}
