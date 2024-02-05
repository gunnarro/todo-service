package org.gunnarro.microservice.todoservice.repository;

import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.repository.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    @Query("SELECT i FROM TodoItem i WHERE i.fkTodoId = :todoId")
    List<TodoItem> getTodoItems(@Param("todoId") Long todoId);

}
