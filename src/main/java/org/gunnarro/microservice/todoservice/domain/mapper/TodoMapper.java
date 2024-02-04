package org.gunnarro.microservice.todoservice.domain.mapper;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;

import java.util.List;
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
                .uuid(todo.getUuid())
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
                .uuid(toDoDto.getUuid())
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
}
