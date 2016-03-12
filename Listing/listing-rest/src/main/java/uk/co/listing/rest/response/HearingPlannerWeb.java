package uk.co.listing.rest.response;

import java.util.Date;

public class HearingPlannerWeb {

    private String hearingName;
    private String hearingKey;
    private String hearingCase;
    private Date trialKPIDate;
    private String leadDefendant;
    private int trailDay;
    private int totalNoDays;


    public HearingPlannerWeb() {
        super();
    }

    public HearingPlannerWeb(final String hearingName, final String hearingCase, final String leadDefendant) {
        super();
        this.hearingName = hearingName;
        this.hearingCase = hearingCase;
        this.leadDefendant = leadDefendant;
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

    public String getHearingKey() {
        return hearingKey;
    }

    public void setHearingKey(final String hearingKey) {
        this.hearingKey = hearingKey;
    }

    public String getLeadDefendant() {
        return leadDefendant;
    }

    public void setLeadDefendant(final String leadDefendant) {
        this.leadDefendant = leadDefendant;
    }

    public Date getTrialKPIDate() {
        return trialKPIDate;
    }

    public void setTrialKPIDate(final Date trialKPIDate) {
        this.trialKPIDate = trialKPIDate;
    }

    public int getTrailDay() {
        return trailDay;
    }

    public void setTrailDay(final int trailDay) {
        this.trailDay = trailDay;
    }

    public int getTotalNoDays() {
        return totalNoDays;
    }

    public void setTotalNoDays(final int totalNoDays) {
        this.totalNoDays = totalNoDays;
    }


}
