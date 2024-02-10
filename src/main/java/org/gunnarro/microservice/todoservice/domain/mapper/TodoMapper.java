package org.gunnarro.microservice.todoservice.domain.mapper;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.repository.entity.TodoItem;

import java.util.List;
import java.util.stream.Collectors;

public class TodoMapper {

    public static List<TodoDto> toTodoDtoList(List<Todo> todoList) {
        if (todoList == null) {
            return List.of();
        }
        return todoList.stream().map(TodoMapper::toTodoDto).collect(Collectors.toList());
    }

    public static TodoDto toTodoDto(Todo todo) {
        if (todo == null) {
            return null;
        }
        return TodoDto.builder()
                .id(todo.getId())
                .idStr(todo.getId().toString())
                .name(todo.getName())
                .status(todo.getStatus())
                .description(todo.getDescription())
                .createdDate(todo.getCreatedDate())
                .lastModifiedDate(todo.getLastModifiedDate())
                .createdByUser(todo.getCreatedByUser())
                .lastModifiedByUser(todo.getLastModifiedByUser())
                .toDoItemDtoList(todoItemDtoList(todo.getTodoItemList()))
                .build();
    }

    public static Todo fromTodoDto(TodoDto toDoDto) {
        if (toDoDto == null) {
            return null;
        }
        return Todo.builder()
                .id(toDoDto.getId())
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


    public static List<TodoItemDto> todoItemDtoList(List<TodoItem> todoItemList) {
        if (todoItemList == null) {
            return List.of();
        }
        return todoItemList.stream().map(TodoMapper::toTodoItemDto).collect(Collectors.toList());
    }

    public static TodoItemDto toTodoItemDto(TodoItem todoItem) {
        if (todoItem == null) {
            return null;
        }
        return TodoItemDto.builder()
                .id(todoItem.getId())
                .todoId(todoItem.getFkTodoId())
                .name(todoItem.getName())
                .description(todoItem.getDescription())
                .status(todoItem.getStatus())
                .action(todoItem.getAction())
                .assignedTo(todoItem.getAssignedTo())
                .createdByUser(todoItem.getCreatedByUser())
                .lastModifiedByUser(todoItem.getLastModifiedByUser())
                .build();
    }

    public static TodoItem fromTodoItemDto(TodoItemDto todoItemDto) {
        if (todoItemDto == null) {
            return null;
        }
        return TodoItem.builder()
                .id(todoItemDto.getId())
                .fkTodoId(todoItemDto.getTodoId())
                .name(todoItemDto.getName())
                .description(todoItemDto.getDescription())
                .status(todoItemDto.getStatus())
                .action(todoItemDto.getAction())
                .assignedTo(todoItemDto.getAssignedTo())
                .createdByUser(todoItemDto.getCreatedByUser())
                .lastModifiedByUser(todoItemDto.getLastModifiedByUser())
                .build();
    }
}
