/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.gigabyte.*;
import by.insane.DAO.ProductDAO;
import by.insane.DAO.*;
import by.insane.DAO.mapping.*;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.inject.Inject;

/**
 *
 * @author Andriy
 */
public class ProductDAOImpl implements ProductDAO, Serializable {

    private Statement statement;
    private PreparedStatement addStatement;
    private PreparedStatement updateStatement;
    @Inject
    private DataBaseConnection conn;

    public ProductDAOImpl() {
        try {
            statement = DataBaseConnection.getInstance().getStatement();
            Connection connection = DataBaseConnection.getInstance().getConnection();
            addStatement = connection.prepareStatement("INSERT INTO product(category_id,name, count, price, description,adding_date,image_id) "
                    + "VALUES(?,?,?,?,?,NOW(),?)");
            updateStatement = connection.prepareStatement("UPDATE product SET category_id = ?, name = ?, count = ?, price = ?, description = ?"
                    + ", adding_date = NOW(), image_id = ? WHERE product_id = ?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addProduct(Product product) {
        try {
            addStatement.setInt(1, product.getCategory().getCategory_id());
            addStatement.setString(2, product.getName());
            addStatement.setInt(3, product.getCount());
            addStatement.setInt(4, product.getPrice());
            addStatement.setString(5, product.getDescription());
            addStatement.setInt(6, product.getMainImage().getImagesId());
            addStatement.executeUpdate();
            ResultSet rs = statement.executeQuery("select last_insert_id() as last_id from product");
            if (rs.next()) {
                product.setProduct_id(rs.getInt("last_id"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        try {
            updateStatement.setInt(1, product.getCategory().getCategory_id());
            updateStatement.setString(2, product.getName());
            updateStatement.setInt(3, product.getCount());
            updateStatement.setInt(4, product.getPrice());
            updateStatement.setString(5, product.getDescription());
            updateStatement.setInt(6, product.getMainImage().getImagesId());
            updateStatement.setInt(7, product.getProduct_id());
            updateStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Product getProductById(int product_id) {
        Product product = new Product();
        try {
            ResultSet set = DataBaseConnection.getInstance().getStatement().executeQuery(
                    "SELECT distinct * FROM product p LEFT OUTER JOIN images i ON p.image_id = i.images_id WHERE p.product_id = " + product_id);

            if (set.next()) {

                product.setProduct_id(set.getInt("product_id"));
                product.setCount(set.getInt("count"));
                product.setDescription(set.getString("description"));
                product.setName(set.getString("name"));
                product.setDate(set.getDate("adding_date"));
                product.setPrice(set.getInt("price"));
                
                Images image = new Images();
                image.setImagesId(set.getInt("images_id"));
                image.setPath(set.getString("path"));
                image.setProduct(product);
                image.setName(set.getString(12));
                product.setMainImage(image);

                DAOFactory factory = DAOFactory.getFactory(DAOFactory.MY_SQL);
                product.setCategory(factory.getCategoryDAO().getCategoryById(set.getInt("category_id")));
                product.setImages(factory.getImagesDAO().getImagesByProduct(product));
                product.setComments(factory.getCommentsDAO().getCommentsByProductId(product_id));
                product.setFeatures(factory.getFeaturesDAO().getFeaturesByProduct(product));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public Collection<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            ResultSet set = DataBaseConnection.getInstance().getStatement().executeQuery(
                    "SELECT distinct * FROM product p LEFT OUTER JOIN images i ON p.image_id = i.images_id ORDER BY p.adding_date DESC");

            while (set.next()) {
                Product product = new Product();
                product.setProduct_id(set.getInt("product_id"));
                product.setCount(set.getInt("count"));
                product.setDescription(set.getString("description"));
                product.setName(set.getString("name"));
                product.setDate(set.getDate("adding_date"));
                product.setPrice(set.getInt("price"));
                Category c = new Category();
                c.setCategory_id(set.getInt("category_id"));
                
                   Images image = new Images();
                image.setImagesId(set.getInt("images_id"));
                image.setPath(set.getString("path"));
                image.setProduct(product);
                image.setName(set.getString(12));
                product.setMainImage(image);

                product.setCategory(c);
                products.add(product);

            }
            Iterator<Product> it = products.iterator();
            DAOFactory factory = DAOFactory.getFactory(DAOFactory.MY_SQL);
            while (it.hasNext()) {
                Product next = it.next();

                next.setCategory(factory.getCategoryDAO().getCategoryByIdForProduct(next.getCategory().getCategory_id()));
                next.setImages(factory.getImagesDAO().getImagesByProduct(next));
                next.setComments(factory.getCommentsDAO().getCommentsByProductId(next.getProduct_id()));
                next.setFeatures(factory.getFeaturesDAO().getFeaturesByProduct(next));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public void deleteProduct(Product product) {
        try {
            statement.executeUpdate("DELETE FROM product WHERE product_id = " + product.getProduct_id());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public Collection<Product> getProductsLikeName(String likeName) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery("SELECT product_id FROM product WHERE name LIKE '%" + likeName + "%'");
            while (result.next()) {
                Product p = new Product();
                p.setProduct_id(result.getInt("product_id"));
                products.add(p);
            }
            for (int i = 0; i < products.size(); i++) {
                products.set(i, getProductById(products.get(i).getProduct_id()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    @Override
    public Set<Product> getProductsInCategory(Category category) {
        TreeSet<Product> products = new TreeSet<>();
        try {
            ResultSet set = DataBaseConnection.getInstance().getStatement().executeQuery(
                    "SELECT * FROM product p INNER JOIN images i ON p.image_id = i.images_id WHERE category_id = " + category.getCategory_id());

            while (set.next()) {
                Product product = new Product();
                product.setProduct_id(set.getInt("product_id"));
                product.setCount(set.getInt("count"));
                product.setDescription(set.getString("description"));
                product.setName(set.getString("name"));
                product.setDate(set.getDate("adding_date"));
                product.setCategory(category);
                product.setPrice(set.getInt("price"));
                Category category1 = new Category();
                category1.setCategory_id(set.getInt("category_id"));
                product.setCategory(category1);
                   Images image = new Images();
                image.setImagesId(set.getInt("images_id"));
                image.setPath(set.getString("path"));
                image.setProduct(product);
                image.setName(set.getString(12));
                product.setMainImage(image);
                
                
                products.add(product);
            }

            DAOFactory factory = DAOFactory.getFactory(DAOFactory.MY_SQL);

            Iterator<Product> iterator = products.iterator();
            while (iterator.hasNext()) {
                Product p = iterator.next();
                Set<Comments> lSet = new TreeSet<>();
                lSet.addAll(factory.getCommentsDAO().getCommentsByProductId(p.getProduct_id()));
                p.setComments(lSet);
                p.setCategory(category);
                p.setImages(factory.getImagesDAO().getImagesByProduct(p));
                p.setFeatures(factory.getFeaturesDAO().getFeaturesByProduct(p));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return products;
    }

}
