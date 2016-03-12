@CCS-196
@groupone
Feature: Add judge to list of judges
  As a Listing Officer
  I want add judge to list of judges
  So that I can assign judges to sitting and manage my Court Centre
  I want to record the type of a judge
  So that I can ensure that judges are hearing appropriate cases
  
  @CCS-728
  Scenario Outline: 1 Create a judge in a list of judges by selecting a Judge type
    Given I go to listing admin
    And I can see element "listOfJudges"
    When I enter <JudgeName> in "judgeName"
    And I select Judge type as <JudgeType>
    # CCS-728 Scenario: 2 Is QC flag is mandatory
    And I select Is QC as <IsQC>
    And I select "addJudge"
    Then I see <JudgeName> in "listOfJudges"
    And Judge type for Judge <JudgeName> is <JudgeType>
    And Is QC for Judge <JudgeName> is <IsQC>
  
  	Examples:
  		| JudgeName | JudgeType    			 | IsQC  |
  		| "Judge-A" | "High Court"		     | "Yes" |
  		| "Judge-B" | "High Court"		     | "No"  |
  		| "Judge-C" | "Circuit" 			 | "Yes" |
  		| "Judge-D" | "Circuit"				 | "No"  |
  		| "Judge-E" | "Recorder"			 | "Yes" |
  		| "Judge-F" | "Recorder" 			 | "No"  |
  		| "Judge-G" | "Deputy Circuit Judge" | "Yes" |
  		| "Judge-H" | "Deputy Circuit Judge" | "No"  |
  	
  Scenario: 2 Cannot add judge twice
    Given I go to listing admin
    And I can see element "listOfJudges"
    And I enter "Judge-I" in "judgeName"
    And I select Judge type as "High Court"
    And I select "addJudge"
    And I see "Judge-I" in "listOfJudges"
    When I enter "Judge-I" in "judgeName"
    And I select Judge type as "High Court"
    And I select "addJudge"
    Then I see "judgeSavedMessage" "Judge-I", already exists
  
  Scenario: 3 Name not case sensitive
    Given I go to listing admin
    And I can see element "listOfJudges"
    And I enter "Judge-J" in "judgeName"
    And I select Judge type as "High Court"
    And I select "addJudge"
    And I see "Judge-J" in "listOfJudges"
    When I enter "JUDGE-j" in "judgeName"
    And I select Judge type as "High Court"
    And I select "addJudge"
    Then I see "judgeSavedMessage" "Judge-j", already exists
  
  Scenario: 4 Only allowed characters
    Given I go to listing admin
    And I can see element "listOfJudges"
    When I enter "fir-st m. las't" in "judgeName"
    # CCS-728 Scenario: 2 Judge type is mandatory
    Then I see "addJudge" button is disabled
    When I select Judge type as "High Court"
    And I select "addJudge"
    Then I see "fir-st m. las't" in "listOfJudges"
  
  Scenario: 5 Not disallowed characters
    Given I go to listing admin
    And I can see element "listOfJudges"
    When I enter "first 1" in "judgeName"
    Then I see "judgeNameInvalid" validation error.
    When I enter "first_b" in "judgeName"
    Then I see "judgeNameInvalid" validation error.
    When I enter "first?" in "judgeName"
    Then I see "judgeNameInvalid" validation error.
  
  @CCS-728
  Scenario: 6 Can specify that a Circuit Judge is also a Resident Judge
    Given I go to listing admin
    And I can see element "listOfJudges"
    When I enter "Judge-K" in "judgeName"
    And I select Judge type as "Circuit"
    And I select Is QC as "Yes"
    And I mark Judge as Resident Judge
    And I select "addJudge"
    Then I see "Judge-K" in "listOfJudges"
    And Judge type for Judge "Judge-K" is "Circuit"
    And Is QC for Judge "Judge-K" is "Yes"
    And Resident Judge is "Yes" for Judge "Judge-K"
    
  @CCS-728
  Scenario Outline: 7 Can not specify Recorder, Deputy Circuit Judge or High Court Judge is a Resident Judge
    Given I go to listing admin
    And I can see element "listOfJudges"
    And I enter "Judge-L" in "judgeName"
    When I select Judge type as <JudgeType>
    Then Is Resident Judge is disabled
    
    Examples:
      | JudgeType 			   |
      | "High Court" 	       |
      | "Recorder" 			   |
      | "Deputy Circuit Judge" |
 
 @CCS-329
  Scenario Outline: 8 Select tickets when creating a Circuit Judge
    Given I go to listing admin
    And I can see element "listOfJudges"
    When I enter <JudgeName> in "judgeName"
    And I select Judge type as "Circuit"
    And I select listOfTickets <listOfTickets>
    And I select "addJudge"
    Then I see <JudgeName> in "listOfJudges"
    And JudgeName <JudgeName> has <listOfTickets>
    
  	Examples:
		|JudgeName|listOfTickets|
		|"CJOne"|"Murder"|
		|"CJTwo"|"Attempted Murder"|
		|"CJThree"|"Sexual Offences"|
		|"CJFour"|"Fraud"|
		|"CJFive"|"Health and Safety"|
		|"CJSix"|"Murder,Attempted Murder"|
		|"CJSeven"|"Murder,Sexual Offences"|
		|"CJEight"|"Murder,Fraud"|
		|"CJNine"|"Murder,Health and Safety"|
		|"CJTen"|"Attempted Murder,Sexual Offences"|
		|"CJEleven"|"Attempted Murder,Fraud"|
		|"CJTwelve"|"Attempted Murder,Health and Safety"|
		|"CJThirteen"|"Sexual Offences,Fraud"|
		|"CJFourteen"|"Sexual Offences,Health and Safety"|
		|"CJ A"|"Fraud,Health and Safety"|
		|"CJ B"|"Murder,Attempted Murder,Sexual Offences"|
		|"CJ C"|"Murder,Attempted Murder,Fraud"|
		|"CJ D"|"Murder,Attempted Murder,Health and Safety"|
		|"CJ E"|"Murder,Sexual Offences,Fraud"|
		|"CJ F"|"Murder,Sexual Offences,Health and Safety"|
		|"CJ G"|"Murder,Fraud,Health and Safety"|
		|"CJ H"|"Attempted Murder,Sexual Offences,Fraud"|
		|"CJ I"|"Attempted Murder,Sexual Offences,Health and Safety"|
		|"CJ J"|"Attempted Murder,Fraud,Health and Safety"|
		|"CJ K"|"Sexual Offences,Fraud,Health and Safety"|
		|"CJ L"|"Murder,Attempted Murder,Sexual Offences,Fraud"|
		|"CJ M"|"Murder,Attempted Murder,Sexual Offences,Health and Safety"|
		|"CJ N"|"Murder,Attempted Murder,Fraud,Health and Safety"|
		|"CJ O"|"Murder,Sexual Offences,Fraud,Health and Safety"|
		|"CJ P"|"Attempted Murder,Sexual Offences,Fraud,Health and Safety"|
		|"CJ Q"|"Murder,Attempted Murder,Sexual Offences,Fraud,Health and Safety"|
		|"CJ R"|""|
		
 @CCS-329
  Scenario Outline: 9 Select tickets when creating a Recorder
   Given I go to listing admin
    And I can see element "listOfJudges"
    When I enter <JudgeName> in "judgeName"
    And I select Judge type as "Recorder"
    And I don't select other tickets
    And I select listOfTickets <listOfTickets>
    And I select "addJudge"
    Then I see <JudgeName> in "listOfJudges"
    And JudgeName <JudgeName> has <listOfTickets>
    But JudgeName <JudgeName> does not have any other tickets
    
    Examples:
		|JudgeName|listOfTickets|
		|"Rec One"|"Sexual Offences"|
		|"Rec Two"|""|
		
  @CCS-329
  Scenario: 10  High Court Judge has all tickets, user can not select them
   Given I go to listing admin
    And I can see element "listOfJudges"
    When I enter "HCJ" in "judgeName"
    And I select Judge type as "High Court"
    And I can not select a ticket
    And I select "addJudge"
    Then I see "HCJ" in "listOfJudges"
    And JudgeName "HCJ" has "Murder,Attempted Murder,Sexual Offences,Fraud,Health and Safety"
    

		