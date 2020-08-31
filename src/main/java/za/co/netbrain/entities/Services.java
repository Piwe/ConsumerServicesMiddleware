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
@Table(name = "services")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Services.findAll", query = "SELECT s FROM Services s")
    , @NamedQuery(name = "Services.findByServiceid", query = "SELECT s FROM Services s WHERE s.serviceid = :serviceid")
    , @NamedQuery(name = "Services.findByServicedescription", query = "SELECT s FROM Services s WHERE s.servicedescription = :servicedescription")})
public class Services implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "serviceid")
    private Integer serviceid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "servicedescription")
    private String servicedescription;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicesFk")
    private Collection<ServiceDetail> serviceDetailCollection;

    public Services() {
    }

    public Services(Integer serviceid) {
        this.serviceid = serviceid;
    }

    public Services(Integer serviceid, String servicedescription) {
        this.serviceid = serviceid;
        this.servicedescription = servicedescription;
    }

    public Integer getServiceid() {
        return serviceid;
    }

    public void setServiceid(Integer serviceid) {
        this.serviceid = serviceid;
    }

    public String getServicedescription() {
        return servicedescription;
    }

    public void setServicedescription(String servicedescription) {
        this.servicedescription = servicedescription;
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
        hash += (serviceid != null ? serviceid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Services)) {
            return false;
        }
        Services other = (Services) object;
        if ((this.serviceid == null && other.serviceid != null) || (this.serviceid != null && !this.serviceid.equals(other.serviceid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.co.netbrain.entities.Services[ serviceid=" + serviceid + " ]";
    }
    
}
