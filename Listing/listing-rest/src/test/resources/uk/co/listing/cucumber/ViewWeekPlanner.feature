@CCS-193
@groupone
Feature: View Week Planner
  As a Listing Officer
  I want to view a Weekly Plan
  so that I can place hearings, assign judges and manage my Court Centre

  Background: 
    Given I have added a court room "Room 1"

  Scenario: View week Plan
    Given I am on "Week" Plan
    Then I can see element "listHearingTable"
    And I can see element "courtRoom"
    And I can see element "courtCenter"
    And I can see "current" "week"
    And I can see "Mon", "Tue", "Wed", "Thu", "Fri" weekday
    And I can not see "Sat","Sun" weekday

  Scenario: View next Week Plan
    Given I am on "Week" Plan
    And I can see "current" "week"
    Then I can see element "nextButton"
    When I click "nextButton"
    Then I can see "next" "week"

  Scenario: View previous Week Plan
    Given I am on "Week" Plan
    And I can see "current" "week"
    Then I can see element "previousButton"
    When I click "previousButton"
    Then I can see "previous" "week"
