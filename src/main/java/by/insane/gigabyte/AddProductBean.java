/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.gigabyte;

import by.insane.DAO.DAOFactory;
import by.insane.DAO.mapping.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Model;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 *
 * @author Андрій
 */
@ManagedBean
@ViewScoped
@Model
public class AddProductBean implements Serializable {

    private Product product = new Product();
    private int mainCategoryId = 1;
    private int categoryId = 1;
    private static final int COUNT_FILES = 6;
    private List<Part> files = new ArrayList<>(COUNT_FILES);
    private Part mainPhoto;
    private String features;
    private List<Integer> categoriesID = new ArrayList<>();
    private int i = -1;
    private Category category;
    private int myCategoryId;
    private static int productId = 0;

    public int getMyCategoryId() {
        return myCategoryId;
    }

    public void setMyCategoryId(int myCategoryId) {
        this.myCategoryId = myCategoryId;
    }

    public AddProductBean() {
        for (int i = 0; i < COUNT_FILES; i++) {
            Part part = null;
            files.add(part);
        }
        for (Category c : DAOFactory.getFactory(DAOFactory.MY_SQL).getCategoryDAO().getMainCategories()) {
            categoriesID.add(0);
        }
    }

    public void setCategoryID(int i) {
        System.out.println("Set category id: " + i);
        categoriesID.add(i);
    }

    public int getCategoryID() {
        i++;
        return categoriesID.get(i);

    }

    public List<Part> getFiles() {
        System.out.println("Size array list files: " + files.size());
        return files;
    }

    public List<Part> getFiles(int i) {
        System.out.println("Size array list files: " + files.size());
        return files;
    }

    public void setFiles(List<Part> files) {
        this.files = files;
    }

    public String addProduct(boolean isAdd) {
        category = DAOFactory.getFactory(DAOFactory.MY_SQL).getCategoryDAO().getCategoryById(myCategoryId);
        product.setCategory(category);
//        product.setDate(new Date());
        try {
            if (isAdd) {

                Images i = new Images();
                i.setImagesId(1);
                product.setMainImage(i);
                DAOFactory.getFactory(DAOFactory.MY_SQL).getProductDAO().addProduct(product);
                Images image = uploadImages(mainPhoto);
                if (image != null) {
                    product.setMainImage(image);
                    DAOFactory.getFactory(DAOFactory.MY_SQL).getProductDAO().updateProduct(product);
                }

            } else {
                product.setProduct_id(productId);
                Images image = uploadImages(mainPhoto);
                if (image == null) {
                    System.err.println("image == null");
                    image = DAOFactory.getFactory(DAOFactory.MY_SQL).getProductDAO().getProductById(productId).getMainImage();
                } else {
                    System.err.println("image != null");
                }
                product.setMainImage(image);
                DAOFactory.getFactory(DAOFactory.MY_SQL).getProductDAO().updateProduct(product);
                DAOFactory.getFactory(DAOFactory.MY_SQL).getFeaturesDAO().deleteFeaturesByProduct(product);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        uploadListener();
        addFeatures();
        return "/index?faces-redirect=true";
    }

    public List<SelectItem> getSelectedItems() {
        List<SelectItem> retVal = new ArrayList<>();
        for (Category item : DAOFactory.getFactory(DAOFactory.MY_SQL).getCategoryDAO().getAllCategories()) {
            retVal.add(new SelectItem(item.getCategory_id(), item.getName()));
        }

        return retVal;
    }

    public void addFeatures() {
        String[] split = features.split("\n");
        System.out.println("Features: ");
        for (String string : split) {
            int indexOf = string.indexOf("=");
            if (indexOf == -1) {
                continue;
            }
            Features feature = new Features();
            feature.setProduct(product);
            feature.setName(string.substring(0, indexOf).replaceAll("\n", "").trim());
            feature.setValue(string.substring(indexOf + 1, string.length()).replaceAll("\n", "").trim());
            DAOFactory.getFactory(DAOFactory.MY_SQL).getFeaturesDAO().addFeatures(feature);
        }
    }

    public List<SelectItem> getSelectedItemSubcategories(Category category) {
        List<SelectItem> list = new LinkedList<>();
        for (Category subcategory : category.getCategories()) {
            SelectItem selectItem = new SelectItem(subcategory.getCategory_id(), subcategory.getName());
            list.add(selectItem);
        }
        return list;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Product getProduct() {
        return product;
    }

    public int getMainCategoryId() {

        return mainCategoryId;
    }

    public void setProduct(Product product) {
        if (product == null) {
            return;
        }
        if (product.getProduct_id() != 0) {
            productId = product.getProduct_id();
        } else {
            return;
        }
        features = "";
        for (Features feature : product.getFeatures()) {
            features += feature.getName() + " = " + feature.getValue() + "\n";
        }

        this.product = product;
    }

    public void setMainCategoryId(int mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getFeatures() {
        return features;
    }

    public void setMainPhoto(Part mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public Part getMainPhoto() {
        return mainPhoto;
    }

    public void uploadListener() {
        try {
            HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            for (Part part : r.getParts()) {

                if (part != null && part.getSubmittedFileName() != null) {
                    if (mainPhoto != null && mainPhoto.getSubmittedFileName() != null && mainPhoto.getSubmittedFileName().equals(part.getSubmittedFileName())) {
                        continue;
                    }
                    FileOutputStream fos = null;
                    try {
                        uploadImages(part);
                    } catch (IOException ex) {
                    }
                } else {
                    System.out.println("Part is null!");
                }
            }
//            r.getParts().clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Images uploadImages(Part part) throws IOException {
        if (part == null || part.getSubmittedFileName() == null) {
            return null;
        }
        FileOutputStream fos = null;
        String submittedFileName = part.getSubmittedFileName();
        byte[] results = new byte[(int) part.getSize()];
        InputStream in = part.getInputStream();
        in.read(results);
        String newFIleName;
        if (part.getSubmittedFileName().trim().equals("")) {
            return null;
        }

        Images image = new Images();
        image.setProduct(product);
        image.setPath("resources/");
        DAOFactory.getFactory(DAOFactory.MY_SQL).getImagesDAO().addImage(image);
        newFIleName = image.getImagesId() + submittedFileName.substring(submittedFileName.lastIndexOf("."), submittedFileName.length());

        File f = new File(newFIleName);
        FTPLoader ftpUpload = new FTPLoader();
        String serverURL = ftpUpload.getServer();
        image.setName(newFIleName);
        image.setPath("http://" + serverURL.substring(serverURL.indexOf(".") + 1, serverURL.length()) + "/");
        DAOFactory.getFactory(DAOFactory.MY_SQL).getImagesDAO().updateImage(image);

        fos = new FileOutputStream(f);
        fos.write(results);

        ftpUpload.uploadToFtp(f, newFIleName);
        fos.close();
        return image;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

}
