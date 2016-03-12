package uk.co.listing.cucumber;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ListPCMHRequiringAction {

    private static final String PAST = "Past";
    private static final String TODAY = "Today";
    private static final String TOMORROW = "Tomorrow";
    private static final String ID_NOT_SET = "id_not_set";

    @Given("^following Cases exists with corresponding PCM date and PCM status$")
    public void following_Cases_exists_with_corresponding_PCM_date_and_PCM_status(final DataTable arg1) throws Throwable {
        // All the cases and corresponding PCM Hearings have been setup in
        // imports.sql.
        // Hence nothing to do here.
    }

    @Then("^I can see Manage PCMH element$")
    public void i_can_see_Manage_PCMH_element() throws Throwable {
        SeleniumConnector.waitForElementPresent("managePCMHDiv");
    }

    @Then("^I can see Case \"(.*?)\" , \"(.*?)\" button under \"(.*?)\"$")
    public void i_can_see_Case_button_under(final String caseNo, final String buttonText, final String sectionText) throws Throwable {
        SeleniumConnector.waitForElementPresent(caseNo + "_" + getDateFromDescription(sectionText) + "_" + sectionText.toLowerCase());
        SeleniumConnector.waitForElementPresent(caseNo + "_" + getDateFromDescription(sectionText) + "_" + sectionText.toLowerCase() + "_editPcmh");
    }

    @Then("^I can not see following Cases under \"(.*?)\"$")
    public void i_can_not_see_following_Cases_under(final String sectionText, final List<String> caseNos) throws Throwable {
        for (final String caseNo : caseNos) {
            SeleniumConnector.waitForElementToDisappear(caseNo + "_" + getDateFromDescription(sectionText) + "_" + sectionText.toLowerCase(), "id");
        }
    }

    @When("^I click button \"(.*?)\" for case \"(.*?)\" under \"(.*?)\"$")
    public void i_click_button_for_under(final String buttonText, final String caseNo, final String sectionText) throws Throwable {
        SeleniumConnector.waitForElementPresent(caseNo + "_" + getDateFromDescription(sectionText) + "_" + sectionText.toLowerCase());
        SeleniumConnector.waitForElementPresent(caseNo + "_" + getDateFromDescription(sectionText) + "_" + sectionText.toLowerCase() + "_editPcmh");
    }

    public String getDateFromDescription(final String dateDescription) {
        String dayString = ID_NOT_SET;
        if (dateDescription.equals(TOMORROW)) {
            dayString = DateTimeUtils.formatToStandardPattern(DateUtils.addDays(new Date(), 1));
        } else if (dateDescription.equals(TODAY)) {
            dayString = DateTimeUtils.formatToStandardPattern(new Date());
        } else if (dateDescription.equals(PAST)) {
            dayString = DateTimeUtils.formatToStandardPattern(DateUtils.addDays(new Date(), -1));
        }
        return dayString;
    }
}