package com.csci310.spring;

import com.csci310.db.entities.*;
import com.csci310.ticket_master.SearchInformation;
import io.cucumber.java.bs.A;
import io.cucumber.spring.CucumberContextConfiguration;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.csci310.TestSetupUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = APIApplication.class)
class RESTControllerTest {

    private TestSetupUtils utils = new TestSetupUtils();
    private ArrayList<User> clean_up_users = new ArrayList<User>();
    private ArrayList<String> clean_up_proposals = new ArrayList<String>();
    @Autowired
    private RESTController controller;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void textMainApiEndpointContextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void testTestApi() throws Exception {
        String response = controller.testApi();
        assertThat(response).isEqualTo("This response serves as a test for our REST API.");
    }

    @Test
    public void testGreeting() throws Exception {
        String response = controller.greeting();
        assertThat(response).isEqualTo("Hello, World");
    }

    @Test
    public void testIsUsernameTaken() throws Exception{
        // username exist case
        User u = utils.makeUser();
        u.setUsername(u.getUsername());
        u.setId(u.getId());
        String username = u.getUsername();
        u.setConfirmPassword(u.getPassword());

        String name = u.getUsername();
        controller.createNewUser(u);
        u.setUsername(name);
        clean_up_users.add(u);
        ResponseEntity<Boolean> response = controller.isUsernameTaken(username);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertTrue(response.getBody());

        // username does not exist
        response = controller.isUsernameTaken("USERNAME DOES NOT EXIST");

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertFalse(response.getBody());

    }

