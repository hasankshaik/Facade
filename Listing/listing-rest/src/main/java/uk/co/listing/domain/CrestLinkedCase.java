package uk.co.listing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "CrestLinkedCase")
public class CrestLinkedCase {
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

    @Column(name = "LINKED_CASE_17")
    private String linkedCases;

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

    public String getLinkedCases() {
        return linkedCases;
    }

    public void setLinkedCases(String linkedCases) {
        this.linkedCases = linkedCases;
    }

}
