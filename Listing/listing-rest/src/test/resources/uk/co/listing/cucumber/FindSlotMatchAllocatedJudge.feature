@CCS-761
@grouptwo
Feature: Find Slot - Match allocated judge
  As a listing officer
  When I search for a trial slot and a judge is allocated to the case
  I want the slots found to match the allocated judge

  Background: 
    Given I go to "schedule" page for court centre "Centre761"
    And following Cases exists with corresponding judge allocated
      | Crest Case Number | Trial    | Hearing Estimate | Hearing Type | Allocated Judge |
      | "CCS-761-A"       | "TrialA" | "2"              | "Trial"      | "Judge A"       |
      | "CCS-761-B"       | "TrialB" | "2"              | "Trial"      | "Judge B"       |
      | "CCS-761-C"       | "TrialC" | "2"              | "Trial"      | "Judge C"       |
      | "CCS-761-D"       | "TrialD" | "2"              | "Trial"      | ""              |
      | "CCS-761-F"       | "TrialF" | "2"              | "Trial"      | "Judge F"       |
    And The next judges are allocated in the next rooms
      | Judge 		| CourtRoom 	|
      | "Judge A"	| "CourtRoomA"	|
      | "Judge B"	| "CourtRoomF"	|
      | "Judge B"	| "CourtRoomB"	|
      | "Judge C"	| "CourtRoomC"	|
      | ""			| "CourtRoomD"	|
      
  Scenario: 1 Search for slot with allocated judge, finds only available slot
    When I select hearing "TrialA" from unlistedHearings
    And I press Get Available Slots Button
    Then I see the following rooms in Search Results
      | CourtRoomA |
      
      Scenario: 2 Search for slot with allocated judge, finds multiple slots
    When I select hearing "TrialB" from unlistedHearings
    And I press Get Available Slots Button
    Then I see the following rooms in Search Results
      | CourtRoomB |
      | CourtRoomF |
      
      Scenario: 3 Search for slot with no judge allocated, finds any slot with a judge
    When I select hearing "TrialD" from unlistedHearings
    And I press Get Available Slots Button
    Then I see the following rooms in Search Results
      | CourtRoomA |
      | CourtRoomF |
      | CourtRoomB |
      | CourtRoomC |
      
      Scenario: 4 Allocated judge and no slot available
    When I select hearing "TrialF" from unlistedHearings
    And I press Get Available Slots Button
    Then I should see message "Sorry, no slots found" in Available Slots element

 
