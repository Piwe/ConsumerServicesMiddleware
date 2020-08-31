package za.co.netbrain.entities;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simphiwe.Twala
 */
@Entity
@Table(name = "service_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceDetail.findAll", query = "SELECT s FROM ServiceDetail s")
    , @NamedQuery(name = "ServiceDetail.findByServicedetailid", query = "SELECT s FROM ServiceDetail s WHERE s.servicedetailid = :servicedetailid")})
public class ServiceDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "servicedetailid")
    private Integer servicedetailid;
    @JoinColumn(name = "serviceprovider_fk", referencedColumnName = "serviceproviderid")
    @ManyToOne(optional = false)
    private Serviceprovider serviceproviderFk;
    @JoinColumn(name = "services_fk", referencedColumnName = "serviceid")
    @ManyToOne(optional = false)
    private Services servicesFk;

    public ServiceDetail() {
    }

    public ServiceDetail(Integer servicedetailid) {
        this.servicedetailid = servicedetailid;
    }

    public Integer getServicedetailid() {
        return servicedetailid;
    }

    public void setServicedetailid(Integer servicedetailid) {
        this.servicedetailid = servicedetailid;
    }

    public Serviceprovider getServiceproviderFk() {
        return serviceproviderFk;
    }

    public void setServiceproviderFk(Serviceprovider serviceproviderFk) {
        this.serviceproviderFk = serviceproviderFk;
    }

    public Services getServicesFk() {
        return servicesFk;
    }

    public void setServicesFk(Services servicesFk) {
        this.servicesFk = servicesFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (servicedetailid != null ? servicedetailid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceDetail)) {
            return false;
        }
        ServiceDetail other = (ServiceDetail) object;
        if ((this.servicedetailid == null && other.servicedetailid != null) || (this.servicedetailid != null && !this.servicedetailid.equals(other.servicedetailid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.co.netbrain.entities.ServiceDetail[ servicedetailid=" + servicedetailid + " ]";
    }
    
}
