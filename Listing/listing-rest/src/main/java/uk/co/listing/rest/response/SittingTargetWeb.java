package uk.co.listing.rest.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SittingTargetWeb /* extends RestResponse */{

    private String courtCenter;
    private String financialYear;
    private Long annualTarget = 0L;
    private Map<String, Long> monthlyTargets = new HashMap<String, Long>();
    private Long monthlySum = 0L;

    public SittingTargetWeb(final String courtCenter, final String financialYear, final Long annualTarget, final Map<String, Long> monthlyTargets) {
        super();
        this.courtCenter = courtCenter;
        this.financialYear = financialYear;
        this.annualTarget = annualTarget;
        this.monthlyTargets = monthlyTargets;
    }

    public SittingTargetWeb() {
        super();

    }

    public Map<String, Long> getMonthlyTargets() {
        return monthlyTargets;
    }

    public void setMonthlyTargets(final Map<String, Long> monthlyTargets) {
        this.monthlyTargets = monthlyTargets;
    }

    public String getCourtCenter() {
        return courtCenter;
    }

    public void setCourtCenter(final String courtCenter) {
        this.courtCenter = courtCenter;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final String financialYear) {
        this.financialYear = financialYear;
    }

    public Long getAnnualTarget() {
        return annualTarget;
    }

    public void setAnnualTarget(final Long annualTarget) {
        this.annualTarget = annualTarget;
    }

    public Long getMonthlySum() {
        final Set<String> months = monthlyTargets.keySet();
        for (final String month : months) {
            monthlySum += monthlyTargets.get(month) != null ? monthlyTargets.get(month) : 0;
        }
        return monthlySum;
    }
}
