@CCS-137
@groupone
Feature: View actual sitting days booked
  As a Listing Officer
  I want to view the actual days booked in a court centre per year
  so that I can ensure that I have not overbooked sessions

  @CCS-379
  Scenario: 1 Adding sittings for a judge automatically updates the available sitting days
    Given I am in Planner in "The Crown Court at Birmingham"
    And I can see courtCentreSittingsActual
    When I can allocate Judge A to "Court A" in "11/11/2015"
    And I click "allocateJudge"
    Then I can see courtCentreSittingsActual is increased
    And I can see courtCentreSittingsTarget
    And I select a "Court A" from "manageSessionsOfRooms"
    And I enter date "11/11/2015" in "manageSessionsRoomStartDate"
    And I enter date "11/11/2015" in "manageSessionsRoomEndDate"
    When I click "closeSession"
    Then I see message "Session closed" in "manageSessionMessage"
    And I can see courtCentreSittingsActual
    And I can see courtCentreSittingsActual is not increased

  Scenario: 2 Target updates on change of year
    Given I am in Planner in "The Crown Court at Birmingham"
    And I go to next financial year
    And I can see courtCentreSittingsTarget
    And I can see courtCentreSittingsActual
    When I can allocate Judge A to "Court A" in "11/11/2015"
    And I click "allocateJudge"
    Then I see message "Judge has been allocated" in "sessionAllocMessage"
    When I go to next financial year
    Then I can see courtCentreSittingsTarget
    And I can see courtCentreSittingsActual is not increased
