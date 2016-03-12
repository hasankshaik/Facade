@CCS-445
@groupone
Feature: Create Court Centre
  As a Regional Listing Coordinator
  I want to create a new court centre
  So that I can try out new ideas and demonstrate rollout to new court centres. Also so that developers not have to seed data into production

  Background: 
    Given I am logged in as a "Listing Officer"
    And I am in Admin Page
    And "Birmingham (404)" Court Centre Exists
  
  Scenario Outline: 1 Create New Court Centre
    And I enter <Court Centre Name> in "courtCentreNameInput" field
    And I enter <Code> in "courtCentreCode" field
    And I click "addCentreButton"
    Then I can see text <Message>
    And <Court Centre Name> Court Centre Exists

    Examples: 
      | Court Centre Name | Code   | Message |
      | "Leeds 2"         | "441"  | "Saved" |
      | "Manchester"      | "442"  | "Saved" |

  Scenario Outline: 2 Court Centre cannot already exist
    And I enter <Court Centre Name> in "courtCentreNameInput" field
    And I enter <Code> in "courtCentreCode" field
    And I click "addCentreButton"
    Then I can see text <Message>
    And <Court Centre Name> Court Centre Exists

    Examples: 
      | Court Centre Name | Code   | Message                                                                    |
      | "Birmingham"      | "404"  | "Bad Request: Court centre name Birmingham or code 404 already exists" |
  
  Scenario Outline: 2.1 Court Centre invalid name
    And I enter <Court Centre Name> in "courtCentreNameInput" field
    And I enter <Code> in "courtCentreCode" field
    Then I can see text <Message>

    Examples: 
      | Court Centre Name | Code   | Message                                 |
      | "Ru"              | "4453" | "Name should have at least 3 characters" |
      | "Ru^"             | "4453" | "Number should have 3 digits only"      |
