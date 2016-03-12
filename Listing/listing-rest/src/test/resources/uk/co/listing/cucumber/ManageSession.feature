@CCS-379 
@grouptwo
Feature: Manage session of Rooms
  As a Listing Officer
  I want to close and reopen sessions
  So that they are not available  for listing

  Background: 
    Given I am on Planner page
  
  Scenario: 1 Close room for 1 day
    And I select a "Room379-1" from "manageSessionsOfRooms"
    And I select first day+1 of next week as "manageSessionsRoomStartDate"
    And I select first day+1 of next week as "manageSessionsRoomEndDate"
    When I Close session by clicking "closeSession"
    Then I see message "Session closed" in "manageSessionMessage"
    And I can see "Room379-1"  first day+1 of next week as Non-sitting day "true"
  
  Scenario: 2 Close room for multiple days
    And I select a "Room379-1" from "manageSessionsOfRooms"
    And I select first day+2 of next week as "manageSessionsRoomStartDate"
    And I select first day+3 of next week as "manageSessionsRoomEndDate"
    When I Close session by clicking "closeSession"
    Then I see message "Session closed" in "manageSessionMessage"
    And I can see "Room379-1"  first day+2 of next week as Non-sitting day "true"
    And I can see "Room379-1"  first day+3 of next week as Non-sitting day "true"
  
  Scenario: 3 Close multiple rooms
    And I multi-select a "Room379-2,Room379-3" from "manageSessionsOfRooms"
    And I select first day+1 of next week as "manageSessionsRoomStartDate"
    And I select first day+2 of next week as "manageSessionsRoomEndDate"
    When I Close session by clicking "closeSession"
    Then I see message "Session closed" in "manageSessionMessage"
    And I can see "Room379-2"  first day+1 of next week as Non-sitting day "true"
    And I can see "Room379-3"  first day+2 of next week as Non-sitting day "true"

  Scenario: 4 Re-open closed rooms
    And I select a "Room379-1" from "manageSessionsOfRooms"
    And I select first day+4 of next week as "manageSessionsRoomStartDate"
    And I select first day+4 of next week as "manageSessionsRoomEndDate"
    When I Close session by clicking "closeSession"
    Then I see message "Session closed" in "manageSessionMessage"
    And I can see "Room379-1"  first day+4 of next week as Non-sitting day "true"
    When I select a "Room379-1" from "manageSessionsOfRooms"
    And I select first day+4 of next week as "manageSessionsRoomStartDate"
    And I select first day+4 of next week as "manageSessionsRoomEndDate"
    And I Open session by clicking "openSession"
    Then I see message "Session open" in "manageSessionMessage"
    And I can see "Room379-1"  first day+4 of next week as Non-sitting day "false"

  Scenario Outline: 5 cannot book hearing in closed room
    And I select a "Room379-4" from "manageSessionsOfRooms"
    And I select first day+1 of next week as "manageSessionsRoomStartDate"
    And I select first day+1 of next week as "manageSessionsRoomEndDate"
    When I Close session by clicking "closeSession"
    Then I see message "Session closed" in "manageSessionMessage"
    And I can see "Room379-4"  first day+1 of next week as Non-sitting day "true"
    When I select <Hearing> from "selectedUnscheduledHearing" element
    And I select first day+1 of next week as "hearingStartDate"
    And I select <Booking Type> from "hearingBookingType" element
    When I select "Room379-4" from "roomForListing" element
    And I click on "listHearing" button
    Then I can see text "Bad Request: This hearing cannot be listed in this slot"

    Examples: 
      | Hearing        | Booking Type  |
      | "Hearing379-1" | "Provisional" |
