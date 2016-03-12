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

import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;

@Entity
@Table(name = "CrestData")
public class CrestData extends AbstractDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "batchId", nullable = false)
    @ForeignKey(name = "fk_CrestDataBatchJob_Id")
    private CrestDataBatchJob crestDataBatchJob;

    private String actionPerformed;

    @Column(name = "CASE_NO_03", nullable = false)
    private String caseNumber;

    @Column(name = "CASE_TITLE_04", nullable = false)
    private String caseTitle;

    @Column(name = "NO_DEFTS_05", nullable = false)
    private Integer noOfDefendants;

    @Column(name = "CHARGE_06A")
    private String offence;

    @Column(name = "CASE_CLASS_06B", nullable = false)
    private OffenceClass offenceClass;

    @Column(name = "LENGTH_P_08A")
    private Double trailEstProsecutionDuration;

    @Column(name = "LENGTH_UNIT_P_08B")
    private TimeEstimationUnit trailEstProsecutionUnits;

    @Column(name = "LENGTH_LO_08C")
    private Double trailEstListingOfficerDuration;

    @Column(name = "LENGTH_UNIT_LO_08D")
    private TimeEstimationUnit trailEstListingOfficerUnits;

    @Column(name = "TICKET_REQ_14A")
    private String ticketingRequirement;

    @Column(name = "TICKET_TYPE_14B")
    private TicketingRequirement ticketingType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SENT_DATE_24A")
    private Date sentForTrailDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COMM_DATE24B")
    private Date commitalDate;

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(final String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(final String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(final String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public Integer getNoOfDefendants() {
        return noOfDefendants;
    }

    public void setNoOfDefendants(final Integer noOfDefendants) {
        this.noOfDefendants = noOfDefendants;
    }

    public String getOffence() {
        return offence;
    }

    public void setOffence(final String offence) {
        this.offence = offence;
    }

    public OffenceClass getOffenceClass() {
        return offenceClass;
    }

    public void setOffenceClass(final OffenceClass offenceClass) {
        this.offenceClass = offenceClass;
    }

    public Double getTrailEstProsecutionDuration() {
        return trailEstProsecutionDuration;
    }

    public void setTrailEstProsecutionDuration(final Double trailEstProsecutionDuration) {
        this.trailEstProsecutionDuration = trailEstProsecutionDuration;
    }

    public TimeEstimationUnit getTrailEstProsecutionUnits() {
        return trailEstProsecutionUnits;
    }

    public void setTrailEstProsecutionUnits(final TimeEstimationUnit trailEstProsecutionUnits) {
        this.trailEstProsecutionUnits = trailEstProsecutionUnits;
    }

    public Double getTrailEstListingOfficerDuration() {
        return trailEstListingOfficerDuration;
    }

    public void setTrailEstListingOfficerDuration(final Double trailEstListingOfficerDuration) {
        this.trailEstListingOfficerDuration = trailEstListingOfficerDuration;
    }

    public TimeEstimationUnit getTrailEstListingOfficerUnits() {
        return trailEstListingOfficerUnits;
    }

    public void setTrailEstListingOfficerUnits(final TimeEstimationUnit trailEstListingOfficerUnits) {
        this.trailEstListingOfficerUnits = trailEstListingOfficerUnits;
    }

    public String getTicketingRequirement() {
        return ticketingRequirement;
    }

    public void setTicketingRequirement(final String ticketingRequirement) {
        this.ticketingRequirement = ticketingRequirement;
    }

    public TicketingRequirement getTicketingType() {
        return ticketingType;
    }

    public void setTicketingType(final TicketingRequirement ticketingType) {
        this.ticketingType = ticketingType;
    }

    public Date getSentForTrailDate() {
        return sentForTrailDate;
    }

    public void setSentForTrailDate(final Date sentForTrailDate) {
        this.sentForTrailDate = sentForTrailDate;
    }

    public Date getCommitalDate() {
        return commitalDate;
    }

    public void setCommitalDate(final Date commitalDate) {
        this.commitalDate = commitalDate;
    }

    public CrestDataBatchJob getCrestDataBatchJob() {
        return crestDataBatchJob;
    }

    public void setCrestDataBatchJob(final CrestDataBatchJob crestDataBatchJob) {
        this.crestDataBatchJob = crestDataBatchJob;
    }

    public Long getId() {
        return Id;
    }

    public void setId(final Long id) {
        Id = id;
    }

}
