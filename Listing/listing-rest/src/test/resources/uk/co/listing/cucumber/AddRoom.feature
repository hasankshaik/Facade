@CCS-150
@groupone
Feature: Add Rooms
    As a Listing Officer
    I want to Add a Room to a Court Centre
    So that I can plan using the room
    
    Scenario: 2 Cannot Add Room Already Created
      Given I go to listing admin
      And court room "UniqueRoom" already exists
      And I can see element "courtRoomInput"
      When I enter "UniqueRoom" in "courtRoomInput"
      And I select "saveCourtRoomButton"
      Then I see "UniqueRoom" in "listOfRooms"