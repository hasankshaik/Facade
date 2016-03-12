package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.Keys;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GotoDatePlannerSteps {

	@When("^I enter \"(.*?)\" date \\+(\\d+) \"(.*?)\"$")
	public void i_enter_date(String period, int amount, String unit)
			throws Throwable {
		Date date = null;
		if (period.equalsIgnoreCase("current")) {
			date = new Date();
		}

		if (unit.equalsIgnoreCase("day")) {
			date = DateUtils.addDays(date, amount);
		} else if (unit.equalsIgnoreCase("month")) {
			date = DateUtils.addMonths(date, amount);
		} else if (unit.equalsIgnoreCase("year")) {
			date = DateUtils.addYears(date, amount);
		}
		SeleniumConnector.enterDateInSelector("dateInput", date);
		SeleniumConnector.pressKey("goto", Keys.ENTER);
	}

	@When("^I Goto date (\\d+)/(\\d+)/(\\d+)$")
	public void i_Goto_date(String arg1, String arg2, String arg3)
			throws Throwable {
		SeleniumConnector.enterDateInSelector("dateInput", arg1, arg2, arg3);
		SeleniumConnector.pressKey("goto", Keys.ENTER);
	}

	@Then("^I can see \"(.*?)\" date \\+(\\d+) \"(.*?)\"$")
	public void i_can_see_date(String period, int amount, String unit)
			throws Throwable {
		Date date = null;
		if (period.equalsIgnoreCase("current")) {
			date = new Date();
		}

		if (unit.equalsIgnoreCase("day")) {
			date = DateUtils.addDays(date, amount);
		} else if (unit.equalsIgnoreCase("month")) {
			date = DateUtils.addMonths(date, amount);
		} else if (unit.equalsIgnoreCase("year")) {
			date = DateUtils.addYears(date, amount);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM");
		assertTrue(SeleniumConnector.isTextPresent(sdf.format(date)));

	}

	@When("^I press Enter$")
	public void i_press_Enter() throws Throwable {
		SeleniumConnector.pressKey("dateInput", Keys.RETURN);
	}

	@Given("^I enter date (\\d+)/(\\d+)/(\\d+)$")
	public void i_enter_date(String arg1, String arg2, String arg3)
			throws Throwable {
		SeleniumConnector.enterDateInSelector("dateInput", arg1, arg2, arg3);
		SeleniumConnector.clickAndWait("goto");
	}

	@Then("^I can see date (\\d+)/(\\d+)/(\\d+)$")
	public void i_can_see_date(int arg1, int arg2, int arg3) throws Throwable {
		Date date = DateUtils.parseDate("" + arg1 + "/" + (arg2) + "/" + arg3,
				"dd/MM/yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		assertTrue(sdf.format(date).equals(
				SeleniumConnector.getValue("dateInput")));
	}

}
