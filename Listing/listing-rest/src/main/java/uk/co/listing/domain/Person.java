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
@Table(name = "Person")
public class Person extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Person_Seq_Gen")
    @SequenceGenerator(name = "Person_Seq_Gen", sequenceName = "Person_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private String personFullName;

    private String phoneNumber;

    private String address;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<PersonInCase> personInCase = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getPersonFullName() {
        return personFullName;
    }

    public void setPersonFullName(final String personFullName) {
        this.personFullName = personFullName;
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

    public Set<PersonInCase> getPersonInCase() {
        return personInCase;
    }

    public void setPersonInCase(final Set<PersonInCase> personInCase) {
        this.personInCase = personInCase;
    }
}
