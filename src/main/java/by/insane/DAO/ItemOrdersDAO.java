/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO;

import java.util.Collection;
import java.util.Set;
import by.insane.DAO.mapping.*;

/**
 *
 * @author Андрій
 */
public interface ItemOrdersDAO {

    public void addItemOrders(ItemOrders itemOrders);

    public void updateItemOrders(ItemOrders itemOrders);

    public ItemOrders getItemOrdersId(int itemOrdersId);

    public Collection<ItemOrders> getAllItemOrders();

    public void deleteItemOrders(ItemOrders itemOrders);
    
    public Set<ItemOrders> getItemOrdersByOrders(Orders order);
}
