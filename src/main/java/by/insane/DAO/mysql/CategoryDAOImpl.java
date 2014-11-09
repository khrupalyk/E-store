/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.DAO.*;
import by.insane.gigabyte.DataBaseConnection;
import by.insane.DAO.mapping.*;
import by.insane.gigabyte.util.HibernateUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Andriy
 */
public class CategoryDAOImpl implements CategoryDAO {

    private Statement statement;
    private ArrayList<Category> categoriesList;

    public CategoryDAOImpl() {
        statement = DataBaseConnection.getInstance().getStatement();
//        categoriesList = new ArrayList<>();
        loadAllCategory();
    }

    @Override
    public void addCategory(Category category) {
        try {
            statement.executeUpdate("INSERT INTO category(name, parent_id) VALUES('"
                    + category.getName() + "'," + ((category.getParentCategory().getCategory_id() < 1) ? "null"
                            : category.getParentCategory().getCategory_id()) + ")");
            loadAllCategory();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateCategory(Category category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.update(category);
        session.getTransaction().commit();
        session.close();
    }

    private Category getParentCategory(Category category) throws SQLException {
        Category parent = new Category();

        ResultSet set = DataBaseConnection.getInstance().getStatement().executeQuery(
                "SELECT * FROM category WHERE category_id = " + category.getParentCategory().getCategory_id());
        if (set.next()) {
            category.setCategory_id(set.getInt("category_id"));
            category.setName(set.getString("name"));
            Category parent2 = new Category();
            parent.setCategory_id(set.getInt("parent_id"));
            category.setParentCategory(parent2);
        }

        return parent;
    }

    @Override
    public Category getCategoryById(int category_id) {
        Category category = new Category();
        try {

            Statement statement = DataBaseConnection.getInstance().getStatement();

            for (Iterator<Category> iterator = categoriesList.iterator(); iterator.hasNext();) {
                Category next = iterator.next();
                if (next.getCategory_id() == category_id) {
                    category = next;
                    break;
                }
            }

            DAOFactory factory = DAOFactory.getFactory(DAOFactory.MY_SQL);

            category.setProducts(factory.getProductDAO().getProductsInCategory(category));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return category;
    }

    private Set<Category> getCategories(Category category, Statement statement) throws Exception {
        ResultSet set = statement.executeQuery(
                "SELECT * FROM category c1 "
                + "WHERE c1.parent_id = " + category.getCategory_id());
        Set<Category> setCategory = new TreeSet<Category>();
        while (set.next()) {
            Category c = new Category();
            c.setCategory_id(set.getInt("category_id"));
            c.setName(set.getString("name"));
            c.setParentCategory(category);
            setCategory.add(c);
        }

        Iterator<Category> iterator = setCategory.iterator();
        while (iterator.hasNext()) {

            Category cat = iterator.next();
            cat.setProducts(DAOFactory.getFactory(DAOFactory.MY_SQL).getProductDAO().getProductsInCategory(cat));
            cat.setCategories(getCategories(cat, statement));
        }

        return setCategory;
    }

    @Override
    public Collection<Category> getAllCategories() {

        return categoriesList;
    }

    @Override
    public void deleteCategory(Category category) {
        try {
            statement.executeUpdate("DELETE FROM category WHERE category_id = " + category.getCategory_id());
            loadAllCategory();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Collection<Category> getMainCategories() {
        List<Category> categories = new ArrayList<>();
        for (Category category : this.categoriesList) {
            if (category.getParentCategory().getCategory_id() == 0) {
                categories.add(category);
            }
        }
        return categories;
    }

    @Override
    public Category getCategoryByName(String name) {
        Collection<Category> allCategories = getAllCategories();
        for (Category category : allCategories) {
            if (name.equals(category.getName())) {
                return category;
            }
        }
        return null;
    }

    private void loadAllCategory() {
        try {
            categoriesList = new ArrayList<>();
            ResultSet result = statement.executeQuery("SELECT DISTINCT * FROM category");
            Category parent = null;
            while (result.next()) {
                parent = new Category();
                Category category = new Category();
                category.setCategory_id(result.getInt("category_id"));
                category.setName(result.getString("name"));

                parent.setCategory_id(result.getInt("parent_id"));
                category.setParentCategory(parent);
                category.setCategories(new TreeSet<Category>());
                categoriesList.add(category);
            }
            for (int i = 0; i < categoriesList.size(); i++) {
                for (int j = 0; j < categoriesList.size(); j++) {
                    if (categoriesList.get(i).getCategory_id() == categoriesList.get(j).getParentCategory().getCategory_id()) {
                        categoriesList.get(j).setParentCategory(categoriesList.get(i));
                        categoriesList.get(i).getCategories().add(categoriesList.get(j));
                    }
                }
            }
            for (Category categoriesList1 : categoriesList) {
                System.out.println("Category: " + categoriesList1.getName() + ", parent is: " + categoriesList1.getParentCategory().getName() + " id- " + categoriesList1.getParentCategory().getCategory_id());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Category getCategoryByIdForProduct(int categoryId) {
        for (Iterator<Category> iterator = categoriesList.iterator(); iterator.hasNext();) {
            Category next = iterator.next();
            if (next.getCategory_id() == categoryId) {
                return next;
            }
        }
        return null;
    }

}
