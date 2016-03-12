package uk.co.listing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;

import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.domain.constant.RoleInCase;

@Entity
@Audited
@Table(name = "PersonInCase")
public class PersonInCase extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PersonInCase_Seq_Gen")
    @SequenceGenerator(name = "PersonInCase_Seq_Gen", sequenceName = "PersonInCase_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleInCase roleInCase;

    @Enumerated(EnumType.STRING)
    private CustodyStatus custodyStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ctlExpiresOn;

    @ManyToOne
    @JoinColumn(name = "personid", nullable = false)
    @ForeignKey(name = "fk_personincase_person")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "caseid", nullable = false)
    @ForeignKey(name = "fk_personincase_caserelated")
    private CaseRelated caseRelated;

    private String caseUrn;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public RoleInCase getRoleInCase() {
        return roleInCase;
    }

    public void setRoleInCase(final RoleInCase roleInCase) {
        this.roleInCase = roleInCase;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(final Person person) {
        this.person = person;
    }

    public CaseRelated getCaseRelated() {
        return caseRelated;
    }

    public void setCaseRelated(final CaseRelated caseRelated) {
        this.caseRelated = caseRelated;
    }

    public CustodyStatus getCustodyStatus() {
        return custodyStatus;
    }

    public void setCustodyStatus(final CustodyStatus custodyStatus) {
        this.custodyStatus = custodyStatus;
    }

    public Date getCtlExpiresOn() {
        return ctlExpiresOn;
    }

    public void setCtlExpiresOn(final Date ctlExpiresOn) {
        this.ctlExpiresOn = ctlExpiresOn;
    }

    public String getCaseUrn() {
        return caseUrn;
    }

    public void setCaseUrn(final String caseUrn) {
        this.caseUrn = caseUrn;
    }

}
