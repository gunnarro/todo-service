package org.gunnarro.microservice.todoservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.UserDto;
import org.gunnarro.microservice.todoservice.domain.mapper.TodoMapper;
import org.gunnarro.microservice.todoservice.repository.ApprovalRepository;
import org.gunnarro.microservice.todoservice.repository.ParticipantRepository;
import org.gunnarro.microservice.todoservice.repository.TodoItemRepository;
import org.gunnarro.microservice.todoservice.repository.TodoRepository;
import org.gunnarro.microservice.todoservice.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final TodoRepository todoRepository;
    private final TodoItemRepository todoItemRepository;
    private final ParticipantRepository participantRepository;
    private final ApprovalRepository approvalRepository;

    public AdminServiceImpl(TodoRepository todoRepository, TodoItemRepository todoItemRepository, ParticipantRepository participantRepository, ApprovalRepository approvalRepository) {
        this.todoRepository = todoRepository;
        this.todoItemRepository = todoItemRepository;
        this.participantRepository = participantRepository;
        this.approvalRepository = approvalRepository;
    }

    //-------------------------------------------------------------------
    // Todo
    //-------------------------------------------------------------------
    @Override
    public List<TodoDto> getTodos() {
        return TodoMapper.toTodoDtoList(todoRepository.getTodos());
    }

    @Override
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
        log.debug("deleted todo, todoId={}", todoId);
    }

    //-------------------------------------------------------------------
    // User
    //-------------------------------------------------------------------
    @Override
    public List<UserDto> getUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(Long userId) {
        log.debug("deleted user, userId={}", userId);
    }
}
