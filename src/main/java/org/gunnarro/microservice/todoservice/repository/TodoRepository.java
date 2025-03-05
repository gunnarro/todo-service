package org.gunnarro.microservice.todoservice.repository;

import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, RevisionRepository<Todo, Long, Long> {

    @Query("SELECT t FROM Todo t ORDER BY t.lastModifiedDate ASC")
    List<Todo> getTodos();

    @Query("SELECT t FROM Todo t WHERE t.createdByUser LIKE :userName OR t.lastModifiedByUser LIKE :userName ORDER BY t.lastModifiedDate ASC")
    List<Todo> getTodosByUserName(@Param("userName") String userName);

    @Query("SELECT t FROM Todo t WHERE t.id = :id")
    Optional<Todo> getTodoById(@Param("id") Long id);

    @Query("SELECT name"
            + " , sum(CASE WHEN status = 'OPEN' THEN 1 ELSE 0 END) AS open"
            + " , sum(CASE WHEN status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS in_progress"
            + " , sum(CASE WHEN status = 'ON_HOLD' THEN 1 ELSE 0 END) AS on_hold"
            + " , sum(CASE WHEN status = 'DONE' THEN 1 ELSE 0 END) AS done"
            + " , sum(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled"
            + " FROM Todo"
            + " GROUP BY name")
    void getTodoStatestikkByStatus();
}
