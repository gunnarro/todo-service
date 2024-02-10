package org.gunnarro.microservice.todoservice.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.gunnarro.microservice.todoservice.domain.mapper.TodoMapper;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.exception.RestInputValidationException;
import org.gunnarro.microservice.todoservice.repository.TodoItemRepository;
import org.gunnarro.microservice.todoservice.repository.TodoRepository;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    List<TodoDto> createTodoTestData() {
        Long b39TodoId = 2000L;
        Long stvgt35TodoId = 3000L;
        List<TodoItemDto> b39ToDoItemDtoList = List.of(createItem(b39TodoId, "tv", "Active"));
        List<TodoItemDto> stv35ToDoItemDtoList = List.of(createItem(stvgt35TodoId, "fryser", "Active"), createItem(stvgt35TodoId, "stol", "Finished"));
        return List.of(TodoDto.builder()
                        .name("B39")
                        .id(b39TodoId)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .lastModifiedByUser("adm")
                        .status("Active")
                        .toDoItemDtoList(b39ToDoItemDtoList)
                        .build(),
                TodoDto.builder()
                        .name("STV35")
                        .id(stvgt35TodoId)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .lastModifiedByUser("adm")
                        .status("Finished")
                        .toDoItemDtoList(stv35ToDoItemDtoList)
                        .build());
    }

    TodoItemDto createItem(Long todoId, String name, String status) {
        return TodoItemDto.builder()
                .id(new Random().nextLong())
                .todoId(todoId)
                .name(name)
                .description("stue")
                .action("selges")
                .status(status)
                .assignedTo("guro")
                .build();
    }
}