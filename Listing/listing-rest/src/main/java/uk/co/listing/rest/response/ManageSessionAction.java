package uk.co.listing.rest.response;

import java.util.Date;
import java.util.List;

public class ManageSessionAction extends RestResponse  {
    
    private Boolean closed;
    private Date sittingStartDate;
    private Date sittingEndDate;
    private List<String> courtRoomNames;

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
    public List<String> getCourtRoomNames() {
        return courtRoomNames;
    }
    public void setCourtRoomNames(final List<String> courtRoomNames) {
        this.courtRoomNames = courtRoomNames;
    }

    public Boolean getClosed() {
        return closed;
    }
    public void setClosed(final Boolean closed) {
        this.closed = closed;
    }

}
