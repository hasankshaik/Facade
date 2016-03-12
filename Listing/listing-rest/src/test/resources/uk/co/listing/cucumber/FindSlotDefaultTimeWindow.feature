@CCS-739
@grouptwo
Feature: Find Slot - Default Time Window
  As a Listing Officer
  I want searching for a slot to use an appropriate default time window
  So that the dates rturned are suitable for the trial
  
  Scenario Outline: 1 Default dates for search - Single Defendant
    Given Case <CaseNo> has Sending date today <SendingDate> days
    And Case <CaseNo> has 1 Defendant
    And Defendant number 1 has CTL Expires On date: Sending date <DefendantCTLExpiryDate> days
    And KPI date is Sending date + 182 days
    And Case <CaseNo> has hearing <HearingName>
    When I go to "schedule" page for court centre "Centre739"
    And Judge is allocated to End date: Friday prior to <ListBeforeDate>
    And Judge is allocated to Start date: End date "-25" days
    And I select hearing <HearingName> from unlistedHearings
    And I press Get Available Slots Button 
    Then Search End date is Friday prior to <ListBeforeDate>
    And Search Start date is "Search End date -25 days"
    
    Examples:
      | CaseNo      | DefendantCTLExpiryDate | ListBeforeDate | HearingName | SendingDate |
      | "CCS-739-1" | ""                     | "KPI Date"     | "CCS-739-1" | -110 		  |
      | "CCS-739-2" | "+170"   				 | "CTL Date 1"   | "CCS-739-2" | -110		  | 
      | "CCS-739-3" | "+190"  				 | "KPI Date"     | "CCS-739-3" | -110 		  |
  
  Scenario Outline: 2 Default dates for search - Multiple Defendants
    Given Case <CaseNo> has Sending date today <SendingDate> days
    And Case <CaseNo> has 2 Defendants
    And Defendant number 1 has CTL Expires On date: Sending date <DefendantCTLExpiryDate1> days
    And Defendant number 2 has CTL Expires On date: Sending date <DefendantCTLExpiryDate2> days
    And KPI date is Sending date + 182 days
    And Case <CaseNo> has hearing <HearingName>
    When I go to "schedule" page for court centre "Centre739"
    And Judge is allocated to End date: Friday prior to <ListBeforeDate>
    And Judge is allocated to Start date: End date "-25" days
    And I select hearing <HearingName> from unlistedHearings
    And I press Get Available Slots Button
    Then Search End date is Friday prior to <ListBeforeDate>
    And Search Start date is "Search End date -25 days"
    
    Examples:
      | CaseNo       | HearingName  | SendingDate | DefendantCTLExpiryDate1 | DefendantCTLExpiryDate2 | ListBeforeDate |
      | "CCS-739-4"  | "CCS-739-4"  | -110        | ""                      | ""                      | "KPI Date"     |
      | "CCS-739-5"  | "CCS-739-5"  | -110        | "+170"    				| ""                      | "CTL Date 1"   |
      | "CCS-739-6"  | "CCS-739-6"  | -110        | "+190"     				| ""                      | "KPI Date"     |
      | "CCS-739-7"  | "CCS-739-7"  | -110        | ""                      | "+171"     			  | "CTL Date 2"   |
      | "CCS-739-8"  | "CCS-739-8"  | -110        | ""                      | "+189"     		      | "KPI Date"     |
      | "CCS-739-9"  | "CCS-739-9"  | -110        | "+172"     				| "+161"     			  | "CTL Date 2"   |
      | "CCS-739-10" | "CCS-739-10" | -110        | "+162"     				| "+169"     			  | "CTL Date 1"   |
      | "CCS-739-11" | "CCS-739-11" | -110        | "+170"     				| "+190"     			  | "CTL Date 1"   |
      | "CCS-739-12" | "CCS-739-12" | -110        | "+190"     				| "+170"     			  | "CTL Date 2"   |
      | "CCS-739-13" | "CCS-739-13" | -110        | "+190"     				| "+200"     			  | "KPI Date"     |
      | "CCS-739-14" | "CCS-739-14" | -110        | "+200"     				| "+190"     			  | "KPI Date"     |
  
  Scenario Outline: 3 Start date must be after the current date and PCMH date
    Given Case <CaseNo> has Sending date today <SendingDate> days
    And Case <CaseNo> has PCMH Date <PCMHDate>
    And Case <CaseNo> has 1 Defendant
    And Defendant number 1 has CTL Expires On date: Sending date <DefendantCTLExpiryDate> days
    And KPI date is Sending date + 182 days
    And Case <CaseNo> has hearing <HearingName>
    When I go to "schedule" page for court centre "Centre739"
    And Judge is allocated to End date: Friday prior to <ListBeforeDate>
    And Judge is allocated to Start date: <EarliestStartDate>
    And I select hearing <HearingName> from unlistedHearings
    And I press Get Available Slots Button
    Then Search End date is Friday prior to <ListBeforeDate>
    And Search Start date is <EarliestStartDate>
    
    Examples:
      | CaseNo       | HearingName  | SendingDate | DefendantCTLExpiryDate | ListBeforeDate | PCMHDate            | EarliestStartDate |
      | "CCS-739-15" | "CCS-739-15" | -160		  | ""                     | "KPI Date" 	| "SendingDate + 119" | "today + 1 day"   |
      | "CCS-739-16" | "CCS-739-16" | -160		  | ""                     | "KPI Date"     | "SendingDate + 165" | "PCMDate + 1 day" |
  
  Scenario Outline: 4 Search window cannot be before the current date or the PCMH date
    Given Case <CaseNo> has Sending date today <SendingDate> days
    And Case <CaseNo> has PCMH Date <PCMHDate>
    And Case <CaseNo> has 1 Defendant
    And Defendant number 1 has CTL Expires On date: Sending date <DefendantCTLExpiryDate> days
    And KPI date is Sending date + 182 days
    And Case <CaseNo> has hearing <HearingName>
    When I go to "schedule" page for court centre "Centre739"
    And Judge is allocated to End date: Friday prior to <ListBeforeDate>
    And Judge is allocated to Start date: End date "-25" days
    And I select hearing <HearingName> from unlistedHearings
    And I press Get Available Slots Button 
    Then Search End date is Friday prior to <ListBeforeDate>
    And Search Start date is "Search End date -25 days"
    
    Examples:
      | CaseNo       | HearingName  | SendingDate | DefendantCTLExpiryDate | ListBeforeDate 	| PCMHDate            |
      | "CCS-739-17" | "CCS-739-17" | -182		  | ""                     | "today + 35 day"	| "SendingDate + 119" |
      | "CCS-739-18" | "CCS-739-18" | -182		  | ""                     | "PCMDate + 35 day" | "SendingDate + 190" |
              
   