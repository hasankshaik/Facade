@CCS-581
@groupone
Feature: Display KPI date for trial
  As a Listing Officer
  I need to see the KPI date for trial
  So that I can make a decision as to when the case should be listed for trial
  
   Background: 
    Given I am logged in as Listing Officer

Scenario Outline: 1 Use sending date / committal date from Planner Page
    Given <Hearing> is for case <Case>
    And <Case> has CommittalDate <CommittalDate> 
    And <Case> has SendingDate <SendingDate>
    And I am on Planner page
    And I can see <Hearing> in HearingInformation element <Booking_Status> for <Case>
    Then I see Trial KPI Date: <Trial_KPI_Date> days in HearingInformation element
    
     Examples:
      | Hearing        | Case    | CommittalDate   | SendingDate       | Trial_KPI_Date | Booking_Status |
      | "Hearing581-1" | "T1004" | ""		       | "CurrentDate+2"   | 184            | "Unbooked"	 |
      | "Hearing581-2" | "T1005" | "CurrentDate"   | ""		           | 182            | "Unbooked"     |
      | "Hearing581-3" | "T1006" | "CurrentDate"   | "CurrentDate+2"   | 182            | "Unbooked"     |
      | "Hearing581-4" | "T1004" | ""		       | "CurrentDate+2"   | 184            | "Booked"		 |
      | "Hearing581-5" | "T1005" | "CurrentDate"   | ""		           | 182            | "Booked"       |
      | "Hearing581-6" | "T1006" | "CurrentDate"   | "CurrentDate+2"   | 182            | "Booked"       |
      
      
      
Scenario Outline: 2 Use sending date / committal date from Case Page
    Given I am on Case page
    And <Hearing> is for case <Case>
    And <Case> has CommittalDate <CommittalDate> 
    And <Case> has SendingDate <SendingDate>
    And I can see <Hearing> in List of Hearings Element
    Then I see Trial KPI Date: <Trial_KPI_Date> days for <Hearing> Element
    
     Examples:
      | Hearing        | Case    | CommittalDate   | SendingDate       | Trial_KPI_Date |
      | "Hearing581-1" | "T1004" | ""		       | "CurrentDate+2"   | 184            |
      | "Hearing581-2" | "T1005" | "CurrentDate"   | ""		           | 182            |
      | "Hearing581-3" | "T1006" | "CurrentDate"   | "CurrentDate+2"   | 182            |
      | "Hearing581-4" | "T1004" | ""		       | "CurrentDate+2"   | 184            |
      | "Hearing581-5" | "T1005" | "CurrentDate"   | ""		           | 182            |
      | "Hearing581-6" | "T1006" | "CurrentDate"   | "CurrentDate+2"   | 182            |