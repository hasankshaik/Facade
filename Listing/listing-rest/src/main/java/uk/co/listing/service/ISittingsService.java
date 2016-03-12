package uk.co.listing.service;

import java.util.Date;
import java.util.Map;

import uk.co.listing.domain.SittingTarget;
import uk.co.listing.rest.response.SittingTargetWeb;

public interface ISittingsService {

    public void saveAnnualSittingTarget(SittingTargetWeb annualSittingWeb);

    public SittingTarget getAnnualSittingsTarget(String courtCenter, String financialYear);

    public Long getAnnualActualSittingDays(String courtCenterName, String financialYear);

    Map<String, Long> getMonthlyActualSittingDays(String courtCentreName, String financialYear);

    Long getAnnualActualSittingDays(String courtCenterName, Date date);

}
