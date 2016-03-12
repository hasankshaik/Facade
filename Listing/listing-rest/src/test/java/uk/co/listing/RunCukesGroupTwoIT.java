package uk.co.listing;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * This will run all your cucumber features and steps tagged with @grouptwo
 * 
 * @author hshaik
 */

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "json:target/cucumber-report.json",
		"rerun:target/rerun-two.txt",
		"html:target/cucumber-htmlreport-grouptwo" }, strict = true, tags = {
		"@grouptwo", "~@Ignore" })
public class RunCukesGroupTwoIT {
}
