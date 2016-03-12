package uk.co.listing.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import uk.co.listing.domain.constant.TimeEstimationUnit;

public class DateTimeUtilsTest {

    @Test
    public void testGetWeekDates() {
        final Date currentDate = new Date();
        final Date firstDayOfWeek = DateTimeUtils.getFirstDateOfWeek(currentDate);
        final Date lasttDayOfWeek = DateUtils.addDays(firstDayOfWeek, 4);
        final List<Date> listCurrentWeekDate = DateTimeUtils.getWeekDates(1);
        assertTrue(DateUtils.isSameDay(firstDayOfWeek, listCurrentWeekDate.get(0)));
        assertTrue(DateUtils.isSameDay(lasttDayOfWeek, listCurrentWeekDate.get(4)));
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDayOfWeek);
    }

    @Test
    public void testGetCurrentWeek() {
        final Date currentDate = new Date();
        final Date firstDayOfWeek = DateTimeUtils.getFirstDateOfWeek(currentDate);
        final Date lasttDayOfWeek = DateUtils.addDays(firstDayOfWeek, 4);
        final List<Date> listCurrentWeekDate = DateTimeUtils.getCurrentWeek();
        assertTrue(DateUtils.isSameDay(firstDayOfWeek, listCurrentWeekDate.get(0)));
        assertTrue(DateUtils.isSameDay(lasttDayOfWeek, listCurrentWeekDate.get(4)));
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDayOfWeek);
    }

    @Test
    public void testGetCurrentMonth() {
        final Date currentDate = new Date();
        final Date firstDayOfWeek = DateTimeUtils.getFirstDateOfWeek(currentDate);
        final Date lasttDayOfWeek = DateUtils.addDays(firstDayOfWeek, 25);
        final List<Date> listCurrentMonthDate = DateTimeUtils.getCurrentMonth();
        assertTrue(DateUtils.isSameDay(firstDayOfWeek, listCurrentMonthDate.get(0)));
        assertTrue(DateUtils.isSameDay(lasttDayOfWeek, listCurrentMonthDate.get(19)));
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDayOfWeek);
    }

    @Test
    public void testGetFirstDateOfNextWeek() {
        final Calendar calendar = Calendar.getInstance();
        final Date currentDate = new Date();
        for (int i = 0; i < 21; i++) {
            final Date counterDate=DateUtils.addDays(currentDate,i);
            final Date firstDayOfNextWeek=DateTimeUtils.getFirstDateOfNextWeek(counterDate);
            calendar.setTime(firstDayOfNextWeek);
            final int dayOfWeekForFirstDay = calendar.get(Calendar.DAY_OF_WEEK);
            assertTrue(firstDayOfNextWeek.after(counterDate) );
            assertTrue(dayOfWeekForFirstDay == Calendar.MONDAY);

        }
    }

    @Test
    public void testGetFirstDateOfWeek() {
        final Date currentDate = new Date();
        final Date firstDayOfWeek = DateTimeUtils.getFirstDateOfWeek(currentDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        final int dayOfWeekForcurrentDate = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(firstDayOfWeek);
        final int dayOfWeekForFirstDay = calendar.get(Calendar.WEEK_OF_YEAR);
        assertTrue(dayOfWeekForcurrentDate == dayOfWeekForFirstDay);
    }

    @Test
    public void testDayIsSaturday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 9, 10);
        final Date saturday = calendar.getTime();
        final Boolean isMonday = DateTimeUtils.isWeekend(saturday);
        assertTrue(isMonday == true);
    }

    @Test
    public void testDayIsSunday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 9, 11);
        final Date sunday = calendar.getTime();
        final Boolean isMonday = DateTimeUtils.isWeekend(sunday);
        assertTrue(isMonday == true);
    }

    @Test
    public void testDayIsMonday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 9, 12);
        final Date monday = calendar.getTime();
        final Boolean isMonday = DateTimeUtils.isWeekend(monday);
        assertTrue(isMonday == false);
    }

    @Test
    public void testStandardFormat() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 9, 12);
        final Date monday = calendar.getTime();
        final String day = DateTimeUtils.formatToStandardPattern(monday);
        assertTrue(day.equals("12/10/2015"));
    }

    @Test
    public void testEmptyDateStandardFormat() {
        final String day = DateTimeUtils.formatToStandardPattern(null);
        assertTrue(day.equals(""));
    }

    @Test
    public void from1HourToDays(){
        assertTrue(DateTimeUtils.fromDateUnitToDays(1,TimeEstimationUnit.HOURS.getValue())==1);
    }

    @Test
    public void from0HourToDays(){
        assertTrue(DateTimeUtils.fromDateUnitToDays(0,TimeEstimationUnit.HOURS.getValue())==0);
    }

    @Test
    public void from7HourwToDays(){
        assertTrue(DateTimeUtils.fromDateUnitToDays(7.5,TimeEstimationUnit.HOURS.getValue())==1);
    }

    @Test
    public void from8HourwToDays(){
        assertTrue(DateTimeUtils.fromDateUnitToDays(8,TimeEstimationUnit.HOURS.getValue())==1);
    }

    @Test
    public void fromMoreThan8HourwToDays(){
        assertTrue(DateTimeUtils.fromDateUnitToDays(8.1,TimeEstimationUnit.HOURS.getValue())==2);
    }

    @Test
    public void from1DayToDays(){
        assertTrue(DateTimeUtils.fromDateUnitToDays(1,TimeEstimationUnit.DAYS.getValue())==1);
    }

    @Test
    public void from0DayToDays(){
        assertTrue(DateTimeUtils.fromDateUnitToDays(0,TimeEstimationUnit.DAYS.getValue())==0);
    }

   @Test
    public void fromMoreThan8DaysToDays(){
        assertTrue(DateTimeUtils.fromDateUnitToDays(8.1,TimeEstimationUnit.DAYS.getValue())==9);
    }

   @Test
   public void from1WeekToDays(){
       assertTrue(DateTimeUtils.fromDateUnitToDays(1,TimeEstimationUnit.WEEKS.getValue())==5);
   }

   @Test
   public void from0WeekToDays(){
       assertTrue(DateTimeUtils.fromDateUnitToDays(0,TimeEstimationUnit.WEEKS.getValue())==0);
   }

   @Test
   public void fromMoreThan0WeekToDays(){
       assertTrue(DateTimeUtils.fromDateUnitToDays(0.3,TimeEstimationUnit.WEEKS.getValue())==2);
   }


   @Test
   public void fromMoreThan1WeeksToDays(){
       assertTrue(DateTimeUtils.fromDateUnitToDays(1.2,TimeEstimationUnit.WEEKS.getValue())==6);
   }

   @Test
   public void fromAlmost2WeeksToDays(){
       assertTrue(DateTimeUtils.fromDateUnitToDays(1.7,TimeEstimationUnit.WEEKS.getValue())==9);
   }

   @Test
   public void fromMoreThan2WeeksToDays(){
       assertTrue(DateTimeUtils.fromDateUnitToDays(2.2,TimeEstimationUnit.WEEKS.getValue())==11);
   }

   @Test
   public void fromAlmost3WeeksToDays(){
       assertTrue(DateTimeUtils.fromDateUnitToDays(2.7,TimeEstimationUnit.WEEKS.getValue())==14);
   }

   @Test
   public void fromWrongFormat(){
       assertTrue(DateTimeUtils.fromDateUnitToDays(2.7,"Alpha")==0);
   }

    @Test
    public void testStartFinalcialYearBeforeMarch() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 2, 12, 0, 0, 0);
        final Date day = calendar.getTime();

        final Calendar starting = Calendar.getInstance();
        starting.set(2015, 3, 1, 0, 0, 0);
        final Date start = DateTimeUtils.getStartFinancialYearForDate(day);
        assertTrue(start.toString().equals(starting.getTime().toString()));
    }

    @Test
    public void testStartFinalcialYearAfterMarch() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 9, 12, 0, 0, 0);
        final Date day = calendar.getTime();

        final Calendar starting = Calendar.getInstance();
        starting.set(2015, 3, 1, 0, 0, 0);
        final Date start = DateTimeUtils.getStartFinancialYearForDate(day);
        assertTrue(start.toString().equals(starting.getTime().toString()));
    }

    @Test
    public void testStartFinalcialYearMarch() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 2, 31, 21, 20, 40);
        final Date day = calendar.getTime();

        final Calendar starting = Calendar.getInstance();
        starting.set(2015, 3, 1, 0, 0, 0);
        final Date start = DateTimeUtils.getStartFinancialYearForDate(day);
        assertTrue(start.toString().equals(starting.getTime().toString()));
    }

    @Test
    public void testStartFinalcialYearApril() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 3, 1, 4, 20, 40);
        final Date day = calendar.getTime();

        final Calendar starting = Calendar.getInstance();
        starting.set(2016, 3, 1, 0, 0, 0);
        final Date start = DateTimeUtils.getStartFinancialYearForDate(day);
        assertTrue(start.toString().equals(starting.getTime().toString()));
    }

    @Test
    public void testStartFinancialYearForYear() {
        final Calendar starting = Calendar.getInstance();
        starting.set(2015, 3, 1, 0, 0, 0);
        final Date start = DateTimeUtils.getFinancialStartDateForYear("2015");
        assertEquals(start.toString(), starting.getTime().toString());
    }

    @Test
    public void testEndFinancialYearForYear() {
        final Calendar ending = Calendar.getInstance();
        ending.set(2016, 2, 31, 23, 59, 59);
        final Date end = DateTimeUtils.getFinancialEndDateForYear("2015");
        assertEquals(end.toString(), ending.getTime().toString());
    }

    @Test
    public void testEndFinalcialYearBeforeApril() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 2, 12, 0, 0, 0);
        final Date day = calendar.getTime();

        final Calendar starting = Calendar.getInstance();
        starting.set(2016, 2, 31, 23, 59, 59);
        final Date start = DateTimeUtils.getEndFinancialYearForDate(day);
        assertTrue(start.toString().equals(starting.getTime().toString()));
    }

    @Test
    public void testEndFinalcialYearAfterApril() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 9, 12, 0, 0, 0);
        final Date day = calendar.getTime();

        final Calendar ending = Calendar.getInstance();
        ending.set(2016, 2, 31, 23, 59, 59);
        final Date start = DateTimeUtils.getEndFinancialYearForDate(day);
        assertTrue(start.toString().equals(ending.getTime().toString()));
    }

    @Test
    public void testEndFinalcialYearApril() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 3, 1, 5, 20, 40);
        final Date day = calendar.getTime();

        final Calendar ending = Calendar.getInstance();
        ending.set(2017, 2, 31, 23, 59, 59);
        final Date start = DateTimeUtils.getEndFinancialYearForDate(day);
        assertTrue(start.toString().equals(ending.getTime().toString()));
    }

    @Test
    public void testEndFinalcialYearMarch() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 2, 31, 5, 20, 40);
        final Date day = calendar.getTime();

        final Calendar ending = Calendar.getInstance();
        ending.set(2016, 2, 31, 23, 59, 59);
        final Date start = DateTimeUtils.getEndFinancialYearForDate(day);
        assertTrue(start.toString().equals(ending.getTime().toString()));
    }

    @Test
    public void testBeginningDay() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 2, 31, 5, 20, 40);
        final Date day = calendar.getTime();

        final Calendar startDay = Calendar.getInstance();
        startDay.set(2016, 2, 31, 0, 0, 0);
        final Date start = DateTimeUtils.getBeginningOfTheDay(day);
        assertTrue(start.toString().equals(startDay.getTime().toString()));
    }

    @Test
    public void testEndOfDay() {
    	final Calendar calendar = Calendar.getInstance();
    	calendar.set(2016, 2, 31, 5, 20, 40);
    	final Date day = calendar.getTime();

    	final Calendar startDay = Calendar.getInstance();
    	startDay.set(2016, 2, 31, 23, 59, 0);
    	final Date start = DateTimeUtils.getEndOfTheDay(day);
    	assertTrue(start.toString().equals(startDay.getTime().toString()));
    }

    @Test
    public void shouldGetFirstDateOfMonth() {
        final GregorianCalendar gc = new GregorianCalendar(2016, Calendar.AUGUST, 15);
        final Date date = DateTimeUtils.getStartOfMonth(gc.getTime());

        final Calendar cal_2 = Calendar.getInstance();
        cal_2.setTime(date);
        assertEquals("01/08/2016", DateTimeUtils.formatToStandardPattern(date));
        assertEquals(0, cal_2.get(Calendar.HOUR));
        assertEquals(0, cal_2.get(Calendar.MINUTE));
        assertEquals(0, cal_2.get(Calendar.SECOND));
    }

    @Test
    public void shouldGetLastDateOfMonth() {
        final GregorianCalendar gc = new GregorianCalendar(2016, Calendar.AUGUST, 15);
        final Date date = DateTimeUtils.getEndOfMonth(gc.getTime());

        final Calendar cal_2 = Calendar.getInstance();
        cal_2.setTime(date);
        assertEquals("31/08/2016", DateTimeUtils.formatToStandardPattern(date));
        assertEquals(23, cal_2.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, cal_2.get(Calendar.MINUTE));
        assertEquals(59, cal_2.get(Calendar.SECOND));
    }

    @Test
    public void getPreviousWorkingDateFromTuesday() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.SEPTEMBER, 8);
        final Date date = DateTimeUtils.getPreviousWorkingDateFromDate(gc.getTime());

        final Calendar cal_2 = Calendar.getInstance();
        cal_2.setTime(date);
        assertEquals("07/09/2015", DateTimeUtils.formatToStandardPattern(date));
    }

    @Test
    public void getPreviousWorkingDateFromSunday() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.SEPTEMBER, 6);
        final Date date = DateTimeUtils.getPreviousWorkingDateFromDate(gc.getTime());

        final Calendar cal_2 = Calendar.getInstance();
        cal_2.setTime(date);
        assertEquals("04/09/2015", DateTimeUtils.formatToStandardPattern(date));
    }

    @Test
    public void getPreviousFridayFromTuesday() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.SEPTEMBER, 8);
        final Date date = DateTimeUtils.getPreviousFridayFromDate(gc.getTime());

        final Calendar cal_2 = Calendar.getInstance();
        cal_2.setTime(date);
        assertEquals("04/09/2015", DateTimeUtils.formatToStandardPattern(date));
    }

    @Test
    public void getPreviousFridayFromSunday() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.SEPTEMBER, 6);
        final Date date = DateTimeUtils.getPreviousFridayFromDate(gc.getTime());

        final Calendar cal_2 = Calendar.getInstance();
        cal_2.setTime(date);
        assertEquals("04/09/2015", DateTimeUtils.formatToStandardPattern(date));
    }

    @Test
    public void getPreviousFridayFromFriday() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.SEPTEMBER, 11);
        final Date date = DateTimeUtils.getPreviousFridayFromDate(gc.getTime());

        final Calendar cal_2 = Calendar.getInstance();
        cal_2.setTime(date);
        assertEquals("04/09/2015", DateTimeUtils.formatToStandardPattern(date));
    }

    @Test
    public void testGetDaysBetween() {
        final Date today = new Date();
        final Date tommorow = DateUtils.addDays(today, 1);
        assertEquals(DateTimeUtils.getDaysBetween(today, tommorow), 1);
        assertEquals(DateTimeUtils.getDaysBetween(today, today), 0);
        assertEquals(DateTimeUtils.getDaysBetween(tommorow, today), -1);
    }

    @Test
    public void testGetWorkingDaysBetween() {
        final Date today = DateTimeUtils.parseDate("21/10/2015");
        final Date tommorow = DateUtils.addDays(today, 1);
        assertEquals(DateTimeUtils.getWorkingDaysBetween(today, tommorow), 1);
        assertEquals(DateTimeUtils.getWorkingDaysBetween(today, today), 0);
        assertEquals(DateTimeUtils.getWorkingDaysBetween(today, DateUtils.addDays(today, 14)), 10);
    }

    @Test
    public void testparseDate() {
        assertEquals(DateTimeUtils.parseDate("2015-05-27").toString(), "Wed May 27 00:00:00 BST 2015");
        assertEquals(DateTimeUtils.parseDate("27/05/2015").toString(), "Wed May 27 00:00:00 BST 2015");
    }

    @Test(expected = Exception.class)
    public void testparseDateException() {
        DateTimeUtils.parseDate("20115-015-27");
    }

    @Test
    public void testGetEndWorkingDateFromStartDate() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.OCTOBER, 8);
        final Date beginningOfTorrijos = gc.getTime();
        final GregorianCalendar gc1 = new GregorianCalendar(2015, Calendar.OCTOBER, 12);
        final Date endOfTorrijos = gc1.getTime();
        assertEquals(DateTimeUtils.getEndWorkingDateFromStartDate(beginningOfTorrijos, 3), endOfTorrijos);
    }

    @Test
    public void testGetEndWorkingDateFromStartDateZeroDays() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.OCTOBER, 8);
        final Date beginningOfTorrijos = gc.getTime();
        assertEquals(DateTimeUtils.getEndWorkingDateFromStartDate(beginningOfTorrijos, 0), beginningOfTorrijos);
    }

    @Test
    public void testGetEndWorkingDateFromStartDateNegativeDays() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.OCTOBER, 8);
        final Date beginningOfTorrijos = gc.getTime();
        assertEquals(DateTimeUtils.getEndWorkingDateFromStartDate(beginningOfTorrijos, -1), beginningOfTorrijos);
    }

    @Test
    public void testGetEndWorkingDateFromStartDateSaturday() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.OCTOBER,10);
        final Date beginningOfTorrijos = gc.getTime();
        final GregorianCalendar gc1 = new GregorianCalendar(2015, Calendar.OCTOBER, 12);
        final Date endOfTorrijos = gc1.getTime();
        assertEquals(DateTimeUtils.getEndWorkingDateFromStartDate(beginningOfTorrijos, 1), endOfTorrijos);
    }

    @Test
    public void testGetDayOfMonth() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.OCTOBER, 11);
        final Date torrijos = gc.getTime();
        assertEquals(DateTimeUtils.getDayOfMonth(torrijos), 11);
    }

    @Test
    public void testGetMonth() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.OCTOBER, 11);
        final Date torrijos = gc.getTime();
        assertEquals(DateTimeUtils.getMonth(torrijos), 9);
    }

    @Test
    public void testGetFirstMonth() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.JANUARY, 11);
        final Date january = gc.getTime();
        assertEquals(DateTimeUtils.getMonth(january), 0);
    }

    @Test
    public void testGetLastMonth() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.DECEMBER, 11);
        final Date december = gc.getTime();
        assertEquals(DateTimeUtils.getMonth(december), 11);
    }

    @Test
    public void testGetYear() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.OCTOBER, 11);
        final Date torrijos = gc.getTime();
        assertEquals(DateTimeUtils.getYear(torrijos), 2015);
    }

    @Test
    public void testGetWorkingDayBeforeDate() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.OCTOBER, 15);
        final GregorianCalendar gc1 = new GregorianCalendar(2015, Calendar.OCTOBER, 12);
        final Date firstDate = gc.getTime();
        final Date secondDate = gc1.getTime();
        assertEquals(DateTimeUtils.getWorkingDayBeforeDate(firstDate,3), secondDate);
    }

    @Test
    public void testGetWorkingDayBeforeDateOnWeekend() {
        final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.OCTOBER, 13);
        final GregorianCalendar gc1 = new GregorianCalendar(2015, Calendar.OCTOBER, 9);
        final Date firstDate = gc.getTime();
        final Date secondDate = gc1.getTime();
        assertEquals(DateTimeUtils.getWorkingDayBeforeDate(firstDate,2), secondDate);
    }


    @Test
    public void testGetHour() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 3, 1, 5, 20, 40);
        final Date day = calendar.getTime();
        assertEquals(DateTimeUtils.getHour(day), 5);
    }

    @Test
    public void testGetMinute() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 3, 1, 5, 20, 40);
        final Date day = calendar.getTime();
        assertEquals(DateTimeUtils.getMinute(day), 20);
    }

    @Test
    public void testGetSecond() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 3, 1, 5, 20, 40);
        final Date day = calendar.getTime();
        assertEquals(DateTimeUtils.getSecond(day), 40);
    }


    @Test
    public void testGetNoOfWorkingDaysFromStartDateWeekEnd() {
        final Calendar calendar = Calendar.getInstance();
        // below date day is Friday, add two days should be Monday and Tuesday
        calendar.set(2015, 5, 29, 5, 20, 40);
        final Date day = calendar.getTime();
        final List<Date> dates = DateTimeUtils.getWorkingDaysFromStartDate(day, 2);
        assertNotNull(dates);
        assertEquals(dates.size(), 2);

        // both dates are not weekend
        assertTrue(!DateTimeUtils.isWeekend(dates.get(0)));
        assertTrue(!DateTimeUtils.isWeekend(dates.get(1)));
    }

    @Test
    public void testGetNoOfWorkingDaysFromStartDateWeekDays() {
        final Calendar calendar = Calendar.getInstance();
        // below date day is Wednesday, add two days should be Thursday and
        // Friday
        calendar.set(2015, 5, 27, 5, 20, 40);
        final Date day = calendar.getTime();
        final List<Date> dates = DateTimeUtils.getWorkingDaysFromStartDate(day, 2);
        assertNotNull(dates);
        assertEquals(dates.size(), 2);

        // both dates are not weekend
        assertTrue(!DateTimeUtils.isWeekend(dates.get(0)));
        assertTrue(!DateTimeUtils.isWeekend(dates.get(1)));
    }
}
