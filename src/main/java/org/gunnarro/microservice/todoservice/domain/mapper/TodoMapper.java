package org.gunnarro.microservice.todoservice.domain.mapper;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoHistoryDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemHistoryDto;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.repository.entity.TodoHistory;
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
                .id(todo.getId().toString())
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
                .id(Long.valueOf(toDoDto.getId()))
                .name(toDoDto.getName())
                .status(toDoDto.getStatus())
                .description(toDoDto.getDescription())
                .createdDate(toDoDto.getCreatedDate())
                .lastModifiedDate(toDoDto.getLastModifiedDate())
                .createdByUser(toDoDto.getCreatedByUser())
                .lastModifiedByUser(toDoDto.getLastModifiedByUser())
                .todoItemList(List.of())
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
                .id(todoItem.getId().toString())
                .todoId(todoItem.getFkTodoId().toString())
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
                .id(Long.valueOf(todoItemDto.getId()))
                .fkTodoId(Long.valueOf(todoItemDto.getTodoId()))
                .name(todoItemDto.getName())
                .description(todoItemDto.getDescription())
                .status(todoItemDto.getStatus())
                .action(todoItemDto.getAction())
                .assignedTo(todoItemDto.getAssignedTo())
                .createdByUser(todoItemDto.getCreatedByUser())
                .lastModifiedByUser(todoItemDto.getLastModifiedByUser())
                .build();
    }


    public static List<TodoHistoryDto> toTodoHistoryDtoList(List<TodoHistory> todoHistoryList) {
        if (todoHistoryList == null) {
            return List.of();
        }
        return todoHistoryList.stream().map(TodoMapper::toTodoHistoryDto).collect(Collectors.toList());
    }

    public static TodoHistoryDto toTodoHistoryDto(TodoHistory todoHistory) {
        if (todoHistory == null) {
            return null;
        }
        return TodoHistoryDto.builder()
                .id(todoHistory.getId().toString())
                .name(todoHistory.getName())
                .status(todoHistory.getStatus())
                .description(todoHistory.getDescription())
                .createdDate(todoHistory.getCreatedDate())
                .lastModifiedDate(todoHistory.getLastModifiedDate())
                .createdByUser(todoHistory.getCreatedByUser())
                .lastModifiedByUser(todoHistory.getLastModifiedByUser())
                .revisionType(TodoHistoryDto.RevisionTypesEnum.getByType(todoHistory.getRevisionType()).name())
                .revisionNumber(todoHistory.getRevisionId().longValue())
                .revisionEndId(todoHistory.getRevisionEndId())
                .build();
    }

    public static TodoHistoryDto toTodoHistoryDto(Todo todo, Long revisionNumber, String revisionType) {
        if (todo == null) {
            return null;
        }
        return TodoHistoryDto.builder()
                .id(todo.getId().toString())
                .name(todo.getName())
                .status(todo.getStatus())
                .description(todo.getDescription())
                .createdDate(todo.getCreatedDate())
                .lastModifiedDate(todo.getLastModifiedDate())
                .createdByUser(todo.getCreatedByUser())
                .lastModifiedByUser(todo.getLastModifiedByUser())
                .revisionType(revisionType)
                .revisionNumber(revisionNumber)
                .build();
    }

    public static TodoItemHistoryDto toTodoItemHistoryDto(TodoItem todoItem, Long revisionNumber, String revisionType) {
        if (todoItem == null) {
            return null;
        }
        return TodoItemHistoryDto.builder()
                .id(todoItem.getId().toString())
                .todoId(todoItem.getFkTodoId().toString())
                .name(todoItem.getName())
                .status(todoItem.getStatus())
                .action(todoItem.getAction())
                .assignedTo(todoItem.getAssignedTo())
                .description(todoItem.getDescription())
                .createdDate(todoItem.getCreatedDate())
                .lastModifiedDate(todoItem.getLastModifiedDate())
                .createdByUser(todoItem.getCreatedByUser())
                .lastModifiedByUser(todoItem.getLastModifiedByUser())
                .revisionType(revisionType)
                .revisionNumber(revisionNumber)
                .build();
    }
}
