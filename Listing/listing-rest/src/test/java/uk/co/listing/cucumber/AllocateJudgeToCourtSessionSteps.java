package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AllocateJudgeToCourtSessionSteps {
	public static Log log = LogFactory
			.getLog(AllocateJudgeToCourtSessionSteps.class);
	public static boolean judgeAdded = false;
	public static boolean courtAdded = false;

	@Given("^\"(.*?)\" exists in \"(.*?)\"$")
	public void exists_in(final String judgeName,
			final String listOfJudgeElement) throws Throwable {
		if (!judgeAdded) {
			SeleniumConnector.openAndWait("admin");
			SeleniumConnector.setInputValue("judgeName", judgeName);
			SeleniumConnector.waitForElementPresent("listOfJudgeTypes");
			SeleniumConnector.isSelectOptionPresent("listOfJudgeTypes",
					"High Court");
			SeleniumConnector.selectFromDropDown("listOfJudgeTypes",
					"High Court");
			SeleniumConnector.clickAndWait("addJudge");
			SeleniumConnector.waitForTextPresent("Saved");
			judgeAdded = true;
		}
	}

	@Given("^Court Room \"(.*?)\" exists in \"(.*?)\"$")
	public void court_Room_exists_in(final String courtRoomName,
			final String courtRoomsElements) throws Throwable {
		if (!courtAdded) {
			SeleniumConnector.setInputValue("courtRoomInput", courtRoomName);
			SeleniumConnector.clickAndWait("saveCourtRoomButton");
			SeleniumConnector.waitForTextPresent("Saved");
			courtAdded = true;
		}
	}

	@When("^I select a \"(.*?)\" from \"(.*?)\"$")
	public void i_select_a_from(final String elementNameToSelected,
			final String elementId) throws Throwable {
		SeleniumConnector.isSelectOptionPresent(elementId,
				elementNameToSelected);
		SeleniumConnector.selectFromDropDown(elementId, elementNameToSelected);
	}

	@When("^I multi-select a \"(.*?)\" from \"(.*?)\"$")
	public void i_multi_select_a_from(final String elementNameToSelected,
			final String elementId) throws Throwable {
		final List<String> listElementNameToSelected = new ArrayList<String>(
				Arrays.asList(elementNameToSelected.split(",")));
		SeleniumConnector.multiSelectFromDropDown(elementId,
				listElementNameToSelected);
	}

	@When("^I select current date as sessions sitting day \"(.*?)\"$")
	public void i_select_current_date_as_sessions_sitting_day(
			final String courtRoomDateElement) throws Throwable {
		SeleniumConnector.enterDateInSelector(courtRoomDateElement, new Date());
	}

	@When("^I select current date plus (\\d+) as sessions sitting day \"(.*?)\"$")
	public void i_select_current_date_plus_seven_as_sessions_sitting_day(
			final int noDays, final String courtRoomDateElement)
			throws Throwable {
		SeleniumConnector.enterDateInSelector(courtRoomDateElement,
				DateTimeUtils
						.getEndWorkingDateFromStartDate(new Date(), noDays));
	}

	@Then("^I can see \"(.*?)\" in correct location in the whole period in plannerGrid \"(.*?)\"$")
	public void i_can_see_in_correct_location_in_whole_period_in_plannerGrid(
			final String judgeName, final String courtName) throws Throwable {
		Date startDay = new Date();
		final Date endDay = DateTimeUtils.getEndWorkingDateFromStartDate(
				startDay, 7);
		while (!startDay.after(endDay)) {
			if (!DateTimeUtils.isWeekend(startDay)) {
				SeleniumConnector.waitForElementClickable(courtName + "_"
						+ DateTimeUtils.formatToStandardPattern(startDay) + "_"
						+ judgeName);
				assertTrue(judgeName
						.equals(SeleniumConnector.getTextFromElementBySelector(
								courtName
										+ "_"
										+ DateTimeUtils
												.formatToStandardPattern(startDay)
										+ "_" + judgeName, "id")));
			}
			final Calendar day = Calendar.getInstance();
			day.setTime(startDay);
			// We check if the day is Sunday to move to the next week
			if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				SeleniumConnector.clickAndWait("nextButton");
			}
			startDay = DateUtils.addDays(startDay, 1);
		}
	}

	@Then("^I can see \"(.*?)\" in correct location in plannerGrid \"(.*?)\"$")
	public void i_can_see_in_correct_location_in_plannerGrid(
			final String judgeName, final String courtName) throws Throwable {
		final Date startDay = new Date();
		if (!DateTimeUtils.isWeekend(startDay)) {
			SeleniumConnector.waitForElementClickableBySelector(courtName + "_"
					+ DateTimeUtils.formatToStandardPattern(startDay) + "_"
					+ judgeName, "id");
			assertTrue(judgeName.equals(SeleniumConnector
					.getTextFromElementBySelector(courtName + "_"
							+ DateTimeUtils.formatToStandardPattern(startDay)
							+ "_" + judgeName, "id")));
		}
	}

}