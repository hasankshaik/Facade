package uk.co.listing.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.co.listing.dao.CourtCenterDao;
import uk.co.listing.dao.SittingDao;
import uk.co.listing.dao.SittingTargetDao;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.MonthlyTarget;
import uk.co.listing.domain.SittingTarget;
import uk.co.listing.rest.response.SittingTargetWeb;
import uk.co.listing.utils.DateTimeUtils;

@Service("sittingService")
@Transactional(readOnly = true)
public class SittingService implements ISittingsService {

    private static final Logger LOGGER = Logger.getLogger(SittingService.class);

    @Autowired
    private CourtCenterDao courtCenterDao;

    @Autowired
    private SittingTargetDao sittingTargetDao;

    @Autowired
    private SittingDao sittingDao;

    /**
     * 
     * @param courtCenterName
     * @param date
     *            as in a java.util.date
     * @return
     */
    @Override
    public Long getAnnualActualSittingDays(final String courtCenterName, final Date date) {
        final Date startDate = DateTimeUtils.getStartFinancialYearForDate(date);
        final Date endDate = DateTimeUtils.getEndFinancialYearForDate(date);
        return sittingDao.countActualSittingDaysBetweenDates(courtCenterName, startDate, endDate);
    }

    /**
     * financial year as a string to be in form of yyyy-yyyy (2015-2016)
     */
    @Override
    public Long getAnnualActualSittingDays(final String courtCenterName, final String financialYear) {
        final String startYear = financialYear.split("-")[0];
        final Date startDate = DateTimeUtils.getFinancialStartDateForYear(startYear);
        final Date endDate = DateTimeUtils.getFinancialEndDateForYear(startYear);
        return sittingDao.countActualSittingDaysBetweenDates(courtCenterName, startDate, endDate);
    }

    @Override
    public SittingTarget getAnnualSittingsTarget(final String courtCenter, final String financialYear) {
        return sittingTargetDao.findSittingTarget(courtCenter, financialYear);
    }

    /**
     * financial year as a string to be in form of yyyy-yyyy (2015-2016)
     */
    @Override
    public Map<String, Long> getMonthlyActualSittingDays(final String courtCentreName, final String financialYear) {
        final Map<String, Long> monthlyActuals = new HashMap<String, Long>();
        final String startYear = financialYear.split("-")[0];
        final Date startOfFinancialYear = DateTimeUtils.getFinancialStartDateForYear(startYear);
        final Date endOfFinancialYear = DateTimeUtils.getFinancialEndDateForYear(startYear);
        final Calendar monthRoller = Calendar.getInstance();
        monthRoller.setTime(startOfFinancialYear);

        while (monthRoller.getTime().before(endOfFinancialYear)) {
            final Date startOfMonth = DateTimeUtils.getStartOfMonth(monthRoller.getTime());
            final Date endOfMonth = DateTimeUtils.getEndOfMonth(monthRoller.getTime());
            final Long actualSittings = sittingDao.countActualSittingDaysBetweenDates(courtCentreName, startOfMonth, endOfMonth);
            monthlyActuals.put(monthRoller.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH), actualSittings);
            monthRoller.add(Calendar.MONTH, 1);
        }
        return monthlyActuals;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveAnnualSittingTarget(final SittingTargetWeb annualSittingWeb) {
        SittingTarget sittingTarget = sittingTargetDao.findSittingTarget(annualSittingWeb.getCourtCenter(), annualSittingWeb.getFinancialYear());

        if (sittingTarget != null) {
            sittingTarget.setSittings(annualSittingWeb.getAnnualTarget());
        } else {
            final CourtCenter courtCenter = courtCenterDao.findCourtCentreByName(annualSittingWeb.getCourtCenter());
            sittingTarget = new SittingTarget(courtCenter, annualSittingWeb.getFinancialYear(), annualSittingWeb.getAnnualTarget());
        }
        populateMonthlyTarget(annualSittingWeb, sittingTarget);
        sittingTargetDao.save(sittingTarget);
        LOGGER.info("saved sitting targets for: " + annualSittingWeb.getFinancialYear());
    }

    private void addNewMonthlyTarget(final SittingTargetWeb annualSittingWeb, final SittingTarget sittingTarget, final Set<MonthlyTarget> monthlyTargetSet) {
        final Map<String, Long> monthlyTargetMap = annualSittingWeb.getMonthlyTargets();
        if (monthlyTargetMap != null) {
            for (final String month : monthlyTargetMap.keySet()) {
                final MonthlyTarget monthlyTarget = new MonthlyTarget();
                monthlyTarget.setMonth(month);
                monthlyTarget.setSitting(monthlyTargetMap.get(month));
                monthlyTarget.setSittingTarget(sittingTarget);
                monthlyTargetSet.add(monthlyTarget);
            }
        }
    }

    private void populateMonthlyTarget(final SittingTargetWeb annualSittingWeb, final SittingTarget sittingTarget) {
        final Set<MonthlyTarget> monthlyTargetsSet = sittingTarget.getMonthlyTargets();
        final Iterator<MonthlyTarget> monthIter = monthlyTargetsSet.iterator();
        final Map<String, Long> monthlyTargetsWebMap = annualSittingWeb.getMonthlyTargets();
        if (monthlyTargetsWebMap != null) {
            while (monthIter.hasNext()) {
                final MonthlyTarget monthTar = monthIter.next();
                if (monthlyTargetsWebMap.keySet().contains(monthTar.getMonth()) || monthlyTargetsWebMap.get(monthTar.getMonth()) == null) { // update
                    monthTar.setSitting(monthlyTargetsWebMap.get(monthTar.getMonth()));
                } else { // remove
                    monthlyTargetsSet.remove(monthTar);
                }
            }
            // finally add new ones
            addNewMonthlyTarget(annualSittingWeb, sittingTarget, monthlyTargetsSet);
        }
    }

}
