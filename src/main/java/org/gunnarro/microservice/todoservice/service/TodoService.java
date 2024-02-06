package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.gunnarro.microservice.todoservice.repository.entity.TodoItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TodoService {

    List<TodoDto> getTodosForUser(String user);

    TodoDto getTodo(String todoId);

    void deleteTodo(String todoId);

    TodoDto updateTodo(TodoDto toDoDto);

    TodoDto addTodo(TodoDto toDoDto);

    TodoItemDto addTodoItem(TodoItemDto todoItemDto);

    TodoItemDto updateTodoItem(TodoItemDto todoItemDto);

    void deleteTodoItem(String todoId, String todoItemUuid);
}
