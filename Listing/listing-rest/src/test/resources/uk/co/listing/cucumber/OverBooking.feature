@CCS-736
@grouptwo
Feature: Over Booking
  Defect Fix: Overbooking fails when adding slot becomes overbooked
  
  Scenario: 1 Overbooking fails when adding slot becomes overbooked
    Given Court Centre "Centre736" exists with a court room "Room736"
    And I am on Planner page for Court Centre "Centre736"
    And Judge "UniqueJudge" is allocated to Court room "Room736" from "today + 1" to "today + 10" days
    And Court Room "Room736" exists with Hearing "Room736-Estimate-5D" listed from "today + 3" for "5" days as type "Provisional"
    And I select hearing "Room736-Estimate-2D" from unlistedHearings
    And I specify Overbooking is 1 hearing
    And I press Get Available Slots Button
    Then I should see following dates in Available Slot Dates For Hearing
      | "today + 2" |
      | "today + 3" |
      | "today + 4" |
      | "today + 5" |
      | "today + 6" |
      | "today + 7" |