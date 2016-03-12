@CCS-384
@grouptwo
Feature: Edit Hearing from Unlisted Hearings Element
  Edit allowed data fields from unlisted hearings element
  Cannot edit disallowed fields from unlisted hearings element
  Cannot remove values from the Case Page

  Scenario Outline: 1 Edit Hearing from Unlisted Hearings Element
    Given hearing <Hearing> exists for Case "T900" with lead defendant as "Ed"
    And I am logged in as ListingOfficer
    When I select <Hearing> from "selectedUnscheduledHearing" element
    And and I input <TrialEstimate> in  Trial Estimate inputbox "trialEstimateId"
    And I click on "editEstimate" button
    Then I see text <TrialEstimate>
    And <Hearing> has <Status>

    Examples: 
      | Hearing       | TrialEstimate | Status        |
      | "Hearing_384" | "5"           | "Unscheduled" |

  Scenario Outline: 3 Cannot remove values from the Unlisted Hearings Element
    Given I am logged in as ListingOfficer
    When I select <Hearing> from "selectedUnscheduledHearing" element
    And and I input <TrialEstimate> in  Trial Estimate inputbox "trialEstimateId"
    Then EditEstimate button "editEstimate" is disabled

    Examples: 
      | Hearing       | TrialEstimate |
      | "Hearing_384" | ""            |

  @CCS-680
  Scenario Outline: 4 Cannot see PCM hearing in Unlisted Hearings drop down
    Given PCM hearing <PCMHName> exists for case <CaseNo>
    When I go to Planner page
    And I click on Unscheduled Hearings drop down
    Then I should not see PCM hearing <PCMHName> in the Unscheduled Hearing drop down

	Examples:
	  | PCMHName | CaseNo |
	  | "PCMH"   | "T400" | 