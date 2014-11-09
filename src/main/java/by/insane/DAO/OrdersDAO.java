/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO;

import java.util.Collection;
import by.insane.DAO.mapping.*;

/**
 *
 * @author Андрій
 */
public interface OrdersDAO {

    public void addOrder(Orders orders);
    
    public void addOrders(Collection<Orders> orders);
    
    public void updateOrders(Collection<Orders> orders);

    public void updateOrder(Orders orders);

    public Orders getOrderById(int orderId);

    public Collection<Orders> getAllOrders();

    public Collection<Orders> getOrdersByAccount(Account account);

    public void deleteOrder(Orders orders);
}
