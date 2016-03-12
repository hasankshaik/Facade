package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.TimeoutException;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FindSlotDefaultTimeWindowSteps {
    public static Log LOGGER = LogFactory.getLog(FindSlotDefaultTimeWindowSteps.class);
    public static final String COURT_CENTER = "Centre739";
    public static final String COURT_ROOM = "Room739-1";
    public static String courtCentreQueryString = "?courtCenter=" + COURT_CENTER;
    public static Date SendingDate = null;
    public static Date KPIDate, PCMDate, CTLDate1, CTLDate2, StartDate, EndDate;
            
    @After("@CCS-739")
    public void afterScenario() {
    	/* Deallocate judge from start and end dates after each scenario so we see only the
    	 * expected results in the Search slots results window. Close the search results
    	 * window if present.
    	 */
    	if (SeleniumConnector.isElementPresent("cancelButton")) {
    		SeleniumConnector.clickAndWait("cancelButton");
    	}
    	CommonSteps.deallocateJudge("UniqueJudge", COURT_ROOM, StartDate, StartDate);
    	SeleniumConnector.waitForTextPresent("Judge no longer allocated to these sessions");
    	CommonSteps.deallocateJudge("UniqueJudge", COURT_ROOM, EndDate, EndDate);
    	SeleniumConnector.waitForTextPresent("Judge no longer allocated to these sessions");
    }
    
    @Given("^Case \"(.*?)\" has Sending date today (-?\\d+) days$")
    public void case_has_Sending_date_days(String caseNo, int daysString) throws Throwable {
    	// Calculate the sending date for the case
    	SendingDate = null;
    	SendingDate = DateUtils.addDays(new Date(), daysString);
    }
    
    @Given("^Case \"(.*?)\" has (\\d+) Defendants?$")
    public void case_has_Defendant(String caseNo, int noOfDef) throws Throwable {
        // Defendant is setup via import.sql, so nothing to do here.
    }
    
    @Given("^Defendant number (\\d+) has CTL Expires On date: Sending date \"(.*?)\" days$")
    public void defendant_has_CTL_Expires_On_date_days(int defNo, String noDays) throws Throwable {
        // Defendant CTL date is setup via import.sql, we need to re-calculate those CTL days here.
    	if (!StringUtils.isBlank(noDays)) {
    		if (defNo == 1) {
    			CTLDate1 = null;
    			CTLDate1 = DateUtils.addDays(SendingDate, Integer.valueOf(noDays));
    		} else if (defNo == 2) {
    			CTLDate2 = null;
    			CTLDate2 = DateUtils.addDays(SendingDate, Integer.valueOf(noDays));
    		}
    	}
    }
    
    @Given("^KPI date is Sending date \\+ (\\d+) days$")
    public void kpi_date_is_Sending_date_days(int addDays) throws Throwable {
        // Calculate KPI date
    	KPIDate = null;
    	KPIDate = DateUtils.addDays(SendingDate, addDays);
    }
    
    @When("^Judge is allocated to End date: Friday prior to \"(.*?)\"$")
    public void judges_is_allocated_to_End_date_Friday_prior_to(String listBeforeDate) throws Throwable {
    	/* Calculate end date for Time Window
    	 * so that we can allocate judge to that day.
    	 * When we search for slots, this date must
    	 * be returned in the search results. Thus we can 
    	 * verify that boundary values for the Time Window.
    	 */
        EndDate = null;
    	if ("KPI Date".equals(listBeforeDate)) {
    		EndDate = findDayBeforeDate(KPIDate, 6);
    	} else if ("CTL Date 1".equals(listBeforeDate)) {
    		EndDate = findDayBeforeDate(CTLDate1, 6);
		} else if ("CTL Date 2".equals(listBeforeDate)) {
			EndDate = findDayBeforeDate(CTLDate2, 6);
		} else if (listBeforeDate.contains("today")) {
			EndDate = findDayBeforeDate(DateUtils.addDays(new Date(), CommonSteps.parseDigit(listBeforeDate)), 6);
		} else if (listBeforeDate.contains("PCMDate")) {
			EndDate = findDayBeforeDate(DateUtils.addDays(PCMDate, CommonSteps.parseDigit(listBeforeDate)), 6);
		}
    }

    public Date findDayBeforeDate(Date referenceDate, int day) {
    	/* If the day of week is on the same day as referenceDate
    	 * then use previous week, else return current week's day
    	 */
    	Calendar cal = Calendar.getInstance();
        cal.setTime(referenceDate);
        while(true){
        	cal.add(Calendar.DATE, -1);
        	if (cal.get(Calendar.DAY_OF_WEEK) == day)
        	{
        		break;
        	}
        }
        return cal.getTime();
    }
    
    @When("^Judge is allocated to Start date: End date \"(.*?)\" days$")
    public void judges_is_allocated_to_Start_date_End_date_days(String dayOffset) throws Throwable {
    	/* Calculate start date for Time Window
    	 * so that we can allocate judge to that day.
    	 * When we search for slots, this date must
    	 * be returned in the search results. Thus we can 
    	 * verify that boundary values for the Time Window.
    	 */
    	StartDate = null;
    	StartDate = DateUtils.addDays(EndDate, Integer.valueOf(dayOffset));
    	CommonSteps.allocateJudge("UniqueJudge", COURT_ROOM, StartDate, StartDate);
    	SeleniumConnector.waitForTextPresent("Judge has been allocated");
    	CommonSteps.allocateJudge("UniqueJudge", COURT_ROOM, EndDate, EndDate);
    	SeleniumConnector.waitForTextPresent("Judge has been allocated");
    }
    
    @When("^Judge is allocated to Start date: \"(.*?)\"$")
    public void judge_is_allocated_to_Start_date(String dayString) throws Throwable {
        // Calculate start date depending on current day or PCM date
    	StartDate = null;
    	if (dayString.contains("today")) {
    		StartDate = DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), CommonSteps.parseDigit(dayString) + 1);
    	} else if (dayString.contains("PCMDate")) {
    		StartDate = DateTimeUtils.getEndWorkingDateFromStartDate(PCMDate, CommonSteps.parseDigit(dayString) + 1);
		}
    	CommonSteps.allocateJudge("UniqueJudge", COURT_ROOM, StartDate, StartDate);
    	SeleniumConnector.waitForTextPresent("Judge has been allocated");
    	CommonSteps.allocateJudge("UniqueJudge", COURT_ROOM, EndDate, EndDate);
    	SeleniumConnector.waitForTextPresent("Judge has been allocated");
    }
    
    @Given("^Case \"(.*?)\" has hearing \"(.*?)\"$")
    public void case_has_hearing(String arg1, String arg2) throws Throwable {
        // Nothing to do here as the hearing has been setup in import.sql
    }
    
    public void check_page_text(String pageName, String courtCentreName) {
    	if(pageName.equals("schedule")) {
        	SeleniumConnector.waitForTextPresent("The Crown Court at " + courtCentreName);
        } else if (pageName.equals("manage-case")) {
        	SeleniumConnector.waitForTextPresent(courtCentreName);
		}
    }
    
    @When("^I go to \"(.*?)\" page for court centre \"(.*?)\"$")
    public void i_go_to_page_for_court_centre(String pageName, String courtCentreName) throws Throwable {
        // Move this to Common Steps
    	SeleniumConnector.openAndWait(pageName + "?courtCenter="+ courtCentreName);
		try {
			check_page_text(pageName, courtCentreName);
		} catch (final TimeoutException e) {
			SeleniumConnector.openAndWait(pageName + "?courtCenter="+ courtCentreName);
			check_page_text(pageName, courtCentreName);
		}
    }
    
    @Given("^Case \"(.*?)\" has PCMH Date \"(.*?)\"$")
    public void case_has_PCMH_Date(String caseNo, String pcmhDate) throws Throwable {
    	PCMDate = null;
    	PCMDate = DateUtils.addDays(SendingDate, CommonSteps.parseDigit(pcmhDate));
    }

    @Then("^Search End date is Friday prior to \"(.*?)\"$")
    public void search_End_date_is_Friday_prior_to(String arg1) throws Throwable {
        // Verify End date is present in Search results
    	String slotDate = DateTimeUtils.formatToStandardPattern(EndDate);
        String courtRoomNameId = COURT_ROOM + "-" + slotDate;
        String slotDateId = COURT_ROOM + "-" + slotDate + "-date";
        LOGGER.info("SendingDate: " + DateTimeUtils.formatToStandardPattern(SendingDate));
        LOGGER.info("KPIDate: " + DateTimeUtils.formatToStandardPattern(KPIDate));
        LOGGER.info("CTLDate1: " + DateTimeUtils.formatToStandardPattern(CTLDate1));
        LOGGER.info("CTLDate2: " + DateTimeUtils.formatToStandardPattern(CTLDate2));
        LOGGER.info("StartDate: " + DateTimeUtils.formatToStandardPattern(StartDate));
        LOGGER.info("PCMDate: " + DateTimeUtils.formatToStandardPattern(PCMDate));
        LOGGER.info("EndDate: " + DateTimeUtils.formatToStandardPattern(EndDate));
        SeleniumConnector.waitForTextPresent("Available Slot Dates For Hearing");
        assertTrue(COURT_ROOM.equals(SeleniumConnector.getTextFromElementBySelector(courtRoomNameId, "id")));
        assertTrue(slotDate.equals(SeleniumConnector.getTextFromElementBySelector(slotDateId, "id")));
    }

    @Then("^Search Start date is \"(.*?)\"$")
    public void search_Start_date_is_Search_End_date_days(String daysOffset) throws Throwable {
        // Verify Start date is present in Search results
    	String slotDate = DateTimeUtils.formatToStandardPattern(StartDate);
        String courtRoomNameId = COURT_ROOM + "-" + slotDate;
        String slotStartDateId = COURT_ROOM + "-" + slotDate + "-date";
        SeleniumConnector.waitForTextPresent("Available Slot Dates For Hearing");
        assertTrue(COURT_ROOM.equals(SeleniumConnector.getTextFromElementBySelector(courtRoomNameId, "id")));
        assertTrue(slotDate.equals(SeleniumConnector.getTextFromElementBySelector(slotStartDateId, "id")));
    }
}