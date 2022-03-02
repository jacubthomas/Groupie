package com.csci310.db.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name  = "date_proposals")
public class DateProposal
{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "proposal_name")
    private String proposalName;

    @Column(name = "proposal_state")
    @Enumerated(EnumType.STRING)
    private ProposalState proposalState;

    @ManyToOne(cascade = {CascadeType.MERGE})
    //@JsonBackReference
    @JoinColumn(name = "proposer_id")
    private User proposer;

    @OneToMany(mappedBy = "parentProposal", cascade = {CascadeType.ALL}, orphanRemoval = true)
    //@JsonManagedReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Invitee> invitees;

    @OneToMany(mappedBy = "proposal", cascade = {CascadeType.ALL}, orphanRemoval = true)
    //@JsonManagedReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Event> events;

    @Column(name = "best_event_title")
    private String best_event_title;

    public enum ProposalState {
        UNFINALIZED,
        FINALIZED
    }

    public DateProposal() {
    }

    public DateProposal(User proposer, List<Invitee> invitees, String proposalName, List<Event> events) {
        this.proposer = proposer;
        this.proposalName = proposalName;
        this.proposalState = ProposalState.UNFINALIZED;
        this.events = events;
        //Set<Invitee> user_as_invitees = new HashSet<>();
        this.invitees = invitees;

    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getProposer() {
        return this.proposer;
    }

    public void setProposer(User proposer) {
        this.proposer = proposer;
    }

    public String getProposalName() {
        return this.proposalName;
    }

    public void setProposalName(String proposalName) {
        this.proposalName = proposalName;
    }

    public ProposalState getProposalState() {
        return this.proposalState;
    }

    public void setProposalState(ProposalState proposalState) {
        this.proposalState = proposalState;
    }

    public List<Invitee> getInvitees() {
        return this.invitees;
    }

    public void setInvitees(List<Invitee> inviteeResponses) {
        this.invitees = inviteeResponses;
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getBest_event_title() {
        return best_event_title;
    }

    public void setBest_event_title(String best_event_title) {
        this.best_event_title = best_event_title;
    }

    @Override
    public String toString() {
        return "DateProposal{" +
                "id=" + id +
                ", proposalName='" + proposalName + '\'' +
                ", proposalState=" + proposalState +
                ", proposer=" + proposer +
                ", invitees=" + invitees +
                ", events=" + events +
                '}';
    }
}

