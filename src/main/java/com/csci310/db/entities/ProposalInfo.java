package com.csci310.db.entities;

import java.util.List;

public class ProposalInfo {
    private User proposer;
    private List<User> invitees;
    private List<Event> events;
    private String proposal_name;

    public ProposalInfo(User user, List<User> invitees, List<Event> events, String proposal_name){
        this.proposer = user;
        this.invitees = invitees;
        this.events = events;
        this.proposal_name = proposal_name;

    }
    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> event) {
        this.events = event;
    }

    public String getProposal_name() {
        return proposal_name;
    }

    public void setProposal_name(String proposal_name) {
        this.proposal_name = proposal_name;
    }

    public User getProposer() {
        return proposer;
    }

    public void setProposer(User proposer) {
        this.proposer = proposer;
    }

    public List<User> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<User> invitees) {
        this.invitees = invitees;
    }


}
