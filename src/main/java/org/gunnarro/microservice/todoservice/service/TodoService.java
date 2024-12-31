package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.domain.dto.todo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    List<TodoDto> getTodosByUserName(String userName);

    TodoDto getTodo(Long todoId);

    TodoDto createTodo(TodoDto toDoDto);

    TodoDto updateTodo(TodoDto toDoDto);

    void deleteTodo(Long todoId);

    TodoItemDto createTodoItem(TodoItemDto todoItemDto);

    TodoItemDto updateTodoItem(TodoItemDto todoItemDto);

    TodoItemDto getTodoItem(Long todoId, Long todoItemId);

    void deleteTodoItem(Long todoId, Long todoItemId);

    List<TodoHistoryDto> getTodoHistory(Long todoId);

    List<TodoItemHistoryDto> getTodoItemHistory(Long todoId, Long todoItemId);

    List<ParticipantDto> getParticipants(Long todoId);

    ParticipantDto createParticipant(ParticipantDto participantDto);

    ParticipantDto updateParticipant(ParticipantDto participantDto);

    void deleteParticipant(Long todoId, Long participantId);

    List<ApprovalDto> getApprovals(Long todoId, Long todoItemId);

    ApprovalDto createApproval(ApprovalDto approvalDto);

    ApprovalDto updateApproval(ApprovalDto approvalDto);

    void deleteApproval(Long todoItemId, Long participantId);
}
