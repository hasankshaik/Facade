@CCS-525 @CCS-1084 
@groupone
Feature: Edit non availability dates
  As a Listing Officer
  I want to edit non availability dates

  Background: 
    Given I am on the Edit Case page

  Scenario Outline: 1  updating Non-availability date without mandatory fields  or moving away
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I add non-availablity dates details <reason> <startDate> <endDate>
    And I see non-availablity dates details <reason> <startDate> <endDate>
    And I add non-availablity dates details <reason> <endDate> <startDate>
    And I will see message "Bad Request" in "addNonAvailableDateMessage"
    And I enter non-availablity dates details <empty> <newstartDate> <newendDate>
    And I click "editNonAvailabileDate0"
    Then I will see message "Bad Request" in "updateNonAvailableDateMessage"
    And I enter non-availablity dates details <newreason> <newstartDate> <newendDate>
    And I click "editNonAvailabileDate0"
    And I see non-availablity dates details <newreason> <newstartDate> <newendDate>

    Examples: 
      | crestCaseNumber | reason           | startDate   | endDate        | newreason    | newstartDate   | newendDate     | empty |
      | "T525"          | "Sample reason." | "next_week" | "next_week +2" | "New Reason" | "next_week +2" | "next_week +2" | ""    |
  
  Scenario Outline: 2  deleting Non-availability date not confirming  or moving away
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I add non-availablity dates details <reason> <startDate> <endDate>
    And I see non-availablity dates details <reason> <startDate> <endDate>
    And I click "deleteNonAvailabileDate0"
    Then I can see text "Do you want to delete this non available date"
    And I click button "saveButton" and expect it to be "invisible"
    Then I can see text "No Non Available Date"

    Examples: 
      | crestCaseNumber | reason           | startDate   | endDate        |
      | "D525"          | "Sample reason." | "next_week" | "next_week +2" |
