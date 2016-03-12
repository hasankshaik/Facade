@CCS-522
@grouptwo
Feature: Edit Case Compulsory Fields
  As an user
  I want to edit the compulsory 
  fields of a case

  Background: 
    Given I am on the Edit Case page

  Scenario Outline: 1 Can not edit case number
    When I enter new case details <crestCaseNumber> , <leadDefendant> , <numberOfDefendants> , <offenceClass> , <dateOfSending> , <dateOfCommittal> in Create Case page
    And I Select <courtCenterForCase> in "courtCenterList"
    And I click "setCaseDetailsButton" to create case
    Then I see message "Case has been Saved" in "successSaveMessage"
    When I enter <crestCaseNumber2> in "crestCaseNumber"
    And I click "setCaseDetailsButton" to create case
    Then I see message "Cannot Edit Crest Number" in "errorSaveMessage"
    
    Examples:
      | courtCenterForCase | crestCaseNumber | leadDefendant | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal | crestCaseNumber2 |
      | "Birmingham"       | "T12345928"     | "Gabbar"      | "1"                | "1"          | "today + 5"   | "today + 7"     | "T013"           |

  Scenario Outline: 2 Can not edit court center for a case
    When I enter new case details <crestCaseNumber> , <leadDefendant> , <numberOfDefendants> , <offenceClass> , <dateOfSending> , <dateOfCommittal> in Create Case page
    And I Select <courtCenterForCase> in "courtCenterList"
    And I click "setCaseDetailsButton" to create case
    Then I see message "Case has been Saved" in "successSaveMessage"
    When I Select <courtCenterForCase2> in "courtCenterList"
    And I click "setCaseDetailsButton" to create case
    Then I see message "Cannot Edit Court Center" in "errorSaveMessage"

    Examples: 
      | courtCenterForCase | crestCaseNumber | leadDefendant | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal | courtCenterForCase2 |
      | "Birmingham"       | "T12345929"     | "Gabbar"      | "1"                | "1"          | "today + 5"   | "today + 7"     | "Centre371"         |

  Scenario Outline: 3 Can not edit court center and crest number for a case
    When I enter new case details <crestCaseNumber> , <leadDefendant> , <numberOfDefendants> , <offenceClass> , <dateOfSending> , <dateOfCommittal> in Create Case page
    And I Select <courtCenterForCase> in "courtCenterList"
    And I click "setCaseDetailsButton" to create case
    Then I see message "Case has been Saved" in "successSaveMessage"
    When I Select <courtCenterForCase2> in "courtCenterList"
    And I enter <crestCaseNumber2> in "crestCaseNumber"
    And I click "setCaseDetailsButton" to create case
    Then I see message "Cannot Edit Court Center" in "errorSaveMessage"

    Examples: 
      | courtCenterForCase | crestCaseNumber | leadDefendant | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal | courtCenterForCase2 | crestCaseNumber2 |
      | "Birmingham"       | "T12345930"     | "Gabbar"      | "1"                | "1"          | "today + 5"   | "today + 7"     | "Centre371"         | "T013"           |

 Scenario Outline: 4 Edit other fields
    Given The case "T1003" exists in "Birmingham"
    When I enter "T1003" in "crestCaseNumber"
    And I open the case
    And I enter <leadDefendant> in "leadDefendant"
    And I enter <numberOfDefendants> in "numberOfDefendent"
    And I Select <offenceClass> in "offenceClass"
    And I enter date <dateOfSending> in "dateOfSending"
    And I enter date <dateOfCommittal> in "dateOfCommittal"
    And I enter <trialEstimation> in "trialEstimate"
    And I Select <trialEstimationUnit> in "trialEstimateUnit"
    And I enter <mostSeriousOffence> in "mostSeriousOffence"
    And I Select <releaseDecision> in "releaseDecisionStatus"
    And I Select <ticketingRequirement> in "ticketingRequirement"
    And I click "setCaseDetailsButton" to create case
    Then I see message "Case has been Saved" in "successSaveMessage"
    When I view <crestCaseNumber> in View Case page
    Then I can see <crestCaseNumber> in "crestCaseNumber"
    And I can see defendant <leadDefendant> in "leadDefendant"
    And I can see <offenceClass> in "offenceClass"
    And I can see <mostSeriousOffence> in "mostSeriousOffence"
    And I can see <releaseDecision> in "releaseDecisionStatus"
    And I can see <ticketingRequirement> in "ticketingRequirement"
    And I can see <trialEstimation> in "trialEstimate"
    And I can see <trialEstimationUnit> in "trialEstimateUnit"
    And I can see <numberOfDefendants> in "numberOfDefendent"
    And I can see date <dateOfSending> in "dateOfSending"
    And I can see date <dateOfCommittal> in "dateOfCommittal"

    Examples: 
      | courtCenterForCase | crestCaseNumber | leadDefendant | numberOfDefendants | mostSeriousOffence | offenceClass | releaseDecision | ticketingRequirement | dateOfSending | dateOfCommittal | trialEstimation | trialEstimationUnit |
      | "Birmingham"       | "T1003"         | "Defendant1"  | "0"                | "utmost-serious"   | "1"          | "Any Recorder"        | "None"               | "today + 3"   | "today + 1"     | "2"             | "Days"              |
      | "Birmingham"       | "T1003"         | "Defendant2"  | "1"                | "utmost-serious"   | "2"          | "Any Recorder"        | "Attempted Murder"   | "today + 1"   | "today + 2"     | "2"             | "Weeks"             |
      | "Birmingham"       | "T1003"         | "Defendant3"  | "2"                | "utmost-serious"   | "3"          | "Any Recorder"        | "Fraud"              | "today + 5"   | "today + 3"     | "0"             | "Hours"             |
      | "Birmingham"       | "T1003"         | "Defendant4"  | "3"                | "utmost-serious"   | "4"          | "Any Recorder"        | "Murder"             | "today + 7"   | "today + 5"     | "200"           | "Hours"             |
      | "Birmingham"       | "T1003"         | "Defendant5"  | "400"              | "utmost-serious"   | "None"       | "Any Recorder"        | "Rape"               | "today + 9"   | "today + 7"     | "200"           | "Days"              |
      
    Scenario Outline: 5 Move away from page clears edit screen
    When I enter new case details <crestCaseNumber> , <leadDefendant> , <numberOfDefendants> , <offenceClass> , <dateOfSending> , <dateOfCommittal> in Create Case page
    And I Select <courtCenterForCase> in "courtCenterList"
    And I click "setCaseDetailsButton" to create case
    Then I see message "Case has been Saved" in "successSaveMessage"
    When I enter "This offence is not the most serious" in "mostSeriousOffence"
    And I view <crestCaseNumber> in View Case page
    Then I can not see case details like "This offence is not the most serious" on case page

    Examples: 
      | courtCenterForCase | crestCaseNumber | leadDefendant | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal |
      | "Birmingham"       | "T12345931"     | "Gabbar"      | "1"                | "1"          | "today + 5"   | "today + 7"     |
 
      
      
 @CCS-759
 Scenario Outline: 6  Allocated Judge is mandatory for Named Judge release decision
    Given The case <CaseNumber> exists in "Birmingham"
    When I enter <CaseNumber> in "crestCaseNumber"
    And I open the case
    And CaseNumber <CaseNumber>, ReleaseDecision <releaseDecisionStart>, <allocatedJudgeStart>
    And I Select <releaseDecisionEdit> in "releaseDecisionStatus"
    And I Select <allocatedJudgeEdit> in "judgeSelected"
	And I click "setCaseDetailsButton" to create case
	And I see message <message> in <messageholder>
	And CaseNumber <CaseNumber>, ReleaseDecision <releaseDecisionEnd>, <allocatedJudgeEnd>
	
	 Examples: 
	 |CaseNumber | releaseDecisionStart | releaseDecisionEdit | releaseDecisionEnd | allocatedJudgeStart | allocatedJudgeEdit | allocatedJudgeEnd| message                                                                 | messageholder      |
     | "T1003"   | "Any Recorder"       | "Named Judge"       | ""                 | ""                  | ""                 | ""               |"You must allocate a judge when you select Release Decision: Named Judge"|"errorSaveMessage"  |
     | "T1003"   | "Any Recorder"       | "Named Judge"       | "Named Judge"      | ""                  | "UniqueJudge"      | "UniqueJudge"    | "Case has been Saved"                                                   |"successSaveMessage"|
     | "T1003"   | ""                   | "Named Judge"       | "Named Judge"      | "UniqueJudge"       | "UniqueJudge"      | "UniqueJudge"    | "Case has been Saved"                                                   |"successSaveMessage"|

