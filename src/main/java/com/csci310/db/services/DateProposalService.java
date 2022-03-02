package com.csci310.db.services;

import com.csci310.db.entities.DateProposal;
import com.csci310.db.entities.Invitee;
import com.csci310.db.entities.User;
import com.csci310.db.repositories.DateProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Component
public class DateProposalService {

    @Autowired
    DateProposalRepository dateProposalRepository;

    @Transactional
    public Optional<DateProposal> getProposalById(UUID id) {
        return dateProposalRepository.findById(id);
    }

    @Transactional
    public Optional<ArrayList<DateProposal>> getProposalByProposerId(UUID proposal_id) {
        return dateProposalRepository.findAllByProposer_Id(proposal_id);
    }

    @Transactional
    public Optional<DateProposal> getProposalByProposalName(String proposalName) {
        return dateProposalRepository.findByProposalName(proposalName);
    }

    public boolean isNameTaken(String name) {
        return dateProposalRepository.findByProposalName(name).isPresent();
    }

    @Transactional
    public void persistProposal(DateProposal proposal) {
        dateProposalRepository.save(proposal);
    }

    @Transactional
    public void deleteDateProposalById(UUID id) {
        dateProposalRepository.deleteEventByProposalId(id);
        dateProposalRepository.deleteInviteeByProposalId(id);
        dateProposalRepository.deleteDateProposalByProposalId(id);
    }

    @Transactional
    public Optional<Invitee> getInvite(String proposal_name, String invitee_name, String event_name) {
        return dateProposalRepository.getInvite(proposal_name, invitee_name, event_name);
    }

    @Transactional
    public void updateInvite(UUID id, int preferenceRating, Invitee.AvailabilityStatus availabilityStatus, Invitee.ResponseStatus responseStatus) {
        dateProposalRepository.updateInvite(id, preferenceRating, availabilityStatus, responseStatus);
    }

    @Transactional
    public void updateBestEvent(UUID id, String best_event_title) {
        dateProposalRepository.updateBestEvent(id, best_event_title);
    }

    @Transactional
    public void finalizeEvent(UUID id) {
        dateProposalRepository.finalizeEvent(id, DateProposal.ProposalState.FINALIZED);
    }

}  