package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateFormats;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CommonSteps {

	@Then("^I can see element \"(.*?)\"$")
	public void i_can_see_element(final String element) throws Throwable {
		assertTrue(SeleniumConnector.isElementPresent(element));
	}

	@Then("^I can see \"(.*?)\", \"(.*?)\", \"(.*?)\", \"(.*?)\", \"(.*?)\" weekday$")
	public void i_can_see(final String arg1, final String arg2,
			final String arg3, final String arg4, final String arg5)
			throws Throwable {
		assertTrue(SeleniumConnector.isTextPresent(arg1));
		assertTrue(SeleniumConnector.isTextPresent(arg2));
		assertTrue(SeleniumConnector.isTextPresent(arg3));
		assertTrue(SeleniumConnector.isTextPresent(arg4));
		assertTrue(SeleniumConnector.isTextPresent(arg5));
	}

	@Then("^I can not see \"(.*?)\",\"(.*?)\" weekday$")
	public void i_can_not_see(final String arg1, final String arg2)
			throws Throwable {
		assertTrue(SeleniumConnector.isTextNotPresent(arg1));
		assertTrue(SeleniumConnector.isTextNotPresent(arg2));
	}

	@When("^I click \"(.*?)\"$")
	public void i_click(final String element) throws Throwable {
		SeleniumConnector.clickAndWait(element);
	}

	@Given("^I am on \"(.*?)\" Plan$")
	public void i_am_on_Plan(final String selectedLabel) throws Throwable {
		SeleniumConnector.openAndWait("schedule");
		SeleniumConnector.selectFromDropDown("viewSelector", selectedLabel);
	}

	@Then("^I can see \"(.*?)\" \"(.*?)\"$")
	public void i_can_see(final String period, final String unit)
			throws Throwable {
		Date date = new Date();

		if (period.equalsIgnoreCase("previous")) {
			if (unit.equalsIgnoreCase("month")) {
				date = DateUtils.addMonths(new Date(), -1);
			}
			if (unit.equalsIgnoreCase("week")) {
				date = DateUtils.addWeeks(new Date(), -1);
			}
			if (unit.equalsIgnoreCase("date")) {
				date = DateUtils.addDays(new Date(), -1);
			}
		}
		if (period.equalsIgnoreCase("next")) {
			if (unit.equalsIgnoreCase("month")) {
				date = DateUtils.addMonths(new Date(), 1);
			}
			if (unit.equalsIgnoreCase("week")) {
				date = DateUtils.addWeeks(new Date(), 1);
			}
			if (unit.equalsIgnoreCase("date")) {
				date = DateUtils.addDays(new Date(), 1);
			}
		}
		final SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM");
		assertTrue(SeleniumConnector.isTextPresent(sdf.format(date)));
	}

	@Given("^I have added a court room \"(.*?)\"$")
	public void i_have_added_a_court_room(final String arg1) throws Throwable {
		SeleniumConnector.openAndWait("schedule");
		if (SeleniumConnector.isTextNotPresent("Room 1")) {
			SeleniumConnector.openAndWait("admin");
			SeleniumConnector.setInputValue("courtRoomInput", "Room 1");
			SeleniumConnector.clickAndWait("saveCourtRoomButton");
		}
	}

	@Then("^I can see text \"(.*?)\"$")
	public void i_can_see_text(final String text) throws Throwable {
		assertTrue(SeleniumConnector.isTextPresent(text));
	}

	@Then("^I cannot see text \"(.*?)\" in the \"(.*?)\" element$")
	public void i_cannot_see_text_in_the_element(final String text,
			final String id) throws Throwable {
		Assert.assertNotEquals(text,
				SeleniumConnector.getTextFromElementBySelector(id, "id"));
	}

	@Given("^I am on Planner page$")
	public void i_am_on_Planner_page() throws Throwable {
		SeleniumConnector.openAndWait("schedule");
		try {
			SeleniumConnector
					.waitForTextPresent("The Crown Court at Birmingham");
		} catch (final TimeoutException e) {
			SeleniumConnector.openAndWait("schedule");
			SeleniumConnector
					.waitForTextPresent("The Crown Court at Birmingham");
		}
	}

	@Given("^Judge \"(.*?)\" is allocated to Court room \"(.*?)\" from \"(.*?)\" to \"(.*?)\" days$")
	public void judge_is_allocated_to_Court_room_from_to_days(
			final String judgeName, final String courtRoom,
			final String startDay, final String endDay) throws Throwable {
		SeleniumConnector.waitForTextPresent("Allocate Judges to Court Room");
		SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", judgeName);
		SeleniumConnector.selectFromDropDown("courtRooms", courtRoom);
		SeleniumConnector.enterDateInSelector("courtRoomStartDate",
				getDate(startDay));
		SeleniumConnector.enterDateInSelector("courtRoomEndDate",
				getDate(endDay));
		SeleniumConnector.clickAndWait("allocateJudge");
	}

	@Given("^Court Room \"(.*?)\" exists with Hearing \"(.*?)\" listed from \"(.*?)\" for \"(.*?)\" days as type \"(.*?)\"$")
	public void court_Room_exists_with_Hearing_listed_from_to_days(
			final String courtRoom, final String hearingName,
			final String startDay, final String estimate, final String bookType)
			throws Throwable {
		SeleniumConnector.waitForTextPresent("List Hearing");
		SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing",
				hearingName);
		SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing",
				hearingName);
		SeleniumConnector.waitForElementClickable("hearingStartDate");
		SeleniumConnector.enterDateInSelector("hearingStartDate",
				getDate(startDay));
		SeleniumConnector.isSelectOptionPresent("roomForListing", courtRoom);
		SeleniumConnector.selectFromDropDown("roomForListing", courtRoom);
		SeleniumConnector.isSelectOptionPresent("hearingBookingType", bookType);
		SeleniumConnector.selectFromDropDown("hearingBookingType", bookType);
		SeleniumConnector.clickAndWait("listHearing");
		SeleniumConnector.waitForTextPresent(hearingName);
	}

	@When("^I enter Court room \"(.*?)\" , Block Type \"(.*?)\" , start date \"(.*?)\" , end date \"(.*?)\" in Set Block Type element$")
	public void i_enter_Court_room_Block_Type_start_date_end_date_in_Set_Block_Type_element(
			final String courtRoom, final String blockType,
			final String startDay, final String endDay) throws Throwable {
		SeleniumConnector.waitForTextPresent("Manage Blocks");
		SeleniumConnector.isSelectOptionPresent("listOfBlockTypes", blockType);
		SeleniumConnector.selectFromDropDown("listOfBlockTypes", blockType);
		SeleniumConnector.isSelectOptionPresent("courtRoomsManageBlocks",
				courtRoom);
		SeleniumConnector.selectFromDropDown("courtRoomsManageBlocks",
				courtRoom);
		SeleniumConnector
				.waitForElementClickable("courtRoomStartDateManageBlocks");
		SeleniumConnector.enterDateInSelector("courtRoomStartDateManageBlocks",
				getDate(startDay));
		SeleniumConnector
				.waitForElementClickable("courtRoomEndDateManageBlocks");
		SeleniumConnector.enterDateInSelector("courtRoomEndDateManageBlocks",
				getDate(endDay));
	}

	@When("^I click and wait for message , click \"(.*?)\" and wait for \"(.*?)\" to be \"(.*?)\"$")
	public void i_click_Set_button_to_set_Block_types(final String elementId,
			final String messageId, final String message) throws Throwable {
		SeleniumConnector.pressKey(elementId, Keys.ENTER);
		SeleniumConnector.waitForTextToAppear(messageId, message);
	}

	public static Date getDate(final String dayToParse) {
		Date startDate = null;
		if (dayToParse.equals("today")) {
			startDate = new Date();
		} else if (StringUtils.startsWith(dayToParse, "next_week")) {
			startDate = DateTimeUtils.getEndWorkingDateFromStartDate(
					DateTimeUtils.getFirstDateOfNextWeek(new Date()),
					parseDigit(dayToParse));
		} else if (dayToParse.contains(" + ")) {
			startDate = DateTimeUtils.getEndWorkingDateFromStartDate(
					new Date(), parseDigit(dayToParse) + 1);
		} else if (dayToParse.contains(" - ")) {
			startDate = DateTimeUtils.getWorkingDayBeforeDate(new Date(),
					parseDigit(dayToParse));
		}
		return startDate;
	}

	public static int parseDigit(final String dateString) {
		int noOfDays = 0;
		final Matcher expected = Pattern.compile("\\d+").matcher(dateString);
		if (expected.find()) {
			noOfDays = Integer.parseInt(expected.group());
		}
		return noOfDays;
	}

	// Allocate judge to court room from start date to end date
	public static void allocateJudge(String judgeName, String courtRoom,
			Date startDay, Date endDay) {
		SeleniumConnector.waitForTextPresent("Allocate Judges to Court Room");
		SeleniumConnector.isSelectOptionPresent("courtRooms", courtRoom);
		SeleniumConnector.selectFromDropDown("courtRooms", courtRoom);
		SeleniumConnector.isSelectOptionPresent("listOfJudgesForAlloc",
				judgeName);
		SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", judgeName);
		SeleniumConnector.enterDateInSelector("courtRoomStartDate", startDay);
		SeleniumConnector.enterDateInSelector("courtRoomEndDate", endDay);
		SeleniumConnector.clickAndWait("allocateJudge");
	}

	// De-allocate judge to court room from start date to end date
	public static void deallocateJudge(String judgeName, String courtRoom,
			Date startDay, Date endDay) {
		SeleniumConnector.waitForTextPresent("Deallocate Judges to Court Room");
		SeleniumConnector.isSelectOptionPresent("listOfJudgesForDeAlloc",
				judgeName);
		SeleniumConnector.selectFromDropDown("listOfJudgesForDeAlloc",
				judgeName);
		SeleniumConnector.isSelectOptionPresent("courtRoomsDeAlloc", courtRoom);
		SeleniumConnector.selectFromDropDown("courtRoomsDeAlloc", courtRoom);
		SeleniumConnector.enterDateInSelector("courtRoomStartDateDeAlloc",
				startDay);
		SeleniumConnector
				.enterDateInSelector("courtRoomEndDateDeAlloc", endDay);
		SeleniumConnector.pressKey("deallocateJudge", Keys.ENTER);
	}

	@When("^I enter Date \"(.*?)\" with pattern (dd/MM/yyyy) in \"(.*?)\" element$")
	public void i_enter_Date_in_element(final String date, final String element)
			throws Throwable {
		SeleniumConnector.enterDateInSelector(element,
				DateUtils.parseDate(date, DateFormats.getPatterns()));
	}

	@Given("^I select first day\\+(\\d+) of next week as \"(.*?)\"$")
	public void i_select_first_day_of_next_week_as(final int arg1,
			final String courtRoomDateElement) throws Throwable {
		final Date firstDayOfNextWeek = DateTimeUtils
				.getFirstDateOfWeek(DateUtils.addDays(new Date(), 7));
		SeleniumConnector.enterDateInSelector(courtRoomDateElement,
				DateUtils.addDays(firstDayOfNextWeek, arg1));
	}

	@Then("^I will see message \"(.*?)\" in \"(.*?)\"$")
	public void i_see_message_inthefeild(final String message, final String id)
			throws Throwable {
		final String error = SeleniumConnector.getTextFromElementBySelector(id,
				"id");
		assertTrue(StringUtils.containsAny(error, message));
	}

	@Then("^I click button \"(.*?)\" and expect it to be \"(.*?)\"$")
	public void i_click_button_and_expect_it_to_be(String buttonId,
			String visibility) throws Throwable {
		// Click element, then check if it is present/absent.
		SeleniumConnector.waitForElementPresent(buttonId);
		SeleniumConnector.pressKey(buttonId, Keys.ENTER);
		if (visibility.equals("invisible")) {
			try {
				SeleniumConnector.waitForElementToDisappear(buttonId, "id");
			} catch (TimeoutException e) {
				SeleniumConnector.pressKey(buttonId, Keys.ENTER);
				SeleniumConnector.waitForElementToDisappear(buttonId, "id");
			}
		}
	}
}
