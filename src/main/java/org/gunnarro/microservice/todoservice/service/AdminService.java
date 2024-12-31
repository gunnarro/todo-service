package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    List<TodoDto> getTodos();

    void deleteTodo(Long todoId);

    List<UserDto> getUsers();

    public void deleteUser(Long userId);
}
