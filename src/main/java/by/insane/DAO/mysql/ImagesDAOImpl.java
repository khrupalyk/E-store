/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.gigabyte.*;

import by.insane.DAO.ImagesDAO;
import by.insane.DAO.mapping.*;
import by.insane.gigabyte.util.HibernateUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;

/**
 *
 * @author Андрій
 */
public class ImagesDAOImpl implements ImagesDAO {

    private Statement statement;

    public ImagesDAOImpl() {
        statement = DataBaseConnection.getInstance().getStatement();
    }

    @Override
    public void addImage(Images image) {
        try {
            statement.executeUpdate(
                    "INSERT INTO images(product_id,path,name) VALUES( "
                    + image.getProduct().getProduct_id()
                    + ",'" + image.getPath()
                    + "', '" + image.getName()
                    + "')");
            ResultSet rs = statement.executeQuery("select last_insert_id() as last_id from images");
            if (rs.next()) {
                image.setImagesId(rs.getInt("last_id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateImage(Images image) {
        try {
            statement.executeUpdate(
                    "UPDATE images SET product_id = "
                    + image.getProduct().getProduct_id()
                    + ", path = '" + image.getPath()
                    + "', name = '" + image.getName()
                    + "' WHERE images_id = " + image.getImagesId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Images getImageById(int imageId) {
        Images image = new Images();
        try {
            ResultSet result = statement.executeQuery("SELECT DISTINCT * FROM images i INNER JOIN product p "
                    + "ON i.product_id = p.product_id WHERE images_id = " + image);
            if (result.next()) {
                image.setImagesId(imageId);
                image.setName(result.getString("i.name"));
                image.setPath(result.getString("path"));
                image.setProduct(new Product());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return image;
    }

    @Override
    public Collection<Images> getAllImages() {
        List<Images> images = new ArrayList<>();
        try {
            ResultSet set = statement.executeQuery(
                    "SELECT DISTINCT * FROM images");
            while (set.next()) {
                Images image = new Images();
                image.setImagesId(set.getInt("images_id"));
                image.setName(set.getString("name"));
                image.setPath(set.getString("path"));
                images.add(image);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return images;
    }

    @Override
    public void deleteImage(Images image) {
        try {
            statement.executeUpdate("DELETE FROM images WHERE images_id = " + image.getImagesId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Set<Images> getImagesByProduct(Product product) {
        Set<Images> images = new TreeSet<>(new Comparator<Images>() {

            @Override
            public int compare(Images o1, Images o2) {
                if (o1.getImagesId() < o2.getImagesId()) {
                    return -1;
                } else if (o1.getImagesId() > o2.getImagesId()) {
                    return 1;
                } else {
                    return 0;
                }
            }

        });

        try {
            ResultSet set = statement.executeQuery(
                    "SELECT DISTINCT * FROM images WHERE product_id = " + product.getProduct_id());
            while (set.next()) {
                Images image = new Images();
                image.setImagesId(set.getInt("images_id"));
                image.setProduct(product);
                image.setName(set.getString("name"));
                image.setPath(set.getString("path"));
                images.add(image);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return images;
    }

}
