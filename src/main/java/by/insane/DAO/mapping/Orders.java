/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO.mapping;

import by.insane.DAO.mapping.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Андрій
 */
@Entity
@Table(name = "orders")
public class Orders implements Serializable, Comparable<Orders> {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "status")
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_ordering")
    private Date date;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<ItemOrders> itemOrders = new HashSet<>(0);

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setItemOrders(Set<ItemOrders> itemOrders) {
        this.itemOrders = itemOrders;
    }

    public int getOrderId() {
        return orderId;
    }

    public Account getAccount() {
        return account;
    }

    public String getStatus() {
        return status;
    }

    public Set<ItemOrders> getItemOrders() {
        return itemOrders;
    }

    @Override
    public int compareTo(Orders o) {
        if (orderId > o.orderId) {
            return 1;
        } else if (orderId < o.orderId) {
            return -1;
        } else {
            return 0;
        }
    }

}
