package org.gunnarro.microservice.todoservice.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.todo.*;
import org.gunnarro.microservice.todoservice.domain.mapper.TodoMapper;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.exception.RestInputValidationException;
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

    private final TodoRepository todoRepository;
    private final TodoItemRepository todoItemRepository;

    private final ParticipantRepository participantRepository;

    public TodoServiceImpl(TodoRepository todoRepository, TodoItemRepository todoItemRepository, ParticipantRepository participantRepository) {
        this.todoRepository = todoRepository;
        this.todoItemRepository = todoItemRepository;
        this.participantRepository = participantRepository;
    }

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
            return TodoMapper.toTodoDto(todoRepository.save(TodoMapper.fromTodoDto(todoDto)));
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
        return TodoMapper.toTodoItemDto(todoItemRepository.save(TodoMapper.fromTodoItemDto(todoItemDto)));
    }

    @Override
    public void deleteTodoItem(Long todoId, Long todoItemId) {
        todoItemRepository.deleteById(todoItemId);
    }

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