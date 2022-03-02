package com.csci310.db.entities;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

public class InviteeResponse {
    private String responseStatus;
    private String availabilityStatus;
    private int preferenceRating;
    private String proposal_name;
    private String invitee_name;
    private String event_name;

    public InviteeResponse(String responseStatus, String availabilityStatus, int preferenceRating,
                           String proposal_name, String invitee_name, String event_name){
        this.responseStatus = responseStatus;
        this.availabilityStatus = availabilityStatus;
        this.preferenceRating = preferenceRating;
        this.proposal_name = proposal_name;
        this.invitee_name = invitee_name;
        this.event_name = event_name;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public int getPreferenceRating() {
        return preferenceRating;
    }

    public void setPreferenceRating(int preferenceRating) {
        this.preferenceRating = preferenceRating;
    }

    public String getProposal_name() {
        return proposal_name;
    }

    public void setProposal_name(String proposal_name) {
        this.proposal_name = proposal_name;
    }

    public String getInvitee_name() {
        return invitee_name;
    }

    public void setInvitee_name(String invitee_name) {
        this.invitee_name = invitee_name;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }
}
