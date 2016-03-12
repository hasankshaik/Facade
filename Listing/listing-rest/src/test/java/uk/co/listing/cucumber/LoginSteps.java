package uk.co.listing.cucumber;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginSteps {

    public static Log log = LogFactory.getLog(LoginSteps.class);

    @Given("I can see the log in page")
    public void i_can_see_login_page() throws Throwable {
        SeleniumConnector.waitForTextPresent("Sign in to OpenAM");
    }

    @When("^I enter \"(.*?)\" value in \"(.*?)\" input$")
    public void i_enter_value_in_input(String value, String selector) throws Throwable {
        SeleniumConnector.setInputValueBySelector(selector, value, "name");
    }

    @When("^I click on \"(.*?)\" button by name$")
    public void i_click_on_button_by_name(String value) throws Throwable {
        SeleniumConnector.clickAndWait(value, "name");
    }

    @Then("^I can see invalid message \"(.*?)\"$")
    public void i_can_see_invalid_message(String text) throws Throwable {
        SeleniumConnector.waitForTextPresent(text);
    }

    @Then("^I am not authenticated$")
    public void i_am_not_authenticated() throws Throwable {
        SeleniumConnector.waitForTextPresent("Sign in to OpenAM");
    }

    @Then("^I am authenticated$")
    public void i_am_authenticated() throws Throwable {
        SeleniumConnector.waitForTextPresent("Unscheduled Hearings");

    }

    @When("^I click the \"(.*?)\" logout button$")
    public void i_click_the_logout_button(String value) throws Throwable {
        SeleniumConnector.clickAndWait("dropdownId");
        SeleniumConnector.clickAndWait(value);
    }

    @Then("^I should be logout from appliction$")
    public void i_should_be_Logout() throws Throwable {
        SeleniumConnector.waitForTextPresent("You have been logged out");
        SeleniumConnector.clickAndWait("gotoLogin");
        SeleniumConnector.waitForTextPresent("Sign in to OpenAM");
    }
}
