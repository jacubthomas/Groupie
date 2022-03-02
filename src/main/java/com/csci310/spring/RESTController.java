package com.csci310.spring;

import com.csci310.db.entities.*;
import com.csci310.db.services.DateProposalService;
import com.csci310.ticket_master.*;
import com.csci310.ticket_master.SearchInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;

import org.json.*;


import com.csci310.db.services.UserService;

@RestController
@RequestMapping("/api")
public class RESTController {
    @Autowired
    UserService userService;
    @Autowired
    DateProposalService proposalService;

    TicketMaster tmProxy = new TicketMasterCacheProxy();
    @GetMapping("/testresponse")
    public String testApi() {
        return "This response serves as a test for our REST API.";
    }

    @RequestMapping("/")
    public @ResponseBody
    String greeting() {
        return "Hello, World";
    }

    @GetMapping("/user/checkUsername/{uname}")
    public ResponseEntity<Boolean> isUsernameTaken(@PathVariable String uname) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.isUsernameTaken(uname));

    }

    @GetMapping("/user/username/{uname}")
    public ResponseEntity<String> getUserByUsername(@PathVariable String uname) {
        if (!userService.isUsernameTaken(uname)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username does not exist.");
        }

        // user exists, return user in json format
        User u = userService.getUserByUsername(uname);
        JSONObject jsonObject = new JSONObject(u);
        String userInJson = jsonObject.toString();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userInJson);
    }

    @PostMapping("/user/create")
    public ResponseEntity<String> createNewUser(@RequestBody User user) {
        if (!userService.passwordsMatch(user.getPassword(), user.getConfirmPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Passwords do not match.");
        }
        if(user.getOwnedProposals() == null){
            user.setOwnedProposals(new ArrayList<>());
            user.setUserAsInvitees(new ArrayList<>());
        }
        userService.persistUser(user);
        return ResponseEntity.ok("Successfully created user with username: " + userService.decrypt(user.getUsername()));
    }

    @DeleteMapping ("/user/deleteById/{id}")
    //@RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Successfully deleted user with id: " + id);
//        }
    }

    @RequestMapping ("/user/deleteByUsername/{username}")
    //@RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok("Successfully deleted user with username: " + username);

    }


    @PostMapping("/user/checkMatchingPasswords")
    public ResponseEntity<String> checkPasswordsMatch(@RequestBody User user) {
        if (userService.passwordsMatch(user.getPassword(), user.getConfirmPassword())) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Passwords match.");
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Passwords do not match.");
        }
    }

    @PostMapping("/user/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody User user) {
        if (userService.didAuthenticateUser(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok("Successfully authenticated user with username: " + user.getUsername());
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Incorrect username or password");
    }

    @PostMapping ("/searchEvent")
    public ResponseEntity<String> searchEvent(@RequestBody SearchInformation info) throws IOException {
        TicketMaster tmProxy = new TicketMasterCacheProxy();
        return tmProxy.searchEvent(info);
    }

    @PostMapping("/user/proposer/createProposal")
    public ResponseEntity<String> createProposal(@RequestBody ProposalInfo info) {
        if(info != null) {
            User proposer = userService.getUserByUsername(info.getProposer().getUsername());
            List<User> users_to_invite = new ArrayList<>();
            for(User u: info.getInvitees()){
                User user = userService.getUserByUsername(u.getUsername());
                if(user != null) {
                    users_to_invite.add(user);
                }
            }
            String proposalName = info.getProposal_name();
            List<Event> events = info.getEvents();
            ArrayList<Invitee> invitees = new ArrayList<>();
            //Set<Invitee> user_as_invitees = new HashSet<>();
            for(int i = 0; i < users_to_invite.size(); i++){
                for(int j = 0; j < events.size(); j++){
                    Invitee invitee = new Invitee(users_to_invite.get(i), events.get(j));
                    users_to_invite.get(i).addUserAsInvitees(invitee);
                    invitees.add(invitee);
                }
            }
            DateProposal proposal = new DateProposal(proposer, invitees, proposalName, events);
            // store bidirectional relationships
            for(Invitee invitee: proposal.getInvitees()){
                invitee.setParentProposal(proposal);
            }
            for(Event e: proposal.getEvents()){
                e.setProposal(proposal);
            }
            // save proposal to database
            proposer.addProposal(proposal);
            for(Invitee invitee: proposal.getInvitees()){
                invitee.setParentProposal(proposal);
            }
            proposalService.persistProposal(proposal);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("proposal created!");
        }
        else return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("no proposal information");
    }

    /*
     *   getProposalByProposer gives response that contains all invitee information
     */
    @PostMapping("/user/proposer/getProposal/{proposer_name}")
    public ResponseEntity<String> getProposalByProposerName(@PathVariable String proposer_name) {
        User proposer= userService.getUserByUsername(proposer_name);
        Optional<ArrayList<DateProposal>> proposals = proposalService.getProposalByProposerId(proposer.getId());
        if(proposals.get().isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("no existing proposal created by this user");
        }
        System.out.println(proposals.get().get(0));
        ArrayList<DateProposal> result_list = proposals.get();
        JSONObject jsonObject = new JSONObject(result_list);
        String proposalInJson = jsonObject.toString();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(proposalInJson);
    }

    @DeleteMapping  ("/proposer/deleteProposal/{proposal_name}")
    //@RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteProposal(@PathVariable String proposal_name) {
        DateProposal proposal = proposalService.getProposalByProposalName(proposal_name).get();
        proposalService.deleteDateProposalById(proposal.getId());
        return ResponseEntity.ok("Successfully deleted proposal");
    }

    /*
     *   getReceivedProposals gives response that contains all information of a user being invitee
     */

    @GetMapping("/user/invitee/getInvites/{username}")
    public ResponseEntity<String> getReceivedInvites(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        List<Invitee> invitees = user.getUserAsInvitees();
        if (invitees.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("no invitation available");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitees.toString());
    }

    @PostMapping("/user/invitee/respondInvites/")
    public ResponseEntity<String> respondToInvite(@RequestBody InviteeResponse response) {
        Optional<Invitee> invitee = proposalService.getInvite(response.getProposal_name(), userService.encrypt(response.getInvitee_name()), response.getEvent_name());
        if (invitee.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("no invitation available");
        }
        Optional<DateProposal> optional_proposal = proposalService.getProposalByProposalName(response.getProposal_name());
        if(optional_proposal.get().getProposalState() != DateProposal.ProposalState.FINALIZED) {
            proposalService.updateInvite(invitee.get().getId(), response.getPreferenceRating(), Invitee.AvailabilityStatus.valueOf(response.getAvailabilityStatus()), Invitee.ResponseStatus.valueOf(response.getResponseStatus()));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("response submitted!");
        }
        else return ResponseEntity
                .status(HttpStatus.OK)
                .body("proposal is finalized, no longer can be responded");
    }

    @GetMapping("/user/proposer/getBestEvent/{proposal_name}")
    public ResponseEntity<String> selectBestEventByProposalName(@PathVariable String proposal_name) {
        Optional<DateProposal> optional_proposal = proposalService.getProposalByProposalName(proposal_name);
        if(optional_proposal.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("no existing proposal");
        }
        DateProposal proposal = optional_proposal.get();
        List<Event> events = proposal.getEvents();
        List<Invitee> responses = proposal.getInvitees();
        HashMap<Event, Integer> scores= new HashMap<>();
        HashMap<Event, Integer> num_participants= new HashMap<>();
        for(Event e: events){
            scores.put(e, 0);
            num_participants.put(e, 0);
        }
        for(Invitee response: responses){
            // only consider ones that can participate
            if(response.getAvailabilityStatus() == Invitee.AvailabilityStatus.YES){
                Event e = response.getEvent();
                num_participants.put(e, num_participants.get(e)+1);
                scores.put(e, scores.get(e) + response.getPreferenceRating());
            }
        }
        // find the event that most people can join and has highest score
        Event best_event = events.get(0);
        int num_participant = num_participants.get(best_event);
        for(Event e: events){
            int count = num_participants.get(e);
            if(count > num_participant){
                best_event = e;
                num_participant = count;
            }
            else if(count == num_participant){
                if(scores.get(e) > scores.get(best_event)){
                    best_event = e;
                }
            }
        }
        proposalService.updateBestEvent(proposal.getId(), best_event.getTitle());
        // have best event now
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(best_event.toString());
    }

    @PostMapping("/user/proposer/finalize/{proposal_name}")
    public ResponseEntity<String> finalizeProposal(@PathVariable String proposal_name) {
        Optional<DateProposal> optional_proposal = proposalService.getProposalByProposalName(proposal_name);
        if(optional_proposal.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("no existing proposal");
        }
        DateProposal proposal = optional_proposal.get();
        proposalService.finalizeEvent(proposal.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("proposal finalized!");
    }

}

