package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.domain.dto.todo.ToDoDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ToDoService {

    List<ToDoDto> getTodosForUser(String user);

    ToDoDto getTodo(UUID uuid);

    void deleteTodo(String uuid);

    ToDoDto updateTodo(ToDoDto toDoDto);

    ToDoDto addTodo(ToDoDto toDoDto);
}
