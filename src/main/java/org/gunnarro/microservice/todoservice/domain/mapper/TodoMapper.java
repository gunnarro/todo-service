package org.gunnarro.microservice.todoservice.domain.mapper;

import org.gunnarro.microservice.todoservice.domain.dto.todo.*;
import org.gunnarro.microservice.todoservice.repository.entity.*;

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
                .status(TodoStatus.valueOf(todo.getStatus()))
                .description(todo.getDescription())
                .createdDate(todo.getCreatedDate())
                .lastModifiedDate(todo.getLastModifiedDate())
                .createdByUser(todo.getCreatedByUser())
                .lastModifiedByUser(todo.getLastModifiedByUser())
                .todoItemDtoList(todoItemDtoList(todo.getTodoItemList()))
                .participantDtoList(toParticipantDtoList(todo.getParticipantList()))
                .build();
    }

    public static Todo fromTodoDto(TodoDto toDoDto) {
        if (toDoDto == null) {
            return null;
        }
        return Todo.builder()
                .id(toDoDto.getId() != null ? Long.valueOf(toDoDto.getId()) : null)
                .name(toDoDto.getName())
                .status(toDoDto.getStatus().name())
                .description(toDoDto.getDescription())
                .todoItemList(List.of())
                .build();
    }

    public static void updateTodo(Todo todo, TodoDto toDoDto) {
        todo.setName(toDoDto.getName());
        todo.setDescription(toDoDto.getDescription());
        todo.setStatus(toDoDto.getStatus().name());
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
                .category(todoItem.getCategory())
                .description(todoItem.getDescription())
                .status(TodoItemStatus.valueOf(todoItem.getStatus()))
                .action(TaskAction.valueOf(todoItem.getAction()))
                .assignedTo(todoItem.getAssignedTo())
                .createdDate(todoItem.getCreatedDate())
                .lastModifiedDate(todoItem.getLastModifiedDate())
                .createdByUser(todoItem.getCreatedByUser())
                .lastModifiedByUser(todoItem.getLastModifiedByUser())
                .priority(Priority.valueOf(todoItem.getPriority()))
                .approvalRequired(todoItem.getApprovalRequired())
                .approvalList(toApprovalDtoList(todoItem.getApprovalList()))
                .build();
    }

    public static TodoItem fromTodoItemDto(TodoItemDto todoItemDto) {
        if (todoItemDto == null) {
            return null;
        }
        return TodoItem.builder()
                .id(todoItemDto.getId() != null ? Long.valueOf(todoItemDto.getId()) : null)
                .fkTodoId(Long.valueOf(todoItemDto.getTodoId()))
                .name(todoItemDto.getName())
                .category(todoItemDto.getCategory())
                .description(todoItemDto.getDescription())
                .status(todoItemDto.getStatus().name())
                .action(todoItemDto.getAction().name())
                .assignedTo(todoItemDto.getAssignedTo())
                .priority(todoItemDto.getPriority().name())
                .approvalRequired(todoItemDto.getApprovalRequired())
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
                .todoId(todoItem.getFkTodoId() != null ? todoItem.getFkTodoId().toString() : null)
                .name(todoItem.getName())
                .status(todoItem.getStatus())
                .action(todoItem.getAction())
                .assignedTo(todoItem.getAssignedTo())
                .description(todoItem.getDescription())
                .createdDate(todoItem.getCreatedDate())
                .lastModifiedDate(todoItem.getLastModifiedDate())
                .createdByUser(todoItem.getCreatedByUser())
                .lastModifiedByUser(todoItem.getLastModifiedByUser())
                .priority(todoItem.getPriority() != null ? Priority.valueOf(todoItem.getPriority()) : null)
                .revisionType(revisionType)
                .revisionNumber(revisionNumber)
                .build();
    }

    public static List<ParticipantDto> toParticipantDtoList(List<Participant> participantList) {
        if (participantList == null) {
            return List.of();
        }
        return participantList.stream().map(TodoMapper::toParticipantDto).collect(Collectors.toList());
    }

    public static ParticipantDto toParticipantDto(Participant participant) {
        if (participant == null) {
            return null;
        }
        return ParticipantDto.builder()
                .id(participant.getId().toString())
                .name(participant.getName())
                .todoId(participant.getFkTodoId().toString())
                .email(participant.getEmail())
                .enabled(participant.getEnabled())
                .build();
    }

    public static Participant fromParticipantDto(ParticipantDto participantDto) {
        if (participantDto == null) {
            return null;
        }
        return Participant.builder()
                .id(participantDto.getId() != null ? Long.valueOf(participantDto.getId()) : null)
                .fkTodoId(Long.valueOf(participantDto.getTodoId()))
                .name(participantDto.getName())
                .email(participantDto.getEmail())
                .enabled(participantDto.getEnabled())
                .build();
    }

    public static List<ApprovalDto> toApprovalDtoList(List<Approval> ApprovalList) {
        if (ApprovalList == null) {
            return List.of();
        }
        return ApprovalList.stream().map(TodoMapper::toApprovalDto).collect(Collectors.toList());
    }

    public static Approval fromApprovalDto(ApprovalDto approvalDto) {
        if (approvalDto == null) {
            return null;
        }
        return Approval.builder()
                .id(approvalDto.getId() != null ? Long.valueOf(approvalDto.getId()) : null)
                .participantId(Long.valueOf(approvalDto.getParticipantId()))
                .todoItemId(Long.valueOf(approvalDto.getTodoItemId()))
                .approved(approvalDto.getApproved())
                .build();
    }

    public static ApprovalDto toApprovalDto(Approval approval) {
        if (approval == null) {
            return null;
        }
        return ApprovalDto.builder()
                .id(approval.getId().toString())
                .participantId(approval.getParticipantId().toString())
                .todoItemId(approval.getTodoItemId().toString())
                .approved(approval.getApproved())
                .build();
    }
}
