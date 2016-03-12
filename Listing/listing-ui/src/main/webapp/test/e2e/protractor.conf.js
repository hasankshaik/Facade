var HtmlReporter = require('protractor-html-screenshot-reporter');

var reporter = new HtmlReporter({
		baseDirectory: 'test-report',
		takeScreenShotsOnlyForFailedSpecs: true,
		docTitle: 'Protractor Test Report',
		docName: 'Protractor-test-report.html',
	});

exports.config = {
  seleniumAddress: 'http://localhost:4444/wd/hub',
  
  capabilities: {
    browserName: 'chrome',
  },
  
  framework: 'jasmine',
  
  specs: [ 'specs/*Steps.js' ],
  
  jasmineNodeOpts: {
    showColors: true,
	isVerbose: true,
	displayStacktrace: true,
    displaySpecDuration: true,
	defaultTimeoutInterval: 30000,
	includeStackTrace: true
  },
  
  baseUrl: 'http://localhost:8080/listing-ui/',
  
  getPageTimeout: 15000,
  
  onPrepare: function() {
    browser.driver.manage().window().maximize();
	jasmine.getEnv().addReporter(reporter);
  },
}
