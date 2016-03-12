@CCS-320 
@groupone
Feature: View Case - linked information
  As a Listing Officer
  I want to view case details
  so that I can  manage my Court Centre

  Scenario: 1 I need to know the time estimate for the trial or appeal
    Given I am in View case page and example cases Exist
    When I enter "T100" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    And I can see "1.2" in "trialEstimate"
  
  Scenario: 2 I need to know which other cases are linked to the case - no cases
    Given I am in View case page and example cases Exist
    When I enter "T200" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see "No linked cases" in element "emptyLinkedCases"
    # Scenario: 3 I need to see notes recorded against the case - no notes
    Then I can see "No notes" in element "emptyNotes"
   
   Scenario: 4 I need to see notes recorded against the case
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see "Simple text: This is a test note, to check if they are showed on the case page" for table caseNotes
    # Scenario: 5  View all past and future hearings for the case
    Then I can see "Plea and Case Management" with date "20/10/2015" for table hearings
    And I can see "Trial" with date "20/03/2015" for table hearings
    # Scenario: 6   I need to know which other cases are linked to the case
    Then I can see "T200" for table linkedCases
    And I can see "T300" for table linkedCases
    
    
    

    