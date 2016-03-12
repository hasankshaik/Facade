package uk.co.listing.cucumber;

import org.junit.Assert;

import uk.co.listing.SeleniumConnector;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class CreateCourtCentreSteps {

    
    @Given("^I am logged in as a \"(.*?)\"$")
    public void i_am_logged_in_as_a(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //throw new PendingException();
    }

    @Given("^I am in Admin Page$")
    public void i_am_in_Diary() throws Throwable {
        SeleniumConnector.openAndWait("admin");
        SeleniumConnector.waitForTextPresent("List of Court Centres");
    }

    @Given("^\"(.*?)\" Court Centre Exists$")
    public void court_Centre_Exists(String centre) throws Throwable {
        Assert.assertTrue(SeleniumConnector.isSelectOptionPresent("listOfCentres", centre));
    }

    @Given("^I enter \"(.*?)\" in \"(.*?)\" field$")
    public void i_enter_in_field(String text, String id) throws Throwable {
        SeleniumConnector.setInputValue(id, text);
    }

    @Then("^Note Court Centre \"(.*?)\" exists$")
    public void note_Court_Centre_exists(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^I can see \"(.*?)\" in \"(.*?)\" List element$")
    public void i_can_see_in_List_element(String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^I can see \"(.*?)\" once in \"(.*?)\" List element$")
    public void i_can_see_once_in_List_element(String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^I can see \"(.*?)\" once in \"(.*?)\" element$")
    public void i_can_see_once_in_element(String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^I can not see \"(.*?)\" once in \"(.*?)\" List element$")
    public void i_can_not_see_once_in_List_element(String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

}
