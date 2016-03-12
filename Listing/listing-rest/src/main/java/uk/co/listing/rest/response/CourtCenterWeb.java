package uk.co.listing.rest.response;

public class CourtCenterWeb {

    private String courtCenterName;
    private Long code;

    public CourtCenterWeb() {
        super();
    }

    public CourtCenterWeb(final String courtCenterName, final Long code) {
        this.courtCenterName = courtCenterName;
        this.code = code;
    }

    public String getCourtCenterName() {
        return courtCenterName;
    }

    public void setCourtCenterName(final String courtCenterName) {
        this.courtCenterName = courtCenterName;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(final Long code) {
        this.code = code;
    }
}
