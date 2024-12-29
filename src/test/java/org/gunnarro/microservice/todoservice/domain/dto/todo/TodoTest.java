package org.gunnarro.microservice.todoservice.domain.dto.todo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TodoTest {

    @Test
    void todo() {

        TodoItemDto todoItemDtoNo = new TodoItemDto.TodoItemDtoBuilder()
                .id("200")
                .approvalRequired(true)
                .build();

        ParticipantDto participantDto = new ParticipantDto.ParticipantDtoBuilder()
                .id("22")
                .todoId("200")
                .build();

        TodoDto todo = TodoDto.builder()
                .id("200")
                .name("Todo unit test")
                .todoItemDtoList(List.of(todoItemDtoNo))
                .participantDtoList(List.of(participantDto))
                .build();

        assertFalse(todo.getTodoItemDtoList().isEmpty());
        assertFalse(todo.getParticipantDtoList().isEmpty());
    }
}
