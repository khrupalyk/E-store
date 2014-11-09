/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.DAO.CartDAO;
import by.insane.DAO.DAOFactory;
import by.insane.gigabyte.DataBaseConnection;
import by.insane.DAO.mapping.*;
import by.insane.gigabyte.util.HibernateUtil;
import java.io.Serializable;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;

/**
 *
 * @author Андрій
 */
public class CartDAOImpl implements CartDAO, Serializable {

    private Statement statement;
    private PreparedStatement addStatement;
    private PreparedStatement updateStatement;

    public CartDAOImpl()  {
        try {
            statement = DataBaseConnection.getInstance().getStatement();
            Connection connection = DataBaseConnection.getInstance().getConnection();
            addStatement = connection.prepareStatement("INSERT INTO cart VALUES(?)");
            updateStatement = connection.prepareStatement("UPDATE cart SET account_id = ? WHERE account_id = ?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addCart(Cart cart) {

        try {
            addStatement.setInt(1, cart.getAccountId());
            addStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateCart(Cart cart) {

        try {
            updateStatement.setInt(1, cart.getAccountId());
            updateStatement.setInt(2, cart.getAccountId());
            updateStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Cart getCartById(int cart_id) {
        Cart cart = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        cart = (Cart) session.load(Cart.class, cart_id);
        session.getTransaction().commit();
        session.close();
        return cart;
    }

    @Override
    public Collection<Cart> getAllCarts() {
        List<Cart> carts = new LinkedList<>();
        try {

            Statement statement = DataBaseConnection.getInstance().getStatement();
            ResultSet set = statement.executeQuery(
                    "SELECT DISTINCT * FROM cart c INNER JOIN account acc ON c.account_id = acc.account_id");
            if (set.next()) {
                Cart cart = new Cart();
                Account acc = new Account();
                acc.setLname(set.getString("lname"));
                acc.setFname(set.getString("fname"));
                acc.setAccount_id(set.getInt("account_id"));
                acc.setDate(set.getDate("date_registration"));
                acc.setEmail(set.getString("email"));
                acc.setLogin(set.getString("login"));
                cart.setAccount(acc);
                cart.setAccountId(set.getInt("account_id"));
                carts.add(cart);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return carts;
    }

    @Override
    public void deleteCart(Cart cart) {
        try {
            Statement statement = DataBaseConnection.getInstance().getStatement();
            statement.executeUpdate("DELETE FROM cart WHERE account_id =" + cart.getAccountId());

        } catch (SQLException ex) {
            Logger.getLogger(CartDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Cart getCartByAccount(Account account) {
        Cart cart = new Cart();
        try {
            Statement statement = DataBaseConnection.getInstance().getStatement();
            ResultSet set = statement.executeQuery("SELECT DISTINCT * FROM cart WHERE account_id = " + account.getAccount_id());
            boolean f = false;
            Set<ItemCart> items = new TreeSet<>();
            if (set.next()) {
                cart.setAccount(account);
                cart.setAccountId(account.getAccount_id());
                DAOFactory factory = DAOFactory.getFactory(DAOFactory.MY_SQL);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cart;
    }

    @Override
    public void deleteCartByAccount(Account account) {
        try {
            DataBaseConnection.getInstance().getStatement().executeUpdate(
                    "DELETE FROM cart WHERE account_id = " + account.getAccount_id());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteProductFromCartsById(int product_id) {

        try {
            DataBaseConnection.getInstance().getStatement().executeUpdate(
                    "DELETE FROM cart WHERE product_id = " + product_id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
