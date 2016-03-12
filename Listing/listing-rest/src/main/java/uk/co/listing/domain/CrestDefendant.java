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

import uk.co.listing.domain.constant.CustodyStatus;

@Entity
@Table(name = "CrestDefendant")
public class CrestDefendant {
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

    @Column(name = "FORENAME1_11A")
    private String defendantForeNameOne;

    @Column(name = "FORENAME2_11B")
    private String defendantForeNameTwo;

    @Column(name = "SURNAME_11C")
    private String defendantSurname;

    @Column(name = "BC_STATUS_09")
    private CustodyStatus bailCustodyStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CTL_EXPIRY_10")
    private Date ctlExpiryDate;

    @Column(name = "URN_13")
    private String ptiURN;

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

    public String getDefendantForeNameOne() {
        return defendantForeNameOne;
    }

    public void setDefendantForeNameOne(final String defendantForeNameOne) {
        this.defendantForeNameOne = defendantForeNameOne;
    }

    public String getDefendantForeNameTwo() {
        return defendantForeNameTwo;
    }

    public void setDefendantForeNameTwo(final String defendantForeNameTwo) {
        this.defendantForeNameTwo = defendantForeNameTwo;
    }

    public String getDefendantSurname() {
        return defendantSurname;
    }

    public void setDefendantSurname(final String defendantSurname) {
        this.defendantSurname = defendantSurname;
    }

    public CustodyStatus getBailCustodyStatus() {
        return bailCustodyStatus;
    }

    public void setBailCustodyStatus(final CustodyStatus bailCustodyStatus) {
        this.bailCustodyStatus = bailCustodyStatus;
    }

    public Date getCtlExpiryDate() {
        return ctlExpiryDate;
    }

    public void setCtlExpiryDate(final Date ctlExpiryDate) {
        this.ctlExpiryDate = ctlExpiryDate;
    }

    public String getPtiURN() {
        return ptiURN;
    }

    public void setPtiURN(final String ptiURN) {
        this.ptiURN = ptiURN;
    }

}
