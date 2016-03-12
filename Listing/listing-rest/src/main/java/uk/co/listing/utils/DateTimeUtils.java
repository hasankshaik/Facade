package uk.co.listing.utils;

import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.exceptions.CcsException;

public class DateTimeUtils {

    public static void checkDates(final Date startDay, final Date endDay) {
        if (DateTimeUtils.isWeekend(startDay) || DateTimeUtils.isWeekend(endDay)) {
            throw new CcsException(getMessage("ONE_OF_THE_DATES_IS_WEEKEND") + startDay + "," + endDay);
        }
        if (startDay.after(endDay)) {
            throw new CcsException(getMessage("START_DAY_BEFORE_ENDDATE", new String[] {}, new String[] { DateTimeUtils.formatToStandardPattern(startDay) }));
        }
    }

    public static String formatToStandardPattern(final Date date) {
        if (date == null) {
            return "";
        }
        return DateFormatUtils.format(date, DateFormats.STANDARD.getValue());
    }

    /**
     * Function to transform from any unit to days We are working with the assumption that a day is 8h, and a week is 5 days
     *
     * @param number
     * @param units
     * @return
     */
    public static int fromDateUnitToDays(final double number, final String units) {
        int result = 0;
        if (TimeEstimationUnit.HOURS.getValue().equals(units)) {
            result = (int) Math.ceil(number / 8);
        } else if (TimeEstimationUnit.DAYS.getValue().equals(units)) {
            result = (int) Math.ceil(number);
        } else if (TimeEstimationUnit.WEEKS.getValue().equals(units)) {
            result = (int) Math.ceil(number * 5);
        }
        return result;
    }

    public static Date getBeginningOfTheDay(final Date date) {
        final Calendar result = Calendar.getInstance();
        result.setTime(date);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        return result.getTime();
    }

    public static List<Date> getCurrentMonth() {
        return getWeekDates(4);
    }

    public static List<Date> getCurrentWeek() {
        return getWeekDates(1);
    }

    public static int getDayOfMonth(final Date date) {
        return DateUtils.toCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    public static long getDaysBetween(final Date startDate, final Date endDate) {
        final Instant startDateInstant = DateUtils.truncate(startDate, Calendar.DATE).toInstant();
        final Instant endDateInstant = DateUtils.truncate(endDate, Calendar.DATE).toInstant();
        return ChronoUnit.DAYS.between(LocalDateTime.ofInstant(startDateInstant, ZoneId.systemDefault()), LocalDateTime.ofInstant(endDateInstant, ZoneId.systemDefault()));
    }

    public static long getWorkingDaysBetween( Date startDate, final Date endDate) {
        int count = 0;
        while (startDate.before(endDate)) {
            if (!isWeekend(startDate)) {
                count++;
            }
            startDate=DateUtils.addDays(startDate, 1);
        }
        return count;
    }

    public static Date getEndFinancialYearForDate(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final Calendar result = Calendar.getInstance();
        result.setTime(date);
        if (calendar.get(Calendar.MONTH) > Calendar.MARCH) {
            result.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        }

        result.set(Calendar.DAY_OF_MONTH, 31);
        result.set(Calendar.MONDAY, Calendar.MARCH);
        result.set(Calendar.HOUR_OF_DAY, 23);
        result.set(Calendar.MINUTE, 59);
        result.set(Calendar.SECOND, 59);
        return result.getTime();
    }

    public static Date getEndOfMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Date dateResult = getStartOfMonth(cal.getTime());
        dateResult = DateUtils.addMonths(dateResult, 1);
        dateResult = DateUtils.addSeconds(dateResult, -1);
        return dateResult;
    }

    public static Date getEndOfTheDay(final Date date) {
        final Calendar result = Calendar.getInstance();
        result.setTime(date);
        result.set(Calendar.HOUR_OF_DAY, 23);
        result.set(Calendar.MINUTE, 59);
        result.set(Calendar.SECOND, 0);
        return result.getTime();
    }

    public static Date getEndWorkingDateFromStartDate(final Date startDate, final int noOfDays) {

        Date endDate = startDate;
        if (noOfDays > 0) {
            final List<Date> listDates = getWorkingDaysFromStartDate(startDate, noOfDays);
            if (CollectionUtils.isNotEmpty(listDates)) {
                endDate = listDates.get(listDates.size() - 1);
            }
        }
        return endDate;
    }

