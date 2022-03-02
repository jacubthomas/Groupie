Feature: Login Page
	Scenario: We go to the "Sign up" page from the login page
	    Given We are on the login page.
	    When We click on the "Sign up" button.
	    Then We should see the "Sign Up" form.
	Scenario: We click "Cancel" from the Create User page.
		Given We are on the login page.
		When We click on the "Sign up" button.
		And We click "Cancel".
		Then We should be sent back to the login page.
	Scenario: We create a user.
	    Given We are on the login page.
	    When We click on the "Sign up" button.
	    And We enter "username" into the Username input.
	    And We enter "password" into the Password input.
	    And We enter "password" into the Confirm Password input.
	    And We click "Create User".
	    Then We should be sent back to the login page.
	Scenario: We log in.
	    Given We are on the login page.
	    When We enter "username" into the Username input.
	    And We enter "password" into the Password input.
	    And We click "Sign In".
	    Then We should see the user's home page, which includes a Navbar.
	Scenario: We log out using the Navbar.
	    Given We are on the login page.
	    When We are logged in on the home page.
	    And We click "Logout".
	    Then We should be sent back to the login page.
	Scenario: Assigning availability
	    Given We are viewing an invitee sidebar
	    When We click an availability dropdown
	    And We click on an availability
	    Then Assigned availability should update   
	Scenario: Assigning excitement
	    Given We are viewing an invitee sidebar
	    When We click an excitement dropdown
	    And We click on an excitement
	    Then Assigned excitement should update
	Scenario: Unavailable
	    Given We are viewing an invitee sidebar
	    When We click the Unavailable button
	    Then Update all preferences to unavailable
	Scenario: Invited members
		Given We are viewing an invitee sidebar
		Then We see invited
	#Scenario: Finalize
	#   Given We are viewing an invitee sidebar
	#   When We click the Finalize button
	#   Then the form should submit Assigned preferences to db
	Scenario: Close sidebar
		Given We are viewing an invitee sidebar
	    When We click exit
	    Then We return to calendar
