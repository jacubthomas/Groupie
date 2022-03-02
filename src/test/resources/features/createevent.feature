#Feature: Event Creation Page
#    Scenario: We create a user.
#        Given We are on the login page.
#        When We click on the "Sign up" button.
#        And We enter "username" into the Username input.
#        And We enter "password" into the Password input.
#        And We enter "password" into the Confirm Password input.
#        And We click "Create User".
#        Then We should be sent back to the login page.
#    Scenario:  We go to the "Create Event" page from the calendar page. 
#        Given We are on the login page.
#        When We enter "admin" into the Username input.
#        And We enter "admin" into the Password input.
#        And We click "Sign In".
#        And We click Create Event.
#        Then We will be on the Create Event Page.
#    Scenario: We search for an event on the "Create Event" page. 
#        Given We are on the login page.
#        When We enter "admin" into the Username input.
#        And We enter "admin" into the Password input.
#        And We click "Sign In".
#        And We click Create Event.
#        And We are on the Create Event Page.
#        And We enter Los Angeles for the city field.
#        And We enter 90007 for the postal code field.
#        And We select 5:34 PM for the time field.
#        And We click the Search Events button.
#        Then We will be displayed searched Events.
#    Scenario: We invite users to the event  
#        Given We are on the login page.
#        When We enter "admin" into the Username input.
#        And We enter "admin" into the Password input.
#        And We click "Sign In".
#        And We click Create Event.
#        And We are on the Create Event Page.
#        And We enter in the username username in the Add User field.
#        And We press Add User.
#        Then We will have added username to the invitee list.
#    Scenario: We want to create an event
#        Given We are on the login page.
#        When We enter "admin" into the Username input.
#        And We enter "admin" into the Password input.
#        And We click "Sign In".
#        And We click Create Event.
#        And We are on the Create Event Page.
#        And We enter Los Angeles for the city field.
#        And We enter 90007 for the postal code field.
#        And We select 05:34 PM for the time field.
#        And We click the Search Events button.
#        And We click the first event.
#        And We enter in the username username in the Add User field.
#        And We press Add User.
#        And We press Create Event.
#        Then We will have created an event.
