@CCS-247
@groupone
Feature: Manage Monthly Sitting Target
  As a Listing Officer
  I want to view weekly and monthly, sittings and targets
  So that I can meet the sitting target
  
  Background:
    Given I am on sitting target page
    And I select "2018-2019" financal year for sitting days
  
  Scenario: 1 View period, month name, monthly target, actual and cumulative variance for financial year
  	Then I see following period and corresponding name, target, planned and cumulativevariance in monthlyTargetBlock
  	  | period | name  | target | planned | cumulativeVariance |
  	  | 1      | Apr   |  30    | 2       | -28                |
  	  | 2      | May   |  1     | 1       | -28                |
  	  | 3      | Jun   |  0     | 0       | -28                |
  	  | 4      | Jul   |  5     | 10      | -23                |
  	  | 5      | Aug   |  0     | 0       | -23                |
  	  | 6      | Sep   |  25    | 0       | -48                |
  	  | 7      | Oct   |  305   | 0       | -353               |
  	  | 8      | Nov   |  0     | 0       | -353               |
  	  | 9      | Dec   |  0     | 0       | -353               |
  	  | 10     | Jan   |  0     | 0       | -353               | 
  	  | 11     | Feb   |  0     | 5       | -348               |  	    	    	    	    	    	    	  
  	  | 12     | Mar   |  48    | 0       | -396               |
  
  Scenario: 5 View sum of monthly target against annual target
    Then I see sum of monthly targets is 414 in "annualTargetBlock"
    And I see annual target is 400 in "annualTargetBlock"
  
  Scenario: 6 View sum of monthly planned against annual target
    Then I see sum of monthly planned is 18 in "annualTargetBlock"
    And I see annual target is 400 in "annualTargetBlock"
  
  Scenario: 7 View sum of monthly variance against annual target
    Then I see monthly sum - annual target is "14" in "annualTargetBlock"
    And I see annual planned - annual target is "-382" in "annualTargetBlock"
  
  Scenario Outline: 8 Edit target by month also updates the existing monthly and annual targets
    When I enter <target> for month <mon> in monthly sitting
    And I click Set button for monthly sitting
    Then I see <target> in monthlyTarget for <mon>
    
    Examples:
      | mon   | target  |
      | "Jan" | "22"    |
      | "Dec" | "22"    |
      | "Dec" | "99999" |
  
  Scenario: 9 View default target value for month and year when no values have been set
    When I select "2019-2020" financal year for sitting days
    Then I can see "sittingTargetInput" is "0" days
    And I see following period and corresponding name, target, planned and cumulativevariance in monthlyTargetBlock
  	  | period | name    | target | planned | cumulativeVariance  |
  	  | 1      | Apr     |   0    | 0       | 0                   | 
  
  Scenario Outline: 10 Invalid monthly target value is entered
    When I enter <target> for month <mon> in monthly sitting
    Then I see error message "number invalid" next to <mon> target
    
    Examples:
      | mon     | target       |
      | "Dec"   | "-"          |
      | "Apr"   | "100000"     |
      | "May"   | "-123123123" |

  