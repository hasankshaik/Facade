package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class FindSlotMatchingAllocatedJudgeSteps {
	
	public static boolean areJudgesAllocated = false;
	public static final List<String> judges = Arrays.asList("Judge A","Judge B","Judge B","Judge C");
    public static final List<String> rooms = Arrays.asList("CourtRoomA","CourtRoomB","CourtRoomF","CourtRoomC");
	
	@Given("^following Cases exists with corresponding judge allocated$")
	public void cases_exist_with_judge(final List<String> arg2) throws Throwable {
		// Data already setup in import.sql. Nothing to do here.
	}
	
	@Given("^The next judges are allocated in the next rooms$")
	public void case_has_PCM_Hearing_Date(final List<String> arg2) throws Throwable {
		if(!areJudgesAllocated) {
		    areJudgesAllocated = true;
		    for(int i = 0;i<judges.size() ;i++ ) {
                SeleniumConnector.isSelectOptionPresent("courtRooms",rooms.get(i));
                SeleniumConnector.selectFromDropDown("courtRooms", rooms.get(i));
    		    SeleniumConnector.isSelectOptionPresent("listOfJudgesForAlloc",judges.get(i));
    	        SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", judges.get(i));
    	        SeleniumConnector.enterDateInSelector("courtRoomStartDate", new Date());
                SeleniumConnector.enterDateInSelector("courtRoomEndDate", DateTimeUtils.getEndWorkingDateFromStartDate(new Date(),30));
                SeleniumConnector.clickAndWait("allocateJudge");
                SeleniumConnector.waitForTextPresent("Judge has been allocated");
		    }
		}
	}
	
	@Then("^I see the following rooms in Search Results$")
	public void i_see_rooms_in_Search_Results(final List<String> rooms) throws Throwable {
		Date expectedDate;
		String slotDate = "";
		SeleniumConnector.waitForTextPresent("Available Slot Dates For Hearing");
		for (final String room : rooms) {
			expectedDate = DateTimeUtils.getEndWorkingDateFromStartDate(new Date(),2);
			slotDate = DateTimeUtils.formatToStandardPattern(expectedDate);
			String courtRoomNameId = room + "-" + slotDate;
			assertTrue(room.equals(SeleniumConnector.getTextFromElementBySelector(courtRoomNameId, "id")));
		}
		SeleniumConnector.clickAndWait("cancelButton");
	}

}