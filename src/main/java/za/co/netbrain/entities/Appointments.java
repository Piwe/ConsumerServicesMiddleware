package za.co.netbrain.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simphiwe.Twala
 */
@Entity
@Table(name = "appointments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appointments.findAll", query = "SELECT a FROM Appointments a")
    , @NamedQuery(name = "Appointments.findByAppointmentid", query = "SELECT a FROM Appointments a WHERE a.appointmentid = :appointmentid")
    , @NamedQuery(name = "Appointments.findByAppointmentDate", query = "SELECT a FROM Appointments a WHERE a.appointmentDate = :appointmentDate")
    , @NamedQuery(name = "Appointments.findByAppointmentstatus", query = "SELECT a FROM Appointments a WHERE a.appointmentstatus = :appointmentstatus")})
public class Appointments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "appointmentid")
    private Integer appointmentid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "appointment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentDate;
    @Size(max = 45)
    @Column(name = "appointmentstatus")
    private String appointmentstatus;
    @JoinColumn(name = "serviceproviderpk", referencedColumnName = "serviceproviderid")
    @ManyToOne
    private Serviceprovider serviceproviderpk;

    public Appointments() {
    }

    public Appointments(Integer appointmentid) {
        this.appointmentid = appointmentid;
    }

    public Appointments(Integer appointmentid, Date appointmentDate) {
        this.appointmentid = appointmentid;
        this.appointmentDate = appointmentDate;
    }

    public Integer getAppointmentid() {
        return appointmentid;
    }

    public void setAppointmentid(Integer appointmentid) {
        this.appointmentid = appointmentid;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentstatus() {
        return appointmentstatus;
    }

    public void setAppointmentstatus(String appointmentstatus) {
        this.appointmentstatus = appointmentstatus;
    }

    public Serviceprovider getServiceproviderpk() {
        return serviceproviderpk;
    }

    public void setServiceproviderpk(Serviceprovider serviceproviderpk) {
        this.serviceproviderpk = serviceproviderpk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appointmentid != null ? appointmentid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Appointments)) {
            return false;
        }
        Appointments other = (Appointments) object;
        if ((this.appointmentid == null && other.appointmentid != null) || (this.appointmentid != null && !this.appointmentid.equals(other.appointmentid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.co.netbrain.entities.Appointments[ appointmentid=" + appointmentid + " ]";
    }
    
}
