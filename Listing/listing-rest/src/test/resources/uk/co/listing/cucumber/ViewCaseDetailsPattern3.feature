@CCS-321
@CCS-433
@groupone
Feature: View Case - pattern 3
  As a Listing Officer
  I want to view case details
  so that I can  manage my Court Centre

  Scenario: 1 I need to know the time estimate for the trial or appeal
    Given I am in View case page and example cases Exist
    When I enter "T100" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    And I can see "most-serious" in "mostSeriousOffence"
    And I can see "1" in "offenceClass"

  
   Scenario: 2  View all the information of the defendants for the case
    Given I am in View case page and example cases Exist
    When I enter "T100" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see "Tom" with custody status "Bail" and expires on "" and days to expire "" and caseURN "12GT1234515" for table defendants
    And I can see "Nigel" with custody status "Custody" and expires on "4 days from current date" and days to expire "4" and caseURN "16TE5432114" for table defendants
    And I can see "Ted" with custody status "Custody" and expires on "21/05/2015" and days to expire "Expired" and caseURN "" for table defendants
    
    Scenario: 3 URN is not displayed at case level
	 Given I am in View case page and example cases Exist
    When I enter "T100" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
	Then I cant see the caseURN element
	
	
	Scenario: 4  View all the information of the defendants for the case
    Given I am in View case page and example cases Exist
    When I enter "T700" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see "Tom" with custody status "Not Applicable" and expires on "" and days to expire "" and caseURN "12GT1234515" for table defendants
    And I can see "Nigel" with custody status "In Care" and expires on "4 days from current date" and days to expire "4" and caseURN "16TE5432114" for table defendants
    And I can see "Ted" with custody status "In Care" and expires on "21/05/2015" and days to expire "Expired" and caseURN "" for table defendants
    

    