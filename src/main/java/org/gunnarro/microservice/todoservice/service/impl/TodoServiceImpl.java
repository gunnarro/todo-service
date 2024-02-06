package org.gunnarro.microservice.todoservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.gunnarro.microservice.todoservice.domain.mapper.TodoMapper;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.exception.RestInputValidationException;
import org.gunnarro.microservice.todoservice.repository.TodoRepository;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        return TodoMapper.toTodoDtoList(todoRepository.getTodosForUser(user));
    }

    @Override
    public TodoDto getTodo(String todoId) {
        log.debug("todoId={}", todoId);
        return createTodoTestData().stream().filter(t -> t.getId().equals(todoId)).findFirst().orElseThrow();
        // return TodoMapper.toTodoDto(todoRepository.getTodoByUuid(uuid));
    }

    @Override
    public void deleteTodo(String uuid) {
    }

    @Override
    public TodoDto updateTodo(TodoDto toDoDto) {
        try {
            Todo updateTodo = todoRepository.getTodoByUuid(UUID.fromString(toDoDto.getId()));
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

    @Override
    public TodoItemDto addTodoItem(TodoItemDto todoItemDto) {
        return null;
    }

    @Override
    public TodoItemDto updateTodoItem(TodoItemDto todoItemDto) {
        return null;
    }

    @Override
    public void deleteTodoItem(String todoId, String todoItemId) {

    }

    List<TodoDto> createTodoTestData() {
        String b39TodoId = UUID.randomUUID().toString();
        String stvgt35TodoId = UUID.randomUUID().toString();
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

    TodoItemDto createItem(String todoId, String name, String status) {
        return TodoItemDto.builder()
                .id(UUID.randomUUID().toString())
                .todoId(todoId)
                .name(name)
                .description("stue")
                .action("selges")
                .status(status)
                .assignedTo("guro")
                .build();
    }
}