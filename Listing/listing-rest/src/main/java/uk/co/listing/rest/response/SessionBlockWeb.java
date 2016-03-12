package uk.co.listing.rest.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionBlockWeb {

    private Date startDate;

    private Date endDate;

    private String blockType;

    private String blockShortCode;

    private List<HearingPlannerWeb> hearingWebList;

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

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(final String blockType) {
        this.blockType = blockType;
    }

    public List<HearingPlannerWeb> getHearingWebList() {
        if (hearingWebList == null) {
            hearingWebList = new ArrayList<>();
        }
        return hearingWebList;
    }

    public String getBlockShortCode() {
        return blockShortCode;
    }

    public void setBlockShortCode(final String blockShortCode) {
        this.blockShortCode = blockShortCode;
    }

}
