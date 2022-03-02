package com.csci310.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
	private static final String BASE_URL = "http://localhost:8080/";
	public static WebDriver driver;
	private boolean ended = false;

	@Before
	public void setupTest() {
		System.out.println("Setting Up Cucumber Driver");
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--whitelisted-ips");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-extensions");
		driver = new ChromeDriver(options);
	}

	@Given("We are on the login page.")
	public void we_are_on_the_login_page() {
		driver.get(BASE_URL);
	}

	@When("We click on the {string} button.")
	public void we_click_on_the_button(String string) {
		WebElement button = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div/div[3]/div/div"));
		button.click();
	}

	@Then("We should see the {string} form.")
	public void we_should_see_the_form(String string) {
		WebElement formTitle = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/h1"));
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOf(formTitle));
		assertEquals(string, formTitle.getText());
	}

	@When("We click \"Cancel\".")
	public void we_click_cancel() {
		WebElement cancel = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div/div[4]/button[2]"));
		cancel.click();
	}

	@When("We enter {string} into the Username input.")
	public void we_enter_into_the_username_input(String string) {
		WebElement usernameInput = driver.findElement(By.xpath("//*[@id=\"username\"]"));
		usernameInput.sendKeys(string);
	}

	@When("We enter {string} into the Password input.")
	public void we_enter_into_the_password_input(String string) {
		WebElement passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"));
		passwordInput.sendKeys(string);
	}

	@When("We enter {string} into the Confirm Password input.")
	public void we_enter_into_the_confirm_password_input(String string) {
		WebElement passwordInput = driver.findElement(By.xpath("//*[@id=\"confirm-password\"]"));
		passwordInput.sendKeys(string);
	}

	@When("We click \"Create User\".")
	public void we_click() {
		WebElement createUser = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div/div[4]/button[1]"));
		createUser.click();
	}

	@Then("We should be sent back to the login page.")
	public void we_should_be_sent_back_to_the_login_page() {
		WebElement signInButton = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div/div/form/div/div[3]/button")));
		assertEquals("Sign In", signInButton.getText());
	}

	@When("We click \"Sign In\".")
	public void we_click_sign_in() {
		WebElement createUser = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div/div[3]/button"));
		createUser.click();
	}

	@Then("We should see the user's home page, which includes a Navbar.")
	public void we_should_see_the_user_s_home_page() {
		WebElement navbar = (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"navbar\"]")));
		assertNotNull(navbar);
	}

	@When("We are logged in on the home page.")
	public void we_are_logged_in_on_the_home_page() {
		WebElement usernameInput = driver.findElement(By.xpath("//*[@id=\"username\"]"));
		usernameInput.sendKeys("username");
		WebElement passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"));
		passwordInput.sendKeys("password");
		WebElement createUser = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div/div[3]/button"));
		createUser.click();
	}

	@When("We click \"Logout\".")
	public void we_click_logout() {
		WebElement logoutButton = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"navbar\"]/nav/div/div/div[1]/div[2]/div/button[2]")));
		logoutButton.click();
	}

	@Given("We are viewing an invitee sidebar")
	public void we_are_viewing_invitee_sidebar() {
		// create/delete user each test
		driver.get("http://localhost:8080/api/user/deleteByUsername/jacub");
		driver.get(BASE_URL);

		// on login page
		WebElement usernameInput = new WebDriverWait(driver, 10)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"username\"]")));
		usernameInput.sendKeys("username");
		WebElement passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"));
		passwordInput.sendKeys("password");
		WebElement createUser = driver.findElement(By.id("sign_in_button"));
		createUser.click();
		WebElement date = new WebDriverWait(driver, 10)
				.until(ExpectedConditions.elementToBeClickable(By.name("cal20")));
		date.click();
	}

	@When("We click an availability dropdown")
	public void we_click_an_preference_dropdown() {
		WebElement button = driver.findElement(By.id("Availability"));
		button.click();
	}

	@When("We click on an availability")
	public void we_click_on_an_preference() {
		WebElement button = new WebDriverWait(driver, 10)
				.until(ExpectedConditions.elementToBeClickable(By.id("avail_1")));
		button.click();
	}

	@Then("Assigned availability should update")
	public void assigned_avail_should_update() {
		WebElement dropdown = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("Availability")));
		String d_text = dropdown.getText();
		dropdown.click();
		WebElement button = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("avail_1")));
		String b_text = button.getText();
		String preference_text = d_text + ":" + b_text;
		WebElement label = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("avail_assigned_label")));
		WebElement picked_pref = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("avail_assigned")));
		String displayed = label.getText() + picked_pref.getText();
		assertEquals(preference_text, displayed);
	}

	@When("We click an excitement dropdown")
	public void we_click_an_excitement_dropdown() {
		WebElement button = driver.findElement(By.id("Excitement"));
		button.click();
	}

	@When("We click on an excitement")
	public void we_click_on_an_excitement() {
		WebElement button = new WebDriverWait(driver, 10)
				.until(ExpectedConditions.elementToBeClickable(By.id("excite_1")));
		button.click();
	}

	@Then("Assigned excitement should update")
	public void assigned_excitement_should_update() {
		WebElement dropdown = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("Excitement")));
		String d_text = dropdown.getText();
		dropdown.click();
		WebElement button = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("excite_1")));
		String b_text = button.getText();
		String preference_text = d_text + ":" + b_text;
		WebElement label = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("excite_assigned_label")));
		WebElement picked_pref = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("excite_assigned")));
		String displayed = label.getText() + picked_pref.getText();
		assertEquals(preference_text, displayed);
	}

	@When("We click the Unavailable button")
	public void we_click_unavailable_button() {
		WebElement button = new WebDriverWait(driver, 10)
				.until(ExpectedConditions.elementToBeClickable(By.id("unavailable")));
		button.click();
	}

	@Then("Update all preferences to unavailable")
	public void all_preferences_unavailable() {
		WebElement picked_pref = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("avail_assigned")));
		String availability = picked_pref.getText();
		picked_pref = new WebDriverWait(driver, 5)
				.until(ExpectedConditions.elementToBeClickable(By.id("excite_assigned")));
		String excitement = picked_pref.getText();
		assertEquals(availability, "No");
		assertEquals(excitement, "0");
	}

	@Then("We see invited")
	public void we_see_invited() {
		WebElement invited = new WebDriverWait(driver, 10)
				.until(ExpectedConditions.elementToBeClickable(By.name("invited_members")));
		String members = invited.getText();
		assertNotEquals(members.length(), 0);
	}

	@When("We click exit")
	public void we_click_exit() {
		WebElement button = new WebDriverWait(driver, 10)
				.until(ExpectedConditions.elementToBeClickable(By.name("exit")));
		button.click();
	}

	@Then("We return to calendar")
	public void return_to_calendar() {
		WebElement logo = driver.findElement(By.name("logo_text"));
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOf(logo));
		assertEquals("calendar", logo.getText());
		ended = true;
	}

	@When("We click Create Event.")
	public void we_click_create_event() {
		WebElement createEventButton = driver
				.findElement(By.xpath("//*[@id=\"navbar\"]/nav/div/div/div[1]/div[2]/div/button[1]"));
		createEventButton.click();
	}

	@Then("We will be on the Create Event Page.")
	public void we_will_be_on_the_create_event_page() {
		WebElement createEventPage = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/h1"));
		String searchEventText = createEventPage.getText();
		assertEquals("Create Event", searchEventText);
	}

	@When("We are on the Create Event Page.")
	public void we_are_on_the_create_event_page() {
		WebElement createEventPage = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/h1"));
		String searchEventText = createEventPage.getText();
		assertEquals("Create Event", searchEventText);
	}

	@When("We enter Los Angeles for the city field.")
	public void we_enter_los_angeles_in_the_city_field() {
		WebElement cityField = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/form/div/div[1]/input[1]"));
		cityField.sendKeys("Los Angeles");
	}

	@When("We enter {int} for the postal code field.")
	public void we_enter_90007_in_the_postal_code_field(Integer int1) {
		WebElement postalField = driver.findElement(By.xpath("//*[@id=\"postalcode\"]"));
		postalField.sendKeys(Integer.toString(int1));
	}

	@When("We select {int}:{int} PM for the time field.")
	public void we_enter_3_34_PM_in_the_time_code_field(Integer int1, Integer int2) {
		WebElement timeField = driver.findElement(By.xpath("//*[@id=\"distance\"]"));
		String rep = Integer.toString(int1) + ":" + Integer.toString(int2) + " PM";
		timeField.sendKeys(rep);
	}

	@When("We click the Search Events button.")
	public void we_click_the_search_events_button() {
		WebElement searchEventButton = driver
				.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/form/div/div[1]/button"));
		searchEventButton.click();
		searchEventButton.click();
	}

	@Then("We will be displayed searched Events.")
	public void we_will_be_displayed_search_events() {
		WebElement eventOne = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/form/div/div[2]/div[1]"));
		assertTrue(eventOne != null);
	}

	@When("We click the first event.")
	public void we_click_the_first_event() {
		WebElement eventOne = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/form/div/div[2]/div[1]"));
		eventOne.click();
	}

	@When("We enter in the username username in the Add User field.")
	public void we_enter_in_the_username_in_the_add_user_field() {
		WebElement addUserField = driver.findElement(By.xpath("//*[@id=\"username\"]"));
		addUserField.sendKeys("username");
	}

	@When("We press Add User.")
	public void we_press_add_user() {
		WebElement addUserButton = driver
				.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/form/div/div[3]/button"));
		addUserButton.click();
	}

	@Then("We will have added username to the invitee list.")
	public void we_will_have_added_username_to_the_invitee_list() {
		WebElement userSpan = driver.findElement(By.xpath("//*[@id=\"user\"]"));
		assertEquals(userSpan.getText(), "User \"username\" has been invited.");
	}

	@When("We press Create Event.")
	public void we_click_create_event_button() {
		WebElement createEventButton = driver
				.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/form/div/div[4]/button[1]"));
		createEventButton.click();
	}

	@Then("We will have created an event.")
	public void we_will_have_created_an_event() {
		WebElement calendar = driver.findElement(By.xpath("//*[@id=\"logo\"]/span[2]"));
		assertEquals(calendar.getText(), "calendar");
	}

	@When("We select the checkbox to show not responded events.")
	public void we_select_the_checkbox_to_show_not_responded_events() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}

	@Then("only finalized events will be present on the calendar.")
	public void only_finalized_events_will_be_present_on_the_calendar() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}

	@When("We select the checkbox to show responded events.")
	public void we_select_the_checkbox_to_show_responded_events() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}

	@When("We select the checkbox to show not finalized events.")
	public void we_select_the_checkbox_to_show_not_finalized_events() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}

	@When("We select the checkbox to show finalized events.")
	public void we_select_the_checkbox_to_show_finalized_events() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}

	@When("We click Reset.")
	public void we_click_reset() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}

	@Then("the events will be refreshed.")
	public void the_events_will_be_refreshed() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}

	@After
	public void after() {
		// clean up
		if (ended) {
			driver.get("http://localhost:8080/api/user/deleteByUsername/username");
			driver.get("http://localhost:8080/api/user/deleteByUsername/jacub");
		}
		System.out.println("Closing the Cucumber Driver");
		driver.quit();
	}
}