package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoHistoryDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    List<TodoDto> getTodosForUser(String user);

    TodoDto getTodo(Long todoId);

    void deleteTodo(Long todoId);

    TodoDto updateTodo(TodoDto toDoDto);

    TodoDto addTodo(TodoDto toDoDto);

    TodoItemDto addTodoItem(TodoItemDto todoItemDto);

    TodoItemDto updateTodoItem(TodoItemDto todoItemDto);

    void deleteTodoItem(Long todoId, Long todoItemUuid);

    List<TodoHistoryDto> getTodoHistory(Long todoId);
}
