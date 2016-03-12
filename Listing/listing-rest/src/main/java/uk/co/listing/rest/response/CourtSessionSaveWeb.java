package uk.co.listing.rest.response;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import uk.co.listing.utils.DateFormats;

public class CourtSessionSaveWeb extends RestResponse {

    private Date sittingStartDate;
    private Date sittingEndDate;
    private String courtRoomName;
    private String judgeName;
    private String blockType;

    private static final Logger LOGGER = Logger.getLogger(CourtSessionSaveWeb.class);

    public CourtSessionSaveWeb(final String errorMessage) {
        super(errorMessage);
    }

    public CourtSessionSaveWeb(final String sittingStartDate, final String sittingEndDate, final String courtRoomName, final String judgeName) {
        this(sittingStartDate, sittingEndDate, courtRoomName, judgeName, "Trial");
    }

    public CourtSessionSaveWeb(final String sittingStartDate, final String sittingEndDate, final String courtRoomName, final String judgeName, final String blcokType) {
        super();
        try {
            this.sittingStartDate = DateUtils.parseDate(sittingStartDate, DateFormats.getPatterns());
            this.sittingEndDate = DateUtils.parseDate(sittingEndDate, DateFormats.getPatterns());
        } catch (final ParseException e) {
            LOGGER.error(e);
        }
        this.courtRoomName = courtRoomName;
        this.judgeName = judgeName;
        this.blockType = blcokType;
    }

    public CourtSessionSaveWeb() {
        super();
    }

    public String getCourtRoomName() {
        return courtRoomName;
    }

    public Date getSittingStartDate() {
        return sittingStartDate;
    }

    public void setSittingStartDate(final Date sittingStartDate) {
        this.sittingStartDate = sittingStartDate;
    }

    public Date getSittingEndDate() {
        return sittingEndDate;
    }

    public void setSittingEndDate(final Date sittingEndDate) {
        this.sittingEndDate = sittingEndDate;
    }

    public void setCourtRoomName(final String courtRoomName) {
        this.courtRoomName = courtRoomName;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(final String judgeName) {
        this.judgeName = judgeName;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(final String blockType) {
        this.blockType = blockType;
    }

}
