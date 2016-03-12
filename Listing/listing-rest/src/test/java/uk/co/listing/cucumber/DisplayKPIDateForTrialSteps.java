package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.Keys;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class DisplayKPIDateForTrialSteps {

	@Then("^I see \"(.*?)\" CommittalDate \\+ (\\d+)days$")
	public void i_see_CommittalDate_days(final String arg1, final int days)
			throws Throwable {

		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final String dateString = df
				.format(DateUtils.addDays(new Date(), days));
		SeleniumConnector.waitForTextPresent(dateString);
	}

	@Given("^\"(.*?)\" is for case \"(.*?)\"$")
	public void is_for_case(final String hearing, final String courtCase)
			throws Throwable {
		SeleniumConnector.openAndWait("case");
		SeleniumConnector.waitForTextPresent("Case Crest Number");
		SeleniumConnector.setInputValue("caseCrestNumber", courtCase);
		SeleniumConnector.clickAndWait("getCaseDetailsButton");
		SeleniumConnector.waitForTextPresent(hearing);

	}

	@Given("^\"(.*?)\" has CommittalDate \"(.*?)\"$")
	public void has_CommittalDate(final String courtCase,
			final String committalDate) throws Throwable {
		if ("".equals(committalDate)) {
			assertTrue("".equals(SeleniumConnector
					.getNullableValue("dateOfCommittal")));
		}
	}

	@Given("^\"(.*?)\" has SendingDate \"(.*?)\"$")
	public void has_SendingDate(final String courtCase, final String sendingDate)
			throws Throwable {
		if ("".equals(sendingDate)) {
			assertTrue("".equals(SeleniumConnector
					.getNullableValue("dateOfSending")));
		}
	}

	@Then("^I see Trial KPI Date: (\\d+) days in HearingInformation element$")
	public void i_see_Trial_KPI_Date_days_in_HearingInformation_element(
			final int days) throws Throwable {
		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final String dateString = df
				.format(DateUtils.addDays(new Date(), days));
		assertTrue(dateString.equals(SeleniumConnector
				.getTextFromElementBySelector("hearingInfoKPIDate", "id")));
	}

	@Given("^I am on Case page$")
	public void i_am_on_Case_page() throws Throwable {
		// nothing to do here next step will take to case page
	}

	@Given("^I can see \"(.*?)\" in HearingInformation element \"(.*?)\" for \"(.*?)\"$")
	public void i_can_see_in_HearingInformation_element_for(
			final String hearing, final String bookingStatus,
			final String caseNo) throws Throwable {
		SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing",
				hearing);
		SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing",
				hearing);
		SeleniumConnector.waitForTextPresent(hearing);
		if ("Booked".equals(bookingStatus)) {
			final String courtRoom = "Room379-1";
			final String bookType = "Provisional";
			final Date startDate = DateUtils.addDays(new Date(), 7);

			SeleniumConnector.waitForElementClickable("hearingStartDate");
			SeleniumConnector
					.enterDateInSelector("hearingStartDate", startDate);
			SeleniumConnector
					.isSelectOptionPresent("roomForListing", courtRoom);
			SeleniumConnector.selectFromDropDown("roomForListing", courtRoom);
			SeleniumConnector.isSelectOptionPresent("hearingBookingType",
					bookType);
			SeleniumConnector
					.selectFromDropDown("hearingBookingType", bookType);
			SeleniumConnector.clickAndWait("listHearing");
			SeleniumConnector.waitForTextPresent(hearing);

			final String linkId = courtRoom + "_"
					+ DateTimeUtils.formatToStandardPattern(startDate) + "_"
					+ caseNo + "_" + hearing + "_link";
			// Wait for the link to appear in the planner, then attempt to click
			// on the link.
			SeleniumConnector.waitForElementPresent(linkId, "id");
			SeleniumConnector.pressKey(linkId, Keys.ENTER);
		}
	}

	@Then("^I see Trial KPI Date: (\\d+) days for \"(.*?)\" Element$")
	public void i_see_Trial_KPI_Date_days_for_Element(final int days,
			final String hearing) throws Throwable {
		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final String dateString = df
				.format(DateUtils.addDays(new Date(), days));
		SeleniumConnector.waitForTextPresent(dateString);
		assertTrue(dateString
				.equals(SeleniumConnector.getTextFromElementBySelector(
						"hearingKPIDate_" + hearing, "id")));
	}

	@Given("^I can see \"(.*?)\" in List of Hearings Element$")
	public void i_can_see_in_List_of_Hearings_Element(final String hearing)
			throws Throwable {
		SeleniumConnector.waitForTextPresent(hearing);
	}

}
