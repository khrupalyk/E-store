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
 * @author Andriy
 */
public interface ProductDAO {

    public void addProduct(Product product);

    public void updateProduct(Product product);

    public Product getProductById(int product_id);

    public Collection<Product> getAllProducts();

    public Collection<Product> getProductsLikeName(String likeName);

    public void deleteProduct(Product product);

    public Set<Product> getProductsInCategory(Category category);
}
