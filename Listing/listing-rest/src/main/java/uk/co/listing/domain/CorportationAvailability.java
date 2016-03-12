package uk.co.listing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Audited
@Table(name = "CorporationAvailability")
public class CorportationAvailability extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CorportationAvailability_Seq_Gen")
    @SequenceGenerator(name = "CorportationAvailability_Seq_Gen", sequenceName = "CorportationAvailability_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "day")
    private Date day;

    @ManyToOne
    @JoinColumn(name = "CorportationInCaseId", nullable = false)
    @ForeignKey(name = "fk_corportationAvailability_corporationInCase")
    private CorporationInCase corporationInCase;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(final Date day) {
        this.day = day;
    }

    public CorporationInCase getCorporationInCase() {
        return corporationInCase;
    }

    public void setCorporationInCase(final CorporationInCase corporationInCase) {
        this.corporationInCase = corporationInCase;
    }

}
