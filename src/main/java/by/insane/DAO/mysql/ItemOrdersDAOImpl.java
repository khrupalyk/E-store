/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.DAO.ItemOrdersDAO;
import by.insane.DAO.*;
import by.insane.gigabyte.DataBaseConnection;
import by.insane.DAO.mapping.*;
import by.insane.gigabyte.util.HibernateUtil;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.hibernate.Session;

/**
 *
 * @author Андрій
 */
public class ItemOrdersDAOImpl implements ItemOrdersDAO, Serializable {

    private Statement statement;

    public ItemOrdersDAOImpl() {
        statement = DataBaseConnection.getInstance().getStatement();
    }

    @Override
    public void addItemOrders(ItemOrders itemOrders) {
        try {
            statement.executeUpdate("INSERT INTO item_orders(order_id, count, product_id) VALUES("
                    + itemOrders.getOrder().getOrderId() + "," + itemOrders.getCount() + ","
                    + itemOrders.getProduct().getProduct_id() + ")");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateItemOrders(ItemOrders itemOrders) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.update(itemOrders);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public ItemOrders getItemOrdersId(int itemOrdersId) {
        ItemOrders comment = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        comment = (ItemOrders) session.get(ItemOrders.class, itemOrdersId);
        session.getTransaction().commit();
        session.close();
        return comment;
    }

    @Override
    public Collection<ItemOrders> getAllItemOrders() {
        List<ItemOrders> itemOrders = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        itemOrders = session.createCriteria(ItemOrders.class).list();
        session.getTransaction().commit();
        for (ItemOrders comments1 : itemOrders) {
            System.out.println(comments1);
        }
        session.close();
        return itemOrders;
    }

    @Override
    public void deleteItemOrders(ItemOrders itemOrders) {
        try {
            statement.executeUpdate("DELETE FROM item_orders WHERE item_orders_id = " + itemOrders.getItemOrderId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Set<ItemOrders> getItemOrdersByOrders(Orders order) {
        ArrayList<ItemOrders> set = new ArrayList<>();
        ArrayList<Integer> pId = new ArrayList<>();
        try {
            ResultSet result = DataBaseConnection.getInstance().getStatement().executeQuery(
                    "SELECT * FROM item_orders WHERE order_id = " + order.getOrderId());
            while (result.next()) {
                ItemOrders item = new ItemOrders();
                item.setCount(result.getInt("count"));
                item.setItemOrderId(result.getInt("item_orders_id"));
                item.setOrder(order);
                pId.add(result.getInt("product_id"));
                set.add(item);
            }

            Iterator<ItemOrders> it = set.iterator();
            int i = 0;
            while (it.hasNext()) {
                ItemOrders next = it.next();
                result = DataBaseConnection.getInstance().getStatement().executeQuery(
                        "SELECT * FROM product WHERE product_id = " + pId.get(i));
                i++;
                if (result.next()) {
                    Product p = new Product();
                    p.setProduct_id(result.getInt("product_id"));
                    p.setName(result.getString("name"));
                    p.setPrice(result.getInt("price"));
                    p.setImages(DAOFactory.getFactory(DAOFactory.MY_SQL).getImagesDAO().getImagesByProduct(p));
                    next.setProduct(p);
                }

            }

            it = set.iterator();
//            i = 0;
            while (it.hasNext()) {
                ItemOrders next = it.next();
                next.getProduct().setImages(DAOFactory.getFactory(DAOFactory.MY_SQL).getImagesDAO().getImagesByProduct(next.getProduct()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return (new TreeSet<>(set));
    }

}
