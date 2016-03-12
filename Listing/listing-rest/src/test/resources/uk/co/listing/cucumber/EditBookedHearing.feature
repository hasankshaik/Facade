@CCS-382
@groupone
Feature: Edit hearing from a slot
  As a listing officer
  I want to relist a hearing from a slot
  So that booking can be moved into different slot in the planner
  
  Scenario Outline: 1 Edit only allowed fields  from the Planner
    Given The CourtRoom is booked for a judge for  a month
    And I can see a <Hearing> for <Case> with  type <BookingType> listed in <CourtRoom> with <TrialEstimate> starting today in planner page
    When I change startDate as next working date , <NewTrialEstimate> , <NewBookingType> and <AnotherCourtRoom> of  <Hearing> from Edit Hearing panel and relist
    Then I find <Hearing> for <Case> being relisted on <AnotherCourtRoom> with new start date and  <NewTrialEstimate> 

    Examples: 
      | Case   | Hearing        | BookingType   | CourtRoom   | AnotherCourtRoom | NewBookingType | TrialEstimate | NewTrialEstimate |
      | "T800" | "FirstHearing" | "Provisional" | "FirstRoom" | "SecondRoom"     | "Confirmed"    | 1             | 2                |
  
  @CCS-695 
  Scenario Outline: 2  Can edit a text annotation to a hearing from Planner page and 6 Cannot add a second annotation to a hearing
    Given The CourtRoom is booked for a judge for  a month
    And I can see a <Hearing> for <Case> with  type <BookingType> listed in <CourtRoom> with <TrialEstimate> starting today in planner page
    When I enter <Annotation> in Annotation field
    And I click "updateHearing" button to add Annotation
    Then I can see <Annotation> in Annotation field 
      
    Examples: 
      | Case   | Hearing        | BookingType   | CourtRoom   | Annotation  | NewBookingType | TrialEstimate |
      | "T800" | "FirstHearing" | "Provisional" | "FirstRoom" | "Annotation 1"     | "Confirmed"    | 1             |
     
 Scenario Outline: 3 Cannot exceed character limit for annotations
   	Given I am on Planner page
    And I can see and select <Hearing> for <Case> with  type <BookingType> listed in <CourtRoom> with in planner page
    When I enter <Annotation> in Annotation field
    Then I can see "updateHearing" button is disabled to update Annotation
      
    Examples: 
      | Case   | Hearing        | BookingType   | CourtRoom   | Annotation  | NewBookingType | TrialEstimate |
      | "T800" | "FirstHearing" | "Provisional" | "FirstRoom" | "Annotation is more than 50 char 123456789 123456789 123456789 123456789"     | "Confirmed"    | 1             |
  
  @CCS-580 @CCS-607
  Scenario Outline: 3 Cannot change date of scheduled hearing to be in the past
   	Given I am on Planner page
    And I can see and select <Hearing> for <Case> with  type <BookingType> listed in <CourtRoom> with in planner page
    Then I can see following Case Details in Schedule page
    	| crestCaseNumber    | T800 		  |
    	| leadDefendant      | Defendant T800 |
    	| mostSeriousOffence | most-serious   |
    	| trialEstimate      | 1			  |
    	
    When I change StartDate to CurrentDate minus one
    Then I can see text "Bad Request: Hearing cannot be listed in past"
    
    Examples: 
      | Case   | Hearing        | BookingType   | CourtRoom   | Annotation  | NewBookingType | TrialEstimate |
      | "T800" | "FirstHearing" | "Provisional" | "FirstRoom" | "Annotation 1"     | "Confirmed"    | 1             |
      
      
  @CCS-674
  Scenario Outline: 4  Enter non-working day for listed hearing
    Given I am on Planner page
    And I can see and select <Hearing> for <Case> with  type <BookingType> listed in <CourtRoom> with in planner page
    And I enter Date <TheDate> with pattern (dd/MM/yyyy) in "scheduledHearingStartDate" element
    And I click "updateHearing" button to add Annotation
    Then I can see text "StartDate is not a working day"
      
    Examples: 
      | Case   | Hearing        | BookingType   | CourtRoom   | TheDate          |  TrialEstimate |
      | "T800" | "FirstHearing" | "Provisional" | "FirstRoom" | "07/03/2020"     |  1             |
      | "T800" | "FirstHearing" | "Provisional" | "FirstRoom" | "08/03/2020"     |  1             |
  
  