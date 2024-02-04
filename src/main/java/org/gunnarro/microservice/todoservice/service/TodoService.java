package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TodoService {

    List<TodoDto> getTodosForUser(String user);

    TodoDto getTodo(UUID uuid);

    void deleteTodo(String uuid);

    TodoDto updateTodo(TodoDto toDoDto);

    TodoDto addTodo(TodoDto toDoDto);
}
