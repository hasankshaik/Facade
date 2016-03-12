package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ViewCaseDetailsSteps {
    public static Log log = LogFactory.getLog(ViewCaseDetailsSteps.class);

    @Given("^I am in View case page and example cases Exist$")
    public void the_Cases_Exist() throws Throwable {
        SeleniumConnector.openAndWait("case");
        SeleniumConnector.waitForTextPresent("Case Crest Number");
    }

    @When("^I enter (\\d+) in \"(.*?)\"$")
    public void i_enter_in( String arg1,  String arg2) throws Throwable {
        SeleniumConnector.setInputValue(arg2, arg1);
    }

    @Then("^I can see (\\d+) in \"(.*?)\"$")
    public void i_can_see_in( String value,  String elementId) throws Throwable {
        assertTrue(value.equals(SeleniumConnector.getValue(elementId)));
    }

    @Then("^I can see \"(.*?)\" in \"(.*?)\"$")
    public void i_can_see_in_and( String arg1,  String arg2) throws Throwable {
        assertTrue(arg1.equals(SeleniumConnector.getValue(arg2)));
    }

    @Then("^I can see Estimate is \"(.*?)\" in Hearings table for hearing named \"(.*?)\"$")
    public void i_can_see_Estimate_is_in_Hearings_table_for_hearing_named( String estimate,  String hearingName) throws Throwable {
        assertTrue(estimate.equals(SeleniumConnector.getValue("hearingTrialEstimateSelected_" + hearingName)));
    }

    @Then("^I can see \"(.*?)\" in element \"(.*?)\"$")
    public void i_can_see_in_element( String arg1,  String arg2) throws Throwable {
        SeleniumConnector.waitForTextPresent("New Hearing Name");
        assertTrue(arg1.equals(SeleniumConnector.getTextFromElementBySelector(arg2, "id")));
    }

    @Then("^I see \"(.*?)\" in emptyHearings$")
    public void i_can_see_in_emptyhearings( String arg1) throws Throwable {
        assertTrue(arg1.equals(SeleniumConnector.getTextFromElementBySelector("emptyHearings", "id")));
    }

    @Then("^I can see date \"(.*?)\" in \"(.*?)\"$")
    public void i_can_see_date_in_and( String arg1,  String arg2) throws Throwable {
        String date = "";
        if ("".equals(arg1)) {
            //Checking " " as in the entry, it comes as "" and if the input has that, has not value
            SeleniumConnector.waitForElementAttributeByLooping(arg2, "value", " ");
        } else {
            int noOfDays = 0;
             Matcher expected = Pattern.compile("\\d+").matcher(arg1);
            if (expected.find()) {
                noOfDays = Integer.parseInt(expected.group());
            }
             Date dateToEnter = DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), noOfDays + 1);
             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.format(dateToEnter);
            assertTrue(date.equals(SeleniumConnector.getValue(arg2)));
        }
    }

    @Then("^I can see \"(.*?)\" for table caseNotes$")
    public void i_can_see_in_table_casenotes( String arg1) throws Throwable {
         String text1 = SeleniumConnector.getTextFromTable("caseNotes", 1, 2);
         String text2 = SeleniumConnector.getTextFromTable("caseNotes", 2, 2);
         Boolean result = arg1.equals(text1) || arg1.equals(text2);
        assertTrue(result);
    }

    @Then("^I can see \"(.*?)\" with date \"(.*?)\" for table hearings$")
    public void i_can_see_in_table_hearing( String arg1,  String arg2) throws Throwable {
         String text1 = SeleniumConnector.getTextFromTable("hearings", 1, 3);
         String text2 = SeleniumConnector.getTextFromTable("hearings", 2, 3);
         String text3 = SeleniumConnector.getTextFromTable("hearings", 3, 3);
         String date1 = SeleniumConnector.getTextFromTable("hearings", 1, 2);
         String date2 = SeleniumConnector.getTextFromTable("hearings", 2, 2);
         String date3 = SeleniumConnector.getTextFromTable("hearings", 3, 2);

         Boolean result = (arg1.equals(text1) && arg2.equals(date1)) || (arg1.equals(text2) && arg2.equals(date2)) || (arg1.equals(text3) && arg2.equals(date3));
        assertTrue(result);
    }

    @Then("^I can see \"(.*?)\" for table linkedCases$")
    public void i_can_see_in_table_linkedcases( String arg1) throws Throwable {
         String text1 = SeleniumConnector.getTextFromTable("linkedCases", 1, 1);
         String text2 = SeleniumConnector.getTextFromTable("linkedCases", 2, 1);
         Boolean result = arg1.equals(text1) || arg1.equals(text2);
        assertTrue(result);
    }

    @Then("^I can see \"(.*?)\" with custody status \"(.*?)\" and expires on \"(.*?)\" and days to expire \"(.*?)\" and caseURN \"(.*?)\" for table defendants$")
    public void i_can_see_in_table_defendants( String defendant,  String status,  String ctlExpires,  String daysToExpire,  String caseURN) throws Throwable {
        String expires = "";
        if (ctlExpires.contains("days")) {
             char day = ctlExpires.charAt(0);
             Integer numberOfDays = Integer.valueOf(day + "");
             Date date = new Date();
             Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, numberOfDays);
            expires = DateTimeUtils.formatToStandardPattern(calendar.getTime());
        } else if (!ctlExpires.equals("")) {
            expires = ctlExpires;
        }
         String defendant1 = SeleniumConnector.getTextFromTable("peopleInCase", 1, 1);
         String defendant2 = SeleniumConnector.getTextFromTable("peopleInCase", 2, 1);
         String defendant3 = SeleniumConnector.getTextFromTable("peopleInCase", 3, 1);
         String caseURN1 = SeleniumConnector.getTextFromTable("peopleInCase", 1, 2);
         String caseURN2 = SeleniumConnector.getTextFromTable("peopleInCase", 2, 2);
         String caseURN3 = SeleniumConnector.getTextFromTable("peopleInCase", 3, 2);
         String status1 = SeleniumConnector.getTextFromTable("peopleInCase", 1, 3);
         String status2 = SeleniumConnector.getTextFromTable("peopleInCase", 2, 3);
         String status3 = SeleniumConnector.getTextFromTable("peopleInCase", 3, 3);
         String expire1 = SeleniumConnector.getTextFromTable("peopleInCase", 1, 4);
         String expire2 = SeleniumConnector.getTextFromTable("peopleInCase", 2, 4);
         String expire3 = SeleniumConnector.getTextFromTable("peopleInCase", 3, 4);
         String days1 = SeleniumConnector.getTextFromTable("peopleInCase", 1, 5);
         String days2 = SeleniumConnector.getTextFromTable("peopleInCase", 2, 5);
         String days3 = SeleniumConnector.getTextFromTable("peopleInCase", 3, 5);
         Boolean result = (defendant.equals(defendant1) && status.equals(status1) && expires.equals(expire1) && daysToExpire.equals(days1) && caseURN.equals(caseURN1))
                || (defendant.equals(defendant2) && status.equals(status2) && expires.equals(expire2) && daysToExpire.equals(days2) && caseURN.equals(caseURN2))
                || (defendant.equals(defendant3) && status.equals(status3) && expires.equals(expire3) && daysToExpire.equals(days3) && caseURN.equals(caseURN3));
        assertTrue(result);
    }

    @Then("^I cant see the caseURN element$")
    public void i_cant_see_caseURN()  throws Throwable {
        SeleniumConnector.isTextNotPresent("Case URN Number");
    }

    @When("^I click on the case \"(.*?)\"$")
    public void i_click_on_the_case( String arg1) throws Throwable {
         String text1 = SeleniumConnector.getTextFromTable("linkedCases", 1, 1);
         String text2 = SeleniumConnector.getTextFromTable("linkedCases", 2, 1);
        if (arg1.equals(text1)) {
            SeleniumConnector.clickOnTable("linkedCases", 1, 1);
        } else if (arg1.equals(text2)) {
            SeleniumConnector.clickOnTable("linkedCases", 2, 1);
        } else {
            assertTrue(false);
        }
        // Adding this to let the test load the information of the new case
        // properly
        SeleniumConnector.waitForTextPresent("No linked cases");
    }
}