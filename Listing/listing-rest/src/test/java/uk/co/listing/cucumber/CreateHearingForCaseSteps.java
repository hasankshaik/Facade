package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateHearingForCaseSteps {
	public static Log log = LogFactory.getLog(CreateHearingForCaseSteps.class);
	String estimateInputId;
	
	@Given("^I am on Case Page$")
	public void i_am_on_Case_Page() throws Throwable {
		SeleniumConnector.openAndWait("case");
	}

	@Given("^Case \"(.*?)\" exists on the system$")
	public void case_exists_on_the_system(String arg1) throws Throwable {
	  //Nothing to do here, because we cant see the case until we introduce it in the input
	}

	@Then("^I can see \"(.*?)\" in Case Page$")
	public void i_can_see_in_Case_Page(String caseName) throws Throwable {
		assertTrue(SeleniumConnector.isTextPresent(caseName));
	}
	
	@Given("^I see case \"(.*?)\" with Trial Estimate (\\d+) and hearing \"(.*?)\"$")
	public void i_see_case_with_Trial_Estimate_and_hearing(String caseName, int trialEstimate, String hearingName) throws Throwable {
	    SeleniumConnector.openAndWait("case");
        SeleniumConnector.waitForTextPresent("Case Crest Number");
        SeleniumConnector.setInputValue("caseCrestNumber", caseName);
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForElementAttribute("crestCaseNumber", "value", caseName);
        SeleniumConnector.waitForElementAttribute("trialEstimate", "value", String.valueOf(trialEstimate));
        SeleniumConnector.waitForTextPresent(hearingName);
	}

	@Given("^I edit Hearing Estimate of hearing \"(.*?)\" to (\\d+)$")
	public void i_edit_Hearing_Estimate_of_hearing_to(String hearingName, int newEstimate) throws Throwable {
	    String hearingEstimateInputLocator = ".//*[@id='hearings']/tbody//td[text()='" + hearingName + "']/..//input[contains(@id, 'hearingTrialEstimateSelected')]";
	    estimateInputId = SeleniumConnector.getAttribute("id", hearingEstimateInputLocator, "xpath");
	    String setButtonLocator = ".//*[@id='hearings']/tbody//td[text()='" + hearingName + "']/..//button";
        SeleniumConnector.setInputValue(estimateInputId, String.valueOf(newEstimate));
        SeleniumConnector.clickAndWaitBySelector(setButtonLocator, "xpath");
	}

	@Given("^I see case \"(.*?)\" with Trial Estimate (\\d+)$")
	public void i_see_case_with_Trial_Estimate(String caseName, int trialEstimate) throws Throwable {
	    SeleniumConnector.setInputValue("caseCrestNumber", caseName);
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForElementAttribute("crestCaseNumber", "value", caseName);
        SeleniumConnector.waitForElementAttribute("trialEstimate", "value", String.valueOf(trialEstimate));
	}

	@When("^I add hearing \"(.*?)\" to case \"(.*?)\"$")
	public void i_add_hearing_to_case(String hearingName, String caseName) throws Throwable {
	    SeleniumConnector.setInputValue("hearingName", hearingName);
	    SeleniumConnector.clickAndWait("createHearing");
	}
	
	@Then("^Hearing Estimate for hearing \"(.*?)\" is (\\d+)$")
	public void hearing_estimate_for_hearing_is(String hearingName, int hearingEstimate) throws Throwable {
	    assertTrue("Hearing Estimates is not as expected.", hearingEstimate == Integer.valueOf(SeleniumConnector.getValue(estimateInputId)));
	}
}