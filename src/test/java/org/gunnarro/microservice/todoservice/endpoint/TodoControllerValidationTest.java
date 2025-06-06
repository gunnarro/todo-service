package org.gunnarro.microservice.todoservice.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gunnarro.microservice.todoservice.DefaultTestConfig;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoStatus;
import org.gunnarro.microservice.todoservice.handler.RestExceptionHandler;
import org.gunnarro.microservice.todoservice.repository.TodoRepository;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Random;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TodoController.class, RestExceptionHandler.class})
public class TodoControllerValidationTest extends DefaultTestConfig {

    @Autowired
    private TodoController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationFacade AuthenticationFacadeMock;

    @MockBean
    private TodoService todoServiceMock;

    @MockBean
    private TodoRepository todoRepositoryMock;

    @Override
    @BeforeEach
    public void before() {
        super.before();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    void whenControllerInjectedThenNotNull() {
        Assertions.assertNotNull(controller);
    }

    @Test
    void createTodoInputValidationOk() throws Exception {
        TodoDto todoDto = TodoDto.builder()
                .id(String.valueOf(Math.abs(new Random().nextLong())))
                .name("todo-task-v22 test æøå")
                .status(TodoStatus.IN_PROGRESS)
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/todoservice/v1/todos")
                        .content(objectMapper.writeValueAsString(todoDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void createTodoInputValidationError() throws Exception {
        TodoDto todoDto = TodoDto.builder()
                .id(String.valueOf(Math.abs(new Random().nextLong()))) // only positive numbers
                .name("guro*#/")
                .status(TodoStatus.IN_PROGRESS)
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/todoservice/v1/todos")
                        .content(objectMapper.writeValueAsString(todoDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is("Service Input Validation Error. name can only contain lower and uppercase alphabetic chars. Min 2 char, max 50 chars.")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateTodoInputValidationError() throws Exception {
        TodoDto todoDto = TodoDto.builder()
                .id(String.valueOf(Math.abs(new Random().nextLong()))) // only positive
                .name("guro*#/&")
                .status(TodoStatus.IN_PROGRESS)
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/todoservice/v1/todos/" + todoDto.getId())
                        .content(objectMapper.writeValueAsString(todoDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is("Service Input Validation Error. name can only contain lower and uppercase alphabetic chars. Min 2 char, max 50 chars.")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateTodoInvalidId() throws Exception {
        TodoDto todoDto = TodoDto.builder()
                .id("1234567-3")
                .name("guro")
                .status(TodoStatus.IN_PROGRESS)
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();
        // invalid chars in id
        mockMvc.perform(MockMvcRequestBuilders.put("/todoservice/v1/todos/" + todoDto.getId())
                        .content(objectMapper.writeValueAsString(todoDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is("Service Input Validation Error. id can only contain integers, min 1 and max 25")));
    }
}
