/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.DAO.ItemCartDAO;
import by.insane.DAO.mapping.*;
import by.insane.gigabyte.DataBaseConnection;
import by.insane.gigabyte.util.HibernateUtil;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Андрій
 */
public class ItemCartDAOImpl implements ItemCartDAO {

    private Statement statement;

    public ItemCartDAOImpl() {
        statement = DataBaseConnection.getInstance().getStatement();
    }

    @Override
    public void addItemCart(ItemCart itemCart) {

        try {
            statement.executeUpdate("INSERT INTO item_cart(product_id, count, cart_id) VALUES("
                    + itemCart.getProduct().getProduct_id() + "," + itemCart.getCount() + ","
                    + itemCart.getCart().getAccountId() + ")");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        session.getTransaction().begin();
//        session.save(itemCart);
//        session.getTransaction().commit();
//        session.close();
    }

    @Override
    public void updateItemCart(ItemCart itemCart) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.update(itemCart);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public ItemCart getItemCartById(int itemCartId) {
        ItemCart itemCart = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        itemCart = (ItemCart) session.get(ItemCart.class, itemCartId);
        session.getTransaction().commit();
        session.close();
        return itemCart;
    }

    @Override
    public Collection<ItemCart> getAllItemCart() {
        List<ItemCart> itemCart;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        itemCart = session.createCriteria(ItemCart.class).list();
        session.getTransaction().commit();
        session.close();
        return itemCart;
    }

    @Override
    public void deleteItemCart(ItemCart itemCart) {
        try {
            statement.executeUpdate("DELETE FROM item_cart WHERE item_cart_id = " + itemCart.getItemCartId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
