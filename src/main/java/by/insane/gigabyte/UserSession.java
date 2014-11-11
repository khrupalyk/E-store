/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.gigabyte;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.*;
import by.insane.DAO.mapping.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.*;
import by.insane.DAO.DAOFactory;

/**
 *
 * @author insane
 */
@Named("user")
@SessionScoped
public class UserSession implements Serializable {

    private String password;
    private String login;
    private Account account = new Account();
    private Account prevAccount;
    private int id;
    private DataBaseConnection pool;
    private String role = "guest";
    private List<Category> categories;
    private Product product;
    private Product currentProduct;
    private String selectedCategory;
    private DataModel productsModel;
    private String selectedCategoryId;
    private int button_id = 0;
    private Comments comment = new Comments();
    private Cart cart = new Cart();
    private int countProductInPage = 3;
    private int currentCountProduct = countProductInPage;
    private String searchText;
    private List<ItemCart> itemCartGuestUser = new LinkedList<>();
    private boolean registerBeforeOrder = false;
    private DAOFactory factory = DAOFactory.getFactory(DAOFactory.MY_SQL);

    public void setRegisterBeforeOrder(boolean registerBeforeOrder) {
        this.registerBeforeOrder = registerBeforeOrder;
    }

    public String updateCategories(Category category) {
        categories = (List<Category>) factory.getCategoryDAO().getMainCategories();
        return "/index?faces-redirect=true";
    }

    public void setCurrentProduct(Product product) {
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public Set<Images> getImagesForCurrentProduct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String str = request.getParameter("productId");
        productId1 = (str == null || str.trim().equals("")) ? productId1 : Integer.parseInt(str);
        product = factory.getProductDAO().getProductById(productId1);
        return product.getImages();
    }
    private int productId1 = 0;

    public void setProduct(Product p) {
        if (p != null) {
            product = p;
        }
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String goSearch() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        return searchText;
    }

    public void setCurrentCountProduct(int currentCountProduct) {
        this.currentCountProduct = currentCountProduct;
    }

    public int getCurrentCountProduct() {
        return currentCountProduct;
    }

    public int getCountProductInPage() {
        return countProductInPage;
    }

    public void setComment(Comments comment) {
        if (comment != null) {
            this.comment = comment;
        }
    }

    public Comments getComment() {
        return comment;
    }

    public String getButton_id() {
        return "id" + (button_id++);
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        if (selectedCategory != null && !selectedCategory.trim().equals("")) {
            try {
                Integer.parseInt(selectedCategory);
                this.selectedCategory = selectedCategory;
            } catch (Exception e) {
            }

        }
    }

    public Collection<Orders> getOrders() {
        return factory.getOrdersDAO().getOrdersByAccount(account);
    }

    public String check() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = request.getSession(false);
            Statement statement = pool.getStatement();

