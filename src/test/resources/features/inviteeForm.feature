#Feature: Invitee Form sidebar
#Scenario: Close sidebar
#   Given We are viewing an invitee sidebar
#    When We click on the x button in top right corner.
#    Then We should return to the calendar.
    
#Scenario: View Options
#    Given We are viewing an invitee sidebar
#    When We click a preference dropdown.
#    Then We should see all possible dates for the event
#    And We should see an "Unavailable" option
    
    
#Scenario: Assigning priorities (all goes well)
#   Given We are viewing an invitee sidebar
#    When We click a preference dropdown
#    And We click on a preference
#    Then assigned should update with the choice of preference
    
        
#Scenario: Assigning priorities (already assigned)
#    Given We are viewing an invitee sidebar
#    When We click a preference dropdown.
#    And We click on a preference
#    And that preference has been assigned already
#    Then we should get an alert
#    And Assigned should not update
    
#Scenario: Assigning priorities (deselect)
#    Given We are viewing an invitee sidebar
#    When We click a preference dropdown.
#    And We click on a preference
#    And that preference has been assigned already
#    And our click matches the assignment
#    Then Assigned should update to unavailable
    
#Scenario: Unavailable all
#    Given We are viewing an invitee sidebar
#    When We click the Unavailable button
#    Then the form should submit unavailable to db
    
#Scenario: Finalize
#   Given We are viewing an invitee sidebar
#   When We click the Finalize button
#   Then the form should submit Assigned preferences to db

    