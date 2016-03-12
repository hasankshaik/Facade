@CCS-371
@grouptwo
Feature: Navigate to proposed slot
  As a Listing Officer
  I want to navigate to a proposed slot in the diary
  So that I can determine if the slot is suitable for a hearing
  
  Scenario Outline: 1 Navigate to slot for proposed hearing date
    Given I am on <Planner View> view on Planner page
    And court room "Court371" exists
    When I view Find Slot results for a hearing of estimate <Estimate>
    And I select Radio button for court room "Court371" where start date is <Number of Days> days from today
    And I click "navigateButton"
    Then I should see Planner is in <Planner View>
    And Planner date selected is <Number of Days> days from today
    
      Examples:
        | Planner View | Estimate | Number of Days |
        | "Week"       | 1        | 7              |
        
  @CCS-513
  Scenario Outline: 2 Book a hearing from search results
  Given I am on <Planner View> view on Planner page
  And court room "Court371" exists
  When I view Find Slot results for a hearing of estimate <Estimate>
  And I select Radio button for court room "Court371" where start date is <Number of Days> days from today
  And I select booking type as "Provisional"
  And I click List button
  Then I should see Planner is in <Planner View>
  And Planner date selected is <Number of Days> days from today
  And I can see the hearing listed from <Number of Days> days from today for <Estimate> days
    
    Examples:
      | Planner View | Estimate | Number of Days |
      | "Week"       | 10       | 1              |
