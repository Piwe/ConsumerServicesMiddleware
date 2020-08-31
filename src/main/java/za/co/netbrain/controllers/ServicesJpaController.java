package za.co.netbrain.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import za.co.netbrain.entities.ServiceDetail;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import za.co.netbrain.controllers.exceptions.IllegalOrphanException;
import za.co.netbrain.controllers.exceptions.NonexistentEntityException;
import za.co.netbrain.entities.Services;

/**
 *
 * @author Simphiwe.Twala
 */
public class ServicesJpaController implements Serializable {

    public ServicesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Services services) {
        if (services.getServiceDetailCollection() == null) {
            services.setServiceDetailCollection(new ArrayList<ServiceDetail>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ServiceDetail> attachedServiceDetailCollection = new ArrayList<ServiceDetail>();
            for (ServiceDetail serviceDetailCollectionServiceDetailToAttach : services.getServiceDetailCollection()) {
                serviceDetailCollectionServiceDetailToAttach = em.getReference(serviceDetailCollectionServiceDetailToAttach.getClass(), serviceDetailCollectionServiceDetailToAttach.getServicedetailid());
                attachedServiceDetailCollection.add(serviceDetailCollectionServiceDetailToAttach);
            }
            services.setServiceDetailCollection(attachedServiceDetailCollection);
            em.persist(services);
            for (ServiceDetail serviceDetailCollectionServiceDetail : services.getServiceDetailCollection()) {
                Services oldServicesFkOfServiceDetailCollectionServiceDetail = serviceDetailCollectionServiceDetail.getServicesFk();
                serviceDetailCollectionServiceDetail.setServicesFk(services);
                serviceDetailCollectionServiceDetail = em.merge(serviceDetailCollectionServiceDetail);
                if (oldServicesFkOfServiceDetailCollectionServiceDetail != null) {
                    oldServicesFkOfServiceDetailCollectionServiceDetail.getServiceDetailCollection().remove(serviceDetailCollectionServiceDetail);
                    oldServicesFkOfServiceDetailCollectionServiceDetail = em.merge(oldServicesFkOfServiceDetailCollectionServiceDetail);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Services services) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Services persistentServices = em.find(Services.class, services.getServiceid());
            Collection<ServiceDetail> serviceDetailCollectionOld = persistentServices.getServiceDetailCollection();
            Collection<ServiceDetail> serviceDetailCollectionNew = services.getServiceDetailCollection();
            List<String> illegalOrphanMessages = null;
            for (ServiceDetail serviceDetailCollectionOldServiceDetail : serviceDetailCollectionOld) {
                if (!serviceDetailCollectionNew.contains(serviceDetailCollectionOldServiceDetail)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ServiceDetail " + serviceDetailCollectionOldServiceDetail + " since its servicesFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ServiceDetail> attachedServiceDetailCollectionNew = new ArrayList<ServiceDetail>();
            for (ServiceDetail serviceDetailCollectionNewServiceDetailToAttach : serviceDetailCollectionNew) {
                serviceDetailCollectionNewServiceDetailToAttach = em.getReference(serviceDetailCollectionNewServiceDetailToAttach.getClass(), serviceDetailCollectionNewServiceDetailToAttach.getServicedetailid());
                attachedServiceDetailCollectionNew.add(serviceDetailCollectionNewServiceDetailToAttach);
            }
            serviceDetailCollectionNew = attachedServiceDetailCollectionNew;
            services.setServiceDetailCollection(serviceDetailCollectionNew);
            services = em.merge(services);
            for (ServiceDetail serviceDetailCollectionNewServiceDetail : serviceDetailCollectionNew) {
                if (!serviceDetailCollectionOld.contains(serviceDetailCollectionNewServiceDetail)) {
                    Services oldServicesFkOfServiceDetailCollectionNewServiceDetail = serviceDetailCollectionNewServiceDetail.getServicesFk();
                    serviceDetailCollectionNewServiceDetail.setServicesFk(services);
                    serviceDetailCollectionNewServiceDetail = em.merge(serviceDetailCollectionNewServiceDetail);
                    if (oldServicesFkOfServiceDetailCollectionNewServiceDetail != null && !oldServicesFkOfServiceDetailCollectionNewServiceDetail.equals(services)) {
                        oldServicesFkOfServiceDetailCollectionNewServiceDetail.getServiceDetailCollection().remove(serviceDetailCollectionNewServiceDetail);
                        oldServicesFkOfServiceDetailCollectionNewServiceDetail = em.merge(oldServicesFkOfServiceDetailCollectionNewServiceDetail);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = services.getServiceid();
                if (findServices(id) == null) {
                    throw new NonexistentEntityException("The services with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Services services;
            try {
                services = em.getReference(Services.class, id);
                services.getServiceid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The services with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ServiceDetail> serviceDetailCollectionOrphanCheck = services.getServiceDetailCollection();
            for (ServiceDetail serviceDetailCollectionOrphanCheckServiceDetail : serviceDetailCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Services (" + services + ") cannot be destroyed since the ServiceDetail " + serviceDetailCollectionOrphanCheckServiceDetail + " in its serviceDetailCollection field has a non-nullable servicesFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(services);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Services> findServicesEntities() {
        return findServicesEntities(true, -1, -1);
    }

    public List<Services> findServicesEntities(int maxResults, int firstResult) {
        return findServicesEntities(false, maxResults, firstResult);
    }

    private List<Services> findServicesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Services.class));
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

    public Services findServices(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Services.class, id);
        } finally {
            em.close();
        }
    }

    public int getServicesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Services> rt = cq.from(Services.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
