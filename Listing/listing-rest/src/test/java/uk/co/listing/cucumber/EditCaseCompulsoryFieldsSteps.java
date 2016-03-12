package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;
import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EditCaseCompulsoryFieldsSteps {

    @Given("^The case \"(.*?)\" exists in \"(.*?)\"$")
    public void the_case_exists(final String courtCase, final String court) throws Throwable {
        // Nothing to be done here as it is in the import script
    }

    @Then("^I can see defendant \"(.*?)\" in \"(.*?)\"$")
    public void i_can_see_in_cointains(final String arg1, final String arg2) throws Throwable {
        assertTrue(SeleniumConnector.getValue(arg2).contains(arg1));
    }

    @When("^I open the case$")
    public void i_open_the_case() throws Throwable {
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForTextPresent("Date of Sending");
    }

    @When("^CaseNumber \"(.*?)\", ReleaseDecision \"(.*?)\", \"(.*?)\"$")
    public void casenumber_ReleaseDecision(final String caseNumber, final String releaseDecision, final String allocateJudge) throws Throwable {
        assertTrue(SeleniumConnector.getValue("crestCaseNumber").contains(caseNumber));
        assertTrue(SeleniumConnector.getValue("releaseDecisionStatus").contains(releaseDecision));
        assertTrue(SeleniumConnector.getValue("judgeSelected").contains(allocateJudge));
    }

}
