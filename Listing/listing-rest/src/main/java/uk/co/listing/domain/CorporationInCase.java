package uk.co.listing.domain;

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

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;

import uk.co.listing.domain.constant.RoleInCase;

@Entity
@Audited
@Table(name = "CorporationInCase")
public class CorporationInCase extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CorporationInCase_Seq_Gen")
    @SequenceGenerator(name = "CorporationInCase_Seq_Gen", sequenceName = "CorporationInCase_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleInCase roleInCase;

    @ManyToOne
    @JoinColumn(name = "corporationid", nullable = false)
    @ForeignKey(name = "fk_corportationincase_corporation")
    private Corporation corporation;

    @ManyToOne
    @JoinColumn(name = "caseid", nullable = false)
    @ForeignKey(name = "fk_corporationincase_caserelated")
    private CaseRelated caseRelated;

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

    public Corporation getCorporation() {
        return corporation;
    }

    public void setCorporation(final Corporation corporation) {
        this.corporation = corporation;
    }

    public CaseRelated getCaseRelated() {
        return caseRelated;
    }

    public void setCaseRelated(final CaseRelated caseRelated) {
        this.caseRelated = caseRelated;
    }

}
