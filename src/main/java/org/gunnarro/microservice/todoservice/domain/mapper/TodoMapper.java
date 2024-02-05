package org.gunnarro.microservice.todoservice.domain.mapper;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.repository.entity.TodoItem;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TodoMapper {

    public static List<TodoDto> toTodoDtoList(List<Todo> todoList) {
        return todoList.stream().map(TodoMapper::toTodoDto).collect(Collectors.toList());
    }

    public static TodoDto toTodoDto(Todo todo) {
        if (todo == null) {
            return null;
        }
        return TodoDto.builder()
                .id(todo.getUuid().toString())
                .name(todo.getName())
                .status(todo.getStatus())
                .description(todo.getDescription())
                .createdDate(todo.getCreatedDate())
                .lastModifiedDate(todo.getLastModifiedDate())
                .createdByUser(todo.getCreatedByUser())
                .lastModifiedByUser(todo.getLastModifiedByUser())
                .build();
    }

    public static Todo fromTodoDto(TodoDto toDoDto) {
        if (toDoDto == null) {
            return null;
        }
        return Todo.builder()
                .uuid(UUID.fromString(toDoDto.getId()))
                .name(toDoDto.getName())
                .status(toDoDto.getStatus())
                .description(toDoDto.getDescription())
                .createdDate(toDoDto.getCreatedDate())
                .lastModifiedDate(toDoDto.getLastModifiedDate())
                .createdByUser(toDoDto.getCreatedByUser())
                .lastModifiedByUser(toDoDto.getLastModifiedByUser())
                .build();
    }

    public static void updateTodo(Todo todo, TodoDto toDoDto) {
        todo.setName(toDoDto.getName());
        todo.setDescription(toDoDto.getDescription());
        todo.setStatus(toDoDto.getStatus());
        todo.setLastModifiedByUser(toDoDto.getLastModifiedByUser());
    }


    public static TodoItemDto todoItemDto(TodoItem todoItem) {
        return TodoItemDto.builder()
                .id(todoItem.getId().toString())
                .todoId(todoItem.getFkTodoId().toString())
                .name(todoItem.getName())
                .description(todoItem.getDescription())
                .status(todoItem.getStatus())
                .action(todoItem.getAction())
                .assignedTo(todoItem.getAssignedTo())
                .build();
    }

    public static TodoItem fromTodoItemDto(TodoItemDto todoItemDto) {
        return TodoItem.builder()
               // .id(todoItemDto.getId().toString())
               // .todoId(todoItemDto.getTodoId())
                .name(todoItemDto.getName())
                .description(todoItemDto.getDescription())
                .status(todoItemDto.getStatus())
                .action(todoItemDto.getAction())
                .assignedTo(todoItemDto.getAssignedTo())
                .build();
    }
}
