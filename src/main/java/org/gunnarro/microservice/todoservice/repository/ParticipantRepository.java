package org.gunnarro.microservice.todoservice.repository;

import org.gunnarro.microservice.todoservice.repository.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("SELECT p FROM Participant p WHERE p.fkTodoId = :todoId ORDER BY p.name ASC")
    List<Participant> getParticipants(@Param("todoId") Long todoId);

    @Query("SELECT count(p) FROM Participant p WHERE p.fkTodoId = :todoId ORDER BY p.name ASC")
    Integer countParticipants(@Param("todoId") Long todoId);

    @Query("SELECT p FROM Participant p WHERE p.id = :id")
    Optional<Participant> getParticipant(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM Participant i WHERE i.id = :id")
    void deleteByUuid(@Param("id") Long id);
}
