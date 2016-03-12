@CCS-383
@grouptwo
Feature: Edit Hearing from Case Page
  As a Listing Officer
  I want to edit a hearing from the Case Page
  So that I can keep it up date

  Scenario: 1 Modify trial estimation for a not booked hearing
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see a "Unscheduled" hearing in the "Trial" hearing
    And I can see "10" for the trial estimation in the "Trial" hearing
    When I modify "Hearing Estimation" to "4" in the "Trial" hearing
    And I click set in the "Trial" hearing
    And I go to View Case page
    And View case page is loaded successfully
    And I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see "4" for the trial estimation in the "Trial" hearing
    
  Scenario: 2  Not allowed to modify trial estimation for a Booked hearing
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see a "Scheduled" hearing in the "Booked Trial" hearing
    And I can see "10" for the trial estimation in the "Booked Trial" hearing
    And I can not modify "hearingTrialEstimateSelected" in the "Booked Trial" hearing
    
  Scenario: 3 Not allowed to modify booking type for a Not Booked hearing
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see a "Unscheduled" hearing in the "Trial" hearing
    And I can see "10" for the trial estimation in the "Trial" hearing
    And I can not modify "hearingBookingTypeSelected" in the "Trial" hearing
    
   Scenario: 4  Modify booking type in a Booked hearing
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see a "Scheduled" hearing in the "Booked Trial" hearing
    And I can see "Confirmed" for the Booking Type in the "Booked Trial" hearing
  	 When I modify "Booking Type" to "Provisional" in the "Booked Trial" hearing
    And I click set in the "Booked Trial" hearing
    And I go to View Case page
    And View case page is loaded successfully 
    And I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see a "Scheduled" hearing in the "Booked Trial" hearing
    And I can see "Provisional" for the Booking Type in the "Booked Trial" hearing
    
  Scenario: 5 Allowed to remove trial estimation
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see a "Unscheduled" hearing in the "Trial" hearing
    And I can see "10" for the trial estimation in the "Trial" hearing
  	When I modify "Hearing Estimation" to "" in the "Trial" hearing
    And I click set in the "Trial" hearing
    Then I can see "Hearing has been updated" message
 
  @CCS-695
  Scenario: 6 Can add a text annotation to a hearing from Case page
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see a "Scheduled" hearing in the "Booked Trial" hearing
    And I can add "Annotation 1" to Annotation in the "Booked Trial" hearing
    And I click set in the "Booked Trial" hearing
    And I go to View Case page
    And View case page is loaded successfully
    And I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see "Annotation 1" for the hearing annotation in the "Booked Trial" hearing
   
  Scenario: 7 Can edit a text annotation to a hearing from Case page
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see a "Scheduled" hearing in the "Booked Trial" hearing
    And I can see "Annotation 1" for the hearing annotation in the "Booked Trial" hearing
    When I modify "Annotation" to "Annotation 2" in the "Booked Trial" hearing
    And I click set in the "Booked Trial" hearing
    And I go to View Case page
    And View case page is loaded successfully
    And I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see "Annotation 2" for the hearing annotation in the "Booked Trial" hearing
    
   Scenario: 8 Cannot exceed character limit for annotations
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see a "Scheduled" hearing in the "Booked Trial" hearing
    And I can see "Annotation 2" for the hearing annotation in the "Booked Trial" hearing
    When I modify "Annotation" to "Annotation 12345678921234567892123456789212345678921234567892" in the "Booked Trial" hearing
    And I click set in the "Booked Trial" hearing
    Then I can see "Annotation exceeds maximum field length" message 
    
  
    
    

    