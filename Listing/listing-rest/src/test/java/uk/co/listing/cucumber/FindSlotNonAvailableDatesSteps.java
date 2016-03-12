package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.Keys;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class FindSlotNonAvailableDatesSteps {
	
	public static final String COURTROOM = "Room764-1";
    public static final String JUDGE = "Judge A";
	public static boolean isJudgeAllocated = false;
	
	@Given("^I search for case \"(.*?)\" in court centre \"(.*?)\"$")
	public void i_search_for_case_in_court_centre(String crestCaseNumber, String courtCentre) throws Throwable {
	    // Search for a case in Manage Case page of a specific court centre.
		SeleniumConnector.openAndWait("manage-case?courtCenter=" + courtCentre);
        SeleniumConnector.waitForTextPresent("Case Crest Number");
        SeleniumConnector.setInputValueBySelector("crestCaseNumber", crestCaseNumber, "id");
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForElementAttribute("crestCaseNumber", "value", crestCaseNumber);
	}
	
	@Given("^Case \"(.*?)\" has Sending date \"(.*?)\" -(\\d+) days$")
	public void case_has_Sending_date_days(String caseNo, String day, int daysString) throws Throwable {
		// Data already setup in import.sql. Nothing to do here.
	}
	
	@Given("^Case \"(.*?)\" has PCM Hearing Date \"(.*?)\"$")
	public void case_has_PCM_Hearing_Date(String caseNo, String pcmhDate) throws Throwable {
		// Data already setup in import.sql. Nothing to do here.
	}
	
	public void allocateJudge(String roomName, String blockType, String startDate, String endDate) {
		SeleniumConnector.waitForTextPresent("Allocate Judges to Court Room");
        SeleniumConnector.isSelectOptionPresent("courtRooms", roomName);
        SeleniumConnector.selectFromDropDown("courtRooms", roomName);
		SeleniumConnector.isSelectOptionPresent("listOfJudgesForAlloc", JUDGE);
		SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", JUDGE);
		SeleniumConnector.enterDateInSelector("courtRoomStartDate", (DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), CommonSteps.parseDigit(startDate) + 1)));
		SeleniumConnector.enterDateInSelector("courtRoomEndDate", (DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), CommonSteps.parseDigit(endDate) + 1)));
		SeleniumConnector.clickAndWait("allocateJudge");
		SeleniumConnector.waitForTextPresent("Judge has been allocated");
	}
	
	@Given("^Court room \"(.*?)\" exists with \"(.*?)\" blocks from StartDate \"(.*?)\" to EndDate \"(.*?)\" and blocks are empty$")
	public void court_room_exists_with_blocks_from_StartDate_to_EndDate_and_blocks_are_empty(String roomName, String blockType, String startDate, String endDate) throws Throwable {
	    // Set the blocks from start date to end date
		if (!isJudgeAllocated) {
			allocateJudge(roomName, blockType,startDate, endDate);
			isJudgeAllocated = true;
		}
	}

	@Given("^Case has Trial \"(.*?)\" which is not listed$")
	public void case_has_Trial_which_is_not_listed(String arg1) throws Throwable {
	    // This step is just for readability only. Nothing to do here.
	}

	@Given("^Court room \"(.*?)\" contains no other \"(.*?)\" blocks$")
	public void court_room_contains_no_other_blocks(String arg1, String arg2) throws Throwable {
		// This step is just for readability only. Nothing to do here.
	}

	@Given("^Case \"(.*?)\" has (\\d+) Non Available date$")
	public void case_has_Non_Available_date(String arg1, int arg2) throws Throwable {
		// Data is setup via import.sql, nothing to do here
	}

	@Given("^Trial \"(.*?)\" has Estimate (\\d+) days?$")
	public void trial_has_Estimate_day(String arg1, int arg2) throws Throwable {
		// Data is setup via import.sql, nothing to do here
	}

	@Then("^I see the following dates in Search Results$")
	public void i_see_the_following_dates_in_Search_Results(List<String> resultDates) throws Throwable {
		// Verify the search results
		Date expectedDate;
		String slotDate = "";
		SeleniumConnector.waitForTextPresent("Available Slot Dates For Hearing");
		for (final String dateToCheck : resultDates) {
			expectedDate = DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), CommonSteps.parseDigit(dateToCheck) + 1);
			slotDate = DateTimeUtils.formatToStandardPattern(expectedDate);
			String courtRoomNameId = COURTROOM + "-" + slotDate;
			String slotStartDateId = COURTROOM + "-" + slotDate + "-date";
			assertTrue(COURTROOM.equals(SeleniumConnector.getTextFromElementBySelector(courtRoomNameId, "id")));
			assertTrue(slotDate.equals(SeleniumConnector.getTextFromElementBySelector(slotStartDateId, "id")));
		}
        SeleniumConnector.clickAndWait("cancelButton");
	}

	@Given("^Add Non Available dates from start date \"(.*?)\" to end date \"(.*?)\" with reason \"(.*?)\"$")
	public void add_Available_dates_from_start_date_to_end_date_reason(String startDate, String endDate, String reason) throws Throwable {
		SeleniumConnector.waitForElementPresent("addNonAvailableDateButton");
		SeleniumConnector.clickAndWait("addNonAvailableDateButton");
		SeleniumConnector.waitForElementPresent("notAvailabilityReason");
        SeleniumConnector.setInputValueBySelector("notAvailabilityReason", reason, "id");
        SeleniumConnector.enterDateInSelector("notAvailabilityStartDate", CommonSteps.getDate(startDate));
        SeleniumConnector.enterDateInSelector("notAvailabilityEndDate", CommonSteps.getDate(endDate));
        SeleniumConnector.clickAndWait("saveNonAvailableDateButton");
        SeleniumConnector.waitForTextPresent("Non available date has been added");
	}
	
	@Then("^I see dates \"(.*?)\" in Search Results$")
	public void i_see_dates_in_Search_Results(List<String> resultDates) throws Throwable {
	    // Verify dates
		Date expectedDate;
		String slotDate = "";
		SeleniumConnector.waitForTextPresent("Available Slot Dates For Hearing");
		for (final String dateToCheck : resultDates) {
			expectedDate = DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), CommonSteps.parseDigit(dateToCheck) + 1);
			slotDate = DateTimeUtils.formatToStandardPattern(expectedDate);
			String courtRoomNameId = COURTROOM + "-" + slotDate;
			String slotStartDateId = COURTROOM + "-" + slotDate + "-date";
			assertTrue(COURTROOM.equals(SeleniumConnector.getTextFromElementBySelector(courtRoomNameId, "id")));
			assertTrue(slotDate.equals(SeleniumConnector.getTextFromElementBySelector(slotStartDateId, "id")));
		}
		SeleniumConnector.pressKey("cancelButton", Keys.ENTER);
		try {
			SeleniumConnector.waitForElementToDisappear("cancelButton", "id");
		} catch (Exception e) {
			SeleniumConnector.pressKey("cancelButton", Keys.ENTER);
			SeleniumConnector.waitForElementToDisappear("cancelButton", "id");
		}
	}

	@Given("^Case \"(.*?)\" has (\\d+) Non Available periods?$")
	public void case_has_Non_Available_periods(String arg1, int arg2) throws Throwable {
	    // Step kept for readability. Nothing to do here as all data is setup from import.sql
	}
}