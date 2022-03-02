package com.csci310.db.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name  = "events")
public class Event
{
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String title;

    @Column(name = "event_location")
    private String eventLocation;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "genre")
    private String genre;

    @Column(name = "keyword")
    private String keyword;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Invitee> inviteeResponses;

    @ManyToOne(cascade = CascadeType.MERGE)
    //@JsonBackReference
    @JoinColumn(name = "proposal_id")
    private DateProposal proposal;

    public Event() {
    }

    public Event(String title, String eventLocation, String startDate, String startTime, String endDate, String endTime, String genre, String keyword) {
        this.title = title;
        this.eventLocation = eventLocation;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.genre = genre;
        this.keyword = keyword;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventLocation() {
        return this.eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Invitee> getInviteeResponses() {
        return this.inviteeResponses;
    }

    public void setInviteeResponses(List<Invitee> inviteeResponses) {
        this.inviteeResponses = inviteeResponses;
    }

    public DateProposal getProposal() {
        return proposal;
    }

    public void setProposal(DateProposal proposal) {
        this.proposal = proposal;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", endDate=" + endDate +
                ", endTime=" + endTime +
                ", genre='" + genre + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}