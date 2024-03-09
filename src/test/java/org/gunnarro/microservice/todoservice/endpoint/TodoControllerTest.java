package org.gunnarro.microservice.todoservice.endpoint;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TodoControllerTest {
    @Mock
    private TodoService todoServiceMock;
    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void setup() {
        //    this.todoController = new TodoController(todoServiceMock);
    }

    @Test
    void getTodosForUser() {
        TodoDto todoDto = TodoDto.builder()
                .id(String.valueOf(new Random().nextLong()))
                .name("guro")
                .status("Active")
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
/*
    @Test
    void createSubscription() {
        TodoDto todoDto =  TodoDto.builder()
                .uuid(UUID.randomUUID())
                .name("guro")
                .status("Active")
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();
        when(todoServiceMock.saveSubscription(any())).thenReturn(subscription);
        ResponseEntity<SubscriptionDto> response = restServiceController.createSubscription(subscription);
        assertThat(response.getBody()).isNotNull();
        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertEquals(23, response.getBody().getSubscriptionId());
    }

    @Test
    void updateSubscription() {
        TodoDto todoDto =  TodoDto.builder()
                .uuid(UUID.randomUUID())
                .name("guro")
                .status("Active")
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();
        when(todoServiceMock(any())).thenReturn(subscription);
        ResponseEntity<SubscriptionDto> response = restServiceController.updateSubscription(subscription.getSubscriptionId().intValue(), subscription);
        assertThat(response.getBody()).isNotNull();
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(23, response.getBody().getSubscriptionId());
    }

    @Test
    void deleteSubscription() {
        ResponseEntity<Integer> response = restServiceController.deleteSubscription(23);
        assertThat(response).isNotNull();
        Assertions.assertEquals(200, response.getStatusCode().value());
    }*/
}
