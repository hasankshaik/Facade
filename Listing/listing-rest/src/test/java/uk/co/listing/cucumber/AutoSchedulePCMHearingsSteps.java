package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.io.File;

import uk.co.listing.SeleniumConnector;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AutoSchedulePCMHearingsSteps {

	public static boolean pcmHearingsAllocated = false;

	@Given("^Court room \"(.*?)\" exists$")
	public void court_room_exists(final String roomName) throws Throwable {
		// Court rooms have been setup from import.sql, hence nothing to do
		// here.
		// This step has been kept for readability purposes.
	}

	@Given("^Crest Upload file \"(.*?)\" exists with following data$")
	public void crest_Upload_file_exists_with_following_data(
			final String fileName, final DataTable arg2) throws Throwable {
		// PCM hearings with these dates have already been setup in import.sql,
		// Hence nothing to do here. This step has been kept for readability
		// purposes.
	}

	@Given("^an empty block with type \"(.*?)\" is in Court \"(.*?)\" on \"(.*?)\"$")
	public void an_empty_block_with_type_is_in_Court_on(final String blockType,
			final String courtRoom, final String dateString) throws Throwable {
		final String[] dateArr = dateString.split("/");
		SeleniumConnector.isSelectOptionPresent("listOfBlockTypes", blockType);
		SeleniumConnector.selectFromDropDown("listOfBlockTypes", blockType);
		SeleniumConnector.isSelectOptionPresent("courtRoomsManageBlocks",
				courtRoom);
		SeleniumConnector.selectFromDropDown("courtRoomsManageBlocks",
				courtRoom);
		SeleniumConnector
				.waitForElementClickable("courtRoomStartDateManageBlocks");
		SeleniumConnector.enterDateInSelector("courtRoomStartDateManageBlocks",
				dateArr[0], dateArr[1], dateArr[2]);
		SeleniumConnector
				.waitForElementClickable("courtRoomEndDateManageBlocks");
		SeleniumConnector.enterDateInSelector("courtRoomEndDateManageBlocks",
				dateArr[0], dateArr[1], dateArr[2]);
		SeleniumConnector.clickAndWait("manageBlock");
		SeleniumConnector.waitForTextPresent("Block allocated successfully");
		SeleniumConnector.waitForTextToAppear("manageBlocksMessage", "");
	}

	@When("^I goto date \"(.*?)\" on Planner Page$")
	public void i_goto_date_on_Planner_Page(final String dateString)
			throws Throwable {
		final String[] dateArr = dateString.split("/");
		SeleniumConnector.openAndWait("schedule");
		SeleniumConnector.waitForTextPresent("Room Name");
		SeleniumConnector.waitForElementClickable("dateInput");
		SeleniumConnector.enterDateInSelector("dateInput", dateArr[0],
				dateArr[1], dateArr[2]);
		SeleniumConnector.clickAndWait("goto");
		SeleniumConnector.waitForTextPresent(dateArr[0] + "/" + dateArr[1]);
	}

	@Then("^I should see a PCM Hearing \"(.*?)\" in Room \"(.*?)\" on \"(.*?)\"$")
	public void i_should_see_a_PCM_Hearing_in_Room_on(final String caseName,
			final String roomName, final String dateString) throws Throwable {
		final String hearingElementId = roomName + "_" + dateString + "_"
				+ caseName + "_PCM-" + dateString + "_link";
		SeleniumConnector.waitForElementClickableBySelector(hearingElementId,
				"id");
		SeleniumConnector.waitForElementClickableBySelector(roomName + "_"
				+ dateString + "_PCM", "id");
	}

	@Given("^Court rooms \"(.*?)\" , \"(.*?)\" , \"(.*?)\" exists$")
	public void court_rooms_exists(final String arg1, final String arg2,
			final String arg3) throws Throwable {
		// The rooms have been setup from import.sql, hence nothing to do here.
	}

	@Given("^Hearing exists with type \"(.*?)\" in Court \"(.*?)\" on \"(.*?)\"$")
	public void hearing_exists_with_type_is_in_Court_on(
			final String hearingType, final String courtRoom,
			final String dateString) throws Throwable {
		if (!pcmHearingsAllocated) {
			an_empty_block_with_type_is_in_Court_on("PCM", "Room681-1",
					"05/03/2020");
			an_empty_block_with_type_is_in_Court_on("PCM", "Room681-2",
					"05/03/2020");
			SeleniumConnector.openAndWait("admin");
			SeleniumConnector.waitForTextPresent("Upload file");
			final ClassLoader classLoader = getClass().getClassLoader();
			final File testfile = new File(classLoader.getResource(
					"TestFiles/" + "404--CREST-DATA-VALID-AUTO-PCM-SETUP.zip")
					.getFile());
			SeleniumConnector.setFileBrowsePath(testfile.getAbsolutePath(),
					"fileUpload");
			SeleniumConnector.clickAndWait("fileUploadBtn");
			SeleniumConnector
					.waitForTextPresent("404--CREST-DATA-VALID-AUTO-PCM-SETUP");
			SeleniumConnector.openAndWait("schedule");
			SeleniumConnector.waitForTextPresent("Room Name");
			pcmHearingsAllocated = true;
		}
	}

	@Given("^no block with type \"(.*?)\" exist in any Court room on \"(.*?)\"$")
	public void no_block_with_type_exist_in_any_Court_room_on(
			final String arg1, final String arg2) throws Throwable {
		// Since we do not wish to allocate a block on this day, nothing to do
		// here.
	}

	@Given("^dates \"(.*?)\" and \"(.*?)\" are weekends$")
	public void dates_and_are_weekends(final String arg1, final String arg2)
			throws Throwable {
		// Dates selected "01/03/2020" and "29/02/2020" are already weekends,
		// Nothing to do here.
	}

	@When("^Court Centre is \"(.*?)\"$")
	public void court_Centre_is(final String centre) throws Throwable {
		SeleniumConnector.waitForTextPresent(centre);
	}

	@Then("^Booking Status of PCM Hearing for case \"(.*?)\" is \"(.*?)\"$")
	public void booking_Status_of_PCM_Hearing_for_case_is(
			final String caseCrestNo, final String bookingStatus)
			throws Throwable {
		SeleniumConnector.waitForTextPresent("Plea and Case Management");
		assertTrue(bookingStatus.equals(SeleniumConnector.getTextFromTable(
				"hearings", 1, 7)));
	}

	@When("^I Unlist the PCM Hearing \"(.*?)\" in Room \"(.*?)\" on \"(.*?)\"$")
	public void i_Unlist_the_PCM_Hearing_from_date(final String caseName,
			final String roomName, final String dateString) throws Throwable {
		final String hearingElementId = roomName + "_" + dateString + "_"
				+ caseName + "_PCM-" + dateString + "_link";
		SeleniumConnector.waitForElementClickableBySelector(hearingElementId,
				"id");
		SeleniumConnector.clickAndWait(hearingElementId);
		SeleniumConnector.waitForElementClickableBySelector("unListHearing",
				"id");
		SeleniumConnector.clickAndWait("unListHearing");
		final String alertMsg = "Confirm that PCM-" + dateString
				+ " starting on " + dateString + " should be un-listed";
		SeleniumConnector.waitForTextPresent(alertMsg);
		try {
			SeleniumConnector.clickAndWait("saveButton");
			SeleniumConnector.waitForElementToDisappear("saveButton", "id");
		} catch (Exception e) {
			SeleniumConnector.clickAndWait("saveButton");
			SeleniumConnector.waitForElementToDisappear("saveButton", "id");
		}
	}
}