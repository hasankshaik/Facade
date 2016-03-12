package uk.co.listing.rest.response;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import uk.co.listing.utils.DateFormats;

public class PersonInCaseWeb extends RestResponse {

    private String custodyStatus;
    private Date ctlExpiryDate;
    private String ctlExpiryDateStr;
    private String noOfDayForCTLExpiry;
    private String roleType;
    private String fullname;
    private String crestURN;
    private String originalFullname;

    public PersonInCaseWeb(final String custodyStatus, final Date ctlExpiryDate, final String roleType, final String noOfDayForCTLExpiry, final String name, final String crestURN) {
        super();
        this.custodyStatus = custodyStatus;
        this.ctlExpiryDate = ctlExpiryDate;
        ctlExpiryDateStr = DateFormatUtils.format(ctlExpiryDate, DateFormats.HYP_DAY_YYYY_MM_DD.getValue());
        this.roleType = roleType;
        this.noOfDayForCTLExpiry = noOfDayForCTLExpiry;
        fullname = name;
        this.crestURN = crestURN;
    }

    public PersonInCaseWeb() {
        super();
    }

    public String getCtlExpiryDateStr() {
        return ctlExpiryDateStr;
    }

    public void setCtlExpiryDateStr(final String ctlExpiryDateStr) {
        this.ctlExpiryDateStr = ctlExpiryDateStr;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(final String fullname) {
        this.fullname = fullname;
    }

    public String getCustodyStatus() {
        return custodyStatus;
    }

    public void setCustodyStatus(final String custodyStatus) {
        this.custodyStatus = custodyStatus;
    }

    public Date getCtlExpiryDate() {
        return ctlExpiryDate;
    }

    public void setCtlExpiryDate(final Date ctlExpiryDate) {
        this.ctlExpiryDate = ctlExpiryDate;
        ctlExpiryDateStr = DateFormatUtils.format(ctlExpiryDate, DateFormats.HYP_DAY_YYYY_MM_DD.getValue());
    }

    public String getNoOfDayForCTLExpiry() {
        return noOfDayForCTLExpiry;
    }

    public void setNoOfDayForCTLExpiry(final String noOfDayForCTLExpiry) {
        this.noOfDayForCTLExpiry = noOfDayForCTLExpiry;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(final String roleType) {
        this.roleType = roleType;
    }

    public String getCrestURN() {
        return crestURN;
    }

    public void setCrestURN(final String crestURN) {
        this.crestURN = crestURN;
    }

    public String getOriginalFullname() {
        return originalFullname;
    }

    public void setOriginalFullname(final String originalFullname) {
        this.originalFullname = originalFullname;
    }

}
