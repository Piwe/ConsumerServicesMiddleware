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
import za.co.netbrain.entities.Appointments;
import za.co.netbrain.entities.Serviceprovider;

/**
 *
 * @author Simphiwe.Twala
 */
public class AppointmentsJpaController implements Serializable {

    public AppointmentsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Appointments appointments) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Serviceprovider serviceproviderpk = appointments.getServiceproviderpk();
            if (serviceproviderpk != null) {
                serviceproviderpk = em.getReference(serviceproviderpk.getClass(), serviceproviderpk.getServiceproviderid());
                appointments.setServiceproviderpk(serviceproviderpk);
            }
            em.persist(appointments);
            if (serviceproviderpk != null) {
                serviceproviderpk.getAppointmentsCollection().add(appointments);
                serviceproviderpk = em.merge(serviceproviderpk);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Appointments appointments) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Appointments persistentAppointments = em.find(Appointments.class, appointments.getAppointmentid());
            Serviceprovider serviceproviderpkOld = persistentAppointments.getServiceproviderpk();
            Serviceprovider serviceproviderpkNew = appointments.getServiceproviderpk();
            if (serviceproviderpkNew != null) {
                serviceproviderpkNew = em.getReference(serviceproviderpkNew.getClass(), serviceproviderpkNew.getServiceproviderid());
                appointments.setServiceproviderpk(serviceproviderpkNew);
            }
            appointments = em.merge(appointments);
            if (serviceproviderpkOld != null && !serviceproviderpkOld.equals(serviceproviderpkNew)) {
                serviceproviderpkOld.getAppointmentsCollection().remove(appointments);
                serviceproviderpkOld = em.merge(serviceproviderpkOld);
            }
            if (serviceproviderpkNew != null && !serviceproviderpkNew.equals(serviceproviderpkOld)) {
                serviceproviderpkNew.getAppointmentsCollection().add(appointments);
                serviceproviderpkNew = em.merge(serviceproviderpkNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = appointments.getAppointmentid();
                if (findAppointments(id) == null) {
                    throw new NonexistentEntityException("The appointments with id " + id + " no longer exists.");
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
            Appointments appointments;
            try {
                appointments = em.getReference(Appointments.class, id);
                appointments.getAppointmentid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The appointments with id " + id + " no longer exists.", enfe);
            }
            Serviceprovider serviceproviderpk = appointments.getServiceproviderpk();
            if (serviceproviderpk != null) {
                serviceproviderpk.getAppointmentsCollection().remove(appointments);
                serviceproviderpk = em.merge(serviceproviderpk);
            }
            em.remove(appointments);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Appointments> findAppointmentsEntities() {
        return findAppointmentsEntities(true, -1, -1);
    }

    public List<Appointments> findAppointmentsEntities(int maxResults, int firstResult) {
        return findAppointmentsEntities(false, maxResults, firstResult);
    }

    private List<Appointments> findAppointmentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Appointments.class));
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

    public Appointments findAppointments(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Appointments.class, id);
        } finally {
            em.close();
        }
    }

    public int getAppointmentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Appointments> rt = cq.from(Appointments.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
