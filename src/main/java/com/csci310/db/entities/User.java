package com.csci310.db.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name  = "users")
public class User
{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(name = "passcode")
    private String password;

    private String confirmPassword;

    @OneToMany(mappedBy = "proposer", cascade = {CascadeType.ALL})
    //@JsonManagedReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<DateProposal> ownedProposals;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    //@JsonManagedReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Invitee> userAsInvitees;

    private boolean isEncrpted = false;
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.ownedProposals = new ArrayList<>();
        this.userAsInvitees = new ArrayList<>();
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<DateProposal> getOwnedProposals() {
        return this.ownedProposals;
    }

    public void setOwnedProposals(List<DateProposal> ownedProposals) {
        this.ownedProposals = ownedProposals;
    }

    public void addProposal(DateProposal proposal) {
        this.ownedProposals.add(proposal);
    }

    public List<Invitee> getUserAsInvitees() {
        return userAsInvitees;
    }

    public void setUserAsInvitees(List<Invitee> userAsInvitees) {
        this.userAsInvitees = userAsInvitees;
    }

    public void addUserAsInvitees(Invitee userAsInvitee) {
        this.userAsInvitees.add(userAsInvitee);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    public boolean isEncrpted() {
        return isEncrpted;
    }

    public void setEncrpted(boolean encrpted) {
        isEncrpted = encrpted;
    }
}
