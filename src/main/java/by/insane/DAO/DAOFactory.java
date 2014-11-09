/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO;

import java.io.Serializable;

public abstract class DAOFactory implements Serializable{

    public static final int MY_SQL = 1;
    public static final int ORM = 2;
    private static MySQLDAOFactory mySQLDAOFactory = null;
    private static ORMFactory oRMFactory = null;

    public abstract ImagesDAO getImagesDAO();

    public abstract ItemCartDAO getItemCartDAO();

    public abstract FeaturesDAO getFeaturesDAO();

    public abstract OrdersDAO getOrdersDAO();

    public abstract ItemOrdersDAO getItemOrdersDAO();

    public abstract ProductDAO getProductDAO();

    public abstract CategoryDAO getCategoryDAO();

    public abstract AccountDAO getAccountDAO();

    public abstract CommentsDAO getCommentsDAO();

    public abstract CartDAO getCartDAO();

    public static DAOFactory getFactory(int type) {

        switch (type) {
            case MY_SQL:

                return (mySQLDAOFactory == null) ? (mySQLDAOFactory = new MySQLDAOFactory()) : mySQLDAOFactory;
            case ORM:
                return (oRMFactory == null) ? (oRMFactory = new ORMFactory()) : oRMFactory;

        }
        return null;
    }
}
