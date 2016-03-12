package uk.co.listing.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

// Class for storing all the Planned Sitting data.
final class PlannedSittingData {

    private String month;
    private int sittings;
    private String startDate;
    private String endDate;

    public PlannedSittingData(String month, int numberOfSittings, String startDate, String endDate) {
        this.month = month;
        this.sittings = numberOfSittings;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getMonth() {
        return month;
    }

    public int getSittings() {
        return sittings;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}

// Class for storing expected sitting data from Scenario 1
final class MonthlySittingData {
    private int period;
    private String name;
    private String target;
    private int planned;
    private String cumulativeVariance;
    
    public MonthlySittingData(int period, String name, String target, int planned, String cumulativeVariance) {
        this.period = period;
        this.name = name;
        this.target = target;
        this.planned = planned;
        this.cumulativeVariance = cumulativeVariance;
    }

    public int getPeriod() {
        return period;
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }

    public int getPlanned() {
        return planned;
    }

    public String getCumulativeVariance() {
        return cumulativeVariance;
    }
}

public class ManageMonthlySittingTargetSteps {
    
    public static boolean annualTargetsSet = false;
    public static boolean monthlyTargetsSet = false;
    public static boolean monthlyPlannedSet = false;
    public static Log log = LogFactory.getLog(ManageMonthlySittingTargetSteps.class);
    
    @Before("@CCS-247")
    public void scenarioSetup() {
        // Perform setup only once before all scenarios are run.
        if (!annualTargetsSet) {
            setAnnualSittingTarget();
        }
        if (!monthlyTargetsSet) {
            setMonthlySittingTargets();
        }
        if (!monthlyPlannedSet) {
            setMonthlySittingPlanned();
        }
    }

    public void setMonthlySittingTargets() {
        // Monthly Sitting Targets
        Map<String, Integer> targetMap = new HashMap<String, Integer>();
        targetMap.put("Apr", 30);
        targetMap.put("May", 1);
        targetMap.put("Jun", 0);
        targetMap.put("Jul", 5);
        targetMap.put("Aug", 0);
        targetMap.put("Sep", 25);
        targetMap.put("Oct", 305);
        targetMap.put("Nov", 0);
        targetMap.put("Dec", 0);
        targetMap.put("Jan", 0);
        targetMap.put("Feb", 0);
        targetMap.put("Mar", 48);

        // Select year for setting monthly sitting targets
        SeleniumConnector.openAndWait("sitting");
        SeleniumConnector.selectFromDropDown("financialYears", "2018-2019");

        Set<String> months = targetMap.keySet();

        for (String month : months) {
            SeleniumConnector.waitForElementClickableBySelector(month + "TargetInput", "id");
            SeleniumConnector.setInputValue(month + "TargetInput", String.valueOf(targetMap.get(month)));
            SeleniumConnector.waitForElementAttribute(month + "TargetInput", "value", String.valueOf(targetMap.get(month)));
        }
        SeleniumConnector.clickAndWait("setSittingButtonMonthly");
        monthlyTargetsSet = true;
    }

    public void setMonthlySittingPlanned() {
        String judgeCourtName = "MSJ";
        SeleniumConnector.openAndWait("admin");
        SeleniumConnector.clickLinkByPartialText("Admin");
        createJudgeAndRoom(judgeCourtName);

        SeleniumConnector.openAndWait("schedule");
        // Set the planned sitting days
        ArrayList<PlannedSittingData> plannedSitting = new ArrayList<PlannedSittingData>();
        plannedSitting.add(new PlannedSittingData("Apr", 2, "02/04/2018", "03/04/2018"));
        plannedSitting.add(new PlannedSittingData("May", 1, "03/05/2018", "03/05/2018"));
        plannedSitting.add(new PlannedSittingData("Jul", 10, "02/07/2018", "13/07/2018"));
        plannedSitting.add(new PlannedSittingData("Feb", 5, "01/02/2019", "07/02/2019"));

        for (PlannedSittingData item : plannedSitting) {
            String[] dateArr = item.getStartDate().split("/");
            String[] dateArr1 = item.getEndDate().split("/");

            SeleniumConnector.enterDateInSelector("dateInput", "02", "04", "2018");
            //SeleniumConnector.waitForElementAttribute("dateInput", "value", "2018-04-02");

            //int actualSittingBeforeSetting = getActualSittingDaysCount();

            SeleniumConnector.isSelectOptionPresent("listOfJudgesForAlloc", judgeCourtName);
            SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", judgeCourtName);
            SeleniumConnector.waitForOptionSelectedInSelectElement("listOfJudgesForAlloc", judgeCourtName);

            SeleniumConnector.isSelectOptionPresent("courtRooms", judgeCourtName);
            SeleniumConnector.selectFromDropDown("courtRooms", judgeCourtName);
            SeleniumConnector.waitForOptionSelectedInSelectElement("courtRooms", judgeCourtName);

            SeleniumConnector.enterDateInSelector("courtRoomStartDate", dateArr[0], dateArr[1], dateArr[2]);
            //SeleniumConnector.waitForElementAttribute("courtRoomStartDate", "value", dateArr[2] + "-" + dateArr[1] + "-" + dateArr[0]);
            SeleniumConnector.enterDateInSelector("courtRoomEndDate", dateArr1[0], dateArr1[1], dateArr1[2]);
            //SeleniumConnector.waitForElementAttribute("courtRoomEndDate", "value", dateArr1[2] + "-" + dateArr1[1] + "-" + dateArr1[0]);
            SeleniumConnector.clickAndWait("allocateJudge");
            // SeleniumConnector.waitForElementAttribute("actualSittingDays",
            // "value", String.valueOf(actualSittingBeforeSetting +
            // item.getSittings()));

        }
        monthlyPlannedSet = true;
    }

    private static void createJudgeAndRoom(String commonName) {
        SeleniumConnector.setInputValue("courtRoomInput", commonName);
        SeleniumConnector.clickAndWait("saveCourtRoomButton");
        SeleniumConnector.setInputValue("judgeName", commonName);
        SeleniumConnector.waitForElementPresent("listOfJudgeTypes");
		SeleniumConnector.isSelectOptionPresent("listOfJudgeTypes",
				"High Court");
		SeleniumConnector.selectFromDropDown("listOfJudgeTypes",
				"High Court");
        SeleniumConnector.clickAndWait("addJudge");
        SeleniumConnector.waitForTextPresent("Saved");
    }

    public void setAnnualSittingTarget() {
        // Annual sitting target
        String annualTarget = "400";
        SeleniumConnector.openAndWait("sitting");
        SeleniumConnector.selectFromDropDown("financialYears", "2018-2019");
        SeleniumConnector.waitForTextPresent("Cumulative Variance");
        SeleniumConnector.clearInput("sittingTargetInput");
        SeleniumConnector.waitForTextPresent("Cumulative Variance");
        SeleniumConnector.setInputValue("sittingTargetInput", annualTarget);
        SeleniumConnector.clickAndWait("setSittingButtonAnnual");
        SeleniumConnector.waitForTextPresent(annualTarget);
        annualTargetsSet = true;
    }

    public static boolean isSetupCompleted() {
        String expectedAprilTarget = "30";
        SeleniumConnector.openAndWait("sitting");
        SeleniumConnector.selectFromDropDown("financialYears", "2018-2019");
        SeleniumConnector.waitForElementClickableBySelector("AprTargetInput", "id");
        return expectedAprilTarget.equals(SeleniumConnector.getValue("AprTargetInput"));
    }

    public static int getActualSittingDaysCount() {
        SeleniumConnector.waitForTextPresent("Actual sitting days:");
        return Integer.valueOf(SeleniumConnector.getTextFromElementBySelector("actualSittingDays", "id"));
    }

    @And("^I set annual sitting target for \"(.*?)\" financial year to (\\d+) days$")
    public void i_set_annual_sitting_target_for_financial_year_to_days(String arg1, int arg2) throws Throwable {
        SeleniumConnector.selectFromDropDown("financialYears", arg1);
        SeleniumConnector.setInputValue("sittingTargetInput", String.valueOf(arg2));
        SeleniumConnector.clickAndWait("setSittingButton");
    }

    @Given("^I am on sitting target page$")
    public void i_am_on_sitting_page() throws Throwable {
        SeleniumConnector.openAndWait("sitting");
    }

    @And("^I select \"(.*?)\" financal year for sitting days$")
    public void i_select_financal_year_for_sitting_days(String arg1) throws Throwable {
        SeleniumConnector.selectFromDropDown("financialYears", arg1);
        SeleniumConnector.waitForTextPresent(arg1);
    }

    @Then("^I see following period and corresponding name, target, planned and cumulativevariance in monthlyTargetBlock$")
    public void i_see_in_monthlyTargetBlock(List<MonthlySittingData> monthSittingData) throws Throwable {
        String tableId = "monthlyTargetBlock";
        for (MonthlySittingData monthlySitting : monthSittingData) {
            int period = monthlySitting.getPeriod();
            int planned = monthlySitting.getPlanned();
            String per = period + ".";
            String name = monthlySitting.getName();
            String target = monthlySitting.getTarget();
            String variance = monthlySitting.getCumulativeVariance();
            log.info("Verifying Period: " + period + " Name: " + name + " Target: " + target + " Planned: " + planned + " Cumulative Variance: " + variance);
            SeleniumConnector.waitForElementClickableBySelector(tableId, "id");
            assertTrue(per.equals(SeleniumConnector.getTextFromTable(tableId, period, 1)));
            assertTrue(name.equals(SeleniumConnector.getTextFromTable(tableId, period, 2)));
            assertTrue(target.equals(SeleniumConnector.getValue(name + "TargetInput")));
            assertTrue(String.valueOf(planned).equals(SeleniumConnector.getTextFromTable(tableId, period, 4)));
            assertTrue(variance.equals(SeleniumConnector.getTextFromTable(tableId, period, 5)));
        }
    }

    @Then("^I see sum of monthly targets is (\\d+) in \"(.*?)\"$")
    public void i_see_sum_of_monthly_targets_is_in(int sumMonthlyTargets, String tableId) throws Throwable {
        assertTrue(String.valueOf(sumMonthlyTargets).equals(SeleniumConnector.getTextFromTable(tableId, 2, 1)));
    }

    @Then("^I see annual target is (\\d+) in \"(.*?)\"$")
    public void i_see_annual_target_is_in(int annualTarget, String tableId) throws Throwable {
        assertTrue(String.valueOf(annualTarget).equals(SeleniumConnector.getTextFromTable(tableId, 1, 1)));
    }

    @Then("^I see \"(.*?)\" in monthlyTarget for \"(.*?)\"$")
    public void i_see_in_monthlyTarget_for(String target, String month) throws Throwable {
        assertTrue(target.equals(SeleniumConnector.getValue(month + "TargetInput")));
    }

    @When("^I enter \"(.*?)\" for month \"(.*?)\" in monthly sitting$")
    public void i_enter_for_month_in_monthly_sitting(String target, String month) throws Throwable {
        SeleniumConnector.waitForElementClickableBySelector(month + "TargetInput", "id");
        SeleniumConnector.setInputValue(month + "TargetInput", target);
    }

    @When("^I click Set button for monthly sitting$")
    public void i_click_setSittingButton_for_monthly_sitting() throws Throwable {
        SeleniumConnector.clickAndWait("setSittingButtonMonthly");
    }

    @Then("^I see sum of monthly planned is (\\d+) in \"(.*?)\"$")
    public void i_see_sum_of_monthly_planned_is_in(int monthlyPlanned, String tableId) throws Throwable {
        assertTrue(String.valueOf(monthlyPlanned).equals(SeleniumConnector.getTextFromTable(tableId, 1, 2)));
    }

    @Then("^I see monthly sum - annual target is \"(.*?)\" in \"(.*?)\"$")
    public void i_see_monthly_sum_annual_target_is_in(String targetVariance, String tableId) throws Throwable {
        assertTrue(String.valueOf(targetVariance).equals(SeleniumConnector.getTextFromTable(tableId, 3, 1)));
    }

    @Then("^I see annual planned - annual target is \"(.*?)\" in \"(.*?)\"$")
    public void i_see_annual_planned_annual_target_is_in(String plannedVariance, String tableId) throws Throwable {
        assertTrue(String.valueOf(plannedVariance).equals(SeleniumConnector.getTextFromTable(tableId, 1, 3)));
    }

    @Then("^I see error message \"(.*?)\" next to \"(.*?)\" target$")
    public void i_see_error_message_next_to_target(String errMessage, String month) throws Throwable {
        String xpath = ".//*[@id='" + month + "TargetInput']/../p";
        assertEquals(errMessage, SeleniumConnector.getTextFromElementBySelector(xpath, "xpath"));
    }
}