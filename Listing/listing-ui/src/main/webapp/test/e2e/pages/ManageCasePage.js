var ManageCasePage = function() {
	
	this.caseCrestNumberInput = element(by.id('crestCaseNumber'));
	this.findCaseButton = element(by.id('getCaseDetailsButton'));
	this.addHearingButton = element(by.buttonText('Add Hearing'));
	this.hearingNameInput = element(by.id('hearingName'));
	this.createHearingButton = element(by.id('createHearing'));

	/**
     * Load Manage Case page
     * 
     */
    this.loadPage = function(caseNo) {
		browser.get('#/manage-case');
    };
	
	/**
     * Search for a Case
     * 
     */
    this.searchForCase = function(caseNo) {
		this.caseCrestNumberInput.sendKeys(caseNo);
		this.findCaseButton.click();
    };
	
	/**
     * Add a Hearing
     * 
     */
    this.addHearing = function(hearingName) {
		this.addHearingButton.click();
		this.hearingNameInput.sendKeys(hearingName);
		this.createHearingButton.click();
    };
	
	/**
     * Get Hearing Text by Hearing name
     * 
     */
    this.getHearingName = function(hearingName) {
		var hearingAdded = element(by.id('hearingName_' + hearingName));
		return hearingAdded.getText();
    };
	
}

module.exports = new ManageCasePage();