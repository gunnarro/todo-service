package org.gunnarro.microservice.todoservice.repository;

import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.repository.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long>, RevisionRepository<TodoItem, Long, Long> {

    @Query("SELECT i FROM TodoItem i WHERE i.fkTodoId = :todoId ORDER BY i.category, i.name DESC")
    List<TodoItem> getTodoItems(@Param("todoId") Long todoId);

    @Query("SELECT i FROM TodoItem i WHERE i.id = :todoItemId AND i.fkTodoId = :todoId")
    Optional<TodoItem> getTodoItem(@Param("todoId") Long todoId, @Param("todoItemId") Long todoItemId);

    @Modifying
    @Query("DELETE FROM TodoItem i WHERE i.id = :id")
    void deleteByUuid(@Param("id") Long id);

}
