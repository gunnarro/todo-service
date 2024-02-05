package org.gunnarro.microservice.todoservice.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gunnarro.microservice.todoservice.DefaultTestConfig;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
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
import java.util.UUID;

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
    private TodoService todoServiceMock;

    @MockBean
    TodoRepository todoRepositoryMock;

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
    void createTodoInputValidationError() throws Exception {
        TodoDto todoDto = TodoDto.builder()
                .id(UUID.randomUUID().toString())
                .name("guro*#/")
                .status("Active")
                .description("my todo list")
                .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/todoservice/v1/todos/" + todoDto.getId())
                        .content(objectMapper.writeValueAsString(todoDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is("Service Input Validation Error. name Can only contain lower and uppercase alphabetic chars. Min 1 char, max 50 chars.")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateTodoInputValidationError() throws Exception {
        TodoDto todoDto = TodoDto.builder()
                .id(UUID.randomUUID().toString())
                .name("guro*#/")
                .status("Active")
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is("Service Input Validation Error. name Can only contain lower and uppercase alphabetic chars. Min 1 char, max 50 chars.")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getTodoInvalidUuidFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/todoservice/v1/todos/invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is("Service Input Validation Error")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
