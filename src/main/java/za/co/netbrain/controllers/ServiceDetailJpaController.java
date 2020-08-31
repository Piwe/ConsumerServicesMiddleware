package za.co.netbrain.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import za.co.netbrain.controllers.exceptions.NonexistentEntityException;
import za.co.netbrain.entities.ServiceDetail;
import za.co.netbrain.entities.Serviceprovider;
import za.co.netbrain.entities.Services;

/**
 *
 * @author Simphiwe.Twala
 */
public class ServiceDetailJpaController implements Serializable {

    public ServiceDetailJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ServiceDetail serviceDetail) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Serviceprovider serviceproviderFk = serviceDetail.getServiceproviderFk();
            if (serviceproviderFk != null) {
                serviceproviderFk = em.getReference(serviceproviderFk.getClass(), serviceproviderFk.getServiceproviderid());
                serviceDetail.setServiceproviderFk(serviceproviderFk);
            }
            Services servicesFk = serviceDetail.getServicesFk();
            if (servicesFk != null) {
                servicesFk = em.getReference(servicesFk.getClass(), servicesFk.getServiceid());
                serviceDetail.setServicesFk(servicesFk);
            }
            em.persist(serviceDetail);
            if (serviceproviderFk != null) {
                serviceproviderFk.getServiceDetailCollection().add(serviceDetail);
                serviceproviderFk = em.merge(serviceproviderFk);
            }
            if (servicesFk != null) {
                servicesFk.getServiceDetailCollection().add(serviceDetail);
                servicesFk = em.merge(servicesFk);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ServiceDetail serviceDetail) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServiceDetail persistentServiceDetail = em.find(ServiceDetail.class, serviceDetail.getServicedetailid());
            Serviceprovider serviceproviderFkOld = persistentServiceDetail.getServiceproviderFk();
            Serviceprovider serviceproviderFkNew = serviceDetail.getServiceproviderFk();
            Services servicesFkOld = persistentServiceDetail.getServicesFk();
            Services servicesFkNew = serviceDetail.getServicesFk();
            if (serviceproviderFkNew != null) {
                serviceproviderFkNew = em.getReference(serviceproviderFkNew.getClass(), serviceproviderFkNew.getServiceproviderid());
                serviceDetail.setServiceproviderFk(serviceproviderFkNew);
            }
            if (servicesFkNew != null) {
                servicesFkNew = em.getReference(servicesFkNew.getClass(), servicesFkNew.getServiceid());
                serviceDetail.setServicesFk(servicesFkNew);
            }
            serviceDetail = em.merge(serviceDetail);
            if (serviceproviderFkOld != null && !serviceproviderFkOld.equals(serviceproviderFkNew)) {
                serviceproviderFkOld.getServiceDetailCollection().remove(serviceDetail);
                serviceproviderFkOld = em.merge(serviceproviderFkOld);
            }
            if (serviceproviderFkNew != null && !serviceproviderFkNew.equals(serviceproviderFkOld)) {
                serviceproviderFkNew.getServiceDetailCollection().add(serviceDetail);
                serviceproviderFkNew = em.merge(serviceproviderFkNew);
            }
            if (servicesFkOld != null && !servicesFkOld.equals(servicesFkNew)) {
                servicesFkOld.getServiceDetailCollection().remove(serviceDetail);
                servicesFkOld = em.merge(servicesFkOld);
            }
            if (servicesFkNew != null && !servicesFkNew.equals(servicesFkOld)) {
                servicesFkNew.getServiceDetailCollection().add(serviceDetail);
                servicesFkNew = em.merge(servicesFkNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = serviceDetail.getServicedetailid();
                if (findServiceDetail(id) == null) {
                    throw new NonexistentEntityException("The serviceDetail with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServiceDetail serviceDetail;
            try {
                serviceDetail = em.getReference(ServiceDetail.class, id);
                serviceDetail.getServicedetailid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The serviceDetail with id " + id + " no longer exists.", enfe);
            }
            Serviceprovider serviceproviderFk = serviceDetail.getServiceproviderFk();
            if (serviceproviderFk != null) {
                serviceproviderFk.getServiceDetailCollection().remove(serviceDetail);
                serviceproviderFk = em.merge(serviceproviderFk);
            }
            Services servicesFk = serviceDetail.getServicesFk();
            if (servicesFk != null) {
                servicesFk.getServiceDetailCollection().remove(serviceDetail);
                servicesFk = em.merge(servicesFk);
            }
            em.remove(serviceDetail);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ServiceDetail> findServiceDetailEntities() {
        return findServiceDetailEntities(true, -1, -1);
    }

    public List<ServiceDetail> findServiceDetailEntities(int maxResults, int firstResult) {
        return findServiceDetailEntities(false, maxResults, firstResult);
    }

    private List<ServiceDetail> findServiceDetailEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ServiceDetail.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ServiceDetail findServiceDetail(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ServiceDetail.class, id);
        } finally {
            em.close();
        }
    }

    public int getServiceDetailCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ServiceDetail> rt = cq.from(ServiceDetail.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
