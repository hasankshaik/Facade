package uk.co.listing.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;

@Entity
@Audited
@Table(name = "CaseRelated")
public class CaseRelated extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CaseRelated_Seq_Gen")
    @SequenceGenerator(name = "CaseRelated_Seq_Gen", sequenceName = "CaseRelated_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Type(type = "yes_no")
    private Boolean fromCrest;
    @Type(type = "yes_no")
    private Boolean caseClosed;
    private String crestCaseNumber;
    private String leadDefendant;
    private String mostSeriousOffence;
    private double trialEstimate;
    private int numberOfDefendant;
    @Enumerated(EnumType.STRING)
    private OffenceClass offenceClass;
    @Enumerated(EnumType.STRING)
    private ReleaseDecision releaseDecisionStatus;
    @Enumerated(EnumType.STRING)
    private TicketingRequirement ticketingRequirement;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enddate")
    private Date endDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateOfSending")
    private Date dateOfSending;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateOfCommittal")
    private Date dateOfCommittal;

    @Enumerated(EnumType.STRING)
    private TimeEstimationUnit estimationUnit;

    @ManyToOne
    @JoinColumn(name = "courtcenterid", nullable = false)
    @ForeignKey(name = "fk_caserelated_courtcenter")
    private CourtCenter courtCenter;
    
    @ManyToOne
    @JoinColumn(name = "judicialofficerid")
    @ForeignKey(name = "fk_caserelated_judicialofficer")
    private JudicialOfficer judge;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "linkedcases", joinColumns = { @JoinColumn(name = "casedRelatedId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "casedLinkedId", nullable = false, updatable = false) })
    private Set<CaseRelated> linkedCases = new HashSet<CaseRelated>();

    public ReleaseDecision getReleaseDecisionStatus() {
        return releaseDecisionStatus;
    }

    public void setReleaseDecisionStatus(final ReleaseDecision releaseDecisionStatus) {
        this.releaseDecisionStatus = releaseDecisionStatus;
    }

    public TicketingRequirement getTicketingRequirement() {
        return ticketingRequirement;
    }

    public void setTicketingRequirement(final TicketingRequirement ticketingRequirement) {
        this.ticketingRequirement = ticketingRequirement;
    }

    public String getLeadDefendant() {
        return leadDefendant;
    }

    public void setLeadDefendant(final String leadDefendant) {
        this.leadDefendant = leadDefendant;
    }

    public String getMostSeriousOffence() {
        return mostSeriousOffence;
    }

    public void setMostSeriousOffence(final String mostSeriousOffence) {
        this.mostSeriousOffence = mostSeriousOffence;
    }

    public double getTrialEstimate() {
        return trialEstimate;
    }

    public void setTrialEstimate(final double trialEstimate) {
        this.trialEstimate = trialEstimate;
    }

    public String getCrestCaseNumber() {
        return crestCaseNumber;
    }

    public void setCrestCaseNumber(final String crestCaseNumber) {
        this.crestCaseNumber = crestCaseNumber;
    }

    @OneToMany(mappedBy = "caseRelated", fetch = FetchType.LAZY)
    private Set<Hearing> hearings = new HashSet<Hearing>();

    @OneToMany(mappedBy = "caseRelated", fetch = FetchType.LAZY)
    private Set<PersonInCase> personInCase = new HashSet<PersonInCase>();

    @OneToMany(mappedBy = "caseRelated", fetch = FetchType.LAZY)
    private Set<CorporationInCase> corporationInCase = new HashSet<CorporationInCase>();

    @OneToMany(mappedBy = "caseRelated", fetch = FetchType.LAZY)
    private Set<CaseNote> notes = new HashSet<CaseNote>();

    @OneToMany(mappedBy = "caseRelated", fetch = FetchType.LAZY)
    private Set<CrustNonAvailableDates> crustNonAvailableDatesList = new HashSet<>();

    public Set<CrustNonAvailableDates> getCrustNonAvailableDatesList() {
        return crustNonAvailableDatesList;
    }

    public void setCrustNonAvailableDatesList(final Set<CrustNonAvailableDates> crustNonAvailableDatesList) {
        this.crustNonAvailableDatesList = crustNonAvailableDatesList;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public Set<Hearing> getHearings() {
        return hearings;
    }

    public void setHearings(final Set<Hearing> hearings) {
        this.hearings = hearings;
    }

    public Set<PersonInCase> getPersonInCase() {
        return personInCase;
    }

    public void setPersonInCase(final Set<PersonInCase> personInCase) {
        this.personInCase = personInCase;
    }

    public Set<CorporationInCase> getCorporationInCase() {
        return corporationInCase;
    }

    public void setCorporationInCase(final Set<CorporationInCase> corporationInCase) {
        this.corporationInCase = corporationInCase;
    }

    public OffenceClass getOffenceClass() {
        return offenceClass;
    }

    public void setOffenceClass(final OffenceClass offenceClass) {
        this.offenceClass = offenceClass;
    }

    public Set<CaseNote> getNotes() {
        return notes;
    }

    public void setNotes(final Set<CaseNote> notes) {
        this.notes = notes;
    }

    public Set<CaseRelated> getLinkedCases() {
        return linkedCases;
    }

    public void setLinkedCases(final Set<CaseRelated> linkedCases) {
        this.linkedCases = linkedCases;
    }

    public int getNumberOfDefendant() {
        return numberOfDefendant;
    }

    public void setNumberOfDefendant(final int numberOfDefendant) {
        this.numberOfDefendant = numberOfDefendant;
    }

    public CourtCenter getCourtCenter() {
        return courtCenter;
    }

    public void setCourtCenter(final CourtCenter courtCenter) {
        this.courtCenter = courtCenter;
    }

    public Date getDateOfSending() {
        return dateOfSending;
    }

    public void setDateOfSending(final Date dateOfSending) {
        this.dateOfSending = dateOfSending;
    }

    public Date getDateOfCommittal() {
        return dateOfCommittal;
    }

    public void setDateOfCommittal(final Date dateOfCommittal) {
        this.dateOfCommittal = dateOfCommittal;
    }

    public TimeEstimationUnit getEstimationUnit() {
        return estimationUnit;
    }

    public void setEstimationUnit(final TimeEstimationUnit estimationUnit) {
        this.estimationUnit = estimationUnit;
    }

    public Boolean getFromCrest() {
        return fromCrest;
    }

    public void setFromCrest(final Boolean fromCrest) {
        this.fromCrest = fromCrest;
    }

    public Boolean getCaseClosed() {
        return caseClosed;
    }

    public void setCaseClosed(final Boolean caseClosed) {
        this.caseClosed = caseClosed;
    }

    public JudicialOfficer getJudge() {
        return judge;
    }

    public void setJudge(JudicialOfficer judge) {
        this.judge = judge;
    }

}
