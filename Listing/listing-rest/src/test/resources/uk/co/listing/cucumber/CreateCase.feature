@CCS-517 @CCS-428 @CCS-526 @CCS-521 @CCS-519 @CCS-520 @CCS-524 @CCS-759
@groupone
Feature: Create Case with compulsory fields
  As a Listing Officer
  I want to enter a case with the compulsory fields
  So that I can use the system without Create Data

  Background: 
    Given I am on the Edit Case page
  
  @CCS-759 
  Scenario Outline: 1 Add case when compulsory fields are entered
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "addCase"
    And I enter <leadDefendant> in "leadDefendant"
    And I enter <numberOfDefendants> in "numberOfDefendent"
    And I Select <offenceClass> in "offenceClass"
    And I enter date <dateOfSending> in "dateOfSending"
    And I enter date <dateOfCommittal> in "dateOfCommittal"
    And I click "setCaseDetailsButton" to create case
    Then I see message "Case has been Saved" in "successSaveMessage"
    When I view <crestCaseNumber> in View Case page
    Then I can see <crestCaseNumber> in "crestCaseNumber"
    And I can see <leadDefendant> in "leadDefendant"
    And I can see <offenceClass> in "offenceClass"
    And I can see <numberOfDefendants> in "numberOfDefendent"
    And I can see date <dateOfSending> in "dateOfSending"
    And I can see date <dateOfCommittal> in "dateOfCommittal"
    And I can see <releaseDecisionStatus> in "releaseDecisionStatus"

    Examples: 
      | crestCaseNumber | leadDefendant  | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal | releaseDecisionStatus |
      | "T12345600"     | "Gabbar"       | "1"                | "1"          | "today + 5"   | ""              | "Any Recorder"        |
      | "T12345601"     | "Kalia"        | "1"                | "2"          | ""            | "today + 10"    | "Any Recorder"        |
      | "T12345602"     | "Jack Sparrow" | "1"                | "3"          | "today + 3"   | "today + 20"    | "Any Recorder"        |

  @CCS-1002
  Scenario Outline: 3 Cannot Create Case without all compulsory fields
    When I enter "CCS-517" in "crestCaseNumber"
    And I click "addCase"
    And I enter <crestCaseNumber> in "crestCaseNumber"
    And I enter <leadDefendant> in "leadDefendant"
    And I enter <numberOfDefendants> in "numberOfDefendent"
    And I Select <offenceClass> in "offenceClass"
    And I enter date <dateOfSending> in "dateOfSending"
    And I enter date <dateOfCommittal> in "dateOfCommittal"
    Then I see "setCaseDetailsButton" button is disabled
    When I view <crestCaseNumber> in View Case page
    Then I can see text "No case found with the Crest case number"
    And I can not see case details like <leadDefendant> on case page

    Examples: 
      | crestCaseNumber | leadDefendant | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal |
      | "T12345604"     | "Gabbar"      | ""                 | ""           | "today + 5"   | "today + 10"    |
      | "T12345605"     | "Gabbar"      | "3"                | "1"          | ""            | ""              |
      | "T12345605"     | "Gabbar"      | "A"                | "1"          | ""            | ""              |
      | "T12345605"     | "Gabbar"      | "-2"                | "1"          | ""            | ""              |

  Scenario Outline: 3a Cannot Create Case without Case No and Lead Defendant
    When I enter "CCS-517" in "crestCaseNumber"
    And I click "addCase"
    And I enter <crestCaseNumber> in "crestCaseNumber"
    And I enter <leadDefendant> in "leadDefendant"
    And I enter <numberOfDefendants> in "numberOfDefendent"
    And I Select <offenceClass> in "offenceClass"
    And I enter date <dateOfSending> in "dateOfSending"
    And I enter date <dateOfCommittal> in "dateOfCommittal"
    Then I see "setCaseDetailsButton" button is disabled

    Examples: 
      | crestCaseNumber | leadDefendant | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal |
      | ""              | ""            | "3"                | "1"          | "today + 5"   | "today + 10"    |

  Scenario Outline: 4 Cannot Create Case with same Case Number
    Given case with <crestCaseNumber> already exist
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "addCase"
    Then I see "Case already exist" response message
    When I view <crestCaseNumber> in View Case page
    And I can not see case details like <leadDefendant> on case page

    Examples: 
      | crestCaseNumber | leadDefendant | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal |
      | "T100"          | "Gabbar"      | "3"                | "1"          | "today + 5"   | "today + 10"    |

  @CCS-518
  Scenario Outline: 5 Add defendants while creating case
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    When I click "Add Defendant" button
    And I enter defendant name <defendantNameOne> ,custody status <bailCustodyStatusOne> ,custody expiry date <custodyTimeLimitExpiryDateOne> ,urn <defendantURNOne> in Create Case page
    And I click "Add" button
    Then I see message "Defendant has been added" in "errorAddMessage"
    And I see defendant details <defendantNameOne> , <bailCustodyStatusOne> , <custodyTimeLimitExpiryDateOne> , <defendantURNOne> in Create Case page
    When I click "Add Defendants" button
    And I enter defendant name <defendantNameTwo> ,custody status <bailCustodyStatusTwo> ,custody expiry date <custodyTimeLimitExpiryDateTwo> ,urn <defendantURNTwo> in Create Case page
    And I click "Add" button
    Then I see message "Defendant has been added" in "errorAddMessage"
    And I see defendant details <defendantNameTwo> , <bailCustodyStatusTwo> , "" , <defendantURNTwo> in Create Case page
    When I view <crestCaseNumber> in View Case page
    Then I see defendant details <defendantNameOne> , <bailCustodyStatusOne> , <custodyTimeLimitExpiryDateOne> , <defendantURNOne> in View Case page
    And I see defendant details <defendantNameTwo> , <bailCustodyStatusTwo> , "" , <defendantURNTwo> in View Case page

    Examples: 
      | crestCaseNumber | leadDefendant | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal | defendantNameOne | bailCustodyStatusOne | custodyTimeLimitExpiryDateOne | defendantURNOne | defendantNameTwo | bailCustodyStatusTwo | custodyTimeLimitExpiryDateTwo | defendantURNTwo |
      | "T12345606"     | "Esposito"    | "3"                | "1"          | "today"       | "today"         | "Ben Hur"        | "Custody"            | "today + 7"                   | "9A8"           | "Jen Hymn"       | "Bail"               | "today + 7"                   | "6789345361"    |

  @CCS-523
  Scenario Outline: 6,7 Find and Edit defendant created in same session
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I update defendant details for <defendantNameOne> to <defendantNameTwo> ,custody status <bailCustodyStatusTwo> ,custody expiry date <custodyTimeLimitExpiryDateTwo> ,urn <defendantURNTwo> in Create Case page
    And I click "Save" button for <defendantNameOne> in Create Case page
    Then I see message "Defendant has been Updated" in "errorAddMessage"
    And I see defendant details <defendantNameTwo> , <bailCustodyStatusTwo> , <custodyTimeLimitExpiryDateTwo> , <defendantURNTwo> in Create Case page
    And I do not see defendant details <defendantNameOne> , <defendantURNOne>
    When I view <crestCaseNumber> in View Case page
    Then I see defendant details <defendantNameTwo> , <bailCustodyStatusTwo> , <custodyTimeLimitExpiryDateTwo> , <defendantURNTwo> in View Case page
    And I do not see defendant details <defendantNameOne> , <defendantURNOne>

    Examples: 
      | crestCaseNumber | leadDefendant    | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal | defendantNameOne | bailCustodyStatusOne | custodyTimeLimitExpiryDateOne | defendantURNOne | defendantNameTwo | bailCustodyStatusTwo | custodyTimeLimitExpiryDateTwo | defendantURNTwo |
      | "T12345607"     | "Bruce Reynolds" | "300"              | "1"          | "today"       | "today"         | "Buster Edwards" | "Custody"            | "today + 7"                   | "9-A-"          | "Inside Man"     | "Bail"               | "today + 7"                   | "A007-99991"    |

  @CCS-523
  Scenario Outline: 8 Delete Defendant
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Delete" button for <defendantNameOne> in Create Case page
    And I click Yes button for confirming "Delete defendant"
    Then I see message "Defendant deleted" in "errorRemoveMessage"
    And I do not see defendant details <defendantNameOne> , <defendantURNOne>
    When I view <crestCaseNumber> in View Case page
    And I do not see defendant details <defendantNameOne> , <defendantURNOne>

    Examples: 
      | crestCaseNumber | leadDefendant  | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal | defendantNameOne | bailCustodyStatusOne | custodyTimeLimitExpiryDateOne | defendantURNOne |
      | "T12345609"     | "I Dint Do It" | "300"              | "1"          | "today"       | "today"         | "Innocent"       | "Custody"            | "today + 7"                   | "A9-A"          |

  @CCS-521
  Scenario Outline: 9 Create note text for case sets date of note as current date
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Add Note" button
    And I enter Note <note>
    And I click "Add" button to Save the Note
    Then I see message "Note has been added" in "addNoteMessage"
    And I see Note <note>
    When I view <crestCaseNumber> in View Case page
    Then I see Note <note>
    And I see Creation Date is "today" for <note>
    And I see Diary date is "empty"

    Examples: 
      | crestCaseNumber | note                                                    |
      | "T12345610"     | "This is sample note to check that notes can be added." |

  @CCS-521
  Scenario Outline: 10 Set optional diary date for note
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Add Note" button
    And I enter Note <note>
    And I enter Diary date as "today"
    And I click "Add" button to Save the Note
    Then I see message "Note has been added" in "addNoteMessage"
    And I see Note <note>
    When I view <crestCaseNumber> in View Case page
    Then I see Note <note>
    And I see Creation Date is "today" for <note>
    And I see Diary date is "today"

    Examples: 
      | crestCaseNumber | note                                                    |
      | "T12345611"     | "This is sample note to check that notes can be added." |

  @CCS-521
  Scenario Outline: 12 Cannot exceed character limit in note text
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Add Note" button
    And I enter a total of 3070 characters in Notes field
    Then I see "saveNoteButton" button is disabled
    And I see save note error message "Note is mandatory and its maximum size is 3069 characters."
    And I can not see Note with 3070 characters
    When I view <crestCaseNumber> in View Case page
    Then I can not see Note with 3070 characters

    Examples: 
      | crestCaseNumber |
      | "T12345613"     |

  @CCS-521
  Scenario Outline: 13 Cannot save without mandatory fields
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Add Note" button
    And I enter Note <note>
    Then I see "saveNoteButton" button is disabled

    Examples: 
      | crestCaseNumber | note |
      | "T12345614"     | ""   |

  @CCS-526
  Scenario Outline: 14 Delete note
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Delete" note button for <note> in Create Case page
    And I click Yes button for confirming "Delete note"
    Then I see message "Note deleted" in "updateNoteMessage"
    And I can not see Note <note>
    When I view <crestCaseNumber> in View Case page
    And I can not see Note <note>

    Examples: 
      | crestCaseNumber | leadDefendant   | numberOfDefendants | offenceClass | dateOfSending | dateOfCommittal | note                                                    |
      | "T12345615"     | "John McKenzie" | "301"              | "1"          | "today"       | "today"         | "This is sample note to check that notes can be added." |

  @CCS-526
  Scenario Outline: 15 Cancel Delete note
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Delete" note button for <note> in Create Case page
    And I click No button for confirming "Delete note"
    And I see Note <note>
    When I view <crestCaseNumber> in View Case page
    And I see Note <note>

    Examples: 
      | crestCaseNumber | note                                                    |
      | "T12345616"     | "This is sample note to check that notes can be added." |

  @CCS-519
  Scenario Outline: 16 Add empty linked case
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Add Linked Case" button
    And I enter Linked Case <linkedCase>
    Then I see "saveLinkedCaseButton" button is disabled

    Examples: 
      | crestCaseNumber | linkedCase |
      | "T12345617"     | ""         |

  @CCS-519
  Scenario Outline: 17 Add linked case
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Add Linked Case" button
    And I enter Linked Case <linkedCase>
    Then I click "Save Linked Case Button" button
    And I see Linked Case <linkedCase>
    When I view <crestCaseNumber> in View Case page
    Then I see Linked Case <linkedCase>

    Examples: 
      | crestCaseNumber | linkedCase |
      | "T12345618"     | "T100"     |

  @CCS-519
  Scenario Outline: 18 Add non-existent linked case
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Add Linked Case" button
    And I enter Linked Case <linkedCase>
    Then I click "Save Linked Case Button" button
    Then I see save note error message "Case T10aaa0 does not exist"
    When I view <crestCaseNumber> in View Case page
    And I can not see linked case <linkedCase>

    Examples: 
      | crestCaseNumber | linkedCase |
      | "T12345619"     | "T10aaa0"  |

  @CCS-520
  Scenario Outline: 20 Add Non-availability date to existing case
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Add Non Available Date" button
    And I enter reason <reason> with start date <startDate> and end date <endDate>
    Then I click "Save Non Available Date Button" button
    And I find reason <reason>
    When I view <crestCaseNumber> in View Case page
    Then I see reason <reason>

    Examples: 
      | crestCaseNumber | reason                   | startDate | endDate     |
      | "T12345621"     | "This is sample reason." | "today"   | "today + 7" |

  @CCS-520
  Scenario Outline: 21 Add Non-availability date to existing case Cannot exceed character limit in non availability text
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Add Non Available Date" button
    And I enter reason <reason> with start date <startDate> and end date <endDate>
    Then I see "saveNonAvailableDateButton" button is disabled
    And I see save non availability error message "can not exceeded maximum field length"

    Examples: 
      | crestCaseNumber | reason                                                                                                                   | startDate | endDate     |
      | "T12345622"     | "This is sample reason that is going to have more than one hundred elements, so in that way, I can check if it is fine." | "today"   | "today + 7" |

  @CCS-520
  Scenario Outline: 22  Cannot save Non-availability date without mandatory fields
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    When I click "Add Non Available Date" button
    And I enter reason <reason1> with start date <startDate1> and end date <endDate1>
    Then I see "saveNonAvailableDateButton" button is disabled

    Examples: 
      | crestCaseNumber | reason1                 | startDate1 | endDate1    |
      | "T12345623"     | "This is sample reason" | "today"    | ""          |
      | "T12345624"     | ""                      | "today"    | "today + 7" |
      | "T12345625"     | "This is sample reason" | ""         | "today"     |

  @CCS-524
  Scenario Outline: 23  Delete relationship with other case
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Delete" linkedCase button for <linkedCase1> in Create Case page
    And I click Yes button for confirming "Delete linked case"
    And I can not see linked case <linkedCase1>
    When I view <crestCaseNumber> in View Case page
    Then I can not see linked case <linkedCase1>

    Examples: 
      | crestCaseNumber | linkedCase1 |
      | "T12345626"     | "T100"      |

  @CCS-524
  Scenario Outline: 24 Cancel Delete linked case
    When I enter <crestCaseNumber> in "crestCaseNumber"
    And I click "getCaseDetailsButton"
    And I click "Delete" linkedCase button for <linkedCase1> in Create Case page
    And I click No button for confirming "Delete linked case"
    Then I see Linked Case <linkedCase1>
    When I view <crestCaseNumber> in View Case page
    Then I see Linked Case <linkedCase1>

    Examples: 
      | crestCaseNumber | linkedCase1 |
      | "T12345627"     | "T100"      |
