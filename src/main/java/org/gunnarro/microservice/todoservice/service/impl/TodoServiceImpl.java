package org.gunnarro.microservice.todoservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.mapper.TodoMapper;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.exception.RestInputValidationException;
import org.gunnarro.microservice.todoservice.repository.TodoRepository;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<TodoDto> getTodosForUser(String user) {
        log.debug("");
        return TodoMapper.toTodoDtoList(todoRepository.getTodosForUser(user));

        /*
        List<ToDoItemDto> b39ToDoItemDtoList = List.of(ToDoItemDto.builder().uuid(UUID.randomUUID()).build());
        List<ToDoItemDto> stv35ToDoItemDtoList = List.of(ToDoItemDto.builder().uuid(UUID.randomUUID()).build());
        return List.of(ToDoDto.builder()
                        .name("B39")
                        .uuid(UUID.randomUUID())
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .lastModifiedByUser("adm")
                        .toDoItemDtoList(b39ToDoItemDtoList)
                        .build(),
                ToDoDto.builder()
                        .name("STV35")
                        .uuid(UUID.randomUUID())
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .lastModifiedByUser("adm")
                        .toDoItemDtoList(stv35ToDoItemDtoList)
                        .build());*/
    }

    @Override
    public TodoDto getTodo(UUID uuid) {
        log.debug("uuid={}", uuid.toString());
        return TodoMapper.toTodoDto(todoRepository.getTodoByUuid(uuid));

        /*
        List<ToDoItemDto> toDoItemDtoList = List.of(ToDoItemDto.builder().uuid(UUID.randomUUID()).build());
        return ToDoDto.builder()
                .uuid(uuid)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .lastModifiedBy("adm")
                .toDoItemDtoList(toDoItemDtoList)
                .build();*/
    }

    @Override
    public void deleteTodo(String uuid) {
    }

    @Override
    public TodoDto updateTodo(TodoDto toDoDto) {
        try {
            Todo updateTodo = todoRepository.getTodoByUuid(toDoDto.getUuid());
            TodoMapper.updateTodo(updateTodo, toDoDto);
            Todo todo = todoRepository.save(updateTodo);
            return TodoMapper.toTodoDto(todo);
        } catch (DataIntegrityViolationException ex) {
            throw new RestInputValidationException(ex.getMessage());
        } catch (Exception e) {
            throw new ApplicationException("error saving todo", e.getCause());
        }
    }

    // @Mapper(componentModel = "spring") may also use mapstruct
    @Override
    public TodoDto addTodo(TodoDto toDoDto) {
        try {
            Todo todo = todoRepository.save(TodoMapper.fromTodoDto(toDoDto));
            return TodoMapper.toTodoDto(todo);
        } catch (DataIntegrityViolationException ex) {
            throw new RestInputValidationException(ex.getMessage());
        } catch (Exception e) {
            throw new ApplicationException("error saving todo", e.getCause());
        }
    }
}
