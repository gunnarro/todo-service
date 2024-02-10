package org.gunnarro.microservice.todoservice.repository;

import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t WHERE t.createdByUser LIKE :user OR t.lastModifiedByUser LIKE :user")
    List<Todo> getTodosForUser(@Param("user") String user);

    @Query("SELECT t FROM Todo t WHERE t.id = :id")
    Optional<Todo> getTodoByUuid(@Param("id") Long id);

   // @Modifying
   // @Query("DELETE FROM Todo t WHERE t.id = :id")
   // void delete(@Param("id") Long id);

}
