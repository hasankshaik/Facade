@CCS-194
@groupone
Feature: View Monthly Planner
  As a Listing Officer
  I want to view a monthly Plan
  so that I can place hearings, assign judges and manage my Court Centre

  Scenario: 1 View Month Plan from Week Plan
    Given I can see CurrentWeekPlan "listHearingTable" "viewSelector"
    And I can see CurrentDate "dateInput"
    And I can not see CurrentDate plus Seven days "dateInput"
    When I select MonthView "viewSelector"
    Then I can see CurrentDate in the view "dateInput"
    And I can see CurrentDate in the view plus three week in the view "dateInput"
    And I can see "Mon", "Tue", "Wed", "Thu", "Fri" weekday
    And I can not see "Sat","Sun" weekday

  Scenario: 2 View Week Plan from Month Plan
    Given I can see CurrentMonthPlan "listHearingTable" "viewSelector"
    When I select WeekView "viewSelector"
    Then I can not see CurrentDate plus three week in the view "dateInput"
    And I can see "Mon", "Tue", "Wed", "Thu", "Fri" weekday
    And I can not see "Sat","Sun" weekday

  Scenario Outline: 3 View next a number of periods forward
    Given I can see CurrentMonthPlan "listHearingTable" "viewSelector"
    When I click "nextButton" <presses> times
    Then I can see CurrentDate plus <weeks>

    Examples: 
      | presses | weeks |
      | 1       | 4     |
      | 2       | 8     |
  
  Scenario Outline: 4  View previous number of periods backwards
    Given I can see CurrentMonthPlan "listHearingTable" "viewSelector"
    When I click "previousButton" <presses> times
    Then I can see CurrentDate minus <weeks>

    Examples: 
      | presses | weeks |
      | 1       | 4     |
      | 2       | 8     |
