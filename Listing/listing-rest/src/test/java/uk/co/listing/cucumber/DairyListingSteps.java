package uk.co.listing.cucumber;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class DairyListingSteps {

	public static Log log = LogFactory.getLog(DairyListingSteps.class);
	
	@Given("^the page is open \"([^\"]*)\"$")
	public void the_page_is_open(String page) throws Throwable {
		SeleniumConnector.openAndWait(page);
	}

	@Then("^a browser should contains \"([^\"]*)\"$")
	public void a_browser_should_contains(String text) throws Throwable {
		Assert.assertTrue(SeleniumConnector.isTextPresent(text));
	}
}
