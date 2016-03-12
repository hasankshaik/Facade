package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.Keys;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateFormats;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NavigateToProposedSlotSteps {

    public Date startDate;
    public static String hearingName;
    public static String viewSelected;
    public static boolean judgeAllocated = false;
    public final String CASENAME = "T600";
    public final String JUDGENAME = "UniqueJudge";
    public final String COURTROOMNAME = "Court371";
    public final String COURTCENTRENAME = "Centre371";
    public final String HEARINGNAMEPREFIX = "Trial-Estimate-";
    public final String courtCentreQueryString = "?courtCenter=" + COURTCENTRENAME;

    // If not allocated, allocate judge to court room for 100 days
    public void allocateJudge() {
        SeleniumConnector.openAndWait("admin" + courtCentreQueryString);
        SeleniumConnector.openAndWait("admin" + courtCentreQueryString);
        SeleniumConnector.waitForTextPresent("Court Room Name");
        SeleniumConnector.setInputValue("courtRoomInput", COURTROOMNAME);
        SeleniumConnector.clickAndWait("saveCourtRoomButton");
        SeleniumConnector.openAndWait("schedule" + courtCentreQueryString);
        SeleniumConnector.openAndWait("schedule" + courtCentreQueryString);
        SeleniumConnector.waitForTextPresent("The Crown Court at " + COURTCENTRENAME, 10);
        SeleniumConnector.isSelectOptionPresent("courtRooms", COURTROOMNAME);
        SeleniumConnector.selectFromDropDown("courtRooms", COURTROOMNAME);
        SeleniumConnector.isSelectOptionPresent("listOfJudgesForAlloc", JUDGENAME);
        SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", JUDGENAME);
        SeleniumConnector.enterDateInSelector("courtRoomStartDate", new Date());
        SeleniumConnector.enterDateInSelector("courtRoomEndDate", DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), 100));
        SeleniumConnector.clickAndWait("allocateJudge");
        SeleniumConnector.waitForTextPresent("Judge has been allocated");
        SeleniumConnector.getTextFromElementBySelector(COURTROOMNAME + "_" + DateTimeUtils.formatToStandardPattern(new Date()) + "_" + JUDGENAME, "id");
        judgeAllocated = true;
    }

    @Given("^court room \"(.*?)\" exists$")
    public void court_room_exists(final String courtRoomName) throws Throwable {
        SeleniumConnector.waitForTextPresent(courtRoomName);
    }

    @Given("^I am on \"(.*?)\" view on Planner page$")
    public void i_am_on_view_on_Planner_page(final String viewName) throws Throwable {
        SeleniumConnector.openAndWait("schedule" + courtCentreQueryString);
        SeleniumConnector.openAndWait("schedule" + courtCentreQueryString);
        SeleniumConnector.waitForTextPresent("The Crown Court at " + COURTCENTRENAME, 10);
        if (!judgeAllocated) {
            allocateJudge();
        }
        SeleniumConnector.isSelectOptionPresent("viewSelector", viewName);
        SeleniumConnector.selectFromDropDown("viewSelector", viewName);
        SeleniumConnector.waitForTextPresent(COURTROOMNAME);
    }

    @Given("^I view Find Slot results for a hearing of estimate (\\d+)$")
    public void i_view_Find_Slot_results_for_a_hearing_of_estimate(final int estimateDays) throws Throwable {
        hearingName = HEARINGNAMEPREFIX + estimateDays + "D";
        SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing", hearingName);
        SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing", hearingName);
        SeleniumConnector.clickAndWait("getHearingSlotsButton");
        SeleniumConnector.waitForTextPresent("Available Slot Dates For Hearing");
    }

    @Given("^I select Radio button for court room \"(.*?)\" where start date is (\\d+) days from today$")
    public void i_select_court_room_and_start_date_is_days_from_today(final String courtName, final int daysFromToday) throws Throwable {
        final String slotToSelectRadioId = selectDate(daysFromToday);
        SeleniumConnector.clickAndWait(slotToSelectRadioId);
        if (SeleniumConnector.isEnabled("navigateButton")) {
            return;
        }
        SeleniumConnector.clickAndWait(slotToSelectRadioId);
    }

    private String selectDate(final int daysFromToday) {
        startDate = DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), daysFromToday + 1);
        final String slotToSelectRadioId = COURTROOMNAME + "-" + DateTimeUtils.formatToStandardPattern(startDate) + "-radio";
        SeleniumConnector.waitForElementClickableBySelector(slotToSelectRadioId, "id");
        return slotToSelectRadioId;
    }

    @Then("^I should see Planner is in \"(.*?)\"$")
    public void i_should_see_Planner_is_in(final String plannerView) throws Throwable {
        viewSelected = plannerView;
        assertTrue(plannerView.equals(SeleniumConnector.getSelectedOption("viewSelector")));
    }

    @Then("^Planner date selected is (\\d+) days from today$")
    public void planner_date_selected_is_days_from_today(final int noOfDays) throws Throwable {
        final Date date=DateUtils.parseDate(SeleniumConnector.getValue("dateInput"), DateFormats.getPatterns());
        assertTrue(DateUtils.isSameDay(startDate, date));
    }

    @When("^I select booking type as \"(.*?)\"$")
    public void i_select_booking_type_as(final String arg1) throws Throwable {
        SeleniumConnector.isSelectOptionPresent("bookingType", "Provisional");
        SeleniumConnector.selectFromDropDown("bookingType", "Provisional");
    }

    @When("^I click List button$")
    public void i_click_List_button() throws Throwable {
        SeleniumConnector.clickAndWait("slotListButton");
    }

    @Then("^I can see the hearing listed from (\\d+) days from today for (\\d+) days$")
    public void i_can_see_the_hearing_listed_from_days_from_today_for_days(final int hearingStartDate, final int hearingNoOfDays) throws Throwable {
        String dateString;
        String elementId;
        Date startDay = DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), hearingStartDate + 1);
        final Date endDay = DateTimeUtils.getEndWorkingDateFromStartDate(startDay, hearingNoOfDays);
        while (!startDay.after(endDay)) {
            if (!DateTimeUtils.isWeekend(startDay)) {
                dateString = DateFormatUtils.format(startDay, DateFormats.STANDARD.getValue());
                elementId = ".//*[@id = '" + COURTROOMNAME + "_" + dateString + "_" + CASENAME + "_" + hearingName + "_link']";
                SeleniumConnector.waitForElementClickableBySelector(elementId, "xpath");
            }
            final Calendar day = Calendar.getInstance();
            day.setTime(startDay);
            // We check if the day is Sunday to move to the next week
            if (viewSelected.equals("Week") && (day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                SeleniumConnector.pressKey("nextButton", Keys.ENTER);
            }
            startDay = DateUtils.addDays(startDay, 1);
        }
    }
}