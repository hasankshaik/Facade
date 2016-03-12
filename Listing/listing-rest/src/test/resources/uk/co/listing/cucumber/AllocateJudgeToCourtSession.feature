@CCS-197
@groupone
Feature: Allocate Existing Judge to Court / Session
  As a Listing Officer
  I want to allocate a judge to a court Session
  so that the right people are in the right court
  
  Scenario: 1 Allocate a judge to a court session
    Given "JudgeA" exists in "listOfJudgesForAlloc"
    And Court Room "CourtA" exists in "courtRooms"
    When I click "schedule" 
    And I select a "CourtA" from "courtRooms"
    And I select a "JudgeA" from "listOfJudgesForAlloc"
    And I select current date as sessions sitting day "courtRoomStartDate"
    And I select current date as sessions sitting day "courtRoomEndDate"
    And I click "allocateJudge"
    Then I see message "Judge has been allocated" in "sessionAllocMessage"
    And I can see "JudgeA" in correct location in plannerGrid "CourtA"
    
    Scenario: 2  Allocate a judge to multiple court sessions
    Given "JudgeA" exists in "listOfJudgesForAlloc"
    And Court Room "CourtA" exists in "courtRooms"
    When I click "schedule" 
    And I select a "CourtA" from "courtRooms"
    And I select a "JudgeA" from "listOfJudgesForAlloc"
    And I select current date as sessions sitting day "courtRoomStartDate"
    And I select current date plus 7 as sessions sitting day "courtRoomEndDate"
    And I click "allocateJudge"
    Then I see message "Judge has been allocated" in "sessionAllocMessage"
    And I can see "JudgeA" in correct location in the whole period in plannerGrid "CourtA"

