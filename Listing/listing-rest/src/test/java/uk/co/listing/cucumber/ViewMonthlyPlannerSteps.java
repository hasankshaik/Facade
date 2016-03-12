package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.StaleElementReferenceException;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateFormats;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ViewMonthlyPlannerSteps {
	public static Log log = LogFactory.getLog(ViewMonthlyPlannerSteps.class);

	// Scenario 1
	@Given("^I can see CurrentWeekPlan \"(.*?)\" \"(.*?)\"$")
	public void i_can_see_CurrentWeekPlan(final String element1,
			final String element2) throws Throwable {
		SeleniumConnector.openAndWait("schedule");
		assertTrue(SeleniumConnector.isElementPresent(element1));
		assertTrue(SeleniumConnector.isElementPresent(element2));
	}

	@Given("^I can see CurrentDate \"(.*?)\"$")
	public void i_can_see_CurrentDate(final String element) throws Throwable {
		assertTrue(SeleniumConnector.isElementPresent(element));
		final String currentDate = SeleniumConnector.getValue(element);
		final Date date = DateUtils.parseDate(currentDate,
				DateFormats.getPatterns());
		assertTrue(DateUtils.isSameDay(new Date(), date));
	}

	@Given("^I can not see CurrentDate plus Seven days \"(.*?)\"$")
	public void i_can_not_see_CurrentDate_plus_Seven_days(final String element)
			throws Throwable {
		assertTrue(SeleniumConnector.isElementPresent(element));
		final String dateAsString = DateFormatUtils.format(
				DateUtils.addDays(new Date(), 7),
				DateFormats.STANDARD.getValue());
		assertTrue(SeleniumConnector.isTextNotPresent(dateAsString));
	}

	@When("^I select MonthView \"(.*?)\"$")
	public void i_select_MonthView(final String element) throws Throwable {
		SeleniumConnector.selectFromDropDown(element, "4 Weeks");
	}

	@Then("^I can see CurrentDate in the view \"(.*?)\"$")
	public void i_can_see_CurrentDate_in_the_view(final String element)
			throws Throwable {
		assertTrue(SeleniumConnector.isElementPresent(element));
		final String currentDate = SeleniumConnector.getValue(element);
		final Date date = DateUtils.parseDate(currentDate,
				DateFormats.getPatterns());
		assertTrue(DateUtils.isSameDay(new Date(), date));
	}

	@Then("^I can see CurrentDate in the view plus three week in the view \"(.*?)\"$")
	public void i_can_see_CurrentDate_in_the_view_plus_three_week_in_the_view(
			final String element) throws Throwable {
		final String dateAsString = DateFormatUtils.format(
				DateUtils.addDays(new Date(), 21),
				DateFormats.SLASHED_DDMM.getValue());
		assertTrue(SeleniumConnector.isTextPresent(dateAsString));
	}

	// Scenario 2
	@Given("^I can see CurrentMonthPlan \"(.*?)\" \"(.*?)\"$")
	public void i_can_see_CurrentMonthPlan(final String gridElementId,
			final String viewSelectorElement) throws Throwable {
		SeleniumConnector.openAndWait("schedule");
		SeleniumConnector.selectFromDropDown(viewSelectorElement, "4 Weeks");

	}

	@When("^I select WeekView \"(.*?)\"$")
	public void i_select_WeekView(final String viewSelectorElement)
			throws Throwable {
		SeleniumConnector.selectFromDropDown(viewSelectorElement, "Week");
	}

	@Then("^I can not see CurrentDate plus three week in the view \"(.*?)\"$")
	public void i_can_not_see_CurrentDate_plus_three_week_in_the_view(
			final String arg1) throws Throwable {
		final String dateAsString = DateFormatUtils.format(
				DateUtils.addDays(new Date(), 21),
				DateFormats.SLASHED_DDMM.getValue());
		log.info("Going to check the following string is not present in Diary: "
				+ dateAsString);
		SeleniumConnector.waitForElementToDisappear(
				".//td/strong[contains(text(),'" + dateAsString + "')]",
				"xpath");
		assertTrue("Verifying date string is not present on page :"
				+ dateAsString,
				SeleniumConnector.isTextNotPresent(dateAsString));
	}

	// Scenario covered above
	// And I can see "Mon", "Tue", "Wed", "Thu", "Fri" weekday
	// And I can not see "Sat","Sun" weekday

	// Scenario 3

	// Given I can see CurrentMonthPlan "plannerGrid" "viewSelector" #

	@When("^I click \"(.*?)\" (\\d+) times$")
	public void i_click_times(final String nextButtonElementId,
			final int clickTimes) throws Throwable {
		String dateInHeader;
		for (int i = 0; i < clickTimes; i++) {
			try {
				dateInHeader = SeleniumConnector.getTextFromElementBySelector(
						".//*[@id='listHearingTable']/thead//td[2]", "xpath");
			} catch (final StaleElementReferenceException e) {
				dateInHeader = SeleniumConnector.getTextFromElementBySelector(
						".//*[@id='listHearingTable']/thead//td[2]", "xpath");
			}
			log.info("Date in Header BEFORE clicking: " + dateInHeader);
			SeleniumConnector.clickAndWait(nextButtonElementId);
			// Make sure the date has changed after the button is clicked.
			// Else we might end up asserting before the diary has updated.
			try {
				SeleniumConnector
						.waitForElementToDisappear(
								".//td/strong[contains(text(),'" + dateInHeader
										+ "')]", "xpath");
			} catch (Exception e) {
				SeleniumConnector.clickAndWait(nextButtonElementId);
				SeleniumConnector
						.waitForElementToDisappear(
								".//td/strong[contains(text(),'" + dateInHeader
										+ "')]", "xpath");
			}
			try {
				dateInHeader = SeleniumConnector.getTextFromElementBySelector(
						".//*[@id='listHearingTable']/thead//td[2]", "xpath");
			} catch (final StaleElementReferenceException e) {
				dateInHeader = SeleniumConnector.getTextFromElementBySelector(
						".//*[@id='listHearingTable']/thead//td[2]", "xpath");
			}
			log.info("Date in Header AFTER clicking: " + dateInHeader
					+ " Click Number: " + (i + 1));
		}
	}

	@Then("^I can see CurrentDate plus (\\d+)$")
	public void i_can_see_CurrentDate_plus(final int noOfweeks)
			throws Throwable {
		final String dateAsString = DateFormatUtils.format(
				DateUtils.addDays(new Date(), noOfweeks * 7),
				DateFormats.SLASHED_DDMM.getValue());
		log.info("Verifying Date String is present on Diary : " + dateAsString);
		SeleniumConnector.waitForTextPresent(dateAsString);
	}

	// Scenario 4

	// Given I can see CurrentMonthPlan "plannerGrid" "viewSelector" #

	@Given("^MonthPlan includes CurrentDate plus (\\d+)$")
	public void monthplan_includes_CurrentDate_plus(final int noOfweeks)
			throws Throwable {
		final String dateAsString = DateFormatUtils.format(
				DateUtils.addDays(new Date(), noOfweeks * 4),
				DateFormats.SLASHED_DDMM.getValue());
		log.info("Waiting for string to disappear " + dateAsString);
		SeleniumConnector.waitForElementPresent(
				".//td/strong[contains(text(),'" + dateAsString + "')]",
				"xpath");
	}

	@Then("^I can see CurrentDate minus (\\d+)$")
	public void i_can_see_CurrentDate(final int noOfweeks) throws Throwable {
		final SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd/MM");
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -noOfweeks * 7);
		SeleniumConnector.waitForTextPresent(dayMonthFormat.format(cal
				.getTime()));
	}
}