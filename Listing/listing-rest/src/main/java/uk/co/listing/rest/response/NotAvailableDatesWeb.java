package uk.co.listing.rest.response;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import uk.co.listing.utils.DateFormats;

public class NotAvailableDatesWeb extends RestResponse {

    private Long id;
    private String reason;
    private Date startDate;
    private Date endDate;
    private String startDateStr;
    private String endDateStr;
    private String crestCaseNumber;

    public NotAvailableDatesWeb() {
        super();

    }

    public NotAvailableDatesWeb(final String reason, final Date startDate, final Date endDate) {
        super();
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCrestCaseNumber() {
        return crestCaseNumber;
    }

    public void setCrestCaseNumber(final String crestCaseNumber) {
        this.crestCaseNumber = crestCaseNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
        startDateStr = DateFormatUtils.format(startDate, DateFormats.HYP_DAY_YYYY_MM_DD.getValue());

    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
        endDateStr = DateFormatUtils.format(endDate, DateFormats.HYP_DAY_YYYY_MM_DD.getValue());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

}
