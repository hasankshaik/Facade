package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ViewActualSittingDaysSteps {

	private int courtCentreSittingsActual = 0;

	@Given("^I am in Planner in \"(.*?)\"$")
	public void i_am_in_planner_in(String courtCenter) throws Throwable {
		SeleniumConnector.openAndWait("schedule");
		String courtCenterValue = SeleniumConnector.getTextFromElementBySelector("courtCenter", "id");
		assertTrue(SeleniumConnector.isElementPresent("courtCenter"));
		assertTrue(courtCenterValue.equals(courtCenter));
	}

	@Given("^I can see courtCentreSittingsActual$")
	public void i_can_see_courtCentreSittingsActual() throws Throwable {
	    SeleniumConnector.waitForTextPresent("Actual sitting days");
	    SeleniumConnector.waitForElementPresent("actualSittingDays");
	    String actualSittingDays = "0";
		actualSittingDays = SeleniumConnector.getTextFromElementBySelector("actualSittingDays", "id");
		courtCentreSittingsActual = new Integer(actualSittingDays);

	}

	@When("^I can allocate Judge A to \"(.*?)\" in \"(.*?)\"$")
	public void i_can_allocate_Judge_A_to_in(String courtRoomName, String day) throws Throwable {
		SeleniumConnector.openAndWait("admin");
		String surname = RandomStringUtils.randomAlphabetic(10);
		// This is to not repeat the judge twice
		SeleniumConnector.setInputValue("judgeName", "Judge " + surname);
		SeleniumConnector.waitForElementPresent("listOfJudgeTypes");
		SeleniumConnector.isSelectOptionPresent("listOfJudgeTypes",
				"High Court");
		SeleniumConnector.selectFromDropDown("listOfJudgeTypes",
				"High Court");
        SeleniumConnector.clickAndWait("addJudge");
        SeleniumConnector.waitForTextPresent("Saved");
		SeleniumConnector.setInputValue("courtRoomInput", courtRoomName);
		SeleniumConnector.clickAndWait("saveCourtRoomButton");
		SeleniumConnector.clickAndWait("schedule");
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date = format.parse(day);
		SeleniumConnector.isSelectOptionPresent("courtRooms", courtRoomName);
		SeleniumConnector.selectFromDropDown("courtRooms", courtRoomName);
		SeleniumConnector.isSelectOptionPresent("listOfJudgesForAlloc", "Judge " + surname);
		SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", "Judge " + surname);
		SeleniumConnector.enterDateInSelector("courtRoomStartDate", date);
		SeleniumConnector.enterDateInSelector("courtRoomEndDate", date);
	}

	@Then("^I can see courtCentreSittingsActual is increased$")
	public void i_can_see_courtCentreSittingsActual_is_increased() throws Throwable {
		//SeleniumConnector.waitForTextToAppear("actualSittingDays", ""+(courtCentreSittingsActual + 1));
		SeleniumConnector.waitForTextPresent("Actual sitting days: " + String.valueOf(courtCentreSittingsActual + 1));
		String actualSittingDays = SeleniumConnector.getTextFromElementBySelector("actualSittingDays", "id");
		int newCourtCentreSittingsActual = new Integer(actualSittingDays);
		assertTrue(newCourtCentreSittingsActual == courtCentreSittingsActual + 1);
	}

	@Then("^I can see courtCentreSittingsActual is not increased$")
	public void i_can_see_courtCentreSittingsActual_is_not_increased() throws Throwable {
		String actualSittingDays = SeleniumConnector.getTextFromElementBySelector("actualSittingDays", "id");
		int newCourtCentreSittingsActual = new Integer(actualSittingDays);
		assertTrue("not equal newCourtCentreSittingsActual:" + newCourtCentreSittingsActual + ", courtCentreSittingsActual:" + courtCentreSittingsActual,
				newCourtCentreSittingsActual == courtCentreSittingsActual);
	}

	@Then("^I can see courtCentreSittingsTarget$")
	public void i_can_see_courtCentreSittingsTarget() throws Throwable {
		assertTrue(SeleniumConnector.isElementPresent("targetSittingDays"));
	}

	@When("^I go to next financial year$")
	public void i_go_to_next_financial_year() throws Throwable {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2016, 9, 10);
		SeleniumConnector.enterDateInSelector("dateInput", calendar.getTime());
		SeleniumConnector.clickAndWait("goto");
	}

}
