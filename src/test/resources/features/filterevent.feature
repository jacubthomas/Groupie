#Feature: Filter Events
#    Scenario: Filter events by finalized
#        Given We are on the login page.
#        When We are logged in on the home page.
#        And We select the checkbox to show finalized events.
#        Then only finalized events will be present on the calendar.
#    Scenario: Filter events by not finalized
#        Given We are on the login page.
#        When We are logged in on the home page.
#        And We select the checkbox to show not finalized events.
#        Then only finalized events will be present on the calendar.
#    Scenario: Filter events by responded
#        Given We are on the login page.
#        When We are logged in on the home page.
#        And We select the checkbox to show responded events.
#        Then only finalized events will be present on the calendar.
#    Scenario: Filter events by not responded    
#        Given We are on the login page.
#        When We are logged in on the home page.
#        And We select the checkbox to show not responded events.
#        Then only finalized events will be present on the calendar.
#    Scenario: Reset filtered events
#        Given We are on the login page.
#        When We are logged in on the home page.
#        And We select the checkbox to show not responded events.
#        And We click Reset.
#        Then the events will be refreshed.