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
public interface ItemCartDAO {

    public void addItemCart(ItemCart itemCart);

    public void updateItemCart(ItemCart itemCart);

    public ItemCart getItemCartById(int itemCartId);

    public Collection<ItemCart> getAllItemCart();

    public void deleteItemCart(ItemCart itemCart);
}
