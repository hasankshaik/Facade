@CCS-139 @CCS-435
@groupone
Feature: View Case - basic case information
  As a Listing Officer
  I want to view case details
  so that I can  manage my Court Centre
  As a listing officer
  I want to see the trial estimate from CREST with appropriate units
  So that I know how long the trial is expected to take

  # For the CCS-435: Scenarios 1 (for the fist one of the story),3,4,5,6 and 7 (for the 3rd one), and 8 for the second one
  @runme
  Scenario Outline: : 1 I need to know the info of the Case
    Given I am in View case page and example cases Exist
    When I enter <validCrestNumber> in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see <validCrestNumber> in "crestCaseNumber"
    And I can see <mostSeriousOffence> in "mostSeriousOffence"
    And I can see <trialEstimate> in "trialEstimate"
    # In this line I am testing the scenario 1 of the CCS-435
    And I can see <estimationunit> in "trialEstimateUnit"
    And I can see <offenceClass> in "offenceClass"
    And I can see <releaseDecisionStatus> in "releaseDecisionStatus"
    And I can see <ticketingRequirement> in "ticketingRequirement"
    And I see <emptyHearings> in emptyHearings

    Examples: 
      | validCrestNumber | leadDefendant | mostSeriousOffence | trialEstimate | estimationunit | offenceClass | releaseDecisionStatus | ticketingRequirement | emptyHearings        |
      | "T100"           | "Nigel"       | "most-serious"     | "1.2"         | "Days"         | "1"          | "Any Recorder"          | "None"               | "No hearings listed" |
      | "T200"           | "David"       | "most-serious"     | "9"           | "Hours"        | "1"          | "Any Recorder"              | "Murder"             | "No hearings listed" |
      | "T300"           | "Ed"          | "most-serious"     | "8"           | "Hours"        | "2"          | "Any Recorder"              | "Rape"               | "No hearings listed" |
      | "T401"           | "Ed"          | "most-serious"     | "1.7"         | "Weeks"        | "2"          | "Any Recorder"              | "Attempted Murder"   | "No hearings listed" |
      | "T501"           | "Ed"          | "most-serious"     | "0"           | "Hours"        | "2"          | "Any Recorder"              | "Attempted Murder"   | "No hearings listed" |

  Scenario: 2 I see error message when Crest case number doesnt exist
    Given I am in View case page and example cases Exist
    When I enter "T2000" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    Then I can see text "No case found with the Crest case number "

  Scenario: 3 Create hearing for case with days of trial estimation #This case has 1.2 days
    Given I am in View case page and example cases Exist
    When I enter "T100" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    When I enter "Hearing1" in "hearingName"
    And I click "createHearing"
    Then I can see "Hearing1" in Case Page
    And I can see Estimate is "2" in Hearings table for hearing named "Hearing1"

  Scenario: 4 Create hearing for case with nine hours of trial estimation #This case has 9 hours
    Given I am in View case page and example cases Exist
    When I enter "T200" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    When I enter "Hearing1" in "hearingName"
    And I click "createHearing"
    Then I can see "Hearing1" in Case Page
    And I can see Estimate is "2" in Hearings table for hearing named "Hearing1"

  Scenario: 5 Create hearing for case with eight hours of trial estimation #This case has 8 hours
    Given I am in View case page and example cases Exist
    When I enter "T300" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    When I enter "Hearing1" in "hearingName"
    And I click "createHearing"
    Then I can see "Hearing1" in Case Page
    And I can see Estimate is "1" in Hearings table for hearing named "Hearing1"

  Scenario: 6 Create hearing for case with eight hours of trial estimation #This case has 1.7 weeks
    Given I am in View case page and example cases Exist
    When I enter "T401" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    When I enter "Hearing1" in "hearingName"
    And I click "createHearing"
    Then I can see "Hearing1" in Case Page
    And I can see Estimate is "9" in Hearings table for hearing named "Hearing1"

  Scenario: 7 Create hearing for case with eight hours of trial estimation #This case has 0 hours
    Given I am in View case page and example cases Exist
    When I enter "T501" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    When I enter "Hearing1" in "hearingName"
    And I click "createHearing"
    Then I can see "Hearing1" in Case Page
    And I can see Estimate is "0" in Hearings table for hearing named "Hearing1"

  Scenario: 8  No estimate provided
    Given I am on the Edit Case page
    When I enter "T5655" in "crestCaseNumber"
    And I click "addCase"
    And I enter "Peter" in "leadDefendant"
    And I enter "5" in "numberOfDefendent"
    And I Select "1" in "offenceClass"
    And I enter date "today + 5" in "dateOfSending"
    And I enter date "today + 5" in "dateOfCommittal"
    And I click "setCaseDetailsButton" to create case
    Then I am in View case page and example cases Exist
    When I enter "T5655" in "caseCrestNumber"
    And I click "getCaseDetailsButton"
    And I can see "0" in "trialEstimate"
    And I can see "Hours" in "trialEstimateUnit"
