package org.gunnarro.microservice.todoservice.repository;

import org.gunnarro.microservice.todoservice.repository.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    @Query("SELECT a FROM Approval a WHERE a.todoItemId = :todoItemId ")
    List<Approval> getApprovals(@Param("todoItemId") Long todoItemId);

    @Query("SELECT a FROM Approval a WHERE a.id = :id")
    Optional<Approval> getApproval(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM Approval a WHERE a.todoItemId = :todoItemId AND a.participantId = :participantId")
    void deleteApproval(@Param("todoItemId") Long todoItemId, @Param("participantId") Long participantId);

}
