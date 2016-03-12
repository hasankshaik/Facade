@CCS-681
@groupone
Feature: Auto Schedule PCM Hearings
  As a Listing Officer
  I want to have PCMHs with Listings dates to be placed in the least booked PCMH block for the listing date in the same court centre
  So I don't have to manually list them to a room
  
  Background:
    Given I am on Planner page
    And Court rooms "Room681-1" , "Room681-2" , "Room681-3" exists
    And Crest Upload file "404--CREST-DATA-VALID-AUTO-PCM.zip" exists with following data
      | Case      | PCMH_Date_07 |
      | A12345678 | "03/03/2020" |
      | A87654321 | "04/03/2020" |
      | T87654321 | "04/03/2020" |
      | N87654321 | "04/03/2020" |
      | T12345678 | "05/03/2020" |
      | A23456781 | "02/03/2020" |
      | T23456781 | "01/03/2020" |
      | N23456781 | "29/02/2020" |
    And an empty block with type "PCM" is in Court "Room681-1" on "03/03/2020"
    And an empty block with type "PCM" is in Court "Room681-1" on "04/03/2020"
    And an empty block with type "PCM" is in Court "Room681-2" on "04/03/2020"
    And an empty block with type "PCM" is in Court "Room681-3" on "04/03/2020"
    And Hearing exists with type "PCM" in Court "Room681-1" and "Room681-2" on "05/03/2020"
    And an empty block with type "PCM" is in Court "Room681-3" on "05/03/2020"
    And no block with type "PCM" exist in any Court room on "02/03/2020"
    And dates "01/03/2020" and "29/02/2020" are weekends
    When I go to listing admin
    And I enter Upload path for valid file "404--CREST-DATA-VALID-AUTO-PCM.zip" and click Upload button
    And I wait for the Upload button to get Enabled
    And I see Upload success message "File uploaded successfully."
    And I goto date "03/03/2020" on Planner Page
  
  Scenario: 1,2,3,4,5 Put unlisted hearing in one room for a day. List unlisted PCMH for any date to the correct Court Centre. No auto listing if no PCM blocks available.
    # Scen 4: Lists PCMH to the correct Court Centre
    Then Court Centre is "Crown Court at Birmingham"
    # Scen 1: Put unlisted hearing in one room for a day
    And I should see a PCM Hearing "A12345678" in Room "Room681-1" on "03/03/2020"
    # Scen 2: Can list an unlisted PCMH for any date
    And I should see a PCM Hearing "A87654321" in Room "Room681-1" on "04/03/2020"
    And I should see a PCM Hearing "T87654321" in Room "Room681-2" on "04/03/2020"
    And I should see a PCM Hearing "N87654321" in Room "Room681-3" on "04/03/2020"
    # Scen 3 Lists to the least listed PCMH block when more than one PCMH block on a day
    And I should see a PCM Hearing "T12345678" in Room "Room681-1" on "05/03/2020"
    # Scen 5: PCMH Date is a day when no PCM blocks exists 
    When I view "A23456781" in View Case page
    Then Booking Status of PCM Hearing for case "A23456781" is "Unscheduled"
    # Scen 5: PCMH Date is a Sunday
    When I view "T23456781" in View Case page
    Then Booking Status of PCM Hearing for case "T23456781" is "Unscheduled"
    # Scen 5: PCMH Date is a Saturday
    When I view "N23456781" in View Case page
    Then Booking Status of PCM Hearing for case "N23456781" is "Unscheduled"
    
  Scenario: 6 Unlisting an auto scheduled PCM sets its Booking Status to Unscheduled
	When I Unlist the PCM Hearing "A12345678" in Room "Room681-1" on "03/03/2020"
	And I view "A12345678" in View Case page
    Then Booking Status of PCM Hearing for case "A12345678" is "Unscheduled"
     