/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.DAO;

import java.util.Collection;
import java.util.List;
import by.insane.DAO.mapping.*;

/**
 *
 * @author Andriy
 */
public interface CategoryDAO {

    public void addCategory(Category category);

    public void updateCategory(Category category);

    public Category getCategoryById(int category_id);

    public Collection<Category> getAllCategories();

    public Collection<Category> getMainCategories();

    public void deleteCategory(Category category);

    public Category getCategoryByName(String name);
    
    public Category getCategoryByIdForProduct(int categoryId);
}
