package uk.co.listing.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateFormats;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class FindEmptySlotsSteps {

	public static Log log = LogFactory.getLog(FindEmptySlotsSteps.class);

	// Start date for allocating judge is from tomorrow till 12 working days
	public static Date judgeAllocationStartDate = DateTimeUtils
			.getEndWorkingDateFromStartDate(new Date(), 2);
	public static Date judgeAllocationEndDate = DateTimeUtils
			.getEndWorkingDateFromStartDate(judgeAllocationStartDate, 12);

	// Boolean to check if setup has already been performed.
	public static boolean courtAndJudgeAdded = false;
	public static boolean judgeAllocated = false;

	// Name of Judge, Case and Court Rooms.
	public static final String JUDGENAME = "JudgeFindSlot";
	public static final String COURTRROMNAME_A = "RoomA-FindSlot";
	public static final String COURTRROMNAME_B = "RoomB-FindSlot";
	public static final String CASENAME = "T600";

	// Hearings which will be selected in selectedUnscheduledHearing drop down
	// to find available slots.
	public static final String TRIAL_ESTIMATE_5D = "Trial-Estimate-5D";
	public static final String TRIAL_ESTIMATE_4D = "Trial-Estimate-4D";
	public static final String TRIAL_ESTIMATE_2D = "Trial-Estimate-2D";
	public static final String TRIAL_ESTIMATE_1D = "Trial-Estimate-1D";

	/* Hearings used for filling the slots in diary. */
	// Slot A of 4 days for Room A
	public static final String _4D_HEARING_ROOM_A = "Slot-A-4Days-RoomA";

	// Slot B of 4 days for Room B
	public static final String _4D_HEARING_ROOM_B = "Slot-B-4Days-RoomB";

	// 8 Day hearings for Room A and Room B
	public static final String _8D_HEARING1_ROOM_A = "8D-Hearing1-RoomA";
	public static final String _8D_HEARING2_ROOM_A = "8D-Hearing2-RoomA";
	public static final String _8D_HEARING1_ROOM_B = "8D-Hearing1-RoomB";
	public static final String _8D_HEARING2_ROOM_B = "8D-Hearing2-RoomB";

	/* Set the start and end dates of each of the hearings in Room A. */
	// Set the start and end dates of the 4 Day hearing.
	public static Date hearingStartDate4D = DateTimeUtils
			.getEndWorkingDateFromStartDate(new Date(), 2);
	public static Date hearingEndDate4D = DateTimeUtils
			.getEndWorkingDateFromStartDate(hearingStartDate4D, 4);

	// Set the start and end dates of the 8 Day hearing.
	public static Date hearingStartDate8D = DateTimeUtils
			.getEndWorkingDateFromStartDate(hearingEndDate4D, 2);
	public static Date hearingEndDate8D = DateTimeUtils
			.getEndWorkingDateFromStartDate(hearingStartDate8D, 8);

	public static final String COURT_CENTER = "Test-Center";
	public static String courtCentreQueryString = "?courtCenter="
			+ COURT_CENTER;

	// Before every scenario book hearings in all slots in Room A. Add Judge and
	// court room if not done.
	@Before("@CCS-423")
	public static void beforeScenario() {
		// Add Judge and Court.
		if (!courtAndJudgeAdded) {
			createJudge(JUDGENAME);
			addCourtRoom(COURTRROMNAME_A);
			addCourtRoom(COURTRROMNAME_B);
			courtAndJudgeAdded = true;
		}

		// Allocate Judge to court room for the required days
		if (!judgeAllocated) {
			allocateJudge(JUDGENAME, COURTRROMNAME_A, judgeAllocationStartDate,
					judgeAllocationEndDate);
			// allocateJudge(JUDGENAME, COURTRROMNAME_B,
			// judgeAllocationStartDate, judgeAllocationEndDate);
			judgeAllocated = true;
		}

		// Book all the hearings for Room A to fill all slots before running a
		// scenario.
		bookHearing(_4D_HEARING_ROOM_A, hearingStartDate4D, COURTRROMNAME_A,
				"Provisional");
		bookHearing(_8D_HEARING1_ROOM_A, hearingStartDate8D, COURTRROMNAME_A,
				"Provisional");
	}

	// After every scenario unbook hearings in all slots in Room A.
	@After("@CCS-423")
	public static void afterScenario() {
		// Unlist all the hearings to free all slots after running a scenario.
		unListHearing(hearingStartDate4D, _4D_HEARING_ROOM_A);
		unListHearing(hearingStartDate8D, _8D_HEARING1_ROOM_A);
	}

	// Before scenario 4, un-book _8D_HEARING2_ROOM_A as this is booked in
	// Scenario 3
	@Before("@dual-hearing")
	public static void beforeScenario4() {
		allocateJudge(JUDGENAME, COURTRROMNAME_B, judgeAllocationStartDate,
				judgeAllocationEndDate);
		bookHearing(_8D_HEARING1_ROOM_A, hearingStartDate8D, COURTRROMNAME_A,
				"Provisional");
		unListHearing(hearingStartDate8D, _8D_HEARING2_ROOM_A);
	}

	// After scenario 4, un-book hearings in all slots in Room B.
	@After("@dual-hearing")
	public static void afterScenario4() {
		unListHearing(hearingStartDate4D, _4D_HEARING_ROOM_B);
		unListHearing(hearingStartDate8D, _8D_HEARING1_ROOM_B);
		unListHearing(hearingStartDate8D, _8D_HEARING2_ROOM_B);
		unListHearing(hearingStartDate8D, _8D_HEARING2_ROOM_A);
	}

	// Create a judge
	public static void createJudge(final String judgeName) {
		final String judgeNameInputId = "judgeName";
		final String addJudgeInputId = "addJudge";
		SeleniumConnector.openAndWait("admin" + courtCentreQueryString);
		SeleniumConnector.waitForTextPresent("Room Name:");
		SeleniumConnector.setInputValue(judgeNameInputId, judgeName);
		SeleniumConnector.waitForElementPresent("listOfJudgeTypes");
		SeleniumConnector.isSelectOptionPresent("listOfJudgeTypes",
				"High Court");
		SeleniumConnector.selectFromDropDown("listOfJudgeTypes",
				"High Court");
		SeleniumConnector.pressKey(addJudgeInputId, Keys.ENTER);
		SeleniumConnector.waitForTextPresent("Saved");
	}

	// Add a court room
	public static void addCourtRoom(final String roomName) {
		final String roomNameInputId = "courtRoomInput";
		final String saveRoomButtonId = "saveCourtRoomButton";
		SeleniumConnector.openAndWait("admin" + courtCentreQueryString);
		SeleniumConnector.waitForTextPresent("Room Name:");
		SeleniumConnector.setInputValue(roomNameInputId, roomName);
		SeleniumConnector.pressKey(saveRoomButtonId, Keys.ENTER);
		SeleniumConnector.waitForTextPresent("Saved");
	}

	// Allocate judge to court room from start date to end date
	public static void allocateJudge(final String judgeName,
			final String courtRoom, final Date startDay, final Date endDay) {
		SeleniumConnector.openAndWait("schedule" + courtCentreQueryString);
		SeleniumConnector.isSelectOptionPresent("courtRooms", courtRoom);
		SeleniumConnector.selectFromDropDown("courtRooms", courtRoom);
		SeleniumConnector.isSelectOptionPresent("listOfJudgesForAlloc",
				judgeName);
		SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", judgeName);
		SeleniumConnector.enterDateInSelector("courtRoomStartDate", startDay);
		SeleniumConnector.enterDateInSelector("courtRoomEndDate", endDay);
		SeleniumConnector.clickAndWait("allocateJudge");
		SeleniumConnector.waitForTextPresent("Judge has been allocated");
	}

	// Book a hearing in a court room from start date until the trial estimate
	// of hearing
	public static void bookHearing(final String hearingName,
			final Date startDate, final String courtRoom,
			final String bookingType) {
		String dateString;
		String elementId;
		SeleniumConnector.openAndWait("schedule" + courtCentreQueryString);
		SeleniumConnector.waitForTextPresent("Room Name");
		try {
			SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing",
					hearingName);
		} catch (final Exception e) {
			log.info("Exception while selecting hearing: " + hearingName
					+ " from drop down with ID selectedUnscheduledHearing.");
			log.info("Trying again to unlist hearing: " + hearingName);
			unListHearing(startDate, hearingName);
			SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing",
					hearingName);
		}
		log.info("Entering date " + startDate);
		SeleniumConnector.enterDateInSelector("hearingStartDate", startDate);
		log.info("Selecting from drop down the room " + courtRoom);
		SeleniumConnector.selectFromDropDown("roomForListing", courtRoom);
		log.info("Selecting from drop down the booking type Provisional ");
		SeleniumConnector.selectFromDropDown("hearingBookingType",
				"Provisional");
		SeleniumConnector.clickAndWait("listHearing");
		dateString = DateFormatUtils.format(startDate,
				DateFormats.STANDARD.getValue());
		elementId = ".//*[@id = '" + courtRoom + "_" + dateString + "_"
				+ CASENAME + "_" + hearingName + "_link']";
		SeleniumConnector.waitForElementClickableBySelector(elementId, "xpath");
	}

	// Un list a hearing from diary
	public static void unListHearing(final Date hearingStartDate,
			final String hearingName) {
		SeleniumConnector.openAndWait("schedule" + courtCentreQueryString);
		SeleniumConnector.waitForTextPresent("Room Name");
		// As the hearing is already unlisted if present in drop down, no need
		// to un-list again.
		if (!SeleniumConnector.isElementAbsent(
				".//*[@id='selectedUnscheduledHearing']/option[text()='"
						+ hearingName + "']", "xpath")) {
			log.info("Hearing "
					+ hearingName
					+ " already present in unList drop down. So no need to unlist again.");
			return;
		}
		SeleniumConnector.enterDateInSelector("dateInput", hearingStartDate);
		SeleniumConnector.pressKey("goto", Keys.ENTER);
		final DateFormat df = new SimpleDateFormat("dd/MM");
		final String dateString = df.format(hearingStartDate);
		SeleniumConnector.waitForTextPresent(dateString);
		final String courtRoom = hearingName.contains("RoomA") ? COURTRROMNAME_A
				: COURTRROMNAME_B;
		final String linkId = courtRoom + "_"
				+ DateTimeUtils.formatToStandardPattern(hearingStartDate) + "_"
				+ CASENAME + "_" + hearingName + "_link";
		log.info("Checking whether hearing link " + linkId
				+ " appears in order to unlist hearing");
		SeleniumConnector.waitForElementClickable(linkId);
		SeleniumConnector.clickAndWait(linkId);
		SeleniumConnector.getTextFromElementBySelector("hearingInfo", "id");
		SeleniumConnector.clickAndWait("unListHearing");
		final String alertMsg = "Confirm that " + hearingName + " starting on "
				+ DateTimeUtils.formatToStandardPattern(hearingStartDate)
				+ " should be un-listed";
		SeleniumConnector.waitForTextPresent(alertMsg);
		try {
			SeleniumConnector.pressKey("saveButton", Keys.ENTER);
			SeleniumConnector.waitForElementToDisappear("saveButton", "id");
		} catch (Exception e) {
			SeleniumConnector.pressKey("saveButton", Keys.ENTER);
			SeleniumConnector.waitForElementToDisappear("saveButton", "id");
		}
		SeleniumConnector.waitForElementToDisappear(linkId, "id");
	}

	@Given("^slot \"(.*?)\" in Room \"(.*?)\" is of size (\\d+) days$")
	public void slot_exists_of_size_days(final String arg1, final String arg2,
			final int arg3) throws Throwable {
		// Slot "4D-Hearing" is of size 4 days and already setup in DB via
		// import.sql
		// So nothing to do here.
	}

	@Given("^all blocks contain (\\d+) hearing$")
	public void all_blocks_contain_hearing(final int numberOfHearings)
			throws Throwable {
		// @Before already adds 1 hearing for all slots before every scenario.
		// So we need not do anything here
	}

	@Given("^I select hearing \"(.*?)\" from unlistedHearings$")
	public void i_select_hearing_from(final String hearingName)
			throws Throwable {
		// Select hearing from the drop down in Unscheduled hearing section
		SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing",
				hearingName);
		SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing",
				hearingName);
	}

	@Given("^I click \"(.*?)\" to close the Available Slots results$")
	public void i_click_to_close_the_Available_Slots_results(
			final String cancelButton) throws Throwable {
		SeleniumConnector.clickAndWait(cancelButton);
		try {
			SeleniumConnector.waitForElementToDisappear(cancelButton, "id");
		} catch (final TimeoutException e) {
			SeleniumConnector.clickAndWait(cancelButton);
			SeleniumConnector.waitForElementToDisappear(cancelButton, "id");
		}
	}

	@Given("^I modify estimate value to (\\d+) in \"(.*?)\" without saving the Estimate$")
	public void i_modify_estimate_value_to_in_without_saving_the_Estimate(
			final int estimateValue, final String editEstimateInputId)
			throws Throwable {
		// This modification of Estimate value (without saving) is for verifying
		// the defect CCS-577
		SeleniumConnector.setInputValueBySelector(editEstimateInputId,
				String.valueOf(estimateValue), "id");
	}

	@Given("^slot \"(.*?)\" is empty in diary$")
	public void slot_is_empty(final String arg1) throws Throwable {
		// Un list 4 day hearing to make an available free slot .
		unListHearing(hearingStartDate4D, _4D_HEARING_ROOM_A);
	}

	@Given("^all blocks not in slot \"(.*?)\" contain hearings$")
	public void all_blocks_not_in_slot_contain_hearings(final String arg1)
			throws Throwable {
		// All other blocks have already been assigned a hearing in the @Before.
		// So nothing to do here.
	}

    @Given("^I press Get Available Slots Button$")
    public void click_choose_slots_button() throws Throwable {
        SeleniumConnector.pressKey("getHearingSlotsButton", Keys.ENTER);
    }

	@Then("^I should see message \"(.*?)\" in Available Slots element$")
	public void i_should_see_in_find_results_element(final String errorMessage) {
		SeleniumConnector
				.waitForTextPresent("Available Slot Dates For Hearing");
		assertTrue(SeleniumConnector.getTextFromElementBySelector(
				"noResultsFound", "id").contains(errorMessage));
		SeleniumConnector.clickAndWait("cancelButton");
	}

	@Then("^I should see in Available Slots element, room \"(.*?)\" and date is start date of \"(.*?)\"$")
	public void i_should_see_room_and_date_in_Available_Slots_element(
			final String courtRoomName, final String slotName) throws Throwable {
		// Verifying Room Name and Slot Date
		final String slotDate = DateTimeUtils
				.formatToStandardPattern(hearingStartDate4D);
		final String courtRoomNameId = courtRoomName + "-" + slotDate;
		final String slotStartDateId = courtRoomName + "-" + slotDate + "-date";
		SeleniumConnector
				.waitForTextPresent("Available Slot Dates For Hearing");
		assertTrue(courtRoomName.equals(SeleniumConnector
				.getTextFromElementBySelector(courtRoomNameId, "id")));
		assertTrue(slotDate.equals(SeleniumConnector
				.getTextFromElementBySelector(slotStartDateId, "id")));
	}

	@Then("^I should see in Available Slots element, room \"(.*?)\" and date is start date of \"(.*?)\" plus (\\d+) day$")
	public void i_should_see_in_Available_Slots_element_room_and_date_is_start_date_of_plus_day(
			final String courtRoomName, final String slotName,
			final int noOfDays) throws Throwable {
		final Date expectedDate = DateTimeUtils.getEndWorkingDateFromStartDate(
				hearingStartDate4D, noOfDays + 1);

		final String slotDate = DateTimeUtils
				.formatToStandardPattern(expectedDate);
		final String courtRoomNameId = courtRoomName + "-" + slotDate;
		final String slotStartDateId = courtRoomName + "-" + slotDate + "-date";
		SeleniumConnector
				.waitForTextPresent("Available Slot Dates For Hearing");
		if (!SeleniumConnector.isElementAbsent(slotStartDateId, "id")) {
			assertTrue(COURTRROMNAME_A.equals(SeleniumConnector
					.getTextFromElementBySelector(courtRoomNameId, "id")));
			assertTrue(slotDate.equals(SeleniumConnector
					.getTextFromElementBySelector(slotStartDateId, "id")));
		}
	}

	@Given("^following hearings exists with corresponding trial estimates in days$")
	public void case_exists_with_hearings(final List<String> arg2)
			throws Throwable {
		// These must be present as they are setup in DB using import.sql
		SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing",
				TRIAL_ESTIMATE_1D);
		SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing",
				TRIAL_ESTIMATE_2D);
		SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing",
				TRIAL_ESTIMATE_4D);
		SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing",
				TRIAL_ESTIMATE_5D);
	}

	@Given("^slot \"(.*?)\" contains (\\d+) hearing$")
	public void slot_contains_hearing(final String arg1, final int arg2)
			throws Throwable {
		// Before scenario will have already set this up
	}

	@Given("^all blocks not in slot \"(.*?)\" contain (\\d+) hearings$")
	public void all_blocks_not_in_slot_contain_hearings(final String arg1,
			final int arg2) throws Throwable {
		bookHearing(_8D_HEARING2_ROOM_A, hearingStartDate8D, COURTRROMNAME_A,
				"Provisional");
	}

	@Then("^I should see in Available Slots element, room \"(.*?)\" , start date of \"(.*?)\" and hearing is \"(.*?)\"$")
	public void i_should_see_in_Available_Slots_element_room_start_date_is(
			final String courtRoomName, final String slotName,
			final String noHearings) throws Throwable {
		// Verifying Room Name and Slot Date
		i_should_see_room_and_date_in_Available_Slots_element(courtRoomName,
				slotName);
		final String slotDate = DateTimeUtils
				.formatToStandardPattern(hearingStartDate4D);
		final String hearingNumbersId = courtRoomName + "-" + slotDate
				+ "-exist_hearings";
		SeleniumConnector
				.waitForTextPresent("Available Slot Dates For Hearing");
		// Verifying Number of Hearings
		assertTrue(noHearings.contains(SeleniumConnector
				.getTextFromElementBySelector(hearingNumbersId, "id")));
	}

	@Given("^slot \"(.*?)\" contains only (\\d+) hearing$")
	public void slot_contains_only_hearing(final String arg1, final int arg2)
			throws Throwable {
		bookHearing(_4D_HEARING_ROOM_B, hearingStartDate4D, COURTRROMNAME_B,
				"Provisional");
	}

	@Given("^all blocks not in slot \"(.*?)\" and \"(.*?)\"contain (\\d+) hearings$")
	public void all_blocks_not_in_slot_and_contain_hearings(final String arg1,
			final String arg2, final int arg3) throws Throwable {
		bookHearing(_8D_HEARING2_ROOM_A, hearingStartDate8D, COURTRROMNAME_A,
				"Provisional");
		bookHearing(_8D_HEARING1_ROOM_B, hearingStartDate8D, COURTRROMNAME_B,
				"Provisional");
		bookHearing(_8D_HEARING2_ROOM_B, hearingStartDate8D, COURTRROMNAME_B,
				"Provisional");
	}

    @And("^I specify Overbooking is (\\d+) hearing$")
    public void i_specify_overbooking_is_hearing(final int hearingNos) throws Throwable {
        SeleniumConnector.clickAndWait("overBookCheck");
    }

	@Then("^Unlist button is disabled$")
	public void unlist_button_is_disabled() throws Throwable {
		assertFalse(SeleniumConnector.isEnabled("unListHearing"));
	}

	@Then("^I should NOT see in Available Slots element, room \"(.*?)\" , start date of \"(.*?)\" and hearing is \"(.*?)\"$")
	public void i_should_NOT_see_in_Available_Slots_element_room_start_date_of_and_hearing_is(
			final String courtRoomName, final String slotName,
			final String noHearings) throws Throwable {
		final String slotDate = DateTimeUtils
				.formatToStandardPattern(hearingStartDate4D);
		final String hearingNumbersId = courtRoomName + "-" + slotDate
				+ "-exist_hearings";
		SeleniumConnector
				.waitForTextPresent("Available Slot Dates For Hearing");
		SeleniumConnector.waitForElementToDisappear(hearingNumbersId, "id");
	}
}
