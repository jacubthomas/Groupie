package com.csci310.db.repositories;

import com.csci310.db.entities.DateProposal;
import com.csci310.db.entities.Invitee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DateProposalRepository extends JpaRepository<DateProposal, UUID>
{

    Optional<DateProposal> findById(UUID id);
    Optional<ArrayList<DateProposal>> findAllByProposer_Id(UUID id);
    Optional<DateProposal> findByProposalName(String name);
    @Modifying
    @Query(value = "DELETE FROM DateProposal d WHERE d.id = :id")
    void deleteDateProposalByProposalId(UUID id);

    @Modifying
    @Query(value = "DELETE FROM Event e WHERE e.proposal.id = :id")
    void deleteEventByProposalId(UUID id);

    @Modifying
    @Query(value = "DELETE FROM Invitee i WHERE i.parentProposal.id = :id")
    void deleteInviteeByProposalId(UUID id);

    @Query(value = "SELECT i FROM Invitee i WHERE i.parentProposal.proposalName = :proposal_name AND i.user.username = :invitee_name AND i.event.title = :event_name")
    Optional<Invitee> getInvite(String proposal_name, String invitee_name, String event_name);

    @Modifying
    @Query("update Invitee i set i.preferenceRating = :preferenceRating, i.availabilityStatus = :availabilityStatus, i.responseStatus = :responseStatus where i.id = :id")
    void updateInvite(@Param(value = "id") UUID id, @Param(value = "preferenceRating") int preferenceRating, @Param(value = "availabilityStatus") Invitee.AvailabilityStatus availabilityStatus,
                      @Param(value = "responseStatus") Invitee.ResponseStatus responseStatus);

    @Modifying
    @Query("update DateProposal d set d.best_event_title =:best_event_title where d.id = :id")
    void updateBestEvent(@Param(value = "id") UUID id, @Param(value = "best_event_title") String best_event_title);

    @Modifying
    @Query("update DateProposal d set d.proposalState =:finalize where d.id = :id")
    void finalizeEvent(@Param(value = "id") UUID id, @Param(value = "finalize") DateProposal.ProposalState finalize);




}  