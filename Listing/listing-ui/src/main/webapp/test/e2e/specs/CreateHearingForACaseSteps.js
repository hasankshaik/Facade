'use strict';
var manageCasePage = require('../pages/ManageCasePage');

describe('==> Create Hearing For A Case', function() {
	
	/**
     * Search for a case, then create a Hearing for it.
     * 
     */
	it('1: Create Hearing For A Case After Searching for a case', function() {
		var caseCrestNo = 'T500';
		var hearingName = "Hearing1";
		manageCasePage.loadPage();
		manageCasePage.searchForCase(caseCrestNo);
		manageCasePage.addHearing(hearingName);
		expect(manageCasePage.getHearingName(hearingName)).toEqual(hearingName);
	});	
	
});

