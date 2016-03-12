package uk.co.listing.cucumber;

import static org.junit.Assert.assertEquals;
import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.When;

public class EditNonAvailibilityDatesSteps {

    @When("^I add non-availablity dates details \"(.*?)\" \"(.*?)\" \"(.*?)\"$")
    public void i_add_non_availablity_dates_details(final String reason, final String startDate, final String endDate) throws Throwable {
        SeleniumConnector.clickAndWait("addNonAvailableDateButton");
        SeleniumConnector.setInputValue("notAvailabilityReason", reason);
        SeleniumConnector.enterDateInSelector("notAvailabilityStartDate", CommonSteps.getDate(startDate));
        SeleniumConnector.enterDateInSelector("notAvailabilityEndDate", CommonSteps.getDate(endDate));
        SeleniumConnector.clickAndWait("saveNonAvailableDateButton");
    }

    @When("^I enter non-availablity dates details \"(.*?)\" \"(.*?)\" \"(.*?)\"$")
    public void i_enter_non_availablity_dates_details(final String reason, final String startDate, final String endDate) throws Throwable {
        SeleniumConnector.setInputValue("nonAvailableReasonSelected0", reason);
        SeleniumConnector.enterDateInSelector("nonAvailableStartDateSelected0", CommonSteps.getDate(startDate));
        SeleniumConnector.enterDateInSelector("nonAvailableEndDateSelected0", CommonSteps.getDate(endDate));
    }

    @When("^I see non-availablity dates details \"(.*?)\" \"(.*?)\" \"(.*?)\"$")
    public void i_see_non_availablity_dates_details(final String reason, final String startDate, final String endDate) throws Throwable {
        assertEquals(SeleniumConnector.getValue("nonAvailableReasonSelected0"), reason);
        // assertEquals(SeleniumConnector.getValue("nonAvailableStartDateSelected0"), DateTimeUtils.CommonSteps.getDate(startDate));
        // assertEquals(SeleniumConnector.getValue("nonAvailableEndDateSelected0"), CommonSteps.getDate(endDate));

    }

}