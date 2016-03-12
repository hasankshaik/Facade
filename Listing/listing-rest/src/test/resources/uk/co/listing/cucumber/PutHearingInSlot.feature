@CCS-144
@grouptwo
Feature: Put hearing in a slot
  As a listing officer
  I want to put a hearing in a slot
  So that booking can be communicated to the interested parties	
  
  Scenario: 1 Put hearing in empty slot
    Given hearing "Murder Hearing" exists for Case "T500" with lead defendant as "Ed"
    And court room "UniqueRoom" is allocated to judge "UniqueJudge" for current date plus 10 days
	And I see court room "UniqueRoom", start date "current" date and slot is empty
    And I select hearing "Murder Hearing" in "selectedUnscheduledHearing"
	And Trial Estimate is "10" days
	When I enter current date in "hearingStartDate" for room "UniqueRoom"
	And I select "Provisional" from "hearingBookingType" element
	And I click "listHearing"
	Then I can see for court room "UniqueRoom", hearing "Murder Hearing", case "T500", defendant "Ed" in current date plus 10 days
	
  Scenario Outline: 2 View the hearing booking status next to a hearing already scheduled
    Given court room <Room> is allocated to judge <Judge> for current date plus 10 days
    And case <Case1> has hearing <Hearing1>
    And hearing <Hearing1> is booked for case <Case1> for a current date in room <Room>
    And case <Case2> has hearing <Hearing2>
    And hearing <Hearing2> is not booked for case <Case2> for current date in room <Room>
    When I select hearing <Hearing2> in "selectedUnscheduledHearing"
    And I enter current date in "hearingStartDate" for room <Room>
    And I select "Provisional" from "hearingBookingType" element
    And I click "listHearing"
    Then hearing <Hearing2> is booked for case <Case2> for current date in room <Room>
    
    Examples:
      | Case1  | Case2  | Hearing1    | Hearing2    | Room         | Judge         |
      | "T600" | "T700" | "Hearing A" | "Hearing B" | "UniqueRoom" | "UniqueJudge" |
      | "T800" | "T800" | "Hearing C" | "Hearing D" | "UniqueRoom" | "UniqueJudge" |
  
  @CCS-609
  Scenario: 3 Cannot list a 0 estimate hearing. Hearing is listed after editing Estimate to a non zero number.
    Given hearing "Trial-Estimate-0D" of estimate 0 days exists for Case "T600" with lead defendant as "Defendant T600"
    And court room "UniqueRoom" is allocated to judge "UniqueJudge" for current date plus 10 days
    And I see court room "UniqueRoom", start date "current" date and slot is empty
    And I select hearing "Trial-Estimate-0D" in "selectedUnscheduledHearing"
    And Trial Estimate is "0" days
    When I enter current date in "hearingStartDate" for room "UniqueRoom"
    And I select "Provisional" from "hearingBookingType" element
    And I click "listHearing"
    Then I see listing error "Hearing has no estimate"
    And I can not see for court room "UniqueRoom", hearing "Trial-Estimate-0D", case "T600", defendant "Defendant T600" in current date plus 0 days
    When I save the hearing estimate as 1 day
    And I click "listHearing"
    Then I can see for court room "UniqueRoom", hearing "Trial-Estimate-0D", case "T600", defendant "Defendant T600" in current date plus 0 days
    