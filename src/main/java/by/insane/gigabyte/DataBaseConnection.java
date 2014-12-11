/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.gigabyte;

import java.sql.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;

/**
 *
 * @author insane
 */
public class DataBaseConnection implements Serializable {

//    private static String url = "jdbc:mysql://localhost:3306/";
    private static String url = "jdbc:mysql://mysql28679-estore.jelastic.neohost.net/";
    private static String user = "root";
//    private static String password = "111111";
    private static String password = "W9hAWQIuWi";
    private static String database = "GigaByte";
    private Connection connection;
    private Statement statement;
    private static DataBaseConnection instance = null;

    private DataBaseConnection(String database, String user, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url + database, user, password);
            statement = connection.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            statement.executeUpdate("SET NAMES utf8");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public DataBaseConnection() {
        this(database, user, password);
    }

    public static synchronized DataBaseConnection getInstance() {
        if (instance == null) {
            instance = new DataBaseConnection(database, user, password);
        }
        return instance;
    }

    public static synchronized DataBaseConnection getInstance(String database, String user, String password) {
        if (instance == null) {
            instance = new DataBaseConnection(database, user, password);
        }
        return instance;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void closeConnection() throws SQLException {
        System.out.println("Disposes call!");
        statement.close();
        connection.close();
    }

    public void close() {
        try {

            statement.close();
            connection.close();
            instance = null;
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

}
