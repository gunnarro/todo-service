package org.gunnarro.microservice.todoservice.domain.dto.todo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodoItemDtoTest {

    @Test
    void isApprovedByAll() {
        assertFalse(new TodoItemDto().isApprovedByAll());
        ApprovalDto approvalDto1 = new ApprovalDto.ApprovalDtoBuilder()
                .participantId("22")
                .todoItemId("100")
                .approved(true)
                .build();

        ApprovalDto approvalDto2 = new ApprovalDto.ApprovalDtoBuilder()
                .participantId("22")
                .todoItemId("100")
                .approved(false)
                .build();

        TodoItemDto todoItemDtoNot = new TodoItemDto.TodoItemDtoBuilder()
                .approvalRequired(true)
                .approvalList(List.of(approvalDto1, approvalDto2))
                .build();

        assertFalse(todoItemDtoNot.isApprovedByAll());

        TodoItemDto todoItemDto = new TodoItemDto.TodoItemDtoBuilder()
                .approvalRequired(true)
                .approvalList(List.of(approvalDto1))
                .build();

        assertTrue(todoItemDto.isApprovedByAll());
    }
}
