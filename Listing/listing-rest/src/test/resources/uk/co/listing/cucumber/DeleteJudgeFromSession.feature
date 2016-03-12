@CCS-325
@groupone
Feature: Delete judge from session
  As a Listing Officer
  I want to delete a judge from a session
  So that we got the right judge sittings

  Background:
    Given I am in Diary
    And "JudgeDJS" exists in listOfJudgesForAlloc
    And court room "Court325" exists in "courtRooms"
  
  Scenario: 1 De-allocate a judge booked to a court session
    And I can see judge "JudgeDJS" in court room "Court325" in Diary for current date
    When I select court room "Court325" from "courtRoomsDeAlloc"
    And I select judge "JudgeDJS" from "listOfJudgesForDeAlloc"
    And I select court room start date as current date
    And I select court room end date as current date
    And I click "deallocateJudge"
    Then I see deallocation message "Judge no longer allocated to these sessions"
    And I can not see judge "JudgeDJS" for current day in plannerGrid for room "Court325"
    And I see Actual Sitting Days count is updated
    
  Scenario: 2 Error when de-allocating an un-booked judge from a court session
    And I can not see judge "JudgeDJS" for current day in plannerGrid for room "Court325"
    When I select court room "Court325" from "courtRoomsDeAlloc"
    And I select judge "JudgeDJS" from "listOfJudgesForDeAlloc"
    And I select court room start date as current date
    And I select court room end date as current date
    And I click "deallocateJudge"
    Then I see deallocation message "Judge no longer allocated to these sessions"
    And I can not see judge "JudgeDJS" for current day in plannerGrid for room "Court325"
    And I see Actual Sitting Days count is not updated
  
  Scenario: 3 De-allocate a judge booked to multiple court sessions
    And I can see judge "JudgeDJS" in court room "Court325" in Diary for current date plus 7 days
    When I select court room "Court325" from "courtRoomsDeAlloc"
    And I select judge "JudgeDJS" from "listOfJudgesForDeAlloc"
    And I select court room start date as current date
    And I select court room end date as current date plus 7 days
    And I click "deallocateJudge"
    Then I see deallocation message "Judge no longer allocated to these sessions"
    And I can not see judge "JudgeDJS" for current date plus 7 days in plannerGrid for room "Court325"
  
  Scenario: 4 No change when de-allocating an un-booked judge from multiple court sessions
    And I can not see judge "JudgeDJS" for current day in plannerGrid for room "Court325"
    When I select court room "Court325" from "courtRoomsDeAlloc"
    And I select judge "JudgeDJS" from "listOfJudgesForDeAlloc"
    And I select court room start date as current date
    And I select court room end date as current date plus 7 days
    And I click "deallocateJudge"
    Then I see deallocation message "Judge no longer allocated to these sessions"
    And I can not see judge "JudgeDJS" for current date plus 7 days in plannerGrid for room "Court325"
    And I see Actual Sitting Days count is not updated
  
  Scenario: 5 Partial update when de-allocating a part booked judge from multiple court sessions
    And I can not see judge "JudgeDJS" for current date plus 6 days in plannerGrid for room "Court325"
    And I can see judge "JudgeDJS" in court room "Court325" in Diary for current date plus 7th day only
    When I select court room "Court325" from "courtRoomsDeAlloc"
    And I select judge "JudgeDJS" from "listOfJudgesForDeAlloc"
    And I select court room start date as current date
    And I select court room end date as current date plus 7 days
    And I click "deallocateJudge"
    Then I see deallocation message "Judge no longer allocated to these sessions"
    And I can not see judge "JudgeDJS" for current date plus 7 days in plannerGrid for room "Court325"

    
  
         