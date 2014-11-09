/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.DAO.*;
import java.util.Collection;
import java.util.List;
import by.insane.gigabyte.*;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import by.insane.DAO.mapping.*;

/**
 *
 * @author Andriy
 */
public class AccountDAOImpl implements AccountDAO {
    private Statement statement;

    public AccountDAOImpl() {
        statement = DataBaseConnection.getInstance().getStatement();
    }
    
    

    @Override
    public void addAccount(Account account) {
        int key = 0;
        String insertedString = "INSERT INTO account(login,password,fname,lname,"
                + "phone,address,email,role,date_registration) "
                + "VALUES('" + account.getLogin()
                + "','" + account.getPassword()
                + "','" + account.getFname()
                + "','" + account.getLname()
                + "','" + account.getPhone()
                + "','" + account.getAddress()
                + "','" + account.getEmail()
                + "','" + account.getRole() + "',NOW())";
        try {
            statement.executeUpdate(insertedString);
            ResultSet rs = statement.executeQuery("select last_insert_id() as last_id from account");
            if (rs.next()) {
                String lastid = rs.getString("last_id");

                System.out.println("Account id is: " + lastid);
                account.setAccount_id(Integer.parseInt(lastid));
            }
        } catch (SQLException ex) {
            System.out.println("Cant add accountt");
            ex.printStackTrace();
        }

    }

    @Override
    public void updateAccount(Account account) {

        Statement statement = DataBaseConnection.getInstance().getStatement();
        try {
            statement.execute("UPDATE account SET login='"
                    + account.getLogin() + "', lname='"
                    + account.getLname() + "', fname='"
                    + account.getFname() + "', phone='"
                    + account.getPhone() + "', role='"
                    + account.getRole() + "', email='"
                    + account.getEmail() + "' WHERE account_id = " + account.getAccount_id());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Account getAccountById(int account_id) {
        try {
            Account account = new Account();

            Statement statement = DataBaseConnection.getInstance().getStatement();

            ResultSet set = statement.executeQuery("SELECT * FROM account WHERE account_id = " + account_id);
            if (set.next()) {
                account.setAccount_id(set.getInt("account_id"));
                account.setAddress(set.getString("address"));
                account.setEmail(set.getString("email"));
                account.setFname(set.getString("fname"));
                account.setLname(set.getString("lname"));
                account.setPhone(set.getString("phone"));
                account.setRole(set.getString("role"));
                account.setLogin(set.getString("login"));
                account.setDate(set.getDate("date_registration"));
                account.setCart(DAOFactory.getFactory(DAOFactory.MY_SQL).getCartDAO().getCartByAccount(account));
                account.setOrders(new TreeSet<>(DAOFactory.getFactory(DAOFactory.MY_SQL).getOrdersDAO().getOrdersByAccount(account)));
            }
            return account;
        } catch (Exception ex) {
            System.out.println("get account by id: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Account> getAllAccounts() {
        List<Account> accounts = new LinkedList<>();
        try {
            Statement statement = DataBaseConnection.getInstance().getStatement();

            ResultSet set = statement.executeQuery("SELECT * FROM account");
            while (set.next()) {

                Account account = new Account();
                account.setAccount_id(set.getInt("account_id"));
                account.setAddress(set.getString("address"));
                account.setEmail(set.getString("email"));
                account.setFname(set.getString("fname"));
                account.setLname(set.getString("Lname"));
                account.setPhone(set.getString("phone"));
                account.setRole(set.getString("role"));
                account.setLogin(set.getString("login"));
                account.setPassword(set.getString("password"));
                account.setDate(set.getDate("date_registration"));
                accounts.add(account);

            }
            DAOFactory factory = DAOFactory.getFactory(DAOFactory.MY_SQL);
            Iterator<Account> it = accounts.iterator();
            while (it.hasNext()) {
                Account next = it.next();
                next.setCart(factory.getCartDAO().getCartByAccount(next));
                next.setOrders(new TreeSet<>(factory.getOrdersDAO().getOrdersByAccount(next)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return accounts;
    }

    @Override
    public void deleteAccount(Account account) {

        Statement statement = DataBaseConnection.getInstance().getStatement();

        try {
            statement.execute("DELETE FROM account WHERE account_id = " + account.getAccount_id());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateAccountPassword(Account account) {
        Statement statement = DataBaseConnection.getInstance().getStatement();
        try {
            statement.execute("UPDATE account SET password = '" + account.getPassword() + "' WHERE account_id = " + account.getAccount_id());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void registerUser(Account account) {
        try {
            CallableStatement callableStatement
                    = DataBaseConnection.getInstance().getConnection().prepareCall("select register_user(?, ?, ?, ?, ?, ?, ?, ?)");
            callableStatement.setString(1, account.getLogin());
            callableStatement.setString(2, account.getPassword());
            callableStatement.setString(3, account.getFname());
            callableStatement.setString(4, account.getLname());
            callableStatement.setString(5, account.getPhone());
            callableStatement.setString(6, account.getAddress());
            callableStatement.setString(7, account.getEmail());
            callableStatement.setString(8, account.getRole());
            ResultSet set = callableStatement.executeQuery();
            if (set.next()) {
                account.setAccount_id(set.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
