package uk.co.listing.cucumber;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Keys;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ManageSessionSteps {
    public static Log log = LogFactory.getLog(ManageSessionSteps.class);

    @Then("^I can see \"(.*?)\"  first day\\+(\\d+) of next week as Non-sitting day \"(.*?)\"$")
    public void i_can_see_first_day_of_next_week_as_Non_sitting_day(final String roomName, final int dayCount, final String closed) throws Throwable {
        final Date firstDayOfNextWeek = DateTimeUtils.getFirstDateOfWeek(DateUtils.addDays(new Date(), 7));
        final String element = roomName + "_" + DateTimeUtils.formatToStandardPattern(DateUtils.addDays(firstDayOfNextWeek, dayCount)) + "_" + closed;
        SeleniumConnector.enterDateInSelector("dateInput", firstDayOfNextWeek);
        SeleniumConnector.pressKey("goto", Keys.ENTER);
        try {
            SeleniumConnector.waitForElementPresent(element);
        } catch (final Exception e) {
            SeleniumConnector.enterDateInSelector("dateInput", firstDayOfNextWeek);
            SeleniumConnector.pressKey("goto", Keys.ENTER);
            SeleniumConnector.waitForElementPresent(element);
        }
    }

    @Then("^I can see day of trail (\\d+) and total (\\d+) of \"(.*?)\" on first day\\+(\\d+) of next week$")
    public void i_can_see_day_of_trial(final int day, final int total, final String hearingname, final int dayCount) throws Throwable {
        final Date firstDayOfNextWeek = DateTimeUtils.getFirstDateOfWeek(DateUtils.addDays(new Date(), 7));
        final String element = hearingname + "_" + day + "_" + total;
        Date date = DateUtils.addDays(firstDayOfNextWeek, dayCount);
        while (DateTimeUtils.isWeekend(date)) {
            date = DateUtils.addDays(date, 1);
        }
        SeleniumConnector.enterDateInSelector("dateInput", date);
        SeleniumConnector.pressKey("goto", Keys.ENTER);
        try {
            SeleniumConnector.waitForElementPresent(element);
        } catch (final Exception e) {

        }
    }

    @When("^I Open session by clicking \"(.*?)\"$")
    public void i_Open_session_by_clicking(final String buttonId) throws Throwable {
        SeleniumConnector.waitForElementPresent(buttonId);
        SeleniumConnector.pressKey(buttonId, Keys.ENTER);
    }

    @When("^I Close session by clicking \"(.*?)\"$")
    public void i_close_session_by_clicking(final String buttonId) throws Throwable {
        SeleniumConnector.waitForElementPresent(buttonId);
        SeleniumConnector.pressKey(buttonId, Keys.ENTER);
    }
}