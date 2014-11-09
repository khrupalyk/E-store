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
public interface FeaturesDAO {

    public void addFeatures(Features features);

    public void updateFeatures(Features features);

    public Features getFeaturesById(int featuresId);

    public Collection<Features> getAllFeatures();

    public void deleteFeatures(Features features);

    public void deleteFeaturesByProduct(Product product);
    
    public Set<Features> getFeaturesByProduct(Product product);

}
