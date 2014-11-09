/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.DAO.DAOFactory;
import by.insane.gigabyte.*;
import by.insane.DAO.OrdersDAO;
import by.insane.DAO.mapping.*;
import by.insane.gigabyte.util.HibernateUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 *
 * @author Андрій
 */
public class OrdersDAOImpl implements OrdersDAO {

    private Statement statement;

    public OrdersDAOImpl() {
        statement = DataBaseConnection.getInstance().getStatement();
    }

    @Override
    public void addOrder(Orders orders) {
        try {
            statement.executeUpdate("INSERT INTO orders(account_id, status, date_ordering) VALUES("
                    + orders.getAccount().getAccount_id() + ",'" + orders.getStatus() + "',NOW())");
            ResultSet rs = statement.executeQuery("select last_insert_id() as last_id from orders");
            if (rs.next()) {
                String lastid = rs.getString("last_id");

                System.out.println("Account id is: " + lastid);
                orders.setOrderId(Integer.parseInt(lastid));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateOrder(Orders orders) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.update(orders);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Orders getOrderById(int orderId) {
        Orders order = new Orders();
        try {
            ResultSet result = DataBaseConnection.getInstance().getStatement().executeQuery(
                    "SELECT * FROM orders WHERE order_id = " + orderId);
            if (result.next()) {
                order.setOrderId(orderId);
                order.setDate(result.getDate("date_ordering"));
                order.setStatus(result.getString("status"));
                order.setItemOrders(DAOFactory.getFactory(DAOFactory.MY_SQL).getItemOrdersDAO().getItemOrdersByOrders(order));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return order;
    }

    @Override
    public Collection<Orders> getAllOrders() {
        List<Orders> orders = new ArrayList<>();

        try {
            ResultSet result = DataBaseConnection.getInstance().getStatement().executeQuery("SELECT * FROM orders");
            while (result.next()) {
                Orders order = new Orders();
                order.setOrderId(result.getInt("order_id"));
                order.setStatus(result.getString("status"));
                order.setDate(result.getDate("date_ordering"));
                Account n = new Account();
                n.setAccount_id(result.getInt("account_id"));
                order.setAccount(n);
                orders.add(order);
            }
            Iterator<Orders> it = orders.iterator();
            DAOFactory factory = DAOFactory.getFactory(DAOFactory.MY_SQL);
            while (it.hasNext()) {
                Orders next = it.next();
                next.setAccount(factory.getAccountDAO().getAccountById(next.getAccount().getAccount_id()));
                next.setItemOrders(factory.getItemOrdersDAO().getItemOrdersByOrders(next));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    @Override
    public void deleteOrder(Orders orders) {
        try {
            statement.executeUpdate("delete from orders where order_id=" + orders.getOrderId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public Set<Orders> getOrdersByAccount(Account account) {

        Set<Orders> set = new TreeSet<>();
        try {
            ResultSet result = DataBaseConnection.getInstance().getStatement().executeQuery(
                    "select * from orders where account_id = " + account.getAccount_id());
            while (result.next()) {
                Orders order = new Orders();
                order.setAccount(account);
                order.setOrderId(result.getInt("order_id"));
                order.setStatus(result.getString("status"));
                order.setDate(result.getDate("date_ordering"));
                set.add(order);
            }
            Iterator<Orders> it = set.iterator();
            while (it.hasNext()) {
                Orders next = it.next();
                next.setItemOrders(DAOFactory.getFactory(DAOFactory.MY_SQL).getItemOrdersDAO().getItemOrdersByOrders(next));
            }
        } catch (SQLException ex) {
            System.out.println("OrdersDAOImpl:    ");
            ex.printStackTrace();
        }

        return set;
    }

    @Override
    public void addOrders(Collection<Orders> orders) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        for (Orders order : orders) {
            session.save(order);
        }

        session.getTransaction().commit();
        session.close();

    }

    @Override
    public void updateOrders(Collection<Orders> orders) {

        try {
            for (Orders order : orders) {
                statement.executeUpdate("UPDATE orders SET account_id = " + order.getAccount().getAccount_id()
                        + ", status = '" + order.getStatus() + "' WHERE order_id = " + order.getOrderId());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
