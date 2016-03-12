@CCS-145 
@grouptwo
Feature: Book an unbooked Hearing as provisional or confirmed
  
  Scenario: 3 Cannot book a hearing without a booking type
    Given hearing "Hearing_145" exists for Case "T900" with lead defendant as "Ed"
    And court room "UniqueRoom" is allocated to judge "UniqueJudge" for current date plus 30 days
    And I am logged in as ListingOfficer
    When I select "Hearing_145" from "selectedUnscheduledHearing" element
    And I enter current Date in "hearingStartDate" element
    And I select "UniqueRoom" from "roomForListing" element
    And I do not enter hearingBookingType
    Then I cannot click on  "listHearing" button

  @CCS-543 @CCS-614 @CCS-580 
  Scenario Outline: 1 Book an unbooked Hearing as provisional or confirmed
    Given hearing "Hearing_614" exists for Case "T900" with lead defendant as "Ed"
    Given I am logged in as ListingOfficer
    When I select <Hearing> from "selectedUnscheduledHearing" element
    And I enter current Date in "hearingStartDate" element
    And I select <Booking Type> from "hearingBookingType" element
    Then I cannot click on  "listHearing" button
    And I cannot see text Booking Type: <Booking Type> in the "hearingInfoBookingType" element
    When I select "UniqueRoom" from "roomForListing" element
    Then I cannot see text "Court Room: UniqueRoom" in the "hearingInfoCourtRoom" element
    And I click on "listHearing" button
    Then I can see <Hearing> in Planner on CurrentDate

    Examples: 
      | Hearing       | Booking Type  |
      | "Hearing_614" | "Provisional" |
      | "Hearing_2"   | "Confirmed"   |

  @CCS-622 
  Scenario Outline: 2 View the hearing booking type of the hearing already scheduled
    Given I am logged in as ListingOfficer
    And I can see <Case> in Planner page
    And <Case> has <Hearing>
    And I can see <Hearing> in Planner on CurrentDate
    And hearing <Hearing> is Booked in Planner
    When I click on "listHearingTable" link for <Hearing>
    Then I see "ExpandedHearing" element for <Hearing>
    And I see "BookingType" element for <Booking_Type>
    And I see "selectedUnscheduledHearing" drop down element is cleared

    Examples: 
      | Case   | Hearing       | Booking_Type  |
      | "T900" | "Hearing_614" | "Provisional" |
      | "T900" | "Hearing_2"   | "Confirmed"   |
  
  Scenario Outline: 4 Case status needs to change to show the case has a provisional hearing
    Given I select CaseView page
    When I enter case "T900" in "caseCrestNumber" element
    And I click on "getCaseDetailsButton" button
    Then I can see <Booking_Type> in Hearing Booking Type element

    Examples: 
      | Case   | Hearing       | Booking_Type  |
      | "T900" | "Hearing_145" | "Provisional" |
      | "T900" | "Hearing_2"   | "Confirmed"   |
      | "T900" | "Hearing_3"   | ""            |

  @CCS-684 @defect-fix
  Scenario: 5 Cannot relist hearing once a No Slot error is returned
    Given I am on Planner page
    And Court Room "UniqueRoom" exists with Hearing "Hearing684-1" listed from "today" for "1" days as type "Provisional"
    And Court Room "Room684" has no judge allocated for date "today"
    When I try to list Unlisted hearing "Hearing684-2" for date "today" in Court Room "Room684" as type "Provisional"
    Then I can see text "Bad Request: This hearing cannot be listed in this slot"
    When I click on Listed hearing "Hearing684-1"
    And I enter current Date in "scheduledHearingStartDate" element
    When I select "Room684" from "scheduledRoomForListing" element
    And I select "Provisional" from "scheduledHearingBookingType" element
    And I click on "updateHearing" button
    Then I can see text "Bad Request: This hearing cannot be listed in this slot"

  @CCS-580 @CCS-674
  Scenario Outline: 6 Book an unbooked Hearing as provisional or confirmed
    Given hearing <Hearing> exists for Case "T900" with lead defendant as "Ed"
    Given I am logged in as ListingOfficer
    When I select <Hearing> from "selectedUnscheduledHearing" element
    And I enter Date <TheDate> with pattern (dd/MM/yyyy) in "hearingStartDate" element
    And I select "Confirmed" from "hearingBookingType" element
    When I select "UniqueRoom" from "roomForListing" element
    And I click on "listHearing" button
    Then I can see text <message>

    Examples: 
      | Hearing         | TheDate      | Booking Type  | message                                        |
      | "Hearing_580_1" | "26/03/2014" | "Provisional" |"Bad Request: Hearing cannot be listed in past" |
      | "Hearing_580_1" | "29/03/2014" | "Confirmed"   |"Bad Request: Hearing cannot be listed in past" |
      | "Hearing_580_1" | "07/03/2020" | "Confirmed"   |"StartDate is not a working day"                |
      | "Hearing_580_1" | "08/03/2020" | "Confirmed"   |"StartDate is not a working day"                |
