package com.csci310.db.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InviteeResponseTest {
    private InviteeResponse inviteeResponse;
    @Before
    public void setUp(){
        inviteeResponse = new InviteeResponse("RESPONDED", "YES", 5, "proposal", "invitee", "event");
    }

    @Test
    public void getResponseStatus() {
        Assert.assertEquals("RESPONDED", inviteeResponse.getResponseStatus());
    }

    @Test
    public void setResponseStatus() {
        inviteeResponse.setResponseStatus("PENDING");
        Assert.assertEquals("PENDING", inviteeResponse.getResponseStatus());
    }

    @Test
    public void getAvailabilityStatus() {
        Assert.assertEquals("YES", inviteeResponse.getAvailabilityStatus());

    }

    @Test
    public void setAvailabilityStatus() {
        inviteeResponse.setAvailabilityStatus("NO");
        Assert.assertEquals("NO", inviteeResponse.getAvailabilityStatus());

    }

    @Test
    public void getPreferenceRating() {
        Assert.assertEquals(5, inviteeResponse.getPreferenceRating());

    }

    @Test
    public void setPreferenceRating() {
        inviteeResponse.setPreferenceRating(4);
        Assert.assertEquals(4, inviteeResponse.getPreferenceRating());

    }

    @Test
    public void getProposal_name() {
        Assert.assertEquals("proposal", inviteeResponse.getProposal_name());

    }

    @Test
    public void setProposal_name() {
        inviteeResponse.setProposal_name("new");
        Assert.assertEquals("new", inviteeResponse.getProposal_name());

    }

    @Test
    public void getInvitee_name() {
        Assert.assertEquals("invitee", inviteeResponse.getInvitee_name());
    }

    @Test
    public void setInvitee_name() {
        inviteeResponse.setInvitee_name("new name");
        Assert.assertEquals("new name", inviteeResponse.getInvitee_name());

    }

    @Test
    public void getEvent_name() {
        Assert.assertEquals("event", inviteeResponse.getEvent_name());

    }

    @Test
    public void setEvent_name() {
        inviteeResponse.setEvent_name("new event name");
        Assert.assertEquals("new event name", inviteeResponse.getEvent_name());

    }
}