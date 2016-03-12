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
@Table(name = "crestpcmh")
public class CrestPcmh {

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
    @Column(name = "PCMH_DATE_07")
    private Date PCMHDate;

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

    public void setCaseNumber(final String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public Date getPCMHDate() {
        return PCMHDate;
    }

    public void setPCMHDate(final Date pCMHDate) {
        PCMHDate = pCMHDate;
    }

}
