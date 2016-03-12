@CCS-326 @groupone
Feature: Load Crest Data
  As a Listing Officer
  I want to upload data from Crest
  So that the system data is up to date

  Background: 
    Given I go to listing admin

  Scenario Outline: 1 Upload valid file displays message that File uploaded successfully
    Given File <FileName> exits and satisfies the condition <Condition>
    When I enter Upload path for valid file <FileName> and click Upload button
    And I wait for the Upload button to get Enabled
    And I see Upload success message <Message>

    Examples: 
      | FileName                                     | Message                       | Condition                                       |
      | "404.CrestData-Valid.zip"                    | "File uploaded successfully." | "Valid zip file with . after Court Centre code" |
      | "404.CrestData-Valid-ZIP-No-Ext"             | "File uploaded successfully." | "Valid zip file with no file extension."        |
      | "404.CREST-DATA-VALID-TICKET-REQ-EMPTY.zip"  | "File uploaded successfully." | "Valid zip file with no ticketing REQ"          |
      | "404.CREST-DATA-VALID-TICKET-TYPE-EMPTY.zip" | "File uploaded successfully." | "Valid zip file with no ticketing Type"         |

  Scenario Outline: 2 Reject invalid format, headers, separators
    Given File <FileName> exits and satisfies the condition <Condition>
    When I enter Upload path for invalid file <FileName> and click Upload button
    And I wait for the Upload button to get Enabled
    And I see Upload error message <UploadErrorMessage>

    Examples: 
      | FileName                                                                                            | UploadErrorMessage                                             | Condition                                                          |
      | "404.CREST-DATA-INVALID-MISS-MANDATORY-CASENO_3.zip"                                                | "Bad Request: File upload fail, error at case line 2"          | "Missing mandatory field CASENO_3"                                 |
      | "404.CREST-DATA-INVALID-MISS-MANDATORY-CASETITLE_4.zip"                                             | "Bad Request: File upload fail, error at case line 2"          | "Missing mandatory field CASETITLE_4"                              |
      | "404.CREST-DATA-INVALID-MISS-MANDATORY-NODEF_5.zip"                                                 | "Bad Request: File upload fail, error at case line 2"          | "Missing mandatory field NODEF_5"                                  |
      | "404.CREST-DATA-INVALID-NODEF-WRONG-TYPE_7.zip"                                                     | "Bad Request: File upload fail, error at case line 2"          | "No of defendants is of wrong type: Is character and not a number" |
      | "404.CREST-DATA-INVALID-OFFCLASS-WRONG-TYPE_8.zip"                                                  | "Bad Request: File upload fail, error at case line 2"          | "Offencle class is of wrong type: Is character and not a number"   |
      | "404CREST-DATA-INVALID-BLANK-HEADER-LINE-CASE_9.zip"                                                | "Bad Request: Invalid file"                                    | "Header is blank line"                                             |
      | "404CREST-DATA-INVALID-EMPTY_CASE_10.zip"                                                           | "Bad Request: Invalid file"                                    | "Empty file"                                                       |
      | "404CREST-DATA-INVALID-HEADER-FIELD-MISSING-CASE_11.zip"                                            | "Bad Request: Invalid file"                                    | "One of the header fields is missing"                              |
      | "404CREST-DATA-INVALID-MISSING-HEADER-LINE-CASE_12.zip"                                             | "Bad Request: Invalid file"                                    | "No Header line"                                                   |
      | "404CREST-DATA-INVALID-WRONG-SEPERATOR-CASE_13.zip"                                                 | "Bad Request: File upload fail, error at case line 2"          | "Separator is , instead of pipe "                                  |
      | "405-CREST-DATA-INVALID-WRONG-CC-CODE_14.zip"                                                       | "File name must start with court centre code"                  | "Wrong court centre code"                                          |
      # Commenting rows which as are not used by CCS tool.
      # | "404.CREST-DATA-INVALID-LENGTH_P_08A_NPre-LENGTH_UNIT_P_08B_Pre_15.txt"                               | "Bad Request: File upload fail, error at line 2"               | "Field not present: LENGTH_P_08A"                                  |
      # | "404.CREST-DATA-INVALID-LENGTH_P_08A_Pre-LENGTH_UNIT_P_08B_NPre_16.zip"                               | "Bad Request: File upload fail, error at line 2"               | "Field not present: LENGTH_UNIT_P_08B"                             |
      | "404.CREST-DATA-INVALID-LENGTH_P_08C_NPre-LENGTH_UNIT_P_08D_Pre_17.zip"                             | "Bad Request: File upload fail, error at case line 2"          | "Field not present: LENGTH_P_08C"                                  |
      | "404.CREST-DATA-INVALID-LENGTH_P_08C_Pre-LENGTH_UNIT_P_08D_NPre_18.zip"                             | "Bad Request: File upload fail, error at case line 2"          | "Field not present: LENGTH_UNIT_P_08D"                             |
      | "404.CREST-DATA-INVALID-NON_START_11_NPre-NON_END_16A_Pre-NON_REASON_16B_Pre_19.zip"                | "Bad Request: File upload fail, error at non available line 2" | "Field not present: NON_START_11"                                  |
      | "404.CREST-DATA-INVALID-NON_START_11_Pre-NON_END_16A_NPre-NON_REASON_16B_Pre_20.zip"                | "Bad Request: File upload fail, error at non available line 2" | "Field not present: NON_END_16A, NON_REASON_16B"                   |
      | "404.CREST-DATA-INVALID-NON_START_11_Pre-NON_END_16A_Pre-NON_REASON_16B_NPre_21.zip"                | "Bad Request: File upload fail, error at non available line 2" | "Field not present: NON_REASON_16B"                                |
      # Commenting rows which as are not used by CCS tool.
      # | "404.CREST-DATA-INVALID-NOTE_TYPE_12_Pre-NOTE_PRINT_15A_NPre-NOTE_DATE_15B-NPre-NOTE_15C_NPre_22.txt" | "Bad Request: File upload fail, error at line 2"               | "Field not present: NOTE_PRINT_15A, NOTE_DATE_15B, NOTE_15C"       |
      # | "404.CREST-DATA-INVALID-NOTE_TYPE_12_NPre-NOTE_PRINT_15A_Pre-NOTE_DATE_15B_NPre-NOTE_15C_NPre_23.txt" | "Bad Request: File upload fail, error at line 2"               | "Field not present: NOTE_TYPE_12, NOTE_DATE_15B, NOTE_15C"         |
      | "404.CREST-DATA-INVALID-NOTE_TYPE_12_Pre-NOTE_PRINT_15A_Pre-NOTE_DATE_15B_Pre-NOTE_15C_NPre_24.zip" | "Bad Request: File upload fail, error at notes line 2"         | "Field not present: NOTE_15C"                                      |
      | "404.CREST-DATA-INVALID-NOTE_TYPE_12_Pre-NOTE_PRINT_15A_Pre-NOTE_DATE_15B_NPre-NOTE_15C_Pre_25.zip" | "Bad Request: File upload fail, error at notes line 2"         | "Fields not present: NOTE_DATE_15B"                                |
      # Bail Custody status is not mandatory
      | "404.CREST-DATA-INVALID-SURNAME_11C_NPre-BC_STATUS_09_Pre-27.zip"                                   | "Bad Request: File upload fail, error at defendant line 3"     | "Field not present: SURNAME_11C"                                   |
      | "404CREST-DATA-INVALID-HEADER-FIELD-MISSING-DEFENDANT_29.zip"                                       | "Bad Request: Invalid file"                                    | "Header field missing for defendant"                               |

  # Offence class is no longer mandatory for all case types. This field's data was missing in the original sample file received.
  # | "404.CREST-DATA-INVALID-MISS-MANDATORY-OFFCLASS_6.txt"                                                | "Bad Request: File upload fail, error at line 2"               | "Missing mandatory field OFFCLASS_6"                               |
  Scenario Outline: 4 Confirm file processed and record created
    Given case <CaseNo> does not exist
    When I enter Upload path for valid file <FileName> and click Upload button
    And I wait for the Upload button to get Enabled
    Then I see Upload success message "File uploaded successfully."
    And I see File Upload Status for <FileName> is "Processing"
    When I click on "Refresh Upload Status" till Status is "Processed" for <FileName>
    And I view <CaseNo> in View Case page
    Then I should see the case with details <CaseNo> , <LeadDefendant> , <NoOfDefendants> , <Offence> , <OffenceClass> , <TrialEstimate> , <EstimateUnit> , <ReleaseDecisionStatus> , <TicketingRequirement> , <SentDate> , <CommittalDate> displayed on the page
    And I should see 2 Notes
    And I should see 2 Linked cases
    And I should see 2 Defendants
    And I should see 2 Hearings
    And I should see Note with Date <NoteCreationDate> , Content <Note1> and Diary date <NoteDiaryDate1>
    And I should see Note with Date <NoteCreationDate> , Content <Note2> and Diary date <NoteDiaryDate2>
    And I should see following Linked cases
      | <LinkedCase1> |
      | <LinkedCase2> |
    And I should see Non Availability data <NonAvailabilityStartDate> , <NonAvailabilityEndDate> , <NonAvailabilityReason>
    And I should see Defendant with details <Def1Forename1> , <Def1Forename2> , <Def1Surname> , <URN> , <BailCustodyStatus1>
    And I should see Defendant with details <Def2Forename1> , <Def2Forename2> , <Def2Surname> , <URN> , <BailCustodyStatus2>
    And I should see PCMH Hearing with date <PCMHDate1>
    And I should see PCMH Hearing with date <PCMHDate2>

    Examples: 
      | FileName                           | CaseNo      | LeadDefendant | NoOfDefendants | Offence                   | OffenceClass | PCMHDate1    | PCMHDate2    | TrialEstimate | EstimateUnit | NonAvailabilityStartDate | NonAvailabilityEndDate | NonAvailabilityReason   | NoteCreationDate | Note1                 | NoteDiaryDate1 | Note2                 | NoteDiaryDate2 | ReleaseDecisionStatus | TicketingRequirement | LinkedCase1 | LinkedCase2 | Def1Forename1 | Def1Forename2 | Def1Surname | Def2Forename1 | Def2Forename2 | Def2Surname | CTLExpiryDate1 | BailCustodyStatus1 | CTLExpiryDate2 | BailCustodyStatus2 | URN           | SentDate     | CommittalDate |
      | "404--CREST-DATA-VALID-CREATE.zip" | "T99999999" | "JOHNY JOHNY" | "5"            | "ATE SUGAR AND DENIED IT" | "2"          | "02/07/2018" | "03/07/2018" | "2"           | "Days"       | "04/06/2018"             | "20/07/2018"           | "NON AVAILABILITY DAYS" | "22/02/2015"     | "CASE NOTE CONTENT 1" | "23/02/2015"   | "CASE NOTE CONTENT 2" | "24/02/2015"   | "Any Recorder"        | "Attempted Murder"   | T100        | T200        | "DEF 1 FOR 1" | "DEF 1 FOR 2" | "DEF 1 SUR" | "DEF 2 FOR 1" | "DEF 2 FOR 2" | "DEF 2 SUR" | "04/07/2018"   | "Custody"          | ""             | "Bail"             | "04RE1030218" | "2018-06-07" | "2018-06-08"  |

  Scenario Outline: 7 Confirm file processed, record updated
    Given case <CaseNo> does not exist
    When I enter Upload path for valid file <FileName1> and click Upload button
    And I wait for the Upload button to get Enabled
    Then I see Upload success message "File uploaded successfully."
    And I see File Upload Status for <FileName1> is "Processing"
    When I click on "Refresh Upload Status" till Status is "Processed" for <FileName1>
    And I view <CaseNo> in View Case page
    Then I should see case <CaseNo>
    Given I go to listing admin
    When I enter Upload path for valid file <FileName2> and click Upload button
    And I wait for the Upload button to get Enabled
    Then I see Upload success message "File uploaded successfully."
    And I see File Upload Status for <FileName2> is "Processing"
    When I click on "Refresh Upload Status" till Status is "Processed" for <FileName2>
    And I view <CaseNo> in View Case page
    Then I should see the case with details <CaseNo> , <LeadDefendant> , <NoOfDefendants> , <Offence> , <OffenceClass> , <TrialEstimate> , <EstimateUnit> , <ReleaseDecisionStatus> , <TicketingRequirement> , <SentDate> , <CommittalDate> displayed on the page
    And I should see 1 Notes
    And I should see 1 Linked cases
    And I should see 1 Defendants
    And I should see 1 Hearings
    And I should see Note with Date <NoteCreationDate> , Content <Note1> and Diary date <NoteDiaryDate1>
    And I should see following Linked cases
      | <LinkedCase1> |
    And I should see Non Availability data <NonAvailabilityStartDate> , <NonAvailabilityEndDate> , <NonAvailabilityReason>
    # Defendant file BailCustodyStatus (BC_STATUS_09) is empty and should default to Bail.
    And I should see Defendant with details <Def1Forename1> , <Def1Forename2> , <Def1Surname> , <URN> , <BailCustodyStatus1>
    And I should see PCMH Hearing with date <PCMHDate1>

    Examples: 
      | FileName1                           | FileName2                           | CaseNo      | LeadDefendant  | NoOfDefendants | Offence         | OffenceClass | PCMHDate1    | TrialEstimate | EstimateUnit | NonAvailabilityStartDate | NonAvailabilityEndDate | NonAvailabilityReason | NoteCreationDate | Note1               | NoteDiaryDate1 | Note2                 | NoteDiaryDate2 | ReleaseDecisionStatus | TicketingRequirement | LinkedCase1 | Def1Forename1 | Def1Forename2 | Def1Surname | Def2Forename1 | Def2Forename2 | Def2Surname | CTLExpiryDate1 | BailCustodyStatus1 | CTLExpiryDate2 | BailCustodyStatus2 | URN           | SentDate     | CommittalDate |
      | "404--CREST-DATA-VALID-UPDATE1.zip" | "404--CREST-DATA-VALID-UPDATE2.zip" | "T99999998" | "JOHNY WALKER" | "20"           | "DRUNKEN DRIVE" | "1"          | "01/07/2018" | "4"           | "Weeks"      | "05/06/2018"             | "21/07/2018"           | "UPDATED"             | "23/02/2015"     | "UPDATED CONTENT 1" | "24/02/2015"   | "CASE NOTE CONTENT 2" | "24/02/2015"   | "Any Recorder"        | "None"               | T100        | "DEF 1 FOR 1" | "DEF 1 FOR 2" | "DEF 1 SUR" | "DEF 2 FOR 1" | "DEF 2 FOR 2" | "DEF 2 SUR" | "04/07/2018"   | "Bail"             | ""             | "Bail"             | "04RE1030218" | "2018-06-06" | "2018-06-06"  |

  Scenario Outline: 8 Confirm file processed, case closed
    Given case <CaseNo> does not exist
    When I enter Upload path for valid file <FileName1> and click Upload button
    And I wait for the Upload button to get Enabled
    Then I see Upload success message "File uploaded successfully."
    And I see File Upload Status for <FileName1> is "Processing"
    When I click on "Refresh Upload Status" till Status is "Processed" for <FileName1>
    And I view <CaseNo> in View Case page
    Then I should see case <CaseNo>
    Given I go to listing admin
    When I enter Upload path for valid file <FileName2> and click Upload button
    And I wait for the Upload button to get Enabled
    Then I see Upload success message "File uploaded successfully."
    And I see File Upload Status for <FileName2> is "Processing"
    When I click on "Refresh Upload Status" till Status is "Processed" for <FileName2>
    And I view <CaseNo> in View Case page
    Then I should see the case with details <CaseNo> , <LeadDefendant> , <NoOfDefendants> , <Offence> , <OffenceClass> , <TrialEstimate> , <EstimateUnit> , <ReleaseDecisionStatus> , <TicketingRequirement> , <SentDate> , <CommittalDate> displayed on the page
    And I should see Case <CaseNo> is closed

    Examples: 
      | FileName1                           | FileName2                           | CaseNo      | LeadDefendant  | NoOfDefendants | Offence         | OffenceClass | PCMHDate1    | TrialEstimate | EstimateUnit | NonAvailabilityStartDate | NonAvailabilityEndDate | NonAvailabilityReason | NoteCreationDate | Note1               | NoteDiaryDate1 | Note2                 | NoteDiaryDate2 | ReleaseDecisionStatus | TicketingRequirement | LinkedCase1 | Def1Forename1 | Def1Forename2 | Def1Surname | Def2Forename1 | Def2Forename2 | Def2Surname | CTLExpiryDate1 | BailCustodyStatus1 | CTLExpiryDate2 | BailCustodyStatus2 | URN           | SentDate     | CommittalDate |
      | "404--CREST-DATA-VALID-DELETE1.zip" | "404--CREST-DATA-VALID-DELETE2.zip" | "T99999997" | "JOHNY WALKER" | "20"           | "DRUNKEN DRIVE" | "1"          | "01/07/2018" | "4"           | "Weeks"      | "05/06/2018"             | "21/07/2018"           | "UPDATED"             | "23/02/2015"     | "UPDATED CONTENT 1" | "24/02/2015"   | "CASE NOTE CONTENT 2" | "24/02/2015"   | "Any Recorder"        | "Attempted Murder"   | T100        | "DEF 1 FOR 1" | "DEF 1 FOR 2" | "DEF 1 SUR" | "DEF 2 FOR 1" | "DEF 2 FOR 2" | "DEF 2 SUR" | "04/07/2018"   | "Custody"          | ""             | "Bail"             | "04RE1030218" | "2018-06-07" | "2018-06-08"  |

  Scenario Outline: 10 Reject file if case reference not found for defendant, notes, links, non availability, hearings
    Given case <CaseNoNotPresent> does not exist
    And Upload file <FileName> contains data to create valid case <CasePresent>
    And File for <WhatToCheck> has a non existant case <CaseNoNotPresent>
    When I enter Upload path for valid file <FileName> and click Upload button
    And I wait for the Upload button to get Enabled
    Then I see Upload success message "File uploaded successfully."
    When I click on "Refresh Upload Status" till Status is "Error" for <FileName>
    Then I see Processing message <ErrorMessage> in "processingStatusTable" for file <FileName>
    When I view <CasePresent> in View Case page
    Then I can see <CasePresent> in "crestCaseNumber"
    # 0 Defendants, Notes, Linked cases, Non Availability dates, Hearings means the whole defendant file is rejected.
    And I should see 0 <WhatToCheck>

    Examples: 
      | FileName                                                           | CaseNoNotPresent | CasePresent | ErrorMessage                                                                   | WhatToCheck              |
      | "404--CREST-DATA-PROCESS-FAIL-DEF-CASE-NOT-EXISTS_40.zip"          | "X99999999"      | "X99999990" | "File processing failed with following reason : Invalid defendants file"       | "Defendants"             |
      | "404--CREST-DATA-PROCESS-FAIL-NOTES-CASE-NOT-EXISTS_41.zip"        | "X99999999"      | "X99999990" | "File processing failed with following reason : Invalid notes file"            | "Notes"                  |
      | "404--CREST-DATA-PROCESS-FAIL-NONAVAIL-CASE-NOT-EXISTS_42.zip"     | "X99999999"      | "X99999990" | "File processing failed with following reason : Invalid non availability file" | "Non Availability Dates" |
      | "404--CREST-DATA-PROCESS-FAIL-PCM-CASE-NOT-EXISTS_43.zip"          | "X99999999"      | "X99999990" | "File processing failed with following reason : Invalid pcmh file"             | "Hearings"               |
      | "404--CREST-DATA-PROCESS-FAIL-LINKED-CASES-CASE-NOT-EXISTS_44.zip" | "X99999999"      | "X99999990" | "File processing failed with following reason : Invalid linked case file"      | "Linked Cases"           |
