/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mysql;

import by.insane.DAO.CommentsDAO;
import by.insane.DAO.DAOFactory;
import by.insane.DAO.mapping.*;
import by.insane.gigabyte.util.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;

import by.insane.gigabyte.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Андрій
 */
public class CommentsDAOImpl implements CommentsDAO {

    private Statement statement;

    public CommentsDAOImpl() {
        statement = DataBaseConnection.getInstance().getStatement();
    }

    @Override
    public void addComment(Comments comment) {
        try {
            statement.executeUpdate(
                    "INSERT INTO comments(product_id,account_id, description, add_time) "
                    + "VALUES(" + comment.getProduct().getProduct_id() + ","
                    + comment.getAccount().getAccount_id() + ",'" + comment.getDescription() + "',NOW())");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateComment(Comments comment) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.update(comment);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Comments getCommentById(int comment_id) {
        Comments comment = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        comment = (Comments) session.get(Comments.class, comment_id);
        session.getTransaction().commit();
        session.close();
        return comment;
    }

    @Override
    public Collection getAllComments() {
        List<Comments> comments = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        comments = session.createCriteria(Comments.class).list();
        session.getTransaction().commit();
        for (Comments comments1 : comments) {
            System.out.println(comments1);
        }
        session.close();
        return comments;
    }

    @Override
    public void deleteComment(Comments comment) {
        try {
            statement.executeUpdate("DELETE FROM comments WHERE comments_id = " + comment.getComment_id());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Set<Comments> getCommentsByProductId(int product_id) {
        List<Comments> list = new LinkedList<>();
        ArrayList<Integer> accId = new ArrayList<>();
        ArrayList<Integer> pId = new ArrayList<>();
        try {
            ResultSet result = DataBaseConnection.getInstance().getStatement().executeQuery(
                    "SELECT * FROM comments WHERE product_id = " + product_id);
            while (result.next()) {
                Comments comment = new Comments();
                comment.setComment_id(result.getInt("comments_id"));
                comment.setDescription(result.getString("description"));
                comment.setDate(result.getDate("add_time"));
                accId.add(result.getInt("account_id"));
                pId.add(result.getInt("product_id"));
                list.add(comment);
            }

            Iterator<Comments> it = list.iterator();
            DAOFactory factory = DAOFactory.getFactory(DAOFactory.MY_SQL);
            int i = 0;
            while (it.hasNext()) {
                Comments next = it.next();
                next.setAccount(factory.getAccountDAO().getAccountById(accId.get(i)));
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new TreeSet<>(list);
    }

    @Override
    public void deleteCommentsByProductId(int product_id) {
        try {
            statement.executeUpdate("DELETE FROM comments WHERE product_id = " + product_id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
