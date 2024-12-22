package org.gunnarro.microservice.todoservice.endpoint;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoStatus;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "file:src/test/resources/it-application.properties")
public class TodoControllerPerformanceTest {
    @Mock
    private TodoService todoServiceMock;
    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void setup() {
        // junit.jupiter.execution.parallel.enabled = true
    }

    @Execution(ExecutionMode.CONCURRENT)
    @RepeatedTest(2)
    void getTodosForUser() {
        TodoDto todoDto = TodoDto.builder()
                .id(String.valueOf(new Random().nextLong()))
                .name("guro")
                .status(TodoStatus.IN_PROGRESS)
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();
        when(todoServiceMock.getTodosByUserName("guro")).thenReturn(List.of(todoDto));
        ResponseEntity<List<TodoDto>> response = todoController.getTodosByUserName("guro");
        assertThat(response).isNotNull();
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
    }
}
