@CCS-136
@grouptwo
Feature: Set sitting days
  As a Listing Officer
  I want to Set a target number of sitting days for Court Centre
  so that I can see whether I have fully allocated my sessions

  Background: 
#    Given yearStartdate for financial year "2015-2016" is "01/04/2015"
#    And yearEnddate for financial year "2015-2016" is "31/03/2016"
#    Given yearStartdate for financial year "2016-2017" is "01/04/2016"
#    And yearEnddate for financial year "2016-2017" is "31/03/2017"
#    And PlannerStartDate is "01/04/2015"
    And I am on the sittings page
    And I select year "2015-2016" in financialYears

  Scenario: 1 Set yearly Sitting Day target
    Given I am in "Birmingham" court centre
    When I enter 2000 in "sittingTargetInput" for 2015-2016
    And I click "setSittingButtonAnnual"
    And I reload page
    And I select year "2015-2016" in financialYears
    Then I can see "sittingTargetInput" is "2000" for "2015-2016"

  Scenario: 2 Amend the totals for the year
    Given I am in "Birmingham" court centre
    And "sittingTargetInput" is "2000" for "2015-2016"
    When I enter 1000 in "sittingTargetInput" for 2015-2016
    And I click "setSittingButtonAnnual"
    And I reload page
    And I select year "2015-2016" in financialYears
    Then I can see "sittingTargetInput" is "1000" for "2015-2016"
