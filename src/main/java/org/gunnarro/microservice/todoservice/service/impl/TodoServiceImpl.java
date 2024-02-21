package org.gunnarro.microservice.todoservice.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoHistoryDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemHistoryDto;
import org.gunnarro.microservice.todoservice.domain.mapper.TodoMapper;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.exception.RestInputValidationException;
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

    public TodoServiceImpl(TodoRepository todoRepository, TodoItemRepository todoItemRepository) {
        this.todoRepository = todoRepository;
        this.todoItemRepository = todoItemRepository;
    }

    @Override
    public List<TodoDto> getTodosForUser(String user) {
        return TodoMapper.toTodoDtoList(todoRepository.getTodosForUser(user));
    }

    @Override
    public TodoDto getTodo(Long todoId) {
        log.debug("todoId={}", todoId);
        Optional<Todo> todoOpt = todoRepository.getTodoByUuid(todoId);
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
            return TodoMapper.toTodoItemDto(todoItemRepository.save(TodoMapper.fromTodoItemDto(todoItemDto)));
        } catch (DataIntegrityViolationException ex) {
            throw new RestInputValidationException(ex.getMessage());
        } catch (Exception e) {
            throw new ApplicationException(String.format("error add todo item! error: %s", e.getMessage()), e.getCause());
        }
    }

    @Transactional
    @Override
    public TodoItemDto updateTodoItem(TodoItemDto todoItemDto) {
        return null;
    }

    @Override
    public void deleteTodoItem(Long todoId, Long todoItemId) {
        todoItemRepository.deleteById(todoItemId);
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