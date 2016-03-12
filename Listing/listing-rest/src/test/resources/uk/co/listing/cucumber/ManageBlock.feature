@CCS-138 @CCS-377 
@grouptwo
Feature: Create Block with Type
  As a Listing Officer
  I want to set a block for a type of case
  So that I can easily put the right hearing in the right court

  Scenario Outline: 1 Set,View and delete Block Types for series of dates
    Given I am on Planner page
    When I enter Court room <CourtRoom> , Block Type <BlockType> , start date <BlockStartDate> , end date <BlockEndDate> in Set Block Type element
    And I click and wait for message , click "manageBlock" and wait for "manageBlocksMessage" to be "Block allocated successfully"
    And I see Block type code <BlockTypePresentationCode> for type <BlockType> in Court room <CourtRoom> from start date <BlockStartDate> to end date <BlockEndDate>
    And I enter Court room <CourtRoom> , Block Type <BlockType> , start date <BlockStartDate> , end date <BlockEndDate> in Set Block Type element
    And I click and wait for message , click "deleteBlock" and wait for "manageBlocksMessage" to be "Block deleted successfully"
    And I do not see Block type code <BlockTypePresentationCode> for type <BlockType> in Court room <CourtRoom> from start date <BlockStartDate> to end date <BlockEndDate>

    Examples: 
      | CourtRoom   | BlockType                   | BlockStartDate | BlockEndDate    | BlockTypePresentationCode |
      | "Room138-1" | "Trial"                     | "next_week"    | "next_week + 3" | "T"                       |
      | "Room138-1" | "PCM"                       | "next_week"    | "next_week + 3" | "P"                       |
      | "Room138-1" | "Short Work"                | "next_week"    | "next_week + 3" | "SW"                      |
      | "Room138-1" | "Case Management"           | "next_week"    | "next_week + 3" | "CM"                      |
      | "Room138-1" | "Sentence"                  | "next_week"    | "next_week + 3" | "S"                       |
      | "Room138-1" | "Appeal against Conviction" | "next_week"    | "next_week + 3" | "AC"                      |
      | "Room138-1" | "Appeal against Sentence"   | "next_week"    | "next_week + 3" | "AS"                      |

  Scenario Outline: 2 cannot delete Block Types with listed hearing
    Given I am on Planner page
    When I enter Court room <CourtRoom> , Block Type <BlockType> , start date <BlockStartDate> , end date <BlockEndDate> in Set Block Type element
    And I click and wait for message , click "manageBlock" and wait for "manageBlocksMessage" to be "Block allocated successfully"
    And I see Block type code <BlockTypePresentationCode> for type <BlockType> in Court room <CourtRoom> from start date <BlockStartDate> to end date <BlockEndDate>
    And Court Room <CourtRoom> exists with Hearing <HearingName> listed from <BlockStartDate> for "1" days as type "Provisional"
    And I enter Court room <CourtRoom> , Block Type <BlockType> , start date <BlockStartDate> , end date <BlockEndDate> in Set Block Type element
    And I click and wait for message , click "deleteBlock" and wait for "manageBlocksMessage" to be "Bad Request: One of the block contains hearing"

    Examples: 
      | CourtRoom   | HearingName    | BlockType | BlockStartDate  | BlockEndDate    | BlockTypePresentationCode |
      | "Room138-1" | "Hearing138-1" | "Trial"   | "next_week + 4" | "next_week + 5" | "T"                       |