    public static Date getFinancialEndDateForYear(final String year) {
        final Calendar result = Calendar.getInstance();
        result.set(Calendar.YEAR, new Integer(year) + 1);
        result.set(Calendar.DAY_OF_MONTH, 31);
        result.set(Calendar.MONDAY, Calendar.MARCH);
        result.set(Calendar.HOUR_OF_DAY, 23);
        result.set(Calendar.MINUTE, 59);
        result.set(Calendar.SECOND, 59);
        return result.getTime();
    }

    public static Date getFinancialStartDateForYear(final String year) {
        final Calendar result = Calendar.getInstance();
        result.set(Calendar.YEAR, new Integer(year));
        result.set(Calendar.DAY_OF_MONTH, 1);
        result.set(Calendar.MONDAY, Calendar.APRIL);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        return result.getTime();
    }

    public static Date getFirstDateOfNextWeek(final Date date) {
        return DateTimeUtils.getFirstDateOfWeek(DateUtils.addDays(date, 7));
    }

    public static Date getFirstDateOfWeek(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return DateUtils.addDays(calendar.getTime(), -(dayOfWeek - 2));
    }

    public static int getHour(final Date date) {
        return DateUtils.toCalendar(date).get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(final Date date) {
        return DateUtils.toCalendar(date).get(Calendar.MINUTE);
    }

    public static int getMonth(final Date date) {
        return DateUtils.toCalendar(date).get(Calendar.MONTH);
    }

    public static Date getPreviousFridayFromDate(final Date startDate) {
        Date endDate = getPreviousWorkingDateFromDate(startDate);
        final Calendar day = Calendar.getInstance();
        day.setTime(endDate);
        while (day.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            endDate = getPreviousWorkingDateFromDate(endDate);
            day.setTime(endDate);
        }
        return endDate;
    }

    public static Date getPreviousWorkingDateFromDate(final Date startDate) {
        Date endDate = DateUtils.addDays(startDate, -1);
        while (DateTimeUtils.isWeekend(endDate)) {
            endDate = DateUtils.addDays(endDate, -1);
        }
        return endDate;
    }

    public static int getSecond(final Date date) {
        return DateUtils.toCalendar(date).get(Calendar.SECOND);
    }

    public static Date getStartFinancialYearForDate(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final Calendar result = Calendar.getInstance();
        result.setTime(date);
        if (calendar.get(Calendar.MONTH) < Calendar.APRIL) {
            result.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        }
        result.set(Calendar.DAY_OF_MONTH, 1);
        result.set(Calendar.MONTH, Calendar.APRIL);
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        return result.getTime();
    }

    public static Date getStartOfMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static List<Date> getWeekDates(final int numberOfWeek) {
        final Date currentDate = new Date();
        final Calendar calendar = Calendar.getInstance();
        final List<Date> listDates = new ArrayList<Date>();
        for (int i = 0; i < numberOfWeek; i++) {
            calendar.setTime(DateUtils.addDays(currentDate, 7 * i));
            final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            final Date weekStartMonday = DateUtils.addDays(calendar.getTime(), -(dayOfWeek - 2));
            listDates.add(weekStartMonday);
            listDates.add(DateUtils.addDays(weekStartMonday, 1));
            listDates.add(DateUtils.addDays(weekStartMonday, 2));
            listDates.add(DateUtils.addDays(weekStartMonday, 3));
            listDates.add(DateUtils.addDays(weekStartMonday, 4));
        }
        return listDates;
    }

    public static Date getWorkingDayBeforeDate(final Date startDate, final int noOfDays) {
        Date theDate = startDate;
        theDate = DateUtils.addDays(theDate, -noOfDays);
        while (DateTimeUtils.isWeekend(theDate)) {
            theDate = DateUtils.addDays(theDate, -1);
        }
        return theDate;
    }

    public static List<Date> getWorkingDaysFromStartDate(final Date startDate, final int noOfDays) {
        Date theDate = startDate;
        final List<Date> listDates = new ArrayList<>();
        while (listDates.size() < noOfDays) {
            if (!DateTimeUtils.isWeekend(theDate)) {
                listDates.add(theDate);
            }
            theDate = DateUtils.addDays(theDate, 1);
        }
        return listDates;
    }

    public static int getYear(final Date date) {
        return DateUtils.toCalendar(date).get(Calendar.YEAR);
    }

    public static Boolean isWeekend(final Date date) {
        final Calendar day = Calendar.getInstance();
        day.setTime(date);
        if ((day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
            return true;
        }
        return false;
    }

    public static Date parseDate(final String startDate) {
        try {
            return DateUtils.parseDateStrictly(startDate, DateFormats.getPatterns());
        } catch (final ParseException e) {
            throw new CcsException(e);
        }
    }

    private DateTimeUtils() {

    }

}
