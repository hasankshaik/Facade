package uk.co.listing.rest.response;

import java.util.ArrayList;
import java.util.List;

public class CourtSessionWeb {


    private String sittingDate;
    private String courtRoomName;
    private List<String> judgeNames = new ArrayList<>();
    private List<SessionBlockWeb> sessionBlockWebList;
    private Boolean isClosed;

    public CourtSessionWeb() {
        super();
    }

    public CourtSessionWeb(final String sittingDate, final String courtRoomName, final String judgeName) {
        super();
        this.sittingDate = sittingDate;
        this.courtRoomName = courtRoomName;
        judgeNames.add(judgeName);
    }
    
    public CourtSessionWeb(final String sittingDate, final String courtRoomName, final List<String> judgeNames) {
        super();
        this.sittingDate = sittingDate;
        this.courtRoomName = courtRoomName;
        this.judgeNames.addAll(judgeNames);
    }
    
    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(final Boolean isClosed) {
        this.isClosed = isClosed;
    }



    public String getSittingDate() {
        return sittingDate;
    }

    public void setSittingDate(final String sittingDate) {
        this.sittingDate = sittingDate;
    }

    public String getCourtRoomName() {
        return courtRoomName;
    }

    public void setCourtRoomName(final String courtRoomName) {
        this.courtRoomName = courtRoomName;
    }

    public List<String> getJudgeNames() {
        return judgeNames;
    }

    public void setJudgeNames(final List<String> judgeNames) {
        this.judgeNames = judgeNames;
    }


    public List<SessionBlockWeb> getSessionBlockWebList() {
        if (sessionBlockWebList == null) {
            sessionBlockWebList = new ArrayList<>();
        }
        return sessionBlockWebList;
    }

    @Override
    public String toString() {
        return "CourtSessionWeb [sittingDate=" + sittingDate + ", courtRoomName=" + courtRoomName + "]";
    }
}
