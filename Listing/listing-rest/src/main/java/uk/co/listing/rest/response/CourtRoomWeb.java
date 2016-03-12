package uk.co.listing.rest.response;

import java.util.ArrayList;
import java.util.List;

public class CourtRoomWeb {

    private String courtRoomName;
    private String courtCenterName;
    private List<CourtSessionWeb> listCourtSessionWeb = new ArrayList<CourtSessionWeb>();

    @Override
    public String toString() {
        return "CourtRoomWeb [courtRoomName=" + courtRoomName + ", courtCenterName=" + courtCenterName + "]";
    }

    public List<CourtSessionWeb> getListCourtSessionWeb() {
        return listCourtSessionWeb;
    }

    public void setListCourtSessionWeb(List<CourtSessionWeb> listCourtSessionWeb) {
        this.listCourtSessionWeb = listCourtSessionWeb;
    }

    public String getCourtCenterName() {
        return courtCenterName;
    }

    public void setCourtCenterName(String courtCenterName) {
        this.courtCenterName = courtCenterName;
    }

    public String getCourtRoomName() {
        return courtRoomName;
    }

    public void setCourtRoomName(String courtRoomName) {
        this.courtRoomName = courtRoomName;
    }
}
