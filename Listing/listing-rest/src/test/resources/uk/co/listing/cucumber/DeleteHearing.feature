@CCS-368
@groupone
Feature: Delete Hearing
  As a listing officer
  I want to delete a hearing
  So that I can record that the hearing is no longer required
  
  Background:
    Given I am logged in as Listing Officer
    And I am on the View Case page
    And I am in CrestCaseNumber "T700"
    
  Scenario: 1 Delete hearing which is not in planner
    Given CrestCaseNumber has Trial Hearing "DH1"
    And Trial Hearing "DH1" is not in planner
    When I click Delete button for hearing "DH1"
    Then I should see "Do you want to delete this hearing?"
    When I click Yes
    Then I can not see Trial Hearing "DH1" in View Case page
    
  Scenario: 2 Cancel deletion when asked to confirm
    Given CrestCaseNumber has Trial Hearing "DH2"
    And Trial Hearing "DH2" is not in planner
    When I click Delete button for hearing "DH2"
    Then I should see "Do you want to delete this hearing?"
    When I click No
    Then I can see Trial Hearing "DH2" in View Case page
    
  Scenario: 3 Delete hearing which is already in planner
    Given CrestCaseNumber has Trial Hearing "DH3"
    And I am on Planner page
    And Judge "UniqueJudge" is allocated to Court room "UniqueRoom" from "today" to "today + 10" days
    And Trial Hearing "DH3" is in planner starting on current date
    When I click Delete button for hearing "DH3"
    Then I should see message "This hearing is listed on "current date". Do you want to delete this hearing?"
    When I click Yes
    Then I can not see Trial Hearing "DH3" in View Case page
    And I can not see Trial Hearing "DH3" on Planner page for current date
    
  Scenario: 4 Cancel deletion of hearing which is in planner when asked to confirm
    Given CrestCaseNumber has Trial Hearing "DH4"
    And Trial Hearing "DH4" is in planner starting on current date
    When I click Delete button for hearing "DH4"
    Then I should see message "This hearing is listed on "current date". Do you want to delete this hearing?"
    When I click No
    Then I can see Trial Hearing "DH4" in View Case page
    And I can see Trial Hearing "DH4" on Planner page for current date
  