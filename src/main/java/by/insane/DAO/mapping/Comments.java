/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mapping;

import by.insane.DAO.mapping.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Андрій
 */
@Entity
@Table(name = "comments")
public class Comments implements Serializable, Comparable<Comments>, Comparator<Comments> {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "comments_id")
    private int comment_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "add_time")
    private Date date;

    public int getComment_id() {
        return comment_id;
    }

    public Account getAccount() {
        return account;
    }

    public Product getProduct() {
        return product;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Comments o) {
        if (comment_id > o.comment_id) {
            return 1;
        } else if (comment_id < o.comment_id) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int compare(Comments o1, Comments o2) {
        if (o1.getComment_id() > o2.getComment_id()) {
            return 1;
        } else if (o1.getComment_id() < o2.getComment_id()) {
            return -1;
        } else {
            return 0;
        }
    }

}
