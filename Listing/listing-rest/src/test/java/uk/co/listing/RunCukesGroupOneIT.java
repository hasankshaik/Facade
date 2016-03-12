package uk.co.listing;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * This will run all your cucumber features and steps tagged with @groupone
 * 
 * @author hshaik
 */

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "json:target/cucumber-report-one.json",
		"rerun:target/rerun-one.txt",
		"html:target/cucumber-htmlreport-groupone" }, strict = true, tags = {
		"@groupone", "~@Ignore" })
public class RunCukesGroupOneIT {
}
