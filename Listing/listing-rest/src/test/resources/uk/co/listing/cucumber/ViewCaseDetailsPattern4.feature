@CCS-390
@CCS-434
@groupone
Feature: View Case - pattern 4
  As a Listing Officer
  I want to view case details
  so that I can  manage my Court Centre

 Scenario: 1  I need to be able to navigate to the details of each linked case 
	 Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see "T200" for table linkedCases
    When I click on the case "T200"
    Then I can see "T200" in "crestCaseNumber"
    
 Scenario: 2 I need to know the number of defendents with more than two defendents
    Given I am in View case page and example cases Exist
    When I enter "T100" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    And I can see "3" in "numberOfDefendent"
    And I can see "Nigel and 2 others" in "leadDefendant"
    
    Scenario: 3 I need to know the number of defendent when it does not match with the count of defendent
    Given I am in View case page and example cases Exist
    When I enter "T300" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    And I can see "10" in "numberOfDefendent"
    And I can see "Ed and 9 others" in "leadDefendant"
    
      Scenario: 4 I need to know the number of defendents with two defendents
    Given I am in View case page and example cases Exist
    When I enter "T200" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    And I can see "2" in "numberOfDefendent"
    And I can see "David and 1 other" in "leadDefendant"
    
   Scenario: 5 I need to know the number of defendents with one defendent
    Given I am in View case page and example cases Exist
    When I enter "T400" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    And I can see "1" in "numberOfDefendent"
    And I can see "Ed" in "leadDefendant"
    
