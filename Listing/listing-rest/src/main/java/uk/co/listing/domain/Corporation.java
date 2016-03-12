package uk.co.listing.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "Corporation")
public class Corporation extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Corporation_Seq_Gen")
    @SequenceGenerator(name = "Corporation_Seq_Gen", sequenceName = "Corporation_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String name;

    private String phoneNumber;

    private String address;

    private String corporationId;

    private String representative;

    @OneToMany(mappedBy = "corporation", fetch = FetchType.LAZY)
    private Set<CorporationInCase> corporationInCase = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getCorporationId() {
        return corporationId;
    }

    public void setCorporationId(final String corporationId) {
        this.corporationId = corporationId;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(final String representative) {
        this.representative = representative;
    }

    public Set<CorporationInCase> getCorporationInCase() {
        return corporationInCase;
    }

    public void setCorporationInCase(final Set<CorporationInCase> corporationInCase) {
        this.corporationInCase = corporationInCase;
    }
}
