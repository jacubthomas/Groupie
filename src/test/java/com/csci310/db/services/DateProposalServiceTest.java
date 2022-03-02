package com.csci310.db.services;

import com.csci310.db.entities.DateProposal;
import com.csci310.db.entities.Event;
import com.csci310.db.entities.Invitee;
import com.csci310.db.entities.User;
import com.csci310.db.repositories.DateProposalRepository;
import com.csci310.TestSetupUtils;
import com.csci310.spring.APIApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = APIApplication.class)
public class DateProposalServiceTest {
    private TestSetupUtils utils = new TestSetupUtils();
    private User user;
    private DateProposal proposal;
    private ArrayList<Event> events;

    @Autowired
    private DateProposalService dateProposalService;

    @MockBean
    private DateProposalRepository dateProposalRepository;

    @Before
    public void setUp() {
        User user = utils.makeUser();
        User user1 = utils.makeUser();
        User user2 = utils.makeUser();
        User user3 = utils.makeUser();
        List<User> users_to_invite = new ArrayList<>();
        users_to_invite.add(user1);
        users_to_invite.add(user2);
        users_to_invite.add(user3);
        ArrayList<Event> events = new ArrayList<>();
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
        ArrayList<Invitee> invitees = new ArrayList<>();
        //Set<Invitee> user_as_invitees = new HashSet<>();
        for(int i = 0; i < users_to_invite.size(); i++){
            for(int j = 0; j < events.size(); j++){
                Invitee invitee = new Invitee(users_to_invite.get(i), events.get(j));
                //userService.getUserByUsername(users_to_invite.get(i).getUsername()).addUserAsInvitees(invitee);
                invitees.add(invitee);
                //user_as_invitees.add(invitee);
            }
            //users_to_invite.get(i).setUserAsInvitees(user_as_invitees);

        }
        DateProposal proposal = new DateProposal(user, invitees, "newProposal", events);
        this.user = user;
        this.proposal = proposal;
        this.events = events;

    }

    @Test
    public void testGetProposalById() {
        // Setup
        UUID id = UUID.randomUUID();

        // empty case
        // Mocks
        Mockito.when(dateProposalRepository.findById(id)).thenReturn(Optional.empty());

        // Assertions
        Assert.assertTrue(dateProposalService.getProposalById(id).isEmpty());

        // exist case
        Mockito.when(dateProposalRepository.findById(id)).thenReturn(Optional.of(proposal));

        // Assertions
        Optional<DateProposal> result = dateProposalService.getProposalById(id);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals("newProposal", result.get().getProposalName());

    }

    @Test
    public void testGetProposalByProposerId() {
        ArrayList<DateProposal> proposals = new ArrayList<>();
        proposals.add(this.proposal);
        Mockito.when(dateProposalRepository.findAllByProposer_Id(user.getId())).thenReturn(Optional.of(proposals));
        Optional<ArrayList<DateProposal>> result = dateProposalService.getProposalByProposerId(user.getId());
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals("newProposal", result.get().get(0).getProposalName());
    }

    @Test
    public void testGetProposalByProposalName(){
        Mockito.when(dateProposalRepository.findByProposalName("proposal 1")).thenReturn(Optional.of(new DateProposal()));
        Optional<DateProposal> result = dateProposalService.getProposalByProposalName("proposal 1");
        Assert.assertTrue(result.isPresent());

    }

    @Test
    public void testIsNameTaken() {
        // exist case
        Mockito.when(dateProposalRepository.findByProposalName("newProposal")).thenReturn(Optional.of(proposal));
        Mockito.when(dateProposalRepository.findByProposalName("none")).thenReturn(Optional.empty());
        // Assertions
        boolean result = dateProposalService.isNameTaken("newProposal");
        Assert.assertTrue(result);
        result = dateProposalService.isNameTaken("none");
        Assert.assertFalse(result);
    }

    @Test
    public void testPersistProposal() {
        Mockito.when(dateProposalRepository.save(proposal)).thenReturn(proposal);
        dateProposalService.persistProposal(proposal);

    }
    @Test
    public void testDeleteDateProposalById(){
        UUID id = UUID.randomUUID();
        dateProposalService.deleteDateProposalById(id);

    }

    @Test
    public void testGetInvite(){
        Mockito.when(dateProposalRepository.getInvite("proposal_name", "invitee_name", "event_name")).thenReturn(Optional.of(new Invitee()));
        Assert.assertTrue(dateProposalService.getInvite("proposal_name", "invitee_name", "event_name").isPresent());
    }

    @Test
    public void testUpdateInvite(){
        UUID id = UUID.randomUUID();
        dateProposalService.updateInvite(id, 5, Invitee.AvailabilityStatus.MAYBE, Invitee.ResponseStatus.RESPONDED);
    }

    @Test
    public void testUpdateBestEvent(){
        UUID id = UUID.randomUUID();
        dateProposalService.updateBestEvent(id, "title");
    }

    @Test
    public void testFinalizeEvent(){
        UUID id = UUID.randomUUID();
        dateProposalService.finalizeEvent(id);
    }
}