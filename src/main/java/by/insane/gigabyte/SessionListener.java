/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.insane.gigabyte;

import by.insane.DAO.DAOFactory;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author insane
 */
public class SessionListener implements HttpSessionListener {

    private int count;

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        ++count;
        System.out.println("Session created..");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        count--;
        System.out.println("Session destroyed..");
        if(count == 0){
            System.out.println("Memory size: " + Runtime.getRuntime().freeMemory());
            DAOFactory.getFactory(DAOFactory.MY_SQL).close();
            DataBaseConnection.getInstance().close();
            DAOFactory.closeAll();
            Runtime.getRuntime().gc();
            System.out.println("Memory size after gc: " + Runtime.getRuntime().freeMemory());
        }
    }

}