    @Test
    public void testGetUserByUsername_usernameExists() throws Exception {
        User u = utils.makeUser();
        u.setUsername(u.getUsername());
        u.setId(u.getId());
        String username = u.getUsername();
        u.setConfirmPassword(u.getPassword());

        String name = u.getUsername();
        controller.createNewUser(u);
        u.setUsername(name);
        clean_up_users.add(u);
        ResponseEntity<String> response = controller.getUserByUsername(username);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testGetUserByUsername_usernameDoesNotExist() throws Exception {
        ResponseEntity<String> response = controller.getUserByUsername("syoshimoto");

        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assert.assertEquals("Username does not exist.", response.getBody());
    }

    @Test
    public void testCreateNewUser_good() throws Exception {
        User u = utils.makeUser();
        String username = u.getUsername();
        u.setPassword("testPassword");
        u.setConfirmPassword("testPassword");

        ResponseEntity<String> response = controller.createNewUser(u);
        u.setUsername(username);
        clean_up_users.add(u);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Successfully created user with username: " + username, response.getBody());
    }

    @Test
    public void testCreateNewUser_bad() throws Exception {
        User u = utils.makeUser();
        u.setPassword("password");
        u.setConfirmPassword("badPassword");
        String name = u.getUsername();
        ResponseEntity<String> response = controller.createNewUser(u);
        u.setUsername(name);
        clean_up_users.add(u);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("Passwords do not match.", response.getBody());
    }

    @Test
    public void testCheckPasswordsMatch_match() throws Exception {
        User u = new User("hello", "world");
        u.setConfirmPassword("world");
        ResponseEntity<String> response = controller.checkPasswordsMatch(u);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Passwords match.", response.getBody());
    }

    @Test
    public void testCheckPasswordsMatch_noMatch() throws Exception {
        User u = new User("hello", "world");
        u.setConfirmPassword("WORLD");
        ResponseEntity<String> response = controller.checkPasswordsMatch(u);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("Passwords do not match.", response.getBody());
    }

    @Test
    public void testAuthenticateUser_goodLogin() throws Exception {
        User u = utils.makeUser();
        String username = u.getUsername();
        u.setPassword("testPassword");
        u.setConfirmPassword("testPassword");

        // encrypted, need to decrypt to pass to authenticateUser
        controller.createNewUser(u);
        u.setUsername(username);
        clean_up_users.add(u);
        u.setPassword("testPassword");
        u.setConfirmPassword("testPassword");
        ResponseEntity<String> response = controller.authenticateUser(u);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Successfully authenticated user with username: " + username, response.getBody());
    }

    @Test
    public void testAuthenticateUser_badUsername() throws Exception {
        User u = utils.makeUser();
        String username = u.getUsername();
        String password = u.getPassword();
        u.setConfirmPassword(password);

        controller.createNewUser(u);
        u.setUsername(username);
        clean_up_users.add(u);
        User secondUser = new User("wrongUsername", password);
        secondUser.setConfirmPassword(password);
        ResponseEntity<String> response = controller.authenticateUser(secondUser);

        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assert.assertEquals("Incorrect username or password", response.getBody());
    }

    @Test
    public void testAuthenticateUser_badPassword() throws Exception {
        User u = utils.makeUser();
        String username = u.getUsername();

        controller.createNewUser(u);
        clean_up_users.add(u);
        User secondUser = new User(username, "wrongPassword");
        secondUser.setConfirmPassword("wrongPassword");
        ResponseEntity<String> response = controller.authenticateUser(secondUser);

        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assert.assertEquals("Incorrect username or password", response.getBody());
    }

    @Test
    public void testSearchEvent() {
        // test url and response code are correct
        // incorrect input case
        SearchInformation badInfo = new SearchInformation("a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "0", "10");
        try {
            assertThat(controller.searchEvent(badInfo).getStatusCodeValue()).isEqualTo(400);
            // correct input case
            SearchInformation info = new SearchInformation("Los Angeles", "90015", "1000", "miles", "2021-10-04", "2021-12-03", "17:00:00", "02:30:00","rock", "music", "0", "10");
            int responseCode = controller.searchEvent(info).getStatusCodeValue();
            assertTrue(responseCode == 200 || responseCode == 429);
        }
        catch (IOException e){
            // http 429, need to wait some time to request again
        }

    }

    @Test
    public void testCreateProposal() throws Exception{
        // create proposal
        // set up proposer and invitees
        User proposer = utils.makeUser();
        User user1 = utils.makeUser();
        User user2 = utils.makeUser();
        User user3 = utils.makeUser();
        // no invite case
        User user4 = utils.makeUser();

        // username and password will be encrypted, need to store
        String username_proposer = proposer.getUsername();
        String username_user1 = user1.getUsername();
        String username_user2 = user2.getUsername();
        String username_user3 = user3.getUsername();
        String username_user4 = user4.getUsername();
        String password_user1 = user1.getPassword();
        String password_user2 = user2.getPassword();
        String password_user3 = user3.getPassword();
        String password_user4 = user4.getPassword();
        String password_proposer = proposer.getPassword();

        proposer.setConfirmPassword(proposer.getPassword());
        user1.setConfirmPassword(user1.getPassword());
        user2.setConfirmPassword(user2.getPassword());
        user3.setConfirmPassword(user3.getPassword());
        user4.setConfirmPassword(user4.getPassword());

        controller.createNewUser(proposer);
        controller.createNewUser(user1);
        controller.createNewUser(user2);
        controller.createNewUser(user3);
        controller.createNewUser(user4);

        clean_up_users.add(proposer);
        clean_up_users.add(user1);
        clean_up_users.add(user2);
        clean_up_users.add(user3);
        clean_up_users.add(user4);

        proposer.setUsername(username_proposer);
        user1.setUsername(username_user1);
        user2.setUsername(username_user2);
        user3.setUsername(username_user3);
        user4.setUsername(username_user4);

        proposer.setPassword(password_proposer);
        user1.setPassword(password_user1);
        user2.setPassword(password_user2);
        user3.setPassword(password_user3);
        user4.setPassword(password_user4);

        List<User> users_to_invite = new ArrayList<>();
        users_to_invite.add(user1);
        users_to_invite.add(user2);
        users_to_invite.add(user3);
        // set up event
        List<Event> events = new ArrayList<>();
        String event_name = "event";
        String event_location = "LA";
        String start_date = LocalDate.of(2021, 11, 15).toString();
        String end_date = LocalDate.of(2021, 11, 17).toString();
        String start_time = LocalTime.of(12, 0,0 ).toString();
        String end_time = LocalTime.of(12, 0,0 ).toString();
        String genre = "rock";
        String keyword = "music";
        Event event = new Event(event_name, event_location, start_date, start_time, end_date, end_time, genre, keyword);
        events.add(event);
        String proposal_name = "new proposal #1";
        // set up ProposalInfo
        ProposalInfo info = new ProposalInfo(proposer, users_to_invite, events, proposal_name);
        // create proposal
        Assert.assertEquals(HttpStatus.OK, controller.createProposal(info).getStatusCode());
        clean_up_proposals.add(proposal_name);
    }

    @Test
    public void testGetProposalByProposerName(){
        // create proposal
        // set up proposer and invitees
        User proposer = utils.makeUser();
        clean_up_users.add(proposer);
        User user1 = utils.makeUser();
        clean_up_users.add(user1);
        User user2 = utils.makeUser();
        clean_up_users.add(user2);
        User user3 = utils.makeUser();
        clean_up_users.add(user3);
        // no invite case
        User user4 = utils.makeUser();
        clean_up_users.add(user4);

        // username and password will be encrypted, need to store
        String username_proposer = proposer.getUsername();
        String username_user1 = user1.getUsername();
        String username_user2 = user2.getUsername();
        String username_user3 = user3.getUsername();
        String username_user4 = user4.getUsername();
        String password_user1 = user1.getPassword();
        String password_user2 = user2.getPassword();
        String password_user3 = user3.getPassword();
        String password_user4 = user4.getPassword();
        String password_proposer = proposer.getPassword();

        proposer.setConfirmPassword(proposer.getPassword());
        user1.setConfirmPassword(user1.getPassword());
        user2.setConfirmPassword(user2.getPassword());
        user3.setConfirmPassword(user3.getPassword());
        user4.setConfirmPassword(user4.getPassword());

        controller.createNewUser(proposer);
        controller.createNewUser(user1);
        controller.createNewUser(user2);
        controller.createNewUser(user3);
        controller.createNewUser(user4);

        proposer.setUsername(username_proposer);
        user1.setUsername(username_user1);
        user2.setUsername(username_user2);
        user3.setUsername(username_user3);
        user4.setUsername(username_user4);

        proposer.setPassword(password_proposer);
        user1.setPassword(password_user1);
        user2.setPassword(password_user2);
        user3.setPassword(password_user3);
        user4.setPassword(password_user4);

        List<User> users_to_invite = new ArrayList<>();
        users_to_invite.add(user1);
        users_to_invite.add(user2);
        users_to_invite.add(user3);
        // set up event
        List<Event> events = new ArrayList<>();
        String event_name = "event";
        String event_location = "LA";
        String start_date = LocalDate.of(2021, 11, 15).toString();
        String end_date = LocalDate.of(2021, 11, 17).toString();
        String start_time = LocalTime.of(12, 0,0 ).toString();
        String end_time = LocalTime.of(12, 0,0 ).toString();
        String genre = "rock";
        String keyword = "music";
        Event event = new Event(event_name, event_location, start_date, start_time, end_date, end_time, genre, keyword);
        events.add(event);
        String proposal_name = "new proposal #2";
        // set up ProposalInfo
        ProposalInfo info = new ProposalInfo(proposer, users_to_invite, events, proposal_name);
        controller.createProposal(info);
        clean_up_proposals.add(proposal_name);

        // result
        ResponseEntity<String> result = controller.getProposalByProposerName(proposer.getUsername());
        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
        //System.out.println(result.getBody());

        // user created but no proposal
        User user5 = utils.makeUser();
        String username_user5 = user5.getUsername();
        user5.setConfirmPassword(user5.getPassword());
        controller.createNewUser(user5);
        user5.setUsername(username_user5);
        result = controller.getProposalByProposerName(username_user5);
        Assert.assertEquals("no existing proposal created by this user", result.getBody());
        clean_up_users.add(user5);
    }

    @Test
    public void testGetReceivedInvites() {
        // create proposal
        // set up proposer and invitees
        User proposer = utils.makeUser();
        clean_up_users.add(proposer);
        User user1 = utils.makeUser();
        clean_up_users.add(user1);
        User user2 = utils.makeUser();
        clean_up_users.add(user2);
        User user3 = utils.makeUser();
        clean_up_users.add(user3);
        // no invite case
        User user4 = utils.makeUser();
        clean_up_users.add(user4);

        // username and password will be encrypted, need to store
        String username_proposer = proposer.getUsername();
        String username_user1 = user1.getUsername();
        String username_user2 = user2.getUsername();
        String username_user3 = user3.getUsername();
        String username_user4 = user4.getUsername();
        String password_user1 = user1.getPassword();
        String password_user2 = user2.getPassword();
        String password_user3 = user3.getPassword();
        String password_user4 = user4.getPassword();
        String password_proposer = proposer.getPassword();

        proposer.setConfirmPassword(proposer.getPassword());
        user1.setConfirmPassword(user1.getPassword());
        user2.setConfirmPassword(user2.getPassword());
        user3.setConfirmPassword(user3.getPassword());
        user4.setConfirmPassword(user4.getPassword());

        controller.createNewUser(proposer);
        controller.createNewUser(user1);
        controller.createNewUser(user2);
        controller.createNewUser(user3);
        controller.createNewUser(user4);

        proposer.setUsername(username_proposer);
        user1.setUsername(username_user1);
        user2.setUsername(username_user2);
        user3.setUsername(username_user3);
        user4.setUsername(username_user4);

        proposer.setPassword(password_proposer);
        user1.setPassword(password_user1);
        user2.setPassword(password_user2);
        user3.setPassword(password_user3);
        user4.setPassword(password_user4);

        List<User> users_to_invite = new ArrayList<>();
        users_to_invite.add(user1);
        users_to_invite.add(user2);
        users_to_invite.add(user3);
        // set up event
        List<Event> events = new ArrayList<>();
        String event_name = "event";
        String event_location = "LA";
        String start_date = LocalDate.of(2021, 11, 15).toString();
        String end_date = LocalDate.of(2021, 11, 17).toString();
        String start_time = LocalTime.of(12, 0,0 ).toString();
        String end_time = LocalTime.of(12, 0,0 ).toString();
        String genre = "rock";
        String keyword = "music";
        Event event = new Event(event_name, event_location, start_date, start_time, end_date, end_time, genre, keyword);
        events.add(event);
        String proposal_name = "new proposal #3";
        // set up ProposalInfo
        ProposalInfo info = new ProposalInfo(proposer, users_to_invite, events, proposal_name);
        controller.createProposal(info);
        clean_up_proposals.add(proposal_name);
        // get invitees
        ResponseEntity<String> user_1_result = controller.getReceivedInvites(user1.getUsername());
        ResponseEntity<String> user_2_result = controller.getReceivedInvites(user2.getUsername());
        ResponseEntity<String> user_3_result = controller.getReceivedInvites(user3.getUsername());
        Assert.assertEquals(HttpStatus.OK, user_1_result.getStatusCode());
        Assert.assertEquals(HttpStatus.OK, user_2_result.getStatusCode());
        Assert.assertEquals(HttpStatus.OK, user_3_result.getStatusCode());

//        System.out.println(user_1_result.getBody());
//        System.out.println(user_2_result.getBody());
//        System.out.println(user_3_result.getBody());

        ResponseEntity<String> user_4_result = controller.getReceivedInvites(user4.getUsername());
        Assert.assertEquals(HttpStatus.OK, user_4_result.getStatusCode());
        Assert.assertEquals("no invitation available", user_4_result.getBody());

    }

    @Test
    public void testDeleteUserById() {
        // create a user first
        String username = "user3";
        String password = "password";
        User user = new User(username, password);
        user.setConfirmPassword(password);
        controller.createNewUser(user);
        // user should be in database now
        Assert.assertTrue(controller.isUsernameTaken(username).getBody());
        //delete user
        // user should be gone from database
        ResponseEntity<String> response = controller.deleteUserById(user.getId());
        Assert.assertEquals("Successfully deleted user with id: " + user.getId(), response.getBody());
        Assert.assertFalse(controller.isUsernameTaken(username).getBody());
    }

    @Test
    public void testDeleteUserByUsername() {
        // create a user first
        String username = "user6";
        String password = "password";
        User user = new User(username, password);
        user.setConfirmPassword(password);
        controller.createNewUser(user);
        // user should be in database now
        Assert.assertTrue(controller.isUsernameTaken(username).getBody());
        //delete user
        // user should be gone from database
        ResponseEntity<String> response = controller.deleteUserByUsername(username);
        Assert.assertEquals("Successfully deleted user with username: " + username, response.getBody());
        Assert.assertFalse(controller.isUsernameTaken(username).getBody());
    }

    @Test
    public void testDeleteProposal(){
        // create proposal
        // set up proposer and invitees
        User proposer = utils.makeUser();
        clean_up_users.add(proposer);
        User user1 = utils.makeUser();
        clean_up_users.add(user1);

        // username and password will be encrypted, need to store
        String username_proposer = proposer.getUsername();
        String username_user1 = user1.getUsername();
        String password_user1 = user1.getPassword();
        String password_proposer = proposer.getPassword();

        proposer.setConfirmPassword(proposer.getPassword());
        user1.setConfirmPassword(user1.getPassword());

        controller.createNewUser(proposer);
        controller.createNewUser(user1);

        proposer.setUsername(username_proposer);
        user1.setUsername(username_user1);

        proposer.setPassword(password_proposer);
        user1.setPassword(password_user1);

        List<User> users_to_invite = new ArrayList<>();
        users_to_invite.add(user1);
        // set up event
        List<Event> events = new ArrayList<>();
        String event_name = "event";
        String event_location = "LA";
        String start_date = LocalDate.of(2021, 11, 15).toString();
        String end_date = LocalDate.of(2021, 11, 17).toString();
        String start_time = LocalTime.of(12, 0,0 ).toString();
        String end_time = LocalTime.of(12, 0,0 ).toString();
        String genre = "rock";
        String keyword = "music";
        Event event = new Event(event_name, event_location, start_date, start_time, end_date, end_time, genre, keyword);
        events.add(event);
        String proposal_name = "new proposal #4";
        // set up ProposalInfo
        ProposalInfo info = new ProposalInfo(proposer, users_to_invite, events, proposal_name);
        controller.createProposal(info);
        Assert.assertEquals("Successfully deleted proposal", controller.deleteProposal(proposal_name).getBody());
        proposer.setOwnedProposals(null);
        clean_up_users.add(proposer);
        clean_up_users.add(user1);
    }
    @Test
    public void testRespondToInvite(){
        // create proposal
        // set up proposer and invitees
        User proposer = utils.makeUser();
        clean_up_users.add(proposer);
        User user1 = utils.makeUser();
        clean_up_users.add(user1);
        User user2 = utils.makeUser();
        clean_up_users.add(user2);
        User user3 = utils.makeUser();
        clean_up_users.add(user3);
        // no invite case
        User user4 = utils.makeUser();
        clean_up_users.add(user4);

        // username and password will be encrypted, need to store
        String username_proposer = proposer.getUsername();
        String username_user1 = user1.getUsername();
        String username_user2 = user2.getUsername();
        String username_user3 = user3.getUsername();
        String username_user4 = user4.getUsername();
        String password_user1 = user1.getPassword();
        String password_user2 = user2.getPassword();
        String password_user3 = user3.getPassword();
        String password_user4 = user4.getPassword();
        String password_proposer = proposer.getPassword();

        proposer.setConfirmPassword(proposer.getPassword());
        user1.setConfirmPassword(user1.getPassword());
        user2.setConfirmPassword(user2.getPassword());
        user3.setConfirmPassword(user3.getPassword());
        user4.setConfirmPassword(user4.getPassword());

        controller.createNewUser(proposer);
        controller.createNewUser(user1);
        controller.createNewUser(user2);
        controller.createNewUser(user3);
        controller.createNewUser(user4);

        proposer.setUsername(username_proposer);
        user1.setUsername(username_user1);
        user2.setUsername(username_user2);
        user3.setUsername(username_user3);
        user4.setUsername(username_user4);

        proposer.setPassword(password_proposer);
        user1.setPassword(password_user1);
        user2.setPassword(password_user2);
        user3.setPassword(password_user3);
        user4.setPassword(password_user4);

        List<User> users_to_invite = new ArrayList<>();
        users_to_invite.add(user1);
        users_to_invite.add(user2);
        users_to_invite.add(user3);
        // set up event
        List<Event> events = new ArrayList<>();
        String event_name = "film festival";
        String event_location = "LA";
        String start_date = LocalDate.of(2021, 12, 1).toString();
        String end_date = LocalDate.of(2022, 5, 17).toString();
        String start_time = LocalTime.of(12, 0,0 ).toString();
        String end_time = LocalTime.of(12, 0,0 ).toString();
        String genre = "drama";
        String keyword = "film";
        Event event = new Event(event_name, event_location, start_date, start_time, end_date, end_time, genre, keyword);
        events.add(event);
        String proposal_name = "Proposal with Response";
        // set up ProposalInfo
        ProposalInfo info = new ProposalInfo(proposer, users_to_invite, events, proposal_name);
        controller.createProposal(info);
        clean_up_proposals.add(proposal_name);
        // input responses
        InviteeResponse user_1_response = new InviteeResponse("RESPONDED", "YES", 4, proposal_name, username_user1, event_name);
        InviteeResponse user_2_response = new InviteeResponse("RESPONDED", "NO", 3, proposal_name, username_user2, event_name);
        InviteeResponse user_3_response = new InviteeResponse("RESPONDED", "MAYBE", 2, proposal_name, username_user3, event_name);
        InviteeResponse user_4_response = new InviteeResponse("RESPONDED", "YES", 1, proposal_name, username_user4, event_name);

        ResponseEntity<String> user_1_result = controller.respondToInvite(user_1_response);
        ResponseEntity<String> user_2_result = controller.respondToInvite(user_2_response);
        ResponseEntity<String> user_3_result = controller.respondToInvite(user_3_response);
        ResponseEntity<String> user_4_result = controller.respondToInvite(user_4_response);

        Assert.assertEquals("response submitted!", user_1_result.getBody());
        Assert.assertEquals("response submitted!", user_2_result.getBody());
        Assert.assertEquals("response submitted!", user_3_result.getBody());
        Assert.assertEquals("no invitation available", user_4_result.getBody());

    }
    @Test
    public void testSelectBestEventByProposalName(){
        // create proposal
        // set up proposer and invitees
        User proposer = utils.makeUser();
        clean_up_users.add(proposer);
        User user1 = utils.makeUser();
        clean_up_users.add(user1);
        User user2 = utils.makeUser();
        clean_up_users.add(user2);
        User user3 = utils.makeUser();
        clean_up_users.add(user3);

        // username and password will be encrypted, need to store
        String username_proposer = proposer.getUsername();
        String username_user1 = user1.getUsername();
        String username_user2 = user2.getUsername();
        String username_user3 = user3.getUsername();
        String password_user1 = user1.getPassword();
        String password_user2 = user2.getPassword();
        String password_user3 = user3.getPassword();
        String password_proposer = proposer.getPassword();

        proposer.setConfirmPassword(proposer.getPassword());
        user1.setConfirmPassword(user1.getPassword());
        user2.setConfirmPassword(user2.getPassword());
        user3.setConfirmPassword(user3.getPassword());

        controller.createNewUser(proposer);
        controller.createNewUser(user1);
        controller.createNewUser(user2);
        controller.createNewUser(user3);

        proposer.setUsername(username_proposer);
        user1.setUsername(username_user1);
        user2.setUsername(username_user2);
        user3.setUsername(username_user3);

        proposer.setPassword(password_proposer);
        user1.setPassword(password_user1);
        user2.setPassword(password_user2);
        user3.setPassword(password_user3);

        List<User> users_to_invite = new ArrayList<>();
        users_to_invite.add(user1);
        users_to_invite.add(user2);
        users_to_invite.add(user3);
        // set up event
        List<Event> events = new ArrayList<>();
        String event_name = "film festival";
        String event_location = "LA";
        String start_date = LocalDate.of(2021, 12, 1).toString();
        String end_date = LocalDate.of(2022, 5, 17).toString();
        String start_time = LocalTime.of(12, 0,0 ).toString();
        String end_time = LocalTime.of(12, 0,0 ).toString();
        String genre = "drama";
        String keyword = "film";
        Event event = new Event(event_name, event_location, start_date, start_time, end_date, end_time, genre, keyword);
        events.add(event);

        // second event
        String second_event_name = "music festival";
        String second_event_location = "LA";
        String second_start_date = LocalDate.of(2021, 12, 1).toString();
        String second_end_date = LocalDate.of(2022, 5, 17).toString();
        String second_start_time = LocalTime.of(12, 0,0 ).toString();
        String second_end_time = LocalTime.of(12, 0,0 ).toString();
        String second_genre = "rock";
        String second_keyword = "music";
        Event second_event = new Event(second_event_name, second_event_location, second_start_date, second_start_time, second_end_date, second_end_time, second_genre, second_keyword);
        events.add(second_event);

        // third event
        String third_event_name = "sports festival";
        String third_event_location = "LA";
        String third_start_date = LocalDate.of(2021, 12, 1).toString();
        String third_end_date = LocalDate.of(2022, 5, 17).toString();
        String third_start_time = LocalTime.of(12, 0,0 ).toString();
        String third_end_time = LocalTime.of(12, 0,0 ).toString();
        String third_genre = "soccer";
        String third_keyword = "sports";
        Event third_event = new Event(third_event_name, third_event_location, third_start_date, third_start_time, third_end_date, third_end_time, third_genre, third_keyword);
        events.add(third_event);

        String proposal_name = "Proposal with Best Event";
        // set up ProposalInfo
        ProposalInfo info = new ProposalInfo(proposer, users_to_invite, events, proposal_name);
        controller.createProposal(info);
        clean_up_proposals.add(proposal_name);
        // input responses
        InviteeResponse user_1_response = new InviteeResponse("RESPONDED", "YES", 5, proposal_name, username_user1, event_name);
        InviteeResponse user_2_response = new InviteeResponse("RESPONDED", "NO", 5, proposal_name, username_user2, event_name);
        InviteeResponse user_3_response = new InviteeResponse("RESPONDED", "MAYBE", 5, proposal_name, username_user3, event_name);

        InviteeResponse user_1_response_2 = new InviteeResponse("RESPONDED", "YES", 4, proposal_name, username_user1, second_event_name);
        InviteeResponse user_2_response_2 = new InviteeResponse("RESPONDED", "YES", 3, proposal_name, username_user2, second_event_name);
        InviteeResponse user_3_response_2 = new InviteeResponse("RESPONDED", "MAYBE", 2, proposal_name, username_user3, second_event_name);

        InviteeResponse user_1_response_3 = new InviteeResponse("RESPONDED", "YES", 5, proposal_name, username_user1, third_event_name);
        InviteeResponse user_2_response_3 = new InviteeResponse("RESPONDED", "YES", 3, proposal_name, username_user2, third_event_name);
        InviteeResponse user_3_response_3 = new InviteeResponse("RESPONDED", "MAYBE", 2, proposal_name, username_user3, third_event_name);

        // send response
        controller.respondToInvite(user_1_response);
        controller.respondToInvite(user_2_response);
        controller.respondToInvite(user_3_response);

        controller.respondToInvite(user_1_response_2);
        controller.respondToInvite(user_2_response_2);
        controller.respondToInvite(user_3_response_2);

        controller.respondToInvite(user_1_response_3);
        controller.respondToInvite(user_2_response_3);
        controller.respondToInvite(user_3_response_3);

        // found the best event to be event 3
        ResponseEntity<String> event_result = controller.selectBestEventByProposalName(proposal_name);

        Assert.assertEquals("Event{title='sports festival', eventLocation='LA', startDate=2021-12-01, startTime=12:00, endDate=2022-05-17, endTime=12:00, genre='soccer', keyword='sports'}", event_result.getBody());

    }

    @Test
    public void testFinalizeProposal(){
        // create proposal
        // set up proposer and invitees
        User proposer = utils.makeUser();
        clean_up_users.add(proposer);
        User user1 = utils.makeUser();
        clean_up_users.add(user1);
        User user2 = utils.makeUser();
        clean_up_users.add(user2);
        User user3 = utils.makeUser();
        clean_up_users.add(user3);

        // username and password will be encrypted, need to store
        String username_proposer = proposer.getUsername();
        String username_user1 = user1.getUsername();
        String username_user2 = user2.getUsername();
        String username_user3 = user3.getUsername();
        String password_user1 = user1.getPassword();
        String password_user2 = user2.getPassword();
        String password_user3 = user3.getPassword();
        String password_proposer = proposer.getPassword();

        proposer.setConfirmPassword(proposer.getPassword());
        user1.setConfirmPassword(user1.getPassword());
        user2.setConfirmPassword(user2.getPassword());
        user3.setConfirmPassword(user3.getPassword());

        controller.createNewUser(proposer);
        controller.createNewUser(user1);
        controller.createNewUser(user2);
        controller.createNewUser(user3);

        proposer.setUsername(username_proposer);
        user1.setUsername(username_user1);
        user2.setUsername(username_user2);
        user3.setUsername(username_user3);

        proposer.setPassword(password_proposer);
        user1.setPassword(password_user1);
        user2.setPassword(password_user2);
        user3.setPassword(password_user3);

        List<User> users_to_invite = new ArrayList<>();
        users_to_invite.add(user1);
        users_to_invite.add(user2);
        users_to_invite.add(user3);
        // set up event
        List<Event> events = new ArrayList<>();
        String event_name = "concert";
        String event_location = "LA";
        String start_date = LocalDate.of(2021, 12, 1).toString();
        String end_date = LocalDate.of(2022, 5, 17).toString();
        String start_time = LocalTime.of(12, 0,0 ).toString();
        String end_time = LocalTime.of(12, 0,0 ).toString();
        String genre = "music";
        String keyword = "concert";
        Event event = new Event(event_name, event_location, start_date, start_time, end_date, end_time, genre, keyword);
        events.add(event);

        String proposal_name = "Proposal being finalized";
        // set up ProposalInfo
        ProposalInfo info = new ProposalInfo(proposer, users_to_invite, events, proposal_name);
        controller.createProposal(info);
        clean_up_proposals.add(proposal_name);
        // input responses
        InviteeResponse user_1_response = new InviteeResponse("RESPONDED", "YES", 5, proposal_name, username_user1, event_name);
        InviteeResponse user_2_response = new InviteeResponse("RESPONDED", "NO", 5, proposal_name, username_user2, event_name);
        // send response
        controller.respondToInvite(user_1_response);
        controller.respondToInvite(user_2_response);

        // found the best event
        ResponseEntity<String> event_result = controller.selectBestEventByProposalName(proposal_name);

        // finalize
        controller.finalizeProposal(proposal_name);

        // new response can not be sent anymore
        InviteeResponse user_3_response = new InviteeResponse("RESPONDED", "MAYBE", 5, proposal_name, username_user3, event_name);
        ResponseEntity<String> new_response = controller.respondToInvite(user_3_response);
        Assert.assertEquals("proposal is finalized, no longer can be responded", new_response.getBody());

    }

    @AfterAll
    public void tearDown(){
        for(int i = 0; i < clean_up_proposals.size(); i++){
            controller.deleteProposal(clean_up_proposals.get(i));
        }
        for(int i = 0; i < clean_up_users.size(); i++){
            controller.deleteUserByUsername(clean_up_users.get(i).getUsername());
        }
    }

}


