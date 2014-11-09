/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.gigabyte;

import by.insane.DAO.DAOFactory;
import by.insane.DAO.mapping.Category;
import java.io.Serializable;
import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author insane
 */
@ManagedBean
@ViewScoped
public class CategorySettingsBean implements Serializable {

    private Collection<Category> categories;
    private String name;
    private Category parent;
    private int categoryId = 0;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    

    public CategorySettingsBean() {
        categories = DAOFactory.getFactory(DAOFactory.MY_SQL).getCategoryDAO().getAllCategories();
    }

    public List<SelectItem> getSelectedItems() {
        List<SelectItem> retVal = new ArrayList<>();
        retVal.add(new SelectItem(-1, "Is main category"));
        for (Category item : categories) {
            retVal.add(new SelectItem(item.getCategory_id(), item.getName()));
        }

        return retVal;
    }

    public Collection<Category> getCategories() {
        return categories;
    }
    
    public String addCategory(){
//        parent  =  DAOFactory.getFactory(DAOFactory.MY_SQL).getCategoryDAO().getCategoryById(categoryId);
        Category newCategory = new Category();
        parent = new Category();
        parent.setCategory_id(categoryId);
        newCategory.setName(name);
        newCategory.setParentCategory(parent);
        System.err.println("parent category is: " + parent.getName());
        DAOFactory.getFactory(DAOFactory.MY_SQL).getCategoryDAO().addCategory(newCategory);
        return "index?faces-redirect=true";
    }

    public void dropCategory(Category category) {
        DAOFactory.getFactory(DAOFactory.MY_SQL).getCategoryDAO().deleteCategory(category);
    }

    public String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

}
