/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.insane.DAO.orm;

import by.insane.DAO.ProductDAO;
import by.insane.DAO.*;
import by.insane.DAO.mapping.*;
import by.insane.gigabyte.util.HibernateUtil;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author Andriy
 */
public class ProductDAOImpl implements ProductDAO{

    @Override
    public void addProduct(Product product) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.save(product);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void updateProduct(Product product) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.update(product);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Product getProductById(int product_id) {
        Product product = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        product = (Product)session.get(Product.class, product_id);
        session.getTransaction().commit();
        session.close();
        return product;
    }

    @Override
    public Collection<Product> getAllProducts() {
        List<Product> products = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Criteria criteria = session.createCriteria(Product.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        criteria.addOrder(Order.asc("adding_date"));
        products = criteria.list();
        session.getTransaction().commit();
        session.close();
        return products;
    }

    @Override
    public void deleteProduct(Product product) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.delete(product);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Collection<Product> getProductsLikeName(String likeName) {
        List<Product> products = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        products = session.createSQLQuery("SELECT * FROM product WHERE name LIKE '%" + likeName + "%'").addEntity(Product.class).list();
        session.getTransaction().commit();
        session.close();
        return products;
    }

    @Override
    public Set<Product> getProductsInCategory(Category category) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
