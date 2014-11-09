/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.DAO.FeaturesDAO;
import by.insane.gigabyte.DataBaseConnection;
import by.insane.DAO.mapping.*;
import by.insane.gigabyte.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Андрій
 */
public class FeaturesDAOImpl implements FeaturesDAO {

    private Statement statement;

    public FeaturesDAOImpl() {
        statement = DataBaseConnection.getInstance().getStatement();
    }
    
    

    @Override
    public void addFeatures(Features features) {
        try {
            //        Session session = HibernateUtil.getSessionFactory().openSession();
//        session.getTransaction().begin();
//        session.save(features);
//        session.getTransaction().commit();
//        session.close();
            statement.executeUpdate("INSERT INTO features(product_id,name,value)"
                    + "VALUES(" + features.getProduct().getProduct_id() + ",'" + features.getName()
                    + "', '" + features.getValue() + "')");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateFeatures(Features features) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.update(features);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Features getFeaturesById(int featuresId) {
        Features features = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        features = (Features) session.get(Features.class, featuresId);
        session.getTransaction().commit();
        session.close();
        return features;
    }

    @Override
    public Collection<Features> getAllFeatures() {
        List<Features> features;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        features = session.createCriteria(Features.class).list();
        System.out.println("Size: " + features.size());
        session.getTransaction().commit();
        session.close();
        return features;
    }

    @Override
    public void deleteFeatures(Features features) {
        try {
            DataBaseConnection.getInstance().getStatement().executeUpdate("DELETE FROM features WHERE features_id = " + features.getFeaturesId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteFeaturesByProduct(Product product) {
        try {
            DataBaseConnection.getInstance().getStatement().executeUpdate("DELETE FROM features WHERE product_id = " + product.getProduct_id());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Set<Features> getFeaturesByProduct(Product product) {
        Set<Features> set = new TreeSet<>();
        try {
            ResultSet result = DataBaseConnection.getInstance().getStatement().executeQuery("SELECT DISTINCT * FROM features WHERE product_id = " + product.getProduct_id());
            
            while(result.next()){
                Features feature = new Features();
                feature.setFeaturesId(result.getInt("features_id"));
                feature.setName(result.getString("name"));
                feature.setValue(result.getString("value"));
                feature.setProduct(product);
                set.add(feature);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return set;
    }

}
