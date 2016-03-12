package uk.co.listing.cucumber;

import java.util.List;

import org.junit.Assert;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SetSittingDaysForFutureYears {
	
	@Given("^I am on Sittings page$")
	public void i_am_on_sitting_page() throws Throwable {
		SeleniumConnector.openAndWait("sitting");
	}
	
	@Given("^financialYears is \"(.*?)\"$")
	public void financialyears_is(String arg1) throws Throwable {
		Assert.assertEquals(arg1, SeleniumConnector.getSelectedOption("financialYears"));
	}

	
	@Then("^I can see in \"(.*?)\"$")
	public void i_can_see_in(String arg1, List<String> arg2) throws Throwable {
		for (String expectedYear : arg2){
			Assert.assertTrue(SeleniumConnector.isSelectOptionPresent(arg1, expectedYear));
        }
	}
	
	@Then("^I can not see in \"(.*?)\"$")
	public void i_can_not_see_in(String arg1, List<String> arg2) throws Throwable {
		for (String invalidYear : arg2){
			Assert.assertTrue(SeleniumConnector.isSelectOptionNotPresent(arg1, invalidYear));
        }
	}
	
	@When("^I select year \"(.*?)\" in financialYears$")
	public void i_select_year_in_financialyears(String finYear) throws Throwable {
	    SeleniumConnector.selectFromDropDown("financialYears", finYear);
	    SeleniumConnector.waitForTextPresent("Monthly Sittings for " + finYear);
	}
	
	@Then("^I can see \"(.*?)\" is \"(.*?)\" days$")
	public void i_can_see_is_days(String arg1, String arg2) throws Throwable {
		SeleniumConnector.waitForTextToAppear(arg1, arg2);
		Assert.assertEquals(arg2, SeleniumConnector.getValue(arg1));
	}
	
	@When("^I enter (\\d+) in sittingTargetInput$")
	public void i_enter_in_sittingTargetInput(String arg1) throws Throwable {
		SeleniumConnector.clearInput("sittingTargetInput");
		SeleniumConnector.waitForTextToAppear("sittingTargetInput", "");
		SeleniumConnector.setInputValue("sittingTargetInput", arg1);
		SeleniumConnector.waitForTextToAppear("sittingTargetInput", arg1);
	}
	
	@When("^I select year \"(.*?)\" in planner$")
	public void i_select_year_in_planner(String arg1) throws Throwable {
		SeleniumConnector.openAndWait("schedule");
		SeleniumConnector.waitForTextPresent("Room Name");
		SeleniumConnector.scrollToElement("dateInput");
		SeleniumConnector.enterDateInSelector("dateInput", "03", "04", arg1);
		SeleniumConnector.clickAndWait("goto");
	}
	
	@Then("^I can see \"(.*?)\" in targetSittingDays$")
	public void i_can_see_in_targetSittingDays(String tarSit) throws Throwable {
		SeleniumConnector.waitForTextPresent(tarSit);
		Assert.assertEquals(tarSit, SeleniumConnector.getTextFromElementBySelector("targetSittingDays", "id"));
	}

}