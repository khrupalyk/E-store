/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO;

import by.insane.DAO.mysql.AccountDAOImpl;
import by.insane.DAO.mysql.CartDAOImpl;
import by.insane.DAO.mysql.CategoryDAOImpl;
import by.insane.DAO.mysql.CommentsDAOImpl;
import by.insane.DAO.mysql.FeaturesDAOImpl;
import by.insane.DAO.mysql.ImagesDAOImpl;
import by.insane.DAO.mysql.ItemCartDAOImpl;
import by.insane.DAO.mysql.ItemOrdersDAOImpl;
import by.insane.DAO.mysql.OrdersDAOImpl;
import by.insane.DAO.mysql.ProductDAOImpl;

/**
 *
 * @author insane
 */
import java.io.Serializable;

public class MySQLDAOFactory extends DAOFactory implements Serializable {

    private static ProductDAO productDAO = null;
    private static CategoryDAO categoryDAO = null;
    private static AccountDAO accountDAO = null;
    private static CommentsDAO commentsDAO = null;
    private static CartDAO cartDAO = null;
    private static ItemOrdersDAO itemOrdersDAO = null;
    private static OrdersDAO ordersDAO = null;
    private static ImagesDAO ImagesDAO = null;
    private static ItemCartDAO itemCartDAO = null;
    private static FeaturesDAO featuresDAO = null;

    @Override
    public ImagesDAO getImagesDAO() {
        if (ImagesDAO == null) {
            ImagesDAO = new ImagesDAOImpl();
        }
        return ImagesDAO;
    }

    @Override
    public ItemCartDAO getItemCartDAO() {
        if (itemCartDAO == null) {
            itemCartDAO = new ItemCartDAOImpl();
        }
        return itemCartDAO;
    }

    @Override
    public FeaturesDAO getFeaturesDAO() {
        if (featuresDAO == null) {
            featuresDAO = new FeaturesDAOImpl();
        }
        return featuresDAO;
    }

    @Override
    public OrdersDAO getOrdersDAO() {
        if (ordersDAO == null) {
            ordersDAO = new OrdersDAOImpl();
        }
        return ordersDAO;
    }

    @Override
    public ItemOrdersDAO getItemOrdersDAO() {
        if (itemOrdersDAO == null) {
            itemOrdersDAO = new ItemOrdersDAOImpl();
        }
        return itemOrdersDAO;
    }

    @Override
    public ProductDAO getProductDAO() {
        if (productDAO == null) {
            productDAO = new ProductDAOImpl();
        }
        return productDAO;
    }

    @Override
    public CategoryDAO getCategoryDAO() {
        if (categoryDAO == null) {
            categoryDAO = new CategoryDAOImpl();
        }
        return categoryDAO;
    }

    @Override
    public AccountDAO getAccountDAO() {
        if (accountDAO == null) {
            accountDAO = new AccountDAOImpl();
        }
        return accountDAO;
    }

    @Override
    public CommentsDAO getCommentsDAO() {
        if (commentsDAO == null) {
            commentsDAO = new CommentsDAOImpl();
        }
        return commentsDAO;
    }

    @Override
    public CartDAO getCartDAO() {
        if (cartDAO == null) {
            cartDAO = new CartDAOImpl();
        }
        return cartDAO;
    }

    public void close() {
        productDAO = null;
        categoryDAO = null;
        accountDAO = null;
        commentsDAO = null;
        cartDAO = null;
        itemOrdersDAO = null;
        ordersDAO = null;
        ImagesDAO = null;
        itemCartDAO = null;
        featuresDAO = null;

    }
}
