package org.gunnarro.microservice.todoservice.domain.mapper;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoMapperTest {

    @Test
    void toTodoDto() {
        Todo todo = new Todo();
        todo.setUuid(UUID.randomUUID());
        todo.setCreatedByUser("guro");
        todo.setLastModifiedByUser("guro-2");
        todo.setCreatedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        todo.setLastModifiedDate(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        todo.setId(23L);
        todo.setName("b39");
        todo.setDescription("my todo list");
        todo.setStatus("Active");
        todo.setTodoItemList(List.of());

        TodoDto toDoDto = TodoMapper.toTodoDto(todo);
        assertEquals(todo.getUuid().toString(), toDoDto.getUuid().toString());
        assertEquals(todo.getName(), toDoDto.getName());
        assertEquals(todo.getCreatedByUser(), toDoDto.getCreatedByUser());
        assertEquals(todo.getCreatedByUser(), toDoDto.getCreatedByUser());
        assertEquals(todo.getLastModifiedByUser(), toDoDto.getLastModifiedByUser());
        assertEquals(todo.getLastModifiedDate(), toDoDto.getLastModifiedDate());
        assertEquals(todo.getDescription(), toDoDto.getDescription());
        assertEquals(todo.getStatus(), toDoDto.getStatus());
       // assertEquals(0, toDoDto.getToDoItemDtoList().size());
    }

    @Test
    void fromTodoDto() {
        TodoDto todoDto =  TodoDto.builder()
                .uuid(UUID.randomUUID())
                .name("guro")
                .status("Active")
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();

        Todo toDo = TodoMapper.fromTodoDto(todoDto);
        assertEquals(todoDto.getUuid().toString(), toDo.getUuid().toString());
        assertEquals(todoDto.getName(), toDo.getName());
        assertEquals(todoDto.getCreatedByUser(), toDo.getCreatedByUser());
        assertEquals(todoDto.getCreatedByUser(), toDo.getCreatedByUser());
        assertEquals(todoDto.getLastModifiedByUser(), toDo.getLastModifiedByUser());
        assertEquals(todoDto.getLastModifiedDate(), toDo.getLastModifiedDate());
        assertEquals(todoDto.getDescription(), toDo.getDescription());
        assertEquals(todoDto.getStatus(), toDo.getStatus());
        // assertEquals(0, toDoDto.getToDoItemDtoList().size());
    }
}
