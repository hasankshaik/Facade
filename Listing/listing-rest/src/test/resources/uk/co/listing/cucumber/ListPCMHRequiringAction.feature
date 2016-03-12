@CCS-141
@grouptwo
Feature: List PCM Hearings Requiring Action

As a Listing Officer
I want to view a list of Plea and Case Management Hearings which require action
So that I can determine what actions to take

    Background: 
        Given I am on Planner page
        Given following Cases exists with corresponding PCM date and PCM status
          | Crest Case Number | PCM Date    | PCM Status |
          | "T20131111"       | "Yesterday" | "Pending"  |
          | "T20141111"       | "Yesterday" | "Pending"  |
          | "T20151111"       | "Yesterday" | "Prepared" |
          | "T20132222"       | "Today"     | "Pending"  |
          | "T20142222"       | "Today"     | "Pending"  |
          | "T20152222"       | "Today"     | "Prepared" |
          | "T20143333"       | "Tomorrow"  | "Pending"  |
          | "T20153333"       | "Tomorrow"  | "Pending"  |

    Scenario: 1 I can see a list of tomorrow's PCM hearings
        Then I can see Manage PCMH element
        And I can see Case "T20143333" , "Prepared" button under "Tomorrow"
        And I can see Case "T20153333" , "Prepared" button under "Tomorrow"
        And I can not see following Cases under "Tomorrow"
          | "T20131111" |
          | "T20141111" |
          | "T20151111" |
          | "T20132222" |
          | "T20142222" |
          | "T20152222" | 
        
    Scenario: 2 I can see a list of today's PCM hearings
        Then I can see Manage PCMH element
        And I can see Case "T20132222" , "Done" button under "Today"
        And I can see Case "T20142222" , "Done" button under "Today"
        And I can see Case "T20152222" , "Done" button under "Today"
        And I can not see following Cases under "Today"
          | "T20131111" |
          | "T20141111" |
          | "T20151111" |
          | "T20143333" |
          | "T20153333" |
    
    Scenario: 3 I can see a list of past PCM hearings
        Then I can see Manage PCMH element
        And I can see Case "T20131111" , "Done" button under "Past"
        And I can see Case "T20141111" , "Done" button under "Past"
        And I can see Case "T20151111" , "Done" button under "Past"
        And I can not see following Cases under "Past"
          | "T20132222" |
          | "T20142222" |
          | "T20152222" |
          | "T20143333" |
          | "T20153333" |
             
    Scenario: 4 I can complete work for tomorrow's PCM hearings
        When I click button "Prepared" for case "T20143333" under "Tomorrow"
        Then I can see Case "T20153333" , "Prepared" button under "Tomorrow"
        # And Hearing Status of case "T20143333" is "Pending"
        And I can not see following Cases under "Tomorrow"
          | "T20143333" |
          | "T20131111" |
          | "T20141111" |
          | "T20151111" |
          | "T20132222" |
          | "T20142222" |
          | "T20152222" | 
          
    Scenario: 5  I can complete work for today's PCM hearings
        When I click button "Done" for case "T20132222" under "Today"
        And I click button "Done" for case "T20152222" under "Today"
        Then I can see Case "T20142222" , "Done" button under "Today"
        # And Hearing Status of case "T20132222" is "Complete"
        # And Hearing Status of case "T20152222" is "Complete"
        And I can not see following Cases under "Today"
          | "T20132222" |
          | "T20152222" |
          
    Scenario: 6  I can complete work for past PCM hearings
        When I click button "Done" for case "T20131111" under "Past"
        And I click button "Done" for case "T20151111" under "Past"
        Then I can see Case "T20141111" , "Done" button under "Past"
        # And Hearing Status of case "T20131111" is "Complete"
        # And Hearing Status of case "T20151111" is "Complete"
        And I can not see following Cases under "Today"
          | "T20131111" |
          | "T20151111" |   
    
    