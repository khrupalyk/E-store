/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.gigabyte;

import by.insane.DAO.AccountDAO;
import by.insane.DAO.DAOFactory;
import by.insane.DAO.mapping.*;
import java.io.Serializable;
import java.util.*;
import javax.faces.bean.*;
import javax.faces.model.SelectItem;

/**
 *
 * @author Андрій
 */
@ManagedBean
@ViewScoped
public class UpdateUsersRoleBean implements Serializable{

    private LinkedList<Account> accounts = null;

    public UpdateUsersRoleBean() {
        accounts = new LinkedList<>(DAOFactory.getFactory(DAOFactory.MY_SQL).getAccountDAO().getAllAccounts());
        System.out.println("Account size awd: " + accounts.size());
    } 

    public LinkedList<Account> getAccounts() {
        
        return accounts;
    }

    public void setAccounts(LinkedList<Account> accounts) {
        this.accounts = accounts;
    }

    public void updateAccounts() {

        for (Account account : accounts) {
            DAOFactory.getFactory(DAOFactory.MY_SQL).getAccountDAO().updateAccount(account);
        }
    }
    
    public void changeListener(Account account){
        int i = 0;
        for (Account acc : accounts) {
            if(acc.getAccount_id() == account.getAccount_id()){
                accounts.set(i, account);
                break;
            }
            i++;
        }
    }

    
}
