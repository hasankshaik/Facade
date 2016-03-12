package uk.co.listing.rest.response;

import java.util.Date;

public class HearingWeb extends RestResponse {

    private String hearingName;
    private String hearingKey;
    private String hearingCase;
    private String hearingType;
    private String startDate;
    private Double trialEstimate;
    private String bookingType;
    private String bookingStatus;
    private Date startDateForSlot;
    private Boolean active;
    private String courtRoomName;
    private String hearingNote;
    private String hearingStatus;
    private Date trialKPIDate;

    public HearingWeb() {
        super();
    }

    public HearingWeb(final String hearingName, final String hearingCase, final String hearingType, final String startDate, final Double trialEstimate, final String bookingStatus) {
        super();
        this.hearingName = hearingName;
        this.hearingCase = hearingCase;
        this.hearingType = hearingType;
        this.startDate = startDate;
        this.trialEstimate = trialEstimate;
        this.setBookingStatus(bookingStatus);
    }

    public HearingWeb(final String errorMessage) {
        super(errorMessage);
    }

    public Date getStartDateForSlot() {
        return startDateForSlot;
    }

    public void setStartDateForSlot(final Date startDateForSlot) {
        this.startDateForSlot = startDateForSlot;
    }

    public Double getTrialEstimate() {
        return trialEstimate;
    }

    public void setTrialEstimate(final Double trialEstimate) {
        this.trialEstimate = trialEstimate;
    }

    public String getHearingName() {
        return hearingName;
    }

    public void setHearingName(final String hearingName) {
        this.hearingName = hearingName;
    }

    public String getHearingCase() {
        return hearingCase;
    }

    public void setHearingCase(final String hearingCase) {
        this.hearingCase = hearingCase;
    }

    public String getHearingType() {
        return hearingType;
    }

    public void setHearingType(final String hearingType) {
        this.hearingType = hearingType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(final String bookingType) {
        this.bookingType = bookingType;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getCourtRoomName() {
        return courtRoomName;
    }

    public void setCourtRoomName(final String courtRoomName) {
        this.courtRoomName = courtRoomName;
    }

    public String getHearingKey() {
        return hearingKey;
    }

    public void setHearingKey(final String hearingKey) {
        this.hearingKey = hearingKey;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("HearingWeb [hearingName=");
        sb.append(hearingName);
        sb.append(", hearingCase=");
        sb.append(hearingCase);
        sb.append(", hearingType=");
        sb.append(hearingType);
        sb.append(", bookingType=");
        sb.append(bookingType);
        sb.append(", bookingStatus=");
        sb.append(bookingStatus);
        sb.append(", startDate=");
        sb.append(startDate);
        sb.append(", trialEstimate=");
        sb.append(trialEstimate);
        sb.append("]");

        return sb.toString();
    }

    public String getHearingNote() {
        return hearingNote;
    }

    public void setHearingNote(final String hearingNote) {
        this.hearingNote = hearingNote;
    }

    public String getHearingStatus() {
        return hearingStatus;
    }

    public void setHearingStatus(final String hearingStatus) {
        this.hearingStatus = hearingStatus;
    }

    public Date getTrialKPIDate() {
        return trialKPIDate;
    }

    public void setTrialKPIDate(final Date trialKPIDate) {
        this.trialKPIDate = trialKPIDate;
    }

}