            ResultSet result = statement.executeQuery("SELECT account_id, login, role, password FROM account where login = '"
                    + login + "' and password = CONCAT(md5('" + password + "'),md5('" + login + "'));");
            System.out.println("login: " + login + "  passowrd: " + password);
            if (result.next()) {

                if (session != null) {
                    session.setAttribute("authentication", "true");
                    id = result.getInt("account_id");
                    System.out.println("testsss");

                    account = factory.getAccountDAO().getAccountById(id);
                    System.out.println("testsss1");
                    l = account.toArrayList();
                    prevAccount = account;
                    role = account.getRole();

                    if (registerBeforeOrder) {
                        System.out.println("registerBeforeOrder in check: " + registerBeforeOrder);
                        registerBeforeOrder = false;
                    } else {
                        cart = factory.getCartDAO().getCartByAccount(account);
                    }

                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "/index?faces-redirect=true";
    }

    public UserSession() {
        pool = DataBaseConnection.getInstance();
        account.setRole("guest");
        account.setAccount_id(-1);
        categories = (List<Category>) factory.getCategoryDAO().getMainCategories();
        System.out.println("Size: " + categories.size());

    }

    public UserSession(String password, String login) {
        this.password = password;
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean getAuthentication() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute("authentication") != null) {
                return Boolean.valueOf((String) session.getAttribute("authentication"));
            }
        }
        return false;
    }

    public void logout() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("authentication", "false");
        }
        role = "guest";
        account = new Account();
        prevAccount = null;
        account.setAccount_id(-1);
        cart.setAccount(null);
        cart.setAccountId(0);
        cart.getItemCart().clear();
        indexRedirect();
    }

    public String register() {
        account.setRole("user");
        factory.getAccountDAO().registerUser(account);
//        account.setDate(new Date());
//        String pass = account.getPassword();
//        System.out.println("new password: " + getMD5(pass) + getMD5(account.getLogin()));
//        account.setPassword(getMD5(pass) + getMD5(account.getLogin()));
//        factory.getAccountDAO().addAccount(account);
//        System.out.println("Account id: " + account.getAccount_id());
//        cart.setAccount(account);
//        cart.setAccountId(account.getAccount_id());
//        factory.getCartDAO().addCart(cart);
        if (registerBeforeOrder) {
//            registerBeforeOrder = false;
//            System.out.println("registerBeforeOrder: " + registerBeforeOrder);
            login = account.getLogin();
            password = account.getPassword();
            check();
            order();
//
        }
        return "/index?faces-redirect=true";
    }

    public Account getAccount() {
        return account;
    }

    public void accountRedirect() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/GigaByte/faces/user/account.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void categoryRedirect() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            HttpServletRequest request
                    = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response
                    = (HttpServletResponse) context.getExternalContext().getResponse();

            System.out.println("kadn anfkan ka nk           " + request.getParameter("username"));
            response.sendRedirect("category");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.responseComplete();
        }
    }

    public void indexRedirect() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/GigaByte/faces/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, String> getMapUser() {
        Account account = factory.getAccountDAO().getAccountById(id);
        return account.toMap();
    }

    public String getClassForAdminPanel() {
        return role.equals("admin") ? "show" : "hide";
    }

    public String getRole() {
        return role;
    }

    public boolean isShowAdminPanel() {
        return "admin".equals(role);
    }

    public String getLastRole(String role) {
        return "admin".equals(role) ? "user" : "admin";
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Product getProductById(String id) {
        if (id == null || id.trim().equals("")) {
            return null;
        }
        product = factory.getProductDAO().getProductById(Integer.parseInt(id));
        currentProduct = product;
        return product;
    }

    public String[] getNamesCategories() {
        String[] tmp = new String[categories.size()];

        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = ((Category) categories.toArray()[i]).getName();
        }
        return tmp;
    }

    public String[] getNamesSubcategories() {
        if (selected == null || "".equals(selected.trim())) {
            selected = "OS";
        }

        String[] tmpArr = null;

        List<Category> categories = getCategories();
        for (int i = 0; i < getNamesCategories().length; i++) {
            if (categories.get(i).getName().equals(selected)) {
                Set<Category> subcategories = categories.get(i).getCategories();
                Iterator<Category> it = subcategories.iterator();
                tmpArr = new String[subcategories.size()];
                int j = 0;
                while (it.hasNext()) {
                    tmpArr[j++] = it.next().getName();
                }

            }
        }

        return tmpArr;

    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        if (selected == null || selected.equals("")) {
            return;
        }
        this.selected = selected;
    }

    public String getResult() {
        return result;
    }
    private String selected;
    private String result;

    synchronized public List<Product> getAllProduct(String id) {

        if (id == null || id.trim().equals("")) {
            return null;
        }
        int id2 = 0;
        try {
            id2 = Integer.parseInt(id);
        } catch (Exception e) {
            return null;
        }
        Category subcategories = factory.getCategoryDAO().getCategoryById(id2);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String parameter = request.getParameter("product");
        Set<Product> products = new TreeSet<>();
        if (parameter != null && parameter.equalsIgnoreCase("all")) {
            Set<Category> categories1 = subcategories.getCategories();
            for (Category category : categories1) {
                products.addAll(category.getProducts());
            }
        } else {
            products = subcategories.getProducts();
        }

        List<Product> list = new ArrayList<>(products);
        Collections.sort(list);
        return list;
    }

    synchronized public List<Product> getNextPage(String id) {
        List<Product> allProduct = getAllProduct(id);

        if (allProduct == null) {
            return null;
        }

        if (currentCountProduct >= allProduct.size()) {
            return allProduct;
        }

        return allProduct.subList(0, currentCountProduct);
    }

    public void nextPageListener() {
        currentCountProduct += countProductInPage;
    }

    public Product getProductByIdInRequestParameter() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String param = request.getParameter("productId");
        String att = (String) request.getAttribute("productId");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String string = entry.getKey();
            String[] strings = entry.getValue();
            System.out.println("Param: " + string);

        }
        System.out.println("Attribute: " + att);
        if (param == null || param.trim().equals("")) {
            return null;
        }
        return factory.getProductDAO().getProductById(Integer.parseInt(param));
    }

    synchronized public Collection<Account> getAllAccounts() {
        Collection<Account> allAccounts = factory.getAccountDAO().getAllAccounts();
        return allAccounts;
    }

    synchronized public String dropProduct(Product product) throws IOException {
        if (product == null) {
        } else {
            FTPLoader ftp = new FTPLoader();
            ftp.dropImages(product);
            factory.getProductDAO().deleteProduct(product);
        }

        return "/index?faces-redirect=true";
    }

    synchronized public String getSelectedCategoryId() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("Params: " + request.getParameter("categoryId"));
        if (request.getParameter("categoryId") != null) {
            selectedCategoryId = (String) request.getParameter("categoryId");
        }
        return selectedCategoryId;
    }

    public String redirectCart() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/GigaByte/faces/cart.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/cart";
    }

    public void addComment() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String product_id = request.getParameter("productId");

        if (product_id == null || product_id.trim().equals("")) {
            return;
        }

        comment.setAccount(account);
        comment.setProduct(factory.getProductDAO().getProductById(Integer.parseInt(product_id)));
        Date date = new Date();
        comment.setDate(date);
        factory.getCommentsDAO().addComment(comment);
        comment.setDescription("");

    }

    public static String nl2br(String string) {
        return (string != null) ? string.replace("\n", "<br />") : null;
    }

    public Collection<Comments> getCommentsByProductId() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("productId") == null) {
            return null;
        }
        return factory.getCommentsDAO().getCommentsByProductId(Integer.parseInt(request.getParameter("productId")));
    }

    public Cart getCart() {
        return cart;
    }

    public void addToCart() {
        String product_id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("product_id");
        if (product_id == null || product_id.trim().equals("")) {
            return;
        }
        System.out.println("product_id + " + product_id);
        if (account != null && account.getAccount_id() != -1) {
            ItemCart itemCart = new ItemCart();
            itemCart.setCount(1);
            itemCart.setCart(cart);
            itemCart.setProduct(factory.getProductDAO().getProductById(Integer.parseInt(product_id)));
            factory.getItemCartDAO().addItemCart(itemCart);
            cart.getItemCart().add(itemCart);
        } else {
            ItemCart itemCart = new ItemCart();
            itemCart.setCount(1);
            itemCart.setProduct(factory.getProductDAO().getProductById(Integer.parseInt(product_id)));
            cart.getItemCart().add(itemCart);
        }
        System.out.println("Aded to cart!");
    }

    public void countProductInCartListener(Cart cart) {
    }

    public String order() {

        if (!getAuthentication()) {
            System.out.println("Authentication awd");
            registerBeforeOrder = true;
//            try {
//                FacesContext.getCurrentInstance().getExternalContext().redirect("/GigaByte/login?path=create&faces-redirect=true");
//            } catch (IOException ex) {
//                Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
//            }
            System.out.println("Redirect to register!");
            return "/login?path=create&faces-redirect=true";
        }
        Orders order = new Orders();
        order.setAccount(account);
        order.setStatus("new");
        order.setDate(new Date());
        factory.getOrdersDAO().addOrder(order);
        for (ItemCart cart : cart.getItemCart()) {
            ItemOrders item = new ItemOrders();
            item.setCount(cart.getCount());
            item.setProduct(cart.getProduct());
            item.setOrder(order);
            factory.getItemOrdersDAO().addItemOrders(item);
        }

        for (ItemCart itemOrders : cart.getItemCart()) {
            factory.getItemCartDAO().deleteItemCart(itemOrders);
        }
        cart.getItemCart().clear();
        return "/index?faces-redirect=true";
    }

    public void deleteRowFromCart(ItemCart itemCart) {
        if (account != null && account.getAccount_id() != -1) {
            factory.getItemCartDAO().deleteItemCart(itemCart);
        }
        if (cart != null) {
            cart.getItemCart().remove(itemCart);
        }

    }

    public void dropUser(Account account) {
//        by.insane.DAO.Factory.getInstance().getCartDAO().deleteCartByAccount(account);
        factory.getAccountDAO().deleteAccount(account);
    }

    public void updateUsersRole(Collection<Account> account) {
        for (Account account1 : account) {
            System.out.println("awdawdawd       " + account1.getFname() + " role " + account1.getRole());
        }
        Map<String, String> re = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        for (Map.Entry<String, String> entry : re.entrySet()) {
            String s1 = entry.getKey();
            String s2 = entry.getValue();
            System.out.println("key " + s1 + "  value " + s2);

        }

    }

    public void dropComment(Comments comment) {
        System.out.println("Comment dropped! ");
        factory.getCommentsDAO().deleteComment(comment);
    }

    //start update account
    private List<MyEntry<String, String>> l;

    public void updateAccountInfo(MyEntry<String, String> item) {
        System.out.println("saved " + item.getKey() + " " + item.getNewValue());
        boolean isPass = false;
        switch (item.getKey()) {
            case "First name":
                prevAccount.setFname(item.getNewValue());
                break;
            case "Last name":
                prevAccount.setLname(item.getNewValue());
                break;
            case "Phone":
                prevAccount.setPhone(item.getNewValue());
                break;
            case "Address":
                prevAccount.setAddress(item.getNewValue());
                break;
            case "Email":
                prevAccount.setEmail(item.getNewValue());
                break;
            case "Password":
                System.out.println("New pass: " + item.getNewValue());
                prevAccount.setPassword(getMD5(item.getNewValue()) + getMD5(login));
                isPass = true;
                break;

        }
        item.setEditable(false);
        save(isPass);
    }

    public void save(boolean isPass) {
        if (isPass) {
            factory.getAccountDAO().updateAccountPassword(account);
        }
        System.out.println(" dddd " + prevAccount.getAddress());
        account = prevAccount;
        factory.getAccountDAO().updateAccount(account);
        account.setPassword("");
        l = account.toArrayList();

    }

    public void cancel(MyEntry<String, String> item) {
        item.setEditable(false);
        System.out.println(item.getKey() + " editable " + item.isEditable() + " awd");
    }

    public void listener(MyEntry<String, String> m) {

        for (MyEntry<String, String> next : l) {
            next.setEditable(false);
        }
        m.setEditable(true);
        System.out.println(m.getKey() + " editable " + m.isEditable());
    }

    public List<MyEntry<String, String>> getArrayListUser() {

        return l;
    }

    //end update account
    public String getCurrentPage(String path) {
        return (path == null) ? "unknown" : path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
    }

    public boolean isShowLeftBanners(String path) {
        String currentPage = getCurrentPage(path);
        switch (currentPage) {
            case "account":
                return false;
            case "cart":
                return false;
            case "orders":
                return false;
        }
        return true;
    }

    public Orders getOrderById() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Integer orderId = Integer.valueOf(request.getParameter("view"));
        System.out.println("Integer: " + orderId);

        if (orderId != null) {
            return factory.getOrdersDAO().getOrderById(orderId);
        }

        return null;
    }

    public Collection<Product> getAllProduct() {
        return DAOFactory.getFactory(DAOFactory.MY_SQL).getProductDAO().getAllProducts();
    }

    public Collection<Product> getFindsProduct() {
        return (!searchText.trim().equals("")) ? factory.getProductDAO().getProductsLikeName(searchText) : null;
    }

    public String getAbsolutePathForImage(Product product) {
        if (product == null && this.product == null) {
            return null;
        }
        Set<Images> images = null;
        if (product == null) {
            images = this.product.getImages();
        } else {
            images = product.getImages();
        }

        if (images == null) {
            return "resources/images/no-image-available.jpg";
        }
        if (images.iterator().hasNext()) {
            Images image = images.iterator().next();
            return image.getPath() + image.getName();
        } else {
            return "resources/images/no-image-available.jpg";
        }

    }

    public String getMainImage(Product product) {
        if (product == null && this.product == null) {
            return null;
        }
        Images image;
        if (product == null) {
            image = this.product.getMainImage();
        } else {
            image = product.getMainImage();
        }
        if (image == null || image.getImagesId() == 0) {
            return "resources/images/no-image-available.jpg";
        }
        return image.getPath() + image.getName();
    }

    public Collection<Orders> getAllOrders() {
        return DAOFactory.getFactory(DAOFactory.MY_SQL).getOrdersDAO().getAllOrders();
    }

    public void deleteOrder(Orders order) {
        for (ItemOrders item : order.getItemOrders()) {
            factory.getItemOrdersDAO().deleteItemOrders(item);
        }
        factory.getOrdersDAO().deleteOrder(order);
    }

    public Set<Features> getSubFeatures(Product product) {

        if (product == null) {
            return null;
        }
        ArrayList<Features> set = new ArrayList<>(product.getFeatures());
        int n = 5;
        if (set.size() <= 5) {
            n = set.size();
        }
        return new TreeSet<>(set.subList(0, n));
    }

    public boolean isContainedInBasket(Product product) {
        Product tmp = product;
        if (tmp == null) {
            tmp = this.product;
        }
        for (ItemCart item : cart.getItemCart()) {

            if (item.getProduct().equals(tmp)) {
                return true;
            }
        }
        return false;
    }

    public void dropImage(Images image) throws IOException {
        if (image == null) {
            System.out.println("Drop image is call1!");
            return;
        }
        FTPLoader loader = new FTPLoader();
        loader.dropImage(image);
        factory.getImagesDAO().deleteImage(image);
        product = factory.getProductDAO().getProductById(product.getProduct_id());
        System.out.println("Drop image is call!");
    }

    public Collection<Images> getImagesByProduct() {
        return (product == null) ? null : factory.getProductDAO().getProductById(product.getProduct_id()).getImages();
    }
    private int productId;

    public void setProductId(int awd) {
        if (awd > 0) {
            productId = awd;
        }
        System.out.println("ProductIDIS: " + productId);
    }

    private int getProductId() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String productId = request.getParameter("productId");
        if (productId == null || productId.trim().equals("")) {
            return 0;
        }
        return Integer.parseInt(productId);
    }

    public String getMD5(String string) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return string;
        }
        md.update(string.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
