package uk.co.listing.cucumber;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeleteJudgeFromSessionSteps {
    
    private static final String ID = "id";
    private int actualSittingDays;
    
    public void allocateJudge(String judgeName, String courtRoom, int startDayCount, int daysCount) {
        SeleniumConnector.isSelectOptionPresent("courtRooms", courtRoom);
        SeleniumConnector.selectFromDropDown("courtRooms", courtRoom);
        SeleniumConnector.isSelectOptionPresent("listOfJudgesForAlloc", judgeName);
        SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", judgeName);
        SeleniumConnector.enterDateInSelector("courtRoomStartDate", DateUtils.addDays(new Date(), startDayCount));
        SeleniumConnector.enterDateInSelector("courtRoomEndDate", DateUtils.addDays(new Date(), daysCount));
        SeleniumConnector.clickAndWait("allocateJudge");
        SeleniumConnector.waitForTextPresent("Judge has been allocated");
    }
    
    public int getActualSittingDaysCount() {
        SeleniumConnector.waitForTextPresent("Actual sitting days:");
        return Integer.valueOf(SeleniumConnector.getTextFromElementBySelector("actualSittingDays", ID));
    }
    
    @After("@CCS-325")
    public void afterScenario() {
        // Tear down for scenario. Delete the judge from court room so that next scenario can start from clean data.
        SeleniumConnector.isSelectOptionPresent("courtRoomsDeAlloc", "Court325");
        SeleniumConnector.selectFromDropDown("courtRoomsDeAlloc", "Court325");
        SeleniumConnector.isSelectOptionPresent("listOfJudgesForDeAlloc", "JudgeDJS");
        SeleniumConnector.selectFromDropDown("listOfJudgesForDeAlloc", "JudgeDJS");
        SeleniumConnector.enterDateInSelector("courtRoomStartDateDeAlloc", new Date());
        SeleniumConnector.enterDateInSelector("courtRoomEndDateDeAlloc", DateUtils.addDays(new Date(), 10));
        SeleniumConnector.clickAndWait("deallocateJudge");
    }
    
    @Given("^I am in Diary$")
    public void i_am_in_Diary() throws Throwable {
        SeleniumConnector.openAndWait("schedule");
        SeleniumConnector.waitForTextPresent("Room Name");
    }
    
    @Given("^\"(.*?)\" exists in listOfJudgesForAlloc$")
    public void exists_in_listOfJudgesForAlloc(String judgeName) throws Throwable {
        SeleniumConnector.waitForTextPresent(judgeName);
        Assert.assertTrue(!SeleniumConnector.isElementAbsent(".//*[@id='listOfJudgesForDeAlloc']/option[@value='" + judgeName + "']", "xpath"));
    }
    
    @Given("^court room \"(.*?)\" exists in \"(.*?)\"$")
    public void court_room_exists_in(String roomName, String roomId) throws Throwable {
        Assert.assertTrue(!SeleniumConnector.isElementAbsent(".//*[@id='courtRoomsDeAlloc']/option[@value='" + roomName + "']", "xpath"));
    }
    
    @When("^I select court room \"(.*?)\" from \"(.*?)\"$")
    public void i_select_court_room_from(String courtRoomName, String roomId) throws Throwable {
        actualSittingDays = getActualSittingDaysCount();
        SeleniumConnector.isSelectOptionPresent(roomId, courtRoomName);
        SeleniumConnector.selectFromDropDown(roomId, courtRoomName);
    }

    @When("^I select judge \"(.*?)\" from \"(.*?)\"$")
    public void i_select_judge_from(String judgeName, String judgeID) throws Throwable {
        SeleniumConnector.isSelectOptionPresent(judgeID, judgeName);
        SeleniumConnector.selectFromDropDown(judgeID, judgeName);
    }

    @Given("^I select court room start date as current date$")
    public void i_select_court_room_start_date_as_current_date() throws Throwable {
        SeleniumConnector.enterDateInSelector("courtRoomStartDateDeAlloc", new Date());
    }

    @Then("^I see Actual Sitting Days count is updated$")
    public void i_see_Actual_Sitting_Days_count_is_updated() throws Throwable {
        actualSittingDays = actualSittingDays - 1;
        actualSittingDays = actualSittingDays < 0 ? 0 : actualSittingDays;
        SeleniumConnector.waitForTextPresent("Actual sitting days: " + String.valueOf(actualSittingDays));
        Assert.assertTrue("Actual sitting days not as expected" , actualSittingDays == getActualSittingDaysCount());
    }
    
    @Then("^I see Actual Sitting Days count is not updated$")
    public void i_see_Actual_Sitting_Days_count_is_not_updated() throws Throwable {
        Assert.assertTrue("Actual sitting days not as expected" , actualSittingDays == getActualSittingDaysCount());
    }
    
    @Given("^I select court room end date as current date$")
    public void i_select_court_room_end_date_as_current_date() throws Throwable {
        SeleniumConnector.enterDateInSelector("courtRoomEndDateDeAlloc", new Date());
    }
    
    @Given("^I select court room end date as current date plus (\\d+) days$")
    public void i_select_court_room_end_date_as_current_date_plus_days(int daysCount) throws Throwable {
        SeleniumConnector.enterDateInSelector("courtRoomEndDateDeAlloc", DateUtils.addDays(new Date(), daysCount));
    }
    
    @Given("^I see deallocation message \"(.*?)\"$")
    public void i_see_message(String expectedSuccessMsg) throws Throwable {
        SeleniumConnector.waitForTextPresent(expectedSuccessMsg);
    }
    
    @Given("^I can see judge \"(.*?)\" in court room \"(.*?)\" in Diary for current date$")
    public void i_can_see_judge_in_court_room_in_Diary_for_current_date(String judgeName, String courtRoom) throws Throwable {
        Date startDay = new Date();
        allocateJudge(judgeName, courtRoom, 0, 0);
        if(!DateTimeUtils.isWeekend(startDay)) {
            Assert.assertTrue(judgeName.equals(SeleniumConnector.getTextFromElementBySelector(courtRoom + "_" + DateTimeUtils.formatToStandardPattern(startDay) + "_" + judgeName, "id")));
        }
    }
    
    @Given("^I can see judge \"(.*?)\" in court room \"(.*?)\" in Diary for current date plus (\\d+) days$")
    public void i_can_see_judge_in_court_room_in_Diary_for_current_date_plus_days(String judgeName, String courtRoom, int daysCount) throws Throwable {
        allocateJudge(judgeName, courtRoom, 0, daysCount);
    }
    
    @Given("^I can see judge \"(.*?)\" in court room \"(.*?)\" in Diary for current date plus (\\d+)th day only$")
    public void i_can_see_judge_in_court_room_in_Diary_for_current_date_plus_th_day_only(String judgeName, String courtRoom, int nthDay) throws Throwable {
        allocateJudge(judgeName, courtRoom, nthDay, nthDay);
    }
    
    @Then("^I can not see judge \"(.*?)\" for current day in plannerGrid for room \"(.*?)\"$")
    public void i_can_not_see_judge_for_current_day_in_plannerGrid_for_room(String judgeName, String courtRoom) throws Throwable {
        Date startDay = new Date();
        SeleniumConnector.waitForElementToDisappear(courtRoom + "_" + DateTimeUtils.formatToStandardPattern(startDay) + "_" + judgeName, ID);
    }

    @Then("^I can not see judge \"(.*?)\" for current date plus (\\d+) days in plannerGrid for room \"(.*?)\"$")
    public void i_can_not_see_judge_for_current_date_plus_days_in_plannerGrid_for_room(String judgeName, int daysCount, String courtRoom) throws Throwable {
        SeleniumConnector.waitForTextPresent(courtRoom);
                
        Date startDay = new Date();
        Date endDay = DateUtils.addDays(startDay, daysCount);
        while(!startDay.after(endDay)) {
            if(!DateTimeUtils.isWeekend(startDay)) {
                SeleniumConnector.waitForTextPresent(courtRoom);
                Assert.assertTrue(SeleniumConnector.isElementAbsent(courtRoom + "_" + DateTimeUtils.formatToStandardPattern(startDay) + "_" + judgeName, ID));
            }
            Calendar day = Calendar.getInstance();
            day.setTime(startDay);
            //We check if the day is Sunday to move to the next week
            if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                SeleniumConnector.clickAndWait("nextButton");
            }
            startDay = DateUtils.addDays(startDay, 1);
        }
    }
}