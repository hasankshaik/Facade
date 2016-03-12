@CCS-379 @grouptwo @rm
Feature: See day of hearing in planner
  As a Listing Officer
  I want to See day of hearing in planner

  Background: 
    Given I am on Planner page

  Scenario: 1 See day of hearing in planner
    And I select "Hearing780" from "selectedUnscheduledHearing" element
    And I select first day+2 of next week as "hearingStartDate"
    And I select "Confirmed" from "hearingBookingType" element
    And I select "Room780" from "roomForListing" element
    When I click on "listHearing" button
    # Scen 1,2,3
    And I can see day of trail 1 and total 8 of "Hearing780" on first day+2 of next week
    # Scen 4
    And I can see day of trail 4 and total 8 of "Hearing780" on first day+6 of next week
    # Scen 5
    And I can see day of trail 8 and total 8 of "Hearing780" on first day+10 of next week
    # Scen 6
    And I am on "Week" Plan
    And I can see day of trail 1 and total 8 of "Hearing780" on first day+2 of next week
    # Scen 7
    And I am on "Agenda" Plan
    And I can see day of trail 1 and total 8 of "Hearing780" on first day+2 of next week
