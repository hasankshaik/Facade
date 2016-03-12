package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateFormats;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateBlockSteps {

    @Then("^I see Block type code \"(.*?)\" for type \"(.*?)\" in Court room \"(.*?)\" from start date \"(.*?)\" to end date \"(.*?)\"$")
    public void i_see_Block_type_code_in_Court_room_from_start_date_to_end_date(final String blockPresentCode, final String blockType, final String courtRoom, final String startDate,
            final String endDate) throws Throwable {
        Date startDay = CommonSteps.getDate(startDate);
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.enterDateInSelector("dateInput", startDay);
        SeleniumConnector.clickAndWait("goto");
        final DateFormat df = new SimpleDateFormat("dd/MM");
        final String dateString = df.format(startDay);
        SeleniumConnector.waitForTextPresent(dateString);

        final Date endDay = CommonSteps.getDate(endDate);
        while (!startDay.after(endDay)) {
            if (!DateTimeUtils.isWeekend(startDay)) {
                SeleniumConnector.waitForTextPresent(blockPresentCode);
                SeleniumConnector.waitForElementClickableBySelector(".//*[@id = '" + courtRoom + "_" + DateFormatUtils.format(startDay, DateFormats.STANDARD.getValue()) + "_" + blockType + "']/span",
                        "xpath");
                assertTrue(blockPresentCode.equals(SeleniumConnector.getTextFromElementBySelector(".//*[@id = '" + courtRoom + "_" + DateFormatUtils.format(startDay, DateFormats.STANDARD.getValue())
                        + "_" + blockType + "']/span", "xpath")));
            }
            final Calendar day = Calendar.getInstance();
            day.setTime(startDay);
            // We check if the day is Sunday to move to the next week
            if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                SeleniumConnector.clickAndWait("nextButton");
            }
            startDay = DateUtils.addDays(startDay, 1);
        }
    }

    @When("^I do not see Block type code \"(.*?)\" for type \"(.*?)\" in Court room \"(.*?)\" from start date \"(.*?)\" to end date \"(.*?)\"$")
    public void i_do_not_see_Block_type_code_for_type_in_Court_room_from_start_date_to_end_date(final String blockPresentCode, final String blockType, final String courtRoom, final String startDate,
            final String endDate) throws Throwable {
        final Date startDay = CommonSteps.getDate(startDate);
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.enterDateInSelector("dateInput", startDay);
        SeleniumConnector.clickAndWait("goto");
        final DateFormat df = new SimpleDateFormat("dd/MM");
        final String dateString = df.format(startDay);
        SeleniumConnector.waitForTextPresent(dateString);
        final String textToLookFor = courtRoom + "_" + DateTimeUtils.formatToStandardPattern(startDay) + "_" + blockType;
        SeleniumConnector.waitForElementToDisappear(textToLookFor, SeleniumConnector.ID);
    }

//    @Then("^I see Block type allocation message \"(.*?)\"$")
//    public void i_see_Block_type_allocation_message(final String blockMessage) throws Throwable {
//        // assertTrue(blockMessage.equals(SeleniumConnector.getTextFromElementBySelector("manageBlocksMessage", "id")));
//        SeleniumConnector.waitForTextPresent(blockMessage);
//    }
}