package uk.co.listing.rest.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CourtCaseWeb extends RestResponse {

    private String crestCaseNumber;
    private String leadDefendant;
    private Boolean fromCrest;
    private int numberOfDefendent;
    private String mostSeriousOffence;
    private double trialEstimate;
    private String trialEstimateUnit;
    private String offenceClass;
    private String releaseDecisionStatus;
    private String ticketingRequirement;
    private String courtCenter;
    private String judicialOfficer;
    private Date dateOfCommittal;
    private Date dateOfSending;
    private Boolean caseCompleted;
    private List<PersonInCaseWeb> personInCaseList;
    private List<HearingWeb> hearings = new ArrayList<>();
    private List<CaseNoteWeb> notesWeb = new ArrayList<>();
    private List<CourtCaseWeb> linkedCases = new ArrayList<>();
    private List<NotAvailableDatesWeb> crustNonAvailableDateList;

    public CourtCaseWeb() {
        super();
    }

    public CourtCaseWeb(final String errorMessage) {
        super(errorMessage);
    }

    public List<NotAvailableDatesWeb> getCrustNonAvailableDateList() {
        if (crustNonAvailableDateList == null) {
            crustNonAvailableDateList = new ArrayList<>();
        }
        return crustNonAvailableDateList;
    }

    public List<PersonInCaseWeb> getPersonInCaseList() {
        if (personInCaseList == null) {
            personInCaseList = new ArrayList<PersonInCaseWeb>();
        }
        return personInCaseList;
    }

    public String getReleaseDecisionStatus() {
        return releaseDecisionStatus;
    }

    public void setReleaseDecisionStatus(final String releaseDecisionStatus) {
        this.releaseDecisionStatus = releaseDecisionStatus;
    }

    public String getTicketingRequirement() {
        return ticketingRequirement;
    }

    public void setTicketingRequirement(final String ticketingRequirement) {
        this.ticketingRequirement = ticketingRequirement;
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

    public String getOffenceClass() {
        return offenceClass;
    }

    public void setOffenceClass(final String offenceClass) {
        this.offenceClass = offenceClass;
    }

    public List<HearingWeb> getHearings() {
        return hearings;
    }

    public void setHearings(final List<HearingWeb> hearings) {
        this.hearings = hearings;
    }

    public List<CaseNoteWeb> getNotesWeb() {
        return notesWeb;
    }

    public void setNotesWeb(final List<CaseNoteWeb> notesWeb) {
        this.notesWeb = notesWeb;
    }

    public List<CourtCaseWeb> getLinkedCases() {
        return linkedCases;
    }

    public void setLinkedCases(final List<CourtCaseWeb> linkedCases) {
        this.linkedCases = linkedCases;
    }

    public int getNumberOfDefendent() {
        return numberOfDefendent;
    }

    public void setNumberOfDefendent(final int numberOfDefendent) {
        this.numberOfDefendent = numberOfDefendent;
    }

    public String getCourtCenter() {
        return courtCenter;
    }

    public void setCourtCenter(final String courtCenter) {
        this.courtCenter = courtCenter;
    }

    public Date getDateOfCommittal() {
        return dateOfCommittal;
    }

    public void setDateOfCommittal(final Date dateOfCommittal) {
        this.dateOfCommittal = dateOfCommittal;
    }

    public Date getDateOfSending() {
        return dateOfSending;
    }

    public void setDateOfSending(final Date dateOfSending) {
        this.dateOfSending = dateOfSending;
    }

    public String getTrialEstimateUnit() {
        return trialEstimateUnit;
    }

    public void setTrialEstimateUnit(final String trialEstimateUnit) {
        this.trialEstimateUnit = trialEstimateUnit;
    }
    
    public String getJudicialOfficer() {
        return judicialOfficer;
    }

    public void setJudicialOfficer(String judicialOfficer) {
        this.judicialOfficer = judicialOfficer;
    }

    public Boolean getFromCrest() {
        return fromCrest;
    }

    public void setFromCrest(final Boolean fromCrest) {
        this.fromCrest = fromCrest;
    }

    public boolean checkValidObject() {
        boolean valid = true;
        if (StringUtils.isBlank(getCrestCaseNumber())) {
            valid = false;
            setErrorMessage("The crest number is mandatory");
        } else if (getNumberOfDefendent() < 0) {
            valid = false;
            setErrorMessage("The number of defendant is mandatory and has to be positive");
        } else if (getTrialEstimate() < 0) {
            valid = false;
            setErrorMessage("The trial estimation has to be positive");
        } else if (StringUtils.isBlank(getOffenceClass())) {
            valid = false;
            setErrorMessage("The offence class is mandatory");
        } else if (StringUtils.isBlank(getLeadDefendant())) {
            valid = false;
            setErrorMessage("The lead defendant is mandatory");
        } else if (StringUtils.isBlank(getReleaseDecisionStatus())) {
            valid = false;
            setErrorMessage("The release decision status is mandatory");
        } else if (StringUtils.isBlank(getTicketingRequirement())) {
            valid = false;
            setErrorMessage("The ticketing requirement is mandatory");
        } else if (getDateOfSending() == null && getDateOfCommittal() == null) {
            valid = false;
            setErrorMessage("You have to fill up at least one of the next two fields: Date of Sending or Date of Committal");
        }
        return valid;
    }

    public Boolean getCaseCompleted() {
        return caseCompleted;
    }

    public void setCaseCompleted(final Boolean caseCompleted) {
        this.caseCompleted = caseCompleted;
    }

}
