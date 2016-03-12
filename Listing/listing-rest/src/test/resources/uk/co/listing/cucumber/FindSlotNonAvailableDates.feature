@CCS-764
@grouptwo
Feature: Find Slot - Non Available Dates
  As a Listing Officer
  I want to avoid seeing trial dates where participants are not available
  So I list trials that will be effective
  
  Background: 
  	Given I go to "schedule" page for court centre "Centre764"
    And Case "CCS-764-1" has Sending date "today" -160 days
    And Case "CCS-764-1" has PCM Hearing Date "SendingDate + 119 day"
    And Case has Trial "CCS-764-1" which is not listed
    And Court room "Room764-1" exists with "Trial" blocks from StartDate "today + 1" to EndDate "today + 3" and blocks are empty
    And Court room "Room764-1" contains no other "Trial" blocks
   
  Scenario: 1 No non-available dates
    Given Case "CCS-764-1" has 0 Non Available date
    And Trial "CCS-764-1" has Estimate 1 day
    When I select hearing "CCS-764-1" from unlistedHearings
    And I press Get Available Slots Button
    Then I see the following dates in Search Results
      | "today + 1" |
      | "today + 2" |
      | "today + 3" |
  
  Scenario Outline: 3 Two Single day non-available period
    Given Case <CaseNo> has 2 Non Available periods
    And Trial <CaseNo> has Estimate 1 day
    And I search for case <CaseNo> in court centre "Centre764"
    And Add Non Available dates from start date <NAPDate1> to end date <NAPDate1> with reason "Witness On Holiday"
    And Add Non Available dates from start date <NAPDate2> to end date <NAPDate2> with reason "Witness 2 On Holiday"
    When I go to "schedule" page for court centre "Centre764"
    And I select hearing <CaseNo> from unlistedHearings
    And I press Get Available Slots Button
    Then I see dates <ResultDates> in Search Results
    
    Examples:
      | NAPDate1    | NAPDate2    | ResultDates 						| CaseNo       |
      | "today + 1" | "today + 3" | "today + 2" 			  			| "CCS-764-9"  |
  	  | "today"     | "today + 4" | "today + 1","today + 2","today + 3" | "CCS-764-13" |
  	  | "today"     | "today + 1" | "today + 2","today + 3" 		    | "CCS-764-14" |
 	  
  Scenario Outline: 5 Two Multi day non-available periods, Trial Estimate > 1 day
    Given Case <CaseNo> has 2 Non Available periods
    And Trial <CaseNo> has Estimate 2 day
    And I search for case <CaseNo> in court centre "Centre764"
    And Add Non Available dates from start date <NAPDate1Start> to end date <NAPDate1End> with reason "Witness On Holiday"
    And Add Non Available dates from start date <NAPDate2Start> to end date <NAPDate2End> with reason "Witness 2 On Holiday"
    When I go to "schedule" page for court centre "Centre764"
    And I select hearing "CCS-764-5" from unlistedHearings
    And I press Get Available Slots Button
    Then I see dates <ResultDates> in Search Results
    
    Examples:
      | NAPDate1Start | NAPDate1End | NAPDate2Start | NAPDate2End | ResultDates 						| CaseNo       |
      | "today" 	  | "today + 1" | "today + 3" 	| "today + 4" | "today + 2" 						| "CCS-764-27" |
      | "today - 1"   | "today" 	| "today + 3" 	| "today + 4" | "today + 1","today + 2" 			| "CCS-764-28" |
      | "today - 1"   | "today" 	| "today + 1"  	| "today + 4" | "" 		   							| "CCS-764-30" |
      | "today - 1"   | "today + 1" | "today + 4" 	| "today + 5" | "today + 2","today + 3" 	    	| "CCS-764-31" |
  	  | "today - 1"   | "today" 	| "today + 4"	| "today + 5" | "today + 1","today + 2","today + 3" | "CCS-764-37" |
    