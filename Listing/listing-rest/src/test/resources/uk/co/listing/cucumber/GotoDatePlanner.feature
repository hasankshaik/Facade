@CCS-195
@grouptwo
Feature: Go To date in Planner
  As a Listing Officer
  I want to view a specific date on a Plan
  so that I can place hearings, assign judges and manage my Court Centre

  Scenario: Go to new date from Week Plan
    Given I am on "Week" Plan
    And I can see "current" "week"
    And I enter "current" date +7 "day"
    Then I can see "current" date +7 "day"

  Scenario: Go to new date from Month Plan
    Given I am on "4 Weeks" Plan
    And I can see "current" "week"
    When I Goto date 31/08/2015 
    Then I can see date 31/08/2015
    # +1 year as in scenario 
    When I Goto date 31/08/2016
    Then I can see date 31/08/2016
  
  Scenario Outline: Cannot go to invalid format date
    Given I am on "Week" Plan
    # And PlanEndDate is greater than "31/12/2020" (No PlanEndDate as yet)
    And I can see "current" "Date"
    When I Goto date 31/08/2015 
    Then I can see date 31/08/2015
    When I enter date <InvalidDate>
    Then I can see date <ExpectedDate>

    Examples: 
      | InvalidDate | ExpectedDate |
      | 31/02/2016  | 02/03/2016   |
      | 31/09/2016  | 01/10/2016   |

  # This needs to be done when the PlanStartDate and PlanEndDate comes into play
  @Ignore
  Scenario: Cannot go to out of range date
    Given I am on "Week" Plan
    And I can see "Current" "Date"
    And I enter "PlanStartDate" -1 day
    Then I can see "current" "date"
    And I can see element "invalidDateAlert"

