package org.gunnarro.microservice.todoservice.domain.mapper;

import org.gunnarro.microservice.todoservice.domain.dto.todo.*;
import org.gunnarro.microservice.todoservice.repository.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TodoMapperTest {

    private final Random random = new Random();

    @Test
    void toTodoDto() {
        Todo todo = new Todo();
        todo.setId(random.nextLong());
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
        assertEquals(todo.getCreatedDate(), toDoDto.getCreatedDate());
        assertEquals(todo.getLastModifiedDate(), toDoDto.getLastModifiedDate());
        assertEquals(0, toDoDto.getTodoItemDtoList().size());
        assertEquals(0, toDoDto.getParticipantDtoList().size());
    }

    @Test
    void fromTodoDto() {
        TodoDto todoDto = TodoDto.builder()
                .id("1234245234634745869")
                .name("guro")
                .status(TodoStatus.IN_PROGRESS)
                .description("my todo list")
                .build();

        Todo toDo = TodoMapper.fromTodoDto(todoDto);
        assertEquals(todoDto.getId(), toDo.getId().toString());
        assertEquals(todoDto.getName(), toDo.getName());
        assertNull(todoDto.getCreatedByUser());
        assertNull(todoDto.getCreatedByUser());
        assertNull(todoDto.getLastModifiedByUser());
        assertNull(todoDto.getLastModifiedDate());
        assertEquals(todoDto.getDescription(), toDo.getDescription());
        assertEquals(todoDto.getStatus().name(), toDo.getStatus());
    }

    @Test
    void toTodoItemDto() {
        TodoItem todoItem = TodoItem.builder()
                .id(100L)
                .fkTodoId(10L)
                .name("tv")
                .category("stue")
                .status(TodoItemStatus.IN_PROGRESS.name())
                .description("stue")
                .action(TaskAction.TO_BE_SOLD.name())
                .assignedTo("guro")
                .priority(Priority.MEDIUM.name())
                .approvalRequired(false)
                .createdDate(LocalDateTime.of(2024, 1, 2, 12, 10, 23))
                .lastModifiedDate(LocalDateTime.of(2024, 1, 2, 12, 10, 23))
                .build();

        TodoItemDto todoItemDto = TodoMapper.toTodoItemDto(todoItem);
        assertEquals(todoItem.getId().toString(), todoItemDto.getId());
        assertEquals(todoItem.getFkTodoId().toString(), todoItemDto.getTodoId());
        assertEquals(todoItem.getName(), todoItemDto.getName());
        assertEquals(todoItem.getCategory(), todoItemDto.getCategory());
        assertEquals(todoItem.getAction(), todoItemDto.getAction().name());
        assertEquals(todoItem.getAssignedTo(), todoItemDto.getAssignedTo());
        assertEquals(todoItem.getDescription(), todoItemDto.getDescription());
        assertEquals(todoItem.getStatus(), todoItemDto.getStatus().name());
        assertEquals(todoItem.getCreatedByUser(), todoItemDto.getCreatedByUser());
        assertEquals(todoItem.getLastModifiedByUser(), todoItemDto.getLastModifiedByUser());
        assertEquals(todoItem.getApprovalRequired(), todoItemDto.getApprovalRequired());
        assertEquals(todoItem.getPriority(), todoItemDto.getPriority().name());
        assertEquals(todoItem.getCreatedDate(), todoItemDto.getCreatedDate());
        assertEquals(todoItem.getLastModifiedDate(), todoItemDto.getLastModifiedDate());
    }

    @Test
    void fromTodoItemDto() {
        TodoItemDto todoItemDto = TodoItemDto.builder()
                .id(String.valueOf(random.nextLong()))
                .todoId(String.valueOf(random.nextLong()))
                .name("tv")
                .category("stue")
                .status(TodoItemStatus.IN_PROGRESS)
                .description("stue")
                .action(TaskAction.TO_BE_SOLD)
                .assignedTo("guro")
                .approvalRequired(true)
                .priority(Priority.HIGHEST)
                .build();

        TodoItem todoItem = TodoMapper.fromTodoItemDto(todoItemDto);
        assertEquals(todoItemDto.getId(), todoItem.getId().toString());
        assertEquals(todoItemDto.getTodoId(), todoItem.getFkTodoId().toString());
        assertEquals(todoItemDto.getName(), todoItem.getName());
        assertEquals(todoItemDto.getCategory(), todoItem.getCategory());
        assertEquals(todoItemDto.getAction().name(), todoItem.getAction());
        assertEquals(todoItemDto.getAssignedTo(), todoItem.getAssignedTo());
        assertEquals(todoItemDto.getDescription(), todoItem.getDescription());
        assertEquals(todoItemDto.getStatus().name(), todoItem.getStatus());
        assertEquals(todoItemDto.getCreatedByUser(), todoItem.getCreatedByUser());
        assertEquals(todoItemDto.getLastModifiedByUser(), todoItem.getLastModifiedByUser());
        assertEquals(todoItemDto.getApprovalRequired(), todoItem.getApprovalRequired());
        assertEquals(todoItemDto.getPriority().name(), todoItem.getPriority());
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

    @Test
    void fromParticipantDto() {
        ParticipantDto participantDto = ParticipantDto.builder()
                .id("11")
                .name("gunnar")
                .email("gr@mail.org")
                .todoId("123456789")
                .enabled(1)
                .build();
        Participant participant = TodoMapper.fromParticipantDto(participantDto);
        assertEquals(participantDto.getId(), participant.getId().toString());
        assertEquals(participantDto.getTodoId(), participant.getFkTodoId().toString());
        assertEquals(participantDto.getName(), participant.getName());
        assertEquals(participantDto.getEmail(), participant.getEmail());
        assertEquals(participantDto.getEnabled(), participant.getEnabled());
    }

    @Test
    void toParticipantDto() {
        Participant participant = Participant.builder()
                .id(12345L)
                .name("gunnar")
                .email("gr@mail.org")
                .fkTodoId(123456789L)
                .enabled(0)
                .build();
        ParticipantDto participantDto = TodoMapper.toParticipantDto(participant);
        assertEquals(participant.getId().toString(), participantDto.getId());
        assertEquals(participant.getFkTodoId().toString(), participantDto.getTodoId());
        assertEquals(participant.getName(), participantDto.getName());
        assertEquals(participant.getEmail(), participantDto.getEmail());
        assertEquals(participant.getEnabled(), participantDto.getEnabled());
    }

    @Test
    void toApprovalDto() {
        Approval approval = Approval.builder()
                .id(12345L)
                .todoItemId(123456789L)
                .participantId(987654321L)
                .approved(false)
                .build();
        ApprovalDto approvalDto = TodoMapper.toApprovalDto(approval);
        assertEquals(approval.getId().toString(), approvalDto.getId());
        assertEquals(approval.getTodoItemId().toString(), approvalDto.getTodoItemId());
        assertEquals(approval.getParticipantId().toString(), approvalDto.getParticipantId());
    }

    @Test
    void fromApprovalDto() {
        ApprovalDto approvalDto = ApprovalDto.builder()
                .id("23")
                .todoItemId("123456789")
                .participantId("987654321")
                .approved(true)
                .build();
        Approval approval = TodoMapper.fromApprovalDto(approvalDto);
        assertEquals(approvalDto.getId(), approval.getId().toString());
        assertEquals(approvalDto.getTodoItemId(), approval.getTodoItemId().toString());
        assertEquals(approvalDto.getParticipantId(), approval.getParticipantId().toString());
    }
}
