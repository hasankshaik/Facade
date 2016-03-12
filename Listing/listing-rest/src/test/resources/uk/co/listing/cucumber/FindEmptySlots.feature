@CCS-423
@groupone
Feature: Find Empty Slots
  As a Listing Officer
  I want to Find an empty slot for a hearing
  So that I can Schedule the hearing appropriately

  Background: 
    Given following hearings exists with corresponding trial estimates in days
      | Trial-Estimate-1D | 1 |
      | Trial-Estimate-2D | 2 |
      | Trial-Estimate-4D | 4 |
      | Trial-Estimate-5D | 5 |
    And slot "Slot-A-4Days-RoomA" in Room "RoomA-FindSlot" is of size 4 days
    And slot "Slot-B-4Days-RoomB" in Room "RoomB-FindSlot" is of size 4 days

  Scenario: 3 Attempt to find a slot, but no results are found.
    Given all blocks contain 1 hearing
    And I select hearing "Trial-Estimate-1D" from unlistedHearings
    Then Unlist button is disabled
    And I press Get Available Slots Button
    Then I should see message "Sorry, no slots found" in Available Slots element

  @CCS-577
  Scenario: 5 Slot too short
    Given slot "Slot-A-4Days-RoomA" is empty in diary
    And all blocks not in slot "Slot-A-4Days-RoomA" contain hearings
    And I select hearing "Trial-Estimate-5D" from unlistedHearings
    And I modify estimate value to 4 in "trialEstimateId" without saving the Estimate
    And I press Get Available Slots Button
    Then I should see message "Sorry, no slots found" in Available Slots element

  @CCS-379
  Scenario: 1 Find slot for a hearing with no over-booking
    Given slot "Slot-A-4Days-RoomA" is empty in diary
    And all blocks not in slot "Slot-A-4Days-RoomA" contain hearings
    And I select hearing "Trial-Estimate-4D" from unlistedHearings
    And I press Get Available Slots Button
    And I should see in Available Slots element, room "RoomA-FindSlot" and date is start date of "4D-Hearing"
    And I click "cancelButton" to close the Available Slots results
    And I select a "RoomA-FindSlot" from "manageSessionsOfRooms"
    And I select current date plus 2 as sessions sitting day "manageSessionsRoomStartDate"
    And I select current date plus 2 as sessions sitting day "manageSessionsRoomEndDate"
    And I click "closeSession"
    And I select hearing "Trial-Estimate-4D" from unlistedHearings
    When I press Get Available Slots Button
    Then I should see message "Sorry, no slots found" in Available Slots element
    # Re-opening the session we closed to make subsequent tests independant of this test.
    And I select a "RoomA-FindSlot" from "manageSessionsOfRooms"
    And I select current date plus 2 as sessions sitting day "manageSessionsRoomStartDate"
    And I select current date plus 2 as sessions sitting day "manageSessionsRoomEndDate"
    And I Open session by clicking "openSession"

  Scenario: 6 Short trial when long slot is available
    Given slot "Slot-A-4Days-RoomA" is empty in diary
    And all blocks not in slot "Slot-A-4Days-RoomA" contain hearings
    And I select hearing "Trial-Estimate-2D" from unlistedHearings
    And I press Get Available Slots Button
    Then I should see in Available Slots element, room "RoomA-FindSlot" and date is start date of "Slot-A-4Days-RoomA"
    And I should see in Available Slots element, room "RoomA-FindSlot" and date is start date of "Slot-A-4Days-RoomA" plus 1 day
    And I should see in Available Slots element, room "RoomA-FindSlot" and date is start date of "Slot-A-4Days-RoomA" plus 2 day

  @CCS-427
  Scenario: 2 Find slot for a hearing with over-booking allowed
    Given slot "Slot-A-4Days-RoomA" contains 1 hearing
    And all blocks not in slot "Slot-A-4Days-RoomA" contain 2 hearings
    And I select hearing "Trial-Estimate-4D" from unlistedHearings
    And I specify Overbooking is 1 hearing
    And I press Get Available Slots Button
    Then I should see in Available Slots element, room "RoomA-FindSlot" , start date of "Slot-A-4Days-RoomA" and hearing is "1"

  @CCS-427 @dual-hearing
  Scenario: 4 Over-booking option does not find empty slots i.e 0 booking slots
    Given slot "Slot-A-4Days-RoomA" is empty in diary
    And slot "Slot-B-4Days-RoomB" contains only 1 hearing
    And all blocks not in slot "Slot-A-4Days-RoomA" and "Slot-B-4Days-RoomB"contain 2 hearings
    And I select hearing "Trial-Estimate-4D" from unlistedHearings
    And I specify Overbooking is 1 hearing
    And I press Get Available Slots Button
    Then I should NOT see in Available Slots element, room "RoomA-FindSlot" , start date of "Slot-A-4Days-RoomA" and hearing is "0"
    And I should see in Available Slots element, room "RoomB-FindSlot" , start date of "Slot-B-4Days-RoomB" and hearing is "1"
