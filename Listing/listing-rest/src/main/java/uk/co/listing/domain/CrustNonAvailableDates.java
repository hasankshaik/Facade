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
@Table(name = "CrustNonAvailableDates")
public class CrustNonAvailableDates extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CrustNonAvailableDates_Seq_Gen")
    @SequenceGenerator(name = "CrustNonAvailableDates_Seq_Gen", sequenceName = "CrustNonAvailableDates_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "caseid", nullable = false)
    @ForeignKey(name = "fk_CrustNonAvailableDates_caserelated")
    private CaseRelated caseRelated;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date endDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public CaseRelated getCaseRelated() {
        return caseRelated;
    }

    public void setCaseRelated(final CaseRelated caseRelated) {
        this.caseRelated = caseRelated;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

}
