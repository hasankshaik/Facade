package uk.co.listing.cucumber;


import org.junit.Assert;

import uk.co.listing.SeleniumConnector;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SetSittingDaysSteps {

	@Given("^yearStartdate for financial year \"(.*?)\" is \"(.*?)\"$")
	public void yearstartdate_for_financial_year_is(String arg1, String arg2) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@Given("^yearEnddate for financial year \"(.*?)\" is \"(.*?)\"$")
	public void yearenddate_for_financial_year_is(String arg1, String arg2) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@Given("^PlannerStartDate is \"(.*?)\"$")
	public void plannerstartdate_is(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@Given("^I am on the sittings page$")
	public void i_am_on_the_sittings_page() throws Throwable {
		SeleniumConnector.openAndWait("sitting");
	}

	@Given("^I am in \"(.*?)\" court centre$")
	public void i_am_in_court_centre(String arg1) throws Throwable {
		Assert.assertTrue(SeleniumConnector.isTextPresent(arg1));
	}

	@When("^I enter (\\d+) in \"(.*?)\" for (\\d+)-(\\d+)$")
	public void i_enter_in_for(int arg1, String arg2, int arg3, int arg4) throws Throwable {
		SeleniumConnector.setInputValue(arg2, String.valueOf(arg1));
	}

	@When("^I reload page$")
	public void i_reload_page() throws Throwable {
		SeleniumConnector.reloadPage();
	}
	
	@Then("^I can see \"(.*?)\" is \"(.*?)\" for \"(.*?)\"$")
	public void i_can_see_is_for(String arg1, String arg2, String arg3) throws Throwable {
		Assert.assertEquals(arg2, SeleniumConnector.getValue(arg1));
		Assert.assertTrue(SeleniumConnector.isTextPresent(arg3));
	}

	@Given("^\"(.*?)\" is \"(.*?)\" for \"(.*?)\"$")
	public void is_for(String arg1, String arg2, String arg3) throws Throwable {
		SeleniumConnector.setInputValue(arg1, String.valueOf(arg2));
	}
}
