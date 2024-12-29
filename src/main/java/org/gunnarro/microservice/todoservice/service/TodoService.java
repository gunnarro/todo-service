package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.domain.dto.todo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    List<TodoDto> getTodos();

    List<TodoDto> getTodosByUserName(String userName);

    TodoDto getTodo(Long todoId);

    void deleteTodo(Long todoId);

    TodoDto updateTodo(TodoDto toDoDto);

    TodoDto addTodo(TodoDto toDoDto);

    TodoItemDto addTodoItem(TodoItemDto todoItemDto);

    TodoItemDto updateTodoItem(TodoItemDto todoItemDto);

    TodoItemDto getTodoItem(Long todoId, Long todoItemId);

    void deleteTodoItem(Long todoId, Long todoItemId);

    List<TodoHistoryDto> getTodoHistory(Long todoId);

    List<TodoItemHistoryDto> getTodoItemHistory(Long todoId, Long todoItemId);

    List<ParticipantDto> getParticipants(Long todoId);

    ParticipantDto addParticipant(ParticipantDto participantDto);

    ParticipantDto updateParticipant(ParticipantDto participantDto);

    void deleteParticipant(Long todoId, Long participantId);

    List<ApprovalDto> getApprovals(Long todoId, Long todoItemId);

    ApprovalDto addApproval(ApprovalDto approvalDto);

    ApprovalDto updateApproval(ApprovalDto approvalDto);

    void deleteApproval(Long todoItemId, Long participantId);
}
