@Ignore
@CCS-133

Feature: User Login
  
  @CCS-387
  Scenario: 1  Successful Logout
	Given I am logged in as ListingOfficer
	When I click the "logoutID" logout button
	Then I should be logout from appliction

  Scenario: 1 invalid password
 	Given I can see the log in page
    When I enter "test" value in "callback_0" input
    And I enter "invlid" value in "callback_1" input
    And I click on "callback_2" button by name
    Then I can see invalid message "combination is invalid"
    And I am not authenticated
    
   Scenario: 2 invalid user
 	Given I can see the log in page
    When I enter "invalid" value in "callback_0" input
    And I enter "password" value in "callback_1" input
    And I click on "callback_2" button by name
    Then I can see invalid message "combination is invalid"
 	And I am not authenticated
 
   Scenario: 3 Successful Login
 	Given I can see the log in page
    When I enter "test" value in "callback_0" input
    And I enter "password" value in "callback_1" input
    And I click on "callback_2" button by name
    Then I am authenticated