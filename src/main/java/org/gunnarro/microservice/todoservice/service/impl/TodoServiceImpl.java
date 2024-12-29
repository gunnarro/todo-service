package org.gunnarro.microservice.todoservice.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.todo.*;
import org.gunnarro.microservice.todoservice.domain.mapper.TodoMapper;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.exception.RestInputValidationException;
import org.gunnarro.microservice.todoservice.repository.ApprovalRepository;
import org.gunnarro.microservice.todoservice.repository.ParticipantRepository;
import org.gunnarro.microservice.todoservice.repository.TodoItemRepository;
import org.gunnarro.microservice.todoservice.repository.TodoRepository;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.repository.entity.TodoItem;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {

    private final static String DEFAULT_USER = "guest";

    private final TodoRepository todoRepository;
    private final TodoItemRepository todoItemRepository;
    private final ParticipantRepository participantRepository;
    private final ApprovalRepository approvalRepository;

    public TodoServiceImpl(TodoRepository todoRepository, TodoItemRepository todoItemRepository, ParticipantRepository participantRepository, ApprovalRepository approvalRepository) {
        this.todoRepository = todoRepository;
        this.todoItemRepository = todoItemRepository;
        this.participantRepository = participantRepository;
        this.approvalRepository = approvalRepository;
    }

    //-------------------------------------------------------------------
    // Todo admin
    //-------------------------------------------------------------------
    @Override
    public List<TodoDto> getTodos() {
        return TodoMapper.toTodoDtoList(todoRepository.getTodos());
    }

    //-------------------------------------------------------------------
    // Todo
    //-------------------------------------------------------------------
    @Override
    public List<TodoDto> getTodosByUserName(String userName) {
        return TodoMapper.toTodoDtoList(todoRepository.getTodosByUserName(userName));
    }

    @Override
    public TodoDto getTodo(Long todoId) {
        log.debug("todoId={}", todoId);
        Optional<Todo> todoOpt = todoRepository.getTodoById(todoId);
        if (todoOpt.isPresent())
            return TodoMapper.toTodoDto(todoOpt.get());
        else {
            return new TodoDto();
            //throw new NotFoundException("todoId=" + todoId);
        }
    }

    @Override
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
        log.debug("deleted todo, todoId={}", todoId);
    }

    @Transactional
    @Override
    public TodoDto updateTodo(TodoDto todoDto) {
        try {
            return TodoMapper.toTodoDto(TodoMapper.fromTodoDto(todoDto));
        } catch (DataIntegrityViolationException ex) {
            throw new RestInputValidationException(ex.getMessage());
        } catch (Exception e) {
            throw new ApplicationException(String.format("error update todo! error: %s", e.getMessage()), e.getCause());
        }
    }

    // @Mapper(componentModel = "spring") may also use mapstruct
    @Transactional
    @Override
    public TodoDto addTodo(TodoDto todoDto) {
        try {
            log.debug("save todo, {}", todoDto);
            return TodoMapper.toTodoDto(todoRepository.save(TodoMapper.fromTodoDto(todoDto)));
        } catch (DataIntegrityViolationException ex) {
            throw new RestInputValidationException(ex.getMessage());
        } catch (Exception e) {
            throw new ApplicationException(String.format("error add todo! error: %s", e.getMessage()), e.getCause());
        }
    }

    //-------------------------------------------------------------------
    // Todo items
    //-------------------------------------------------------------------

    public TodoItemDto getTodoItem(Long todoId, Long todoItemId) {
        return TodoMapper.toTodoItemDto(todoItemRepository.getTodoItem(todoId, todoItemId).orElse(null));
    }

    @Transactional
    @Override
    public TodoItemDto addTodoItem(TodoItemDto todoItemDto) {
        try {
            log.debug("{}", todoItemDto);
            TodoItem todoItem = TodoMapper.fromTodoItemDto(todoItemDto);
            log.debug("{}", todoItem);
            return TodoMapper.toTodoItemDto(todoItemRepository.save(todoItem));
        } catch (DataIntegrityViolationException ex) {
            throw new RestInputValidationException(ex.getMessage());
        } catch (Exception e) {
            throw new ApplicationException(String.format("error add todo item! error: %s", e.getMessage()), e.getCause());
        }
    }

    @Transactional
    @Override
    public TodoItemDto updateTodoItem(TodoItemDto todoItemDto) {
        TodoItem todoItem = TodoMapper.fromTodoItemDto(todoItemDto);
        log.debug("{}", todoItem);
        return TodoMapper.toTodoItemDto(todoItemRepository.save(todoItem));
    }

    @Override
    public void deleteTodoItem(Long todoId, Long todoItemId) {
        todoItemRepository.deleteById(todoItemId);
    }


    public boolean isApprovedByAll(Long todoItemId) {
        return false;
    }

    //-------------------------------------------------------------------
    // Participants
    //-------------------------------------------------------------------
    @Override
    public List<ParticipantDto> getParticipants(Long todoId) {
        return TodoMapper.toParticipantDtoList(participantRepository.getParticipants(todoId));
    }

    @Override
    public ParticipantDto addParticipant(ParticipantDto participantDto) {
        return TodoMapper.toParticipantDto(participantRepository.save(TodoMapper.fromParticipantDto(participantDto)));
    }

    @Override
    public ParticipantDto updateParticipant(ParticipantDto participantDto) {
        return TodoMapper.toParticipantDto(participantRepository.save(TodoMapper.fromParticipantDto(participantDto)));
    }

    @Override
    public void deleteParticipant(Long todoId, Long participantId) {
        participantRepository.deleteById(participantId);
        log.debug("deleted participant, todoId={}, participantId={}", todoId, participantId);
    }

    //-------------------------------------------------------------------
    // Approvals
    //-------------------------------------------------------------------
    @Override
    public List<ApprovalDto> getApprovals(Long todoId, Long todoItemId) {
       return TodoMapper.toApprovalDtoList(approvalRepository.getApprovals(todoItemId));
    }

    @Override
    public ApprovalDto addApproval(ApprovalDto approvalDto) {
        return TodoMapper.toApprovalDto(approvalRepository.save(TodoMapper.fromApprovalDto(approvalDto)));
    }

    @Override
    public ApprovalDto updateApproval(ApprovalDto approvalDto) {
        return TodoMapper.toApprovalDto(approvalRepository.save(TodoMapper.fromApprovalDto(approvalDto)));
    }

    @Override
    public void deleteApproval(Long todoItemId, Long participantId) {
        approvalRepository.deleteApproval(todoItemId, participantId);
        log.debug("deleted approval, todoItemId={}, participantId={}", todoItemId, participantId);
    }

    //-------------------------------------------------------------------
    // History
    //-------------------------------------------------------------------
    public List<TodoHistoryDto> getTodoHistory(Long todoId) {
        List<TodoHistoryDto> todoHistoryDtoList = new ArrayList<>();
        Revisions<Long, Todo> revisions = this.todoRepository.findRevisions(todoId);
        Iterator<Revision<Long, Todo>> revisionsIterator = revisions.stream().iterator();
        while (revisionsIterator.hasNext()) {
            Revision<Long, Todo> rev = revisionsIterator.next();
            todoHistoryDtoList.add(TodoMapper.toTodoHistoryDto(rev.getEntity(), null, rev.getMetadata().getRevisionType().name()));
        }
        return todoHistoryDtoList;
    }

    public List<TodoItemHistoryDto> getTodoItemHistory(Long todoId, Long todoItemId) {
        List<TodoItemHistoryDto> todoItemHistoryDtoList = new ArrayList<>();
        Revisions<Long, TodoItem> revisions = this.todoItemRepository.findRevisions(todoItemId);
        Iterator<Revision<Long, TodoItem>> revisionsIterator = revisions.stream().iterator();
        while (revisionsIterator.hasNext()) {
            Revision<Long, TodoItem> rev = revisionsIterator.next();
            todoItemHistoryDtoList.add(TodoMapper.toTodoItemHistoryDto(rev.getEntity(), null, rev.getMetadata().getRevisionType().name()));
        }
        return todoItemHistoryDtoList;
    }
}