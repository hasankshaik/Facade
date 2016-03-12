package uk.co.listing.rest.response;

import java.util.Date;

public class SessionWeb extends RestResponse {
    private Date sittingDate;
    private String courtRoomName;
    private String judgeName;

    public SessionWeb(Date sittingDate, String courtRoomName, String judgeName) {
        super();
        this.sittingDate = sittingDate;
        this.courtRoomName = courtRoomName;
        this.judgeName = judgeName;
    }

    public Date getSittingDate() {
        return sittingDate;
    }

    public void setSittingDate(Date sittingDate) {
        this.sittingDate = sittingDate;
    }

    public String getCourtRoomName() {
        return courtRoomName;
    }

    public void setCourtRoomName(String courtRoomName) {
        this.courtRoomName = courtRoomName;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

}
