package com.csci310.db.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name  = "invitees")
public class Invitee
{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "response_status")
    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus;

    @Column(name = "availability_status")
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    @Column(name = "preference_rating")
    private int preferenceRating;

    @ManyToOne
    //@JsonBackReference
    @JoinColumn(name = "proposal_id")
    private DateProposal parentProposal;

    @ManyToOne
    //@JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    //@JsonBackReference
    @JoinColumn(name = "event_id")
    private Event event;

    public Invitee() {
    }

    public Invitee(User invitee, Event event) {
        this.user = invitee;
        this.event = event;
        this.responseStatus = ResponseStatus.PENDING;
    }

    public Invitee(User user) {
        this.user = user;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return this.availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public int getPreferenceRating() {
        return this.preferenceRating;
    }

    public void setPreferenceRating(int preferenceRating) {
        this.preferenceRating = preferenceRating;
    }

    public DateProposal getParentProposal() {
        return this.parentProposal;
    }

    public void setParentProposal(DateProposal parentProposal) {
        this.parentProposal = parentProposal;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User invitee) {
        this.user = invitee;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public enum AvailabilityStatus {
        YES,
        MAYBE,
        NO
    }

    public enum ResponseStatus {
        PENDING,
        RESPONDED
    }

    @Override
    public String toString() {
        return "InviteeResponse{" +
                "invitee=" + user.getUsername() +
                ", event=" + event +
                ", availabilityStatus=" + availabilityStatus +
                ", preferenceRating=" + preferenceRating +
                '}';
    }
}
