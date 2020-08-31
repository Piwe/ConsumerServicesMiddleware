package za.co.netbrain.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Simphiwe.Twala
 */
@Entity
@Table(name = "serviceprovider")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Serviceprovider.findAll", query = "SELECT s FROM Serviceprovider s")
    , @NamedQuery(name = "Serviceprovider.findByServiceproviderid", query = "SELECT s FROM Serviceprovider s WHERE s.serviceproviderid = :serviceproviderid")
    , @NamedQuery(name = "Serviceprovider.findByName", query = "SELECT s FROM Serviceprovider s WHERE s.name = :name")
    , @NamedQuery(name = "Serviceprovider.findBySurname", query = "SELECT s FROM Serviceprovider s WHERE s.surname = :surname")
    , @NamedQuery(name = "Serviceprovider.findByContactnumber", query = "SELECT s FROM Serviceprovider s WHERE s.contactnumber = :contactnumber")
    , @NamedQuery(name = "Serviceprovider.findByEmailaddress", query = "SELECT s FROM Serviceprovider s WHERE s.emailaddress = :emailaddress")
    , @NamedQuery(name = "Serviceprovider.findByPhysicaladdress", query = "SELECT s FROM Serviceprovider s WHERE s.physicaladdress = :physicaladdress")})
public class Serviceprovider implements Serializable {

    @Lob
    @Column(name = "picture")
    private byte[] picture;
    @OneToMany(mappedBy = "serviceproviderpk")
    private Collection<Appointments> appointmentsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "serviceproviderid")
    private Integer serviceproviderid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "contactnumber")
    private String contactnumber;
    @Size(max = 45)
    @Column(name = "emailaddress")
    private String emailaddress;
    @Size(max = 200)
    @Column(name = "physicaladdress")
    private String physicaladdress;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceproviderFk")
    private Collection<ServiceDetail> serviceDetailCollection;

    public Serviceprovider() {
    }

    public Serviceprovider(Integer serviceproviderid) {
        this.serviceproviderid = serviceproviderid;
    }

    public Serviceprovider(Integer serviceproviderid, String name, String surname, String contactnumber) {
        this.serviceproviderid = serviceproviderid;
        this.name = name;
        this.surname = surname;
        this.contactnumber = contactnumber;
    }

    public Integer getServiceproviderid() {
        return serviceproviderid;
    }

    public void setServiceproviderid(Integer serviceproviderid) {
        this.serviceproviderid = serviceproviderid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPhysicaladdress() {
        return physicaladdress;
    }

    public void setPhysicaladdress(String physicaladdress) {
        this.physicaladdress = physicaladdress;
    }


    @XmlTransient
    public Collection<ServiceDetail> getServiceDetailCollection() {
        return serviceDetailCollection;
    }

    public void setServiceDetailCollection(Collection<ServiceDetail> serviceDetailCollection) {
        this.serviceDetailCollection = serviceDetailCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceproviderid != null ? serviceproviderid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Serviceprovider)) {
            return false;
        }
        Serviceprovider other = (Serviceprovider) object;
        if ((this.serviceproviderid == null && other.serviceproviderid != null) || (this.serviceproviderid != null && !this.serviceproviderid.equals(other.serviceproviderid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.co.netbrain.entities.Serviceprovider[ serviceproviderid=" + serviceproviderid + " ]";
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @XmlTransient
    public Collection<Appointments> getAppointmentsCollection() {
        return appointmentsCollection;
    }

    public void setAppointmentsCollection(Collection<Appointments> appointmentsCollection) {
        this.appointmentsCollection = appointmentsCollection;
    }
    
}
