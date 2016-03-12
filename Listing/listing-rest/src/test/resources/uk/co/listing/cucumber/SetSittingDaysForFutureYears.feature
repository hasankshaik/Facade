@CCS-312
@groupone
Feature: Set sitting days for future year
  As a Listing Officer
  I want to set a sitting target in days for a future year
  So that I can plan ahead
  
  Background:
    #Given I am logged in as "Listing Officer"
    And I am on Sittings page
    
    
  Scenario: 1 Select future year 
    Given I select year "2015-2016" in financialYears
    And financialYears is "2015-2016" 
    Then I can see in "financialYears"
      | 2015-2016 |
      | 2016-2017 |
      | 2017-2018 |
      | 2018-2019 |
      | 2019-2020 |     
    But I can not see in "financialYears"
      | 2014-2015 |
      | 2021-2022 |       

  Scenario: 2 Enter Target for future year and confirm
    When I select year "2017-2018" in financialYears
    And I enter 1200 in sittingTargetInput
    And I click "setSittingButtonAnnual"
    And I can see text "Saved" 
    And I select year "2015-2016" in financialYears
    And I select year "2017-2018" in financialYears
    Then I can see "sittingTargetInput" is "1200" days
    When I select year "2017" in planner
    Then I can see "1200" in targetSittingDays
    
  Scenario: 3 Edit existing target for future year and confirm
    Given I am on Sittings page
    When I select year "2016-2017" in financialYears
    And I enter 2000 in sittingTargetInput
    And I click "setSittingButtonAnnual"
    And I reload page
    And I select year "2016-2017" in financialYears
    And I enter 3000 in sittingTargetInput
    And I click "setSittingButtonAnnual"
    And I reload page
    And I select year "2016-2017" in financialYears
    Then I can see "sittingTargetInput" is "3000" days