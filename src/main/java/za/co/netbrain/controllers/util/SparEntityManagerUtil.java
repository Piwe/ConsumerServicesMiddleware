package za.co.netbrain.controllers.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Simphiwe.Twala
 */
public class SparEntityManagerUtil {

    public EntityManagerFactory getEntityManagerFactory() {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("spar_middleware_PU");
        return emf;
        
    }

}
