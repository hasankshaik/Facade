package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.List;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class OverBookingSteps {
    
    public final String COURTCENTRENAME = "Centre736";
    public final String courtCentreQueryString = "?courtCenter=" + COURTCENTRENAME;
    
    @Given("^Court Centre \"(.*?)\" exists with a court room \"(.*?)\"$")
    public void court_Centre_exists(String centreName, String roomName) throws Throwable {
        SeleniumConnector.openAndWait("schedule" + courtCentreQueryString);
        SeleniumConnector.openAndWait("schedule" + courtCentreQueryString);
        SeleniumConnector.waitForTextPresent("Crown Court at " + COURTCENTRENAME, 10);
        SeleniumConnector.openAndWait("admin" + courtCentreQueryString);
        SeleniumConnector.openAndWait("admin" + courtCentreQueryString);
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.setInputValue("courtRoomInput", roomName);
        SeleniumConnector.clickAndWait("saveCourtRoomButton");
        SeleniumConnector.waitForTextPresent(roomName);
    }

    @Given("^I am on Planner page for Court Centre \"(.*?)\"$")
    public void i_am_on_planner_for_court_centre(String centreName) throws Throwable {
        SeleniumConnector.openAndWait("schedule" + "?courtCenter=" + centreName);
        SeleniumConnector.openAndWait("schedule" + "?courtCenter=" + centreName);
        SeleniumConnector.waitForTextPresent("Crown Court at " + centreName);
    }
    
    @Then("^I should see following dates in Available Slot Dates For Hearing$")
    public void i_should_see_available_results(List<String> dateString) throws Throwable {
        SeleniumConnector.waitForTextPresent("Available Slot Dates For Hearing");
        for (final String dateStr : dateString) {
            String expDate = DateTimeUtils.formatToStandardPattern(CommonSteps.getDate(dateStr));
            assertTrue(SeleniumConnector.isTextPresent(expDate));
        }
    }
}
    