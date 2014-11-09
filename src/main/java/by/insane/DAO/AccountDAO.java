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
 * @author Andriy
 */
public interface AccountDAO {

    public void addAccount(Account account);

    public void updateAccount(Account account);

    public Account getAccountById(int account_id);

    public Collection<Account> getAllAccounts();

    public void deleteAccount(Account account);
    
    public void updateAccountPassword(Account account);
    
    public void registerUser(Account account);
}
