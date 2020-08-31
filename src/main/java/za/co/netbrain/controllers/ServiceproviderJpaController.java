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
import za.co.netbrain.entities.Appointments;
import za.co.netbrain.entities.Serviceprovider;

/**
 *
 * @author Simphiwe.Twala
 */
public class ServiceproviderJpaController implements Serializable {

    public ServiceproviderJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Serviceprovider serviceprovider) {
        if (serviceprovider.getServiceDetailCollection() == null) {
            serviceprovider.setServiceDetailCollection(new ArrayList<ServiceDetail>());
        }
        if (serviceprovider.getAppointmentsCollection() == null) {
            serviceprovider.setAppointmentsCollection(new ArrayList<Appointments>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ServiceDetail> attachedServiceDetailCollection = new ArrayList<ServiceDetail>();
            for (ServiceDetail serviceDetailCollectionServiceDetailToAttach : serviceprovider.getServiceDetailCollection()) {
                serviceDetailCollectionServiceDetailToAttach = em.getReference(serviceDetailCollectionServiceDetailToAttach.getClass(), serviceDetailCollectionServiceDetailToAttach.getServicedetailid());
                attachedServiceDetailCollection.add(serviceDetailCollectionServiceDetailToAttach);
            }
            serviceprovider.setServiceDetailCollection(attachedServiceDetailCollection);
            Collection<Appointments> attachedAppointmentsCollection = new ArrayList<Appointments>();
            for (Appointments appointmentsCollectionAppointmentsToAttach : serviceprovider.getAppointmentsCollection()) {
                appointmentsCollectionAppointmentsToAttach = em.getReference(appointmentsCollectionAppointmentsToAttach.getClass(), appointmentsCollectionAppointmentsToAttach.getAppointmentid());
                attachedAppointmentsCollection.add(appointmentsCollectionAppointmentsToAttach);
            }
            serviceprovider.setAppointmentsCollection(attachedAppointmentsCollection);
            em.persist(serviceprovider);
            for (ServiceDetail serviceDetailCollectionServiceDetail : serviceprovider.getServiceDetailCollection()) {
                Serviceprovider oldServiceproviderFkOfServiceDetailCollectionServiceDetail = serviceDetailCollectionServiceDetail.getServiceproviderFk();
                serviceDetailCollectionServiceDetail.setServiceproviderFk(serviceprovider);
                serviceDetailCollectionServiceDetail = em.merge(serviceDetailCollectionServiceDetail);
                if (oldServiceproviderFkOfServiceDetailCollectionServiceDetail != null) {
                    oldServiceproviderFkOfServiceDetailCollectionServiceDetail.getServiceDetailCollection().remove(serviceDetailCollectionServiceDetail);
                    oldServiceproviderFkOfServiceDetailCollectionServiceDetail = em.merge(oldServiceproviderFkOfServiceDetailCollectionServiceDetail);
                }
            }
            for (Appointments appointmentsCollectionAppointments : serviceprovider.getAppointmentsCollection()) {
                Serviceprovider oldServiceproviderpkOfAppointmentsCollectionAppointments = appointmentsCollectionAppointments.getServiceproviderpk();
                appointmentsCollectionAppointments.setServiceproviderpk(serviceprovider);
                appointmentsCollectionAppointments = em.merge(appointmentsCollectionAppointments);
                if (oldServiceproviderpkOfAppointmentsCollectionAppointments != null) {
                    oldServiceproviderpkOfAppointmentsCollectionAppointments.getAppointmentsCollection().remove(appointmentsCollectionAppointments);
                    oldServiceproviderpkOfAppointmentsCollectionAppointments = em.merge(oldServiceproviderpkOfAppointmentsCollectionAppointments);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Serviceprovider serviceprovider) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Serviceprovider persistentServiceprovider = em.find(Serviceprovider.class, serviceprovider.getServiceproviderid());
            Collection<ServiceDetail> serviceDetailCollectionOld = persistentServiceprovider.getServiceDetailCollection();
            Collection<ServiceDetail> serviceDetailCollectionNew = serviceprovider.getServiceDetailCollection();
            Collection<Appointments> appointmentsCollectionOld = persistentServiceprovider.getAppointmentsCollection();
            Collection<Appointments> appointmentsCollectionNew = serviceprovider.getAppointmentsCollection();
            List<String> illegalOrphanMessages = null;
            for (ServiceDetail serviceDetailCollectionOldServiceDetail : serviceDetailCollectionOld) {
                if (!serviceDetailCollectionNew.contains(serviceDetailCollectionOldServiceDetail)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ServiceDetail " + serviceDetailCollectionOldServiceDetail + " since its serviceproviderFk field is not nullable.");
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
            serviceprovider.setServiceDetailCollection(serviceDetailCollectionNew);
            Collection<Appointments> attachedAppointmentsCollectionNew = new ArrayList<Appointments>();
            for (Appointments appointmentsCollectionNewAppointmentsToAttach : appointmentsCollectionNew) {
                appointmentsCollectionNewAppointmentsToAttach = em.getReference(appointmentsCollectionNewAppointmentsToAttach.getClass(), appointmentsCollectionNewAppointmentsToAttach.getAppointmentid());
                attachedAppointmentsCollectionNew.add(appointmentsCollectionNewAppointmentsToAttach);
            }
            appointmentsCollectionNew = attachedAppointmentsCollectionNew;
            serviceprovider.setAppointmentsCollection(appointmentsCollectionNew);
            serviceprovider = em.merge(serviceprovider);
            for (ServiceDetail serviceDetailCollectionNewServiceDetail : serviceDetailCollectionNew) {
                if (!serviceDetailCollectionOld.contains(serviceDetailCollectionNewServiceDetail)) {
                    Serviceprovider oldServiceproviderFkOfServiceDetailCollectionNewServiceDetail = serviceDetailCollectionNewServiceDetail.getServiceproviderFk();
                    serviceDetailCollectionNewServiceDetail.setServiceproviderFk(serviceprovider);
                    serviceDetailCollectionNewServiceDetail = em.merge(serviceDetailCollectionNewServiceDetail);
                    if (oldServiceproviderFkOfServiceDetailCollectionNewServiceDetail != null && !oldServiceproviderFkOfServiceDetailCollectionNewServiceDetail.equals(serviceprovider)) {
                        oldServiceproviderFkOfServiceDetailCollectionNewServiceDetail.getServiceDetailCollection().remove(serviceDetailCollectionNewServiceDetail);
                        oldServiceproviderFkOfServiceDetailCollectionNewServiceDetail = em.merge(oldServiceproviderFkOfServiceDetailCollectionNewServiceDetail);
                    }
                }
            }
            for (Appointments appointmentsCollectionOldAppointments : appointmentsCollectionOld) {
                if (!appointmentsCollectionNew.contains(appointmentsCollectionOldAppointments)) {
                    appointmentsCollectionOldAppointments.setServiceproviderpk(null);
                    appointmentsCollectionOldAppointments = em.merge(appointmentsCollectionOldAppointments);
                }
            }
            for (Appointments appointmentsCollectionNewAppointments : appointmentsCollectionNew) {
                if (!appointmentsCollectionOld.contains(appointmentsCollectionNewAppointments)) {
                    Serviceprovider oldServiceproviderpkOfAppointmentsCollectionNewAppointments = appointmentsCollectionNewAppointments.getServiceproviderpk();
                    appointmentsCollectionNewAppointments.setServiceproviderpk(serviceprovider);
                    appointmentsCollectionNewAppointments = em.merge(appointmentsCollectionNewAppointments);
                    if (oldServiceproviderpkOfAppointmentsCollectionNewAppointments != null && !oldServiceproviderpkOfAppointmentsCollectionNewAppointments.equals(serviceprovider)) {
                        oldServiceproviderpkOfAppointmentsCollectionNewAppointments.getAppointmentsCollection().remove(appointmentsCollectionNewAppointments);
                        oldServiceproviderpkOfAppointmentsCollectionNewAppointments = em.merge(oldServiceproviderpkOfAppointmentsCollectionNewAppointments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = serviceprovider.getServiceproviderid();
                if (findServiceprovider(id) == null) {
                    throw new NonexistentEntityException("The serviceprovider with id " + id + " no longer exists.");
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
            Serviceprovider serviceprovider;
            try {
                serviceprovider = em.getReference(Serviceprovider.class, id);
                serviceprovider.getServiceproviderid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The serviceprovider with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ServiceDetail> serviceDetailCollectionOrphanCheck = serviceprovider.getServiceDetailCollection();
            for (ServiceDetail serviceDetailCollectionOrphanCheckServiceDetail : serviceDetailCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Serviceprovider (" + serviceprovider + ") cannot be destroyed since the ServiceDetail " + serviceDetailCollectionOrphanCheckServiceDetail + " in its serviceDetailCollection field has a non-nullable serviceproviderFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Appointments> appointmentsCollection = serviceprovider.getAppointmentsCollection();
            for (Appointments appointmentsCollectionAppointments : appointmentsCollection) {
                appointmentsCollectionAppointments.setServiceproviderpk(null);
                appointmentsCollectionAppointments = em.merge(appointmentsCollectionAppointments);
            }
            em.remove(serviceprovider);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Serviceprovider> findServiceproviderEntities() {
        return findServiceproviderEntities(true, -1, -1);
    }

    public List<Serviceprovider> findServiceproviderEntities(int maxResults, int firstResult) {
        return findServiceproviderEntities(false, maxResults, firstResult);
    }

    private List<Serviceprovider> findServiceproviderEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Serviceprovider.class));
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

    public Serviceprovider findServiceprovider(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Serviceprovider.class, id);
        } finally {
            em.close();
        }
    }

    public int getServiceproviderCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Serviceprovider> rt = cq.from(Serviceprovider.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
