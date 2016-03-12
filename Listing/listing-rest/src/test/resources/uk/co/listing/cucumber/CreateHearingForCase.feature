@CCS-291
@groupone
Feature: Create Hearing for case
  As a Listing Officer
  I want to create a hearing for a case

  Scenario: 1 Create hearing in case page
    Given I am on Case Page
	And Case "T500" exists on the system
	And I enter "T500" in "caseCrestNumber"
	And I click "getCaseDetailsButton"
	When I enter "Hearing 1" in "hearingName"
	And I click "createHearing"
	Then I can see "Hearing 1" in Case Page

  @CCS-496
  Scenario: 2 Create new hearing for a case using a hearing name that exists for another case.
	Given I see case "T1000" with Trial Estimate 5 and hearing "Hearing496"
	And I edit Hearing Estimate of hearing "Hearing496" to 8
	And I see case "T1001" with Trial Estimate 10
	When I add hearing "Hearing496" to case "T1001"
	Then I see case "T1001" with Trial Estimate 10 and hearing "Hearing496" 
	And I see case "T1000" with Trial Estimate 5 and hearing "Hearing496"
	And Hearing Estimate for hearing "Hearing496" is 8
