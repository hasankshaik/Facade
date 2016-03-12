package uk.co.listing.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AddJudgeToListSteps {
    public static Log log = LogFactory.getLog(AddJudgeToListSteps.class);

    @Given("^I go to listing admin$")
    public void i_can_see_listofJudges() throws Throwable {
        SeleniumConnector.openAndWait("admin");
    }

    @When("^I enter \"(.*?)\" in \"(.*?)\"$")
    public void i_enter(final String name, final String id) throws Throwable {
        SeleniumConnector.setInputValue(id, name);
    }

    @When("^I select \"(.*?)\"$")
    public void i_select(final String id) throws Throwable {
        SeleniumConnector.clickAndWait(id);
    }

    @Then("^I see \"(.*?)\" in \"(.*?)\"$")
    public void i_see_in(final String name, final String id) throws Throwable {
        assertTrue(SeleniumConnector.isSelectOptionPresent(id, name));
    }

    @Then("^I see \"(.*?)\" \"(.*?)\", already exists$")
    public void i_see_error(final String id, final String value) throws Throwable {
        final String error = SeleniumConnector.getTextFromElementBySelector(id, "id");
        assertTrue(StringUtils.containsAny(error, "already exists"));
    }

    @Then("^I see \"(.*?)\" validation error\\.$")
    public void i_see_validation_error(final String id) throws Throwable {
        final String error = SeleniumConnector.getTextFromElementBySelector(id, "id");
        assertTrue(StringUtils.containsAny(error, "Invalid"));
    }

    @When("^I select Judge type as \"(.*?)\"$")
    public void i_select_Judge_type_as(final String judgeType) throws Throwable {
        SeleniumConnector.waitForElementPresent("listOfJudgeTypes");
        SeleniumConnector.isSelectOptionPresent("listOfJudgeTypes", judgeType);
        SeleniumConnector.selectFromDropDown("listOfJudgeTypes", judgeType);
    }

    @When("^I select Is QC as \"(.*?)\"$")
    public void i_select_Is_QC_as(final String isQc) throws Throwable {
        SeleniumConnector.waitForElementPresent("isQC");
        if (isQc.equals("Yes")) {
            SeleniumConnector.clickAndWait("isQC");
        }
    }

    @Then("^Judge type for Judge \"(.*?)\" is \"(.*?)\"$")
    public void judge_type_for_Judge_is(final String judgeName, final String judgeType) throws Throwable {
        SeleniumConnector.waitForElementPresent("judgeType_" + judgeName);
        assertTrue(SeleniumConnector.getTextFromElementBySelector("judgeType_" + judgeName, "id").equals(judgeType));
    }

    @Then("^Is QC for Judge \"(.*?)\" is \"(.*?)\"$")
    public void is_QC_for_Judge_is(final String judgeName, final String isQc) throws Throwable {
        SeleniumConnector.waitForElementPresent("judgeIsQC_" + judgeName);
        assertTrue(SeleniumConnector.getTextFromElementBySelector("judgeIsQC_" + judgeName, "id").equals(isQc));
    }

    @Then("^Is Resident Judge is disabled$")
    public void is_resident_judge_is_disabled() throws Throwable {
        assertTrue(!SeleniumConnector.isEnabled("isResident"));
    }

    @When("^I mark Judge as Resident Judge$")
    public void i_mark_Judge_as_Resident_Judge() throws Throwable {
        SeleniumConnector.waitForElementPresent("isResident");
        SeleniumConnector.clickAndWait("isResident");
    }

    @Then("^Resident Judge is \"(.*?)\" for Judge \"(.*?)\"$")
    public void resident_Judge_is_for_Judge(final String isResidentJudge, final String judgeName) throws Throwable {
        SeleniumConnector.waitForElementPresent("judgeIsResident_" + judgeName);
        assertTrue(SeleniumConnector.getTextFromElementBySelector("judgeIsResident_" + judgeName, "id").equals(isResidentJudge));
    }

    @Then("^JudgeName \"(.*?)\" has \"(.*?)\"$")
    public void judgename_has(final String judgeName, final String listOfTickets) throws Throwable {
        SeleniumConnector.waitForElementPresent("judgeTickets_" + judgeName);
        final StringTokenizer st = new StringTokenizer(listOfTickets, ",");
        String element;
        while (st.hasMoreElements()) {
            element = st.nextToken();
            assertTrue(SeleniumConnector.getTextFromElementBySelector("judgeTickets_" + judgeName, "id").contains(element));
        }
    }

    @When("^I select listOfTickets \"(.*?)\"$")
    public void i_select_listOfTickets(final String listOfTickets) throws Throwable {
        final StringTokenizer st = new StringTokenizer(listOfTickets, ",");
        String element;
        while (st.hasMoreElements()) {
            element = st.nextToken().replace(" ", "_").toLowerCase();
            SeleniumConnector.clickAndWait(element);
        }
    }

    @When("^I don't select other tickets$")
    public void i_don_t_select_other_tickets() throws Throwable {
        assertTrue(!SeleniumConnector.isEnabled("murder"));
        assertTrue(!SeleniumConnector.isEnabled("attempted_murder"));
        assertTrue(!SeleniumConnector.isEnabled("fraud"));
        assertTrue(!SeleniumConnector.isEnabled("health_and_safety"));
    }

    @Then("^JudgeName \"(.*?)\" does not have any other tickets$")
    public void judgename_does_not_have_any_other_tickets(final String judgeName) throws Throwable {
        assertFalse(SeleniumConnector.getTextFromElementBySelector("judgeTickets_" + judgeName, "id").contains("Murder"));
    }

    @When("^I can not select a ticket$")
    public void i_can_not_select_a_ticket() throws Throwable {
        i_don_t_select_other_tickets();
        assertTrue(!SeleniumConnector.isEnabled("sexual_offences"));
    }
}