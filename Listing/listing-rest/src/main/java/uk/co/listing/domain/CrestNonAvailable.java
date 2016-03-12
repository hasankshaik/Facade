package uk.co.listing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "CrestNonAvailable")
public class CrestNonAvailable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "batchId", nullable = false)
    @ForeignKey(name = "fk_CrestDataBatchJob_Id")
    private CrestDataBatchJob crestDataBatchJob;
    
    @Column(name = "PARENT_CASE", nullable = false)
    private String caseNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NON_START_11")
    private Date nonAvailableDateStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NON_END_16A")
    private Date nonAvailableDateEnd;

    @Column(name = "NON_REASON_16B")
    private String nonAvailableDateReason;

    public Long getId() {
        return Id;
    }

    public void setId(final Long id) {
        Id = id;
    }

    public CrestDataBatchJob getCrestDataBatchJob() {
        return crestDataBatchJob;
    }

    public void setCrestDataBatchJob(final CrestDataBatchJob crestDataBatchJob) {
        this.crestDataBatchJob = crestDataBatchJob;
    }
    
    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public Date getNonAvailableDateStart() {
        return nonAvailableDateStart;
    }

    public void setNonAvailableDateStart(Date nonAvailableDateStart) {
        this.nonAvailableDateStart = nonAvailableDateStart;
    }

    public Date getNonAvailableDateEnd() {
        return nonAvailableDateEnd;
    }

    public void setNonAvailableDateEnd(Date nonAvailableDateEnd) {
        this.nonAvailableDateEnd = nonAvailableDateEnd;
    }

    public String getNonAvailableDateReason() {
        return nonAvailableDateReason;
    }

    public void setNonAvailableDateReason(String nonAvailableDateReason) {
        this.nonAvailableDateReason = nonAvailableDateReason;
    }


}
