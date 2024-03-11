package org.gunnarro.microservice.todoservice.domain.mapper;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoHistoryDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoStatus;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.repository.entity.TodoHistory;
import org.gunnarro.microservice.todoservice.repository.entity.TodoItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoMapperTest {

    @Test
    void toTodoDto() {
        Todo todo = new Todo();
        todo.setId(new Random().nextLong());
        todo.setCreatedByUser("guro");
        todo.setLastModifiedByUser("guro-2");
        todo.setCreatedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        todo.setLastModifiedDate(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        todo.setId(23L);
        todo.setName("b39");
        todo.setDescription("my todo list");
        todo.setStatus(TodoStatus.IN_PROGRESS.name());

        TodoDto toDoDto = TodoMapper.toTodoDto(todo);
        assertEquals(todo.getId().toString(), toDoDto.getId());
        assertEquals(todo.getName(), toDoDto.getName());
        assertEquals(todo.getCreatedByUser(), toDoDto.getCreatedByUser());
        assertEquals(todo.getCreatedByUser(), toDoDto.getCreatedByUser());
        assertEquals(todo.getLastModifiedByUser(), toDoDto.getLastModifiedByUser());
        assertEquals(todo.getLastModifiedDate(), toDoDto.getLastModifiedDate());
        assertEquals(todo.getDescription(), toDoDto.getDescription());
        assertEquals(todo.getStatus(), toDoDto.getStatus().name());
        assertEquals(0, toDoDto.getTodoItemDtoList().size());
    }

    @Test
    void fromTodoDto() {
        TodoDto todoDto = TodoDto.builder()
                .id("1234245234634745869")
                .name("guro")
                .status(TodoStatus.IN_PROGRESS)
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();

        Todo toDo = TodoMapper.fromTodoDto(todoDto);
        assertEquals(todoDto.getId(), toDo.getId().toString());
        assertEquals(todoDto.getName(), toDo.getName());
        assertEquals(todoDto.getCreatedByUser(), toDo.getCreatedByUser());
        assertEquals(todoDto.getCreatedByUser(), toDo.getCreatedByUser());
        assertEquals(todoDto.getLastModifiedByUser(), toDo.getLastModifiedByUser());
        assertEquals(todoDto.getLastModifiedDate(), toDo.getLastModifiedDate());
        assertEquals(todoDto.getDescription(), toDo.getDescription());
        assertEquals(todoDto.getStatus().name(), toDo.getStatus());
    }

    @Test
    void toTodoItemDto() {
        TodoItem todoItem = TodoItem.builder()
                .id(100L)
                .fkTodoId(10L)
                .name("tv")
                .status("Active")
                .description("stue")
                .action("selges")
                .assignedTo("guro")
                .build();

        TodoItemDto todoItemDto = TodoMapper.toTodoItemDto(todoItem);
        assertEquals(todoItem.getId().toString(), todoItemDto.getId());
        assertEquals(todoItem.getFkTodoId().toString(), todoItemDto.getTodoId());
        assertEquals(todoItem.getName(), todoItemDto.getName());
        assertEquals(todoItem.getAction(), todoItemDto.getAction());
        assertEquals(todoItem.getAssignedTo(), todoItemDto.getAssignedTo());
        assertEquals(todoItem.getDescription(), todoItemDto.getDescription());
        assertEquals(todoItem.getStatus(), todoItemDto.getStatus());
        assertEquals(todoItem.getCreatedByUser(), todoItemDto.getCreatedByUser());
        assertEquals(todoItem.getLastModifiedByUser(), todoItemDto.getLastModifiedByUser());
    }

    @Test
    void fromTodoItemDto() {
        TodoItemDto todoItemDto = TodoItemDto.builder()
                .id(String.valueOf(new Random().nextLong()))
                .todoId(String.valueOf(new Random().nextLong()))
                .name("tv")
                .status("Active")
                .description("stue")
                .action("selges")
                .assignedTo("guro")
                .build();

        TodoItem todoItem = TodoMapper.fromTodoItemDto(todoItemDto);
        assertEquals(todoItemDto.getId(), todoItem.getId().toString());
        assertEquals(todoItemDto.getTodoId(), todoItem.getFkTodoId().toString());
        assertEquals(todoItemDto.getName(), todoItem.getName());
        assertEquals(todoItemDto.getAction(), todoItem.getAction());
        assertEquals(todoItemDto.getAssignedTo(), todoItem.getAssignedTo());
        assertEquals(todoItemDto.getDescription(), todoItem.getDescription());
        assertEquals(todoItemDto.getStatus(), todoItem.getStatus());
        assertEquals(todoItemDto.getCreatedByUser(), todoItem.getCreatedByUser());
        assertEquals(todoItemDto.getLastModifiedByUser(), todoItem.getLastModifiedByUser());
    }

    @Test
    void toTodoHistoryDto() {
        List<TodoHistory> todoHistoryList = new ArrayList<>();
        TodoHistory todoHistory = TodoHistory.builder()
                .id(23L)
                .name("unit-test")
                .revisionId(2222222)
                .revisionType(1)
                .revisionEndId(1111111)
                .build();
        todoHistoryList.add(todoHistory);
        List<TodoHistoryDto> todoHistoryDtoList = TodoMapper.toTodoHistoryDtoList(todoHistoryList);
        assertEquals(1, todoHistoryDtoList.size());
        assertEquals(todoHistory.getName(), todoHistoryDtoList.get(0).getName());
        assertEquals(todoHistory.getRevisionEndId(), todoHistoryDtoList.get(0).getRevisionEndId());
        assertEquals(todoHistory.getRevisionId(), todoHistoryDtoList.get(0).getRevisionNumber().intValue());
        assertEquals(TodoHistoryDto.RevisionTypesEnum.getByType(todoHistory.getRevisionType()).name(), todoHistoryDtoList.get(0).getRevisionType());
        assertEquals(todoHistory.getId().toString(), todoHistoryDtoList.get(0).getId());
    }

}
