@CCS-146
@grouptwo
Feature: Remove hearing from a slot
  As a listing officer
  I want to remove a hearing from a slot
  So that booking can be removed from the planner

  @CCS-584
  Scenario Outline: 1 I need to be able to cancel the provisional or confirmed hearing
    Given The CourtRoom is booked for judge for  Future
    And Judge "UniqueJudge" is allocated to Court room <CourtRoom> from "today" to "today + 10" days
    And I can see <Case> hearing <Hearing> of <BookingType> for <CourtRoom> in Hearing Information
    When I unlist <Hearing> from Hearing Information
    Then <Hearing> is not present in planner page and in not booked
    And I see confirmation message for <Hearing>
    # Scenario: 2 User sees confirmation and confims of @CCS-584 
    And I click "cancelButton"
    And I see <Hearing> in HearingInformation element
    When I unlist <Hearing> from Hearing Information
    Then <Hearing> is not present in planner page and in not booked
    And I see confirmation message for <Hearing>
	And I click "saveButton"
    And I can see <Hearing> in "selectedUnscheduledHearing" element

    Examples: 
      | Case   | Hearing         | BookingType   | CourtRoom    | 
      | "T800" | "UniqueHearing" | "Provisional" | "UniqueRoom" | 
      | "T800" | "UniqueHearing" | "Confirmed"   | "UniqueRoom" | 
