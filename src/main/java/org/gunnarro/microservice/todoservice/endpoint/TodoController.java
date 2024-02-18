package org.gunnarro.microservice.todoservice.endpoint;


import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.ErrorResponse;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoHistoryDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoItemDto;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * NB! GET method should use consume = MediaType.ALL_VALUE, in order to accept empty Content-Type HTTP Header, which my be sent by clients. Normally GET do not have a Request Body.
 * <p>
 * curl -X 'GET' 'https://localhost:9999/todoservice/v1/todos/ac67ae12-69f6-444e-8d57-5499692691f1'  -H 'Content-Type: application/json' --insecure -v -u 'my-service-name:change-me'
 * <p>
 * preflight request:
 * curl -v -H "Access-Control-Request-Method: GET" -H "Origin: http://localhost:3000" -X OPTIONS https://localhost:9999/todoservice/v1/todos/ping --insecur
 */
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(origins = "https//localhost:9999, http//localhost:3000", maxAge = 3600)
@Tag(name = "Todo Service", description = "Rest API for Todo services")
@ApiResponses(value = {
        @ApiResponse(responseCode = HttpStatusMsg.HTTP_400_CODE, description = HttpStatusMsg.HTTP_400_MSG, content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = HttpStatusMsg.HTTP_401_CODE, description = HttpStatusMsg.HTTP_401_MSG, content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = HttpStatusMsg.HTTP_403_CODE, description = HttpStatusMsg.HTTP_403_MSG, content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = HttpStatusMsg.HTTP_404_CODE, description = HttpStatusMsg.HTTP_404_MSG, content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = HttpStatusMsg.HTTP_429_CODE, description = HttpStatusMsg.HTTP_429_MSG, content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = HttpStatusMsg.HTTP_500_CODE, description = HttpStatusMsg.HTTP_500_MSG, content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = HttpStatusMsg.HTTP_503_CODE, description = HttpStatusMsg.HTTP_503_MSG, content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))})
})
@RestController
@RequestMapping(path = "/todoservice/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class TodoController {
    private static final String REST_SERVICE_METRIC_NAME = "todo.service.api";

    private final TodoService toDoService;

  //  private final AuditService auditService;

    public TodoController(TodoService toDoService) {//}, AuditService auditService) {
        this.toDoService = toDoService;
    //    this.auditService = auditService;
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "ping", description = "check if service is alive")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "a live",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoDto.class))})
    })
    @GetMapping(path = "/todos/ping", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("{ \"status\": \"alive\" }");
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todos created by user", description = "return todos created by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found todos for user",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoDto.class))})
    })
    @GetMapping(path = "/todos/user/{user}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<TodoDto>> getTodosForUser(@PathVariable("user") @NotNull String user) {
        return ResponseEntity.ok(toDoService.getTodosForUser(user));
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todo", description = "return todo information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the todo",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoDto.class))})
    })
    @GetMapping(path = "/todos/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public TodoDto getTodoById(@PathVariable("todoId") Long todoId) {
        return toDoService.getTodo(todoId);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "create a new todo", description = "return created todo information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created todo",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoDto.class))})
    })
    @PostMapping(path = "/todos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public @ResponseBody TodoDto createTodo(@RequestBody @Valid TodoDto todoDto) {
        return toDoService.addTodo(todoDto);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "update todo", description = "return updated todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated todo",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoDto.class))})
    })
    @PutMapping(path = "/todos/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TodoDto updateTodo(@PathVariable("todoId") String todoId, @RequestBody @Valid TodoDto todoDto) {
        return toDoService.updateTodo(todoDto);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "delete todo", description = "id of deleted todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "todo is deleted")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/todos/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public void deleteTodo(@PathVariable("todoId") @NotNull Long todoId) {
        log.info("delete: todoId={} ", todoId);
        toDoService.deleteTodo(todoId);
    }

    // ---------------------------------------------------------
    // todo items
    // ---------------------------------------------------------

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Add new todo item to the todo list", description = "return created todo item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created todo item",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoItemDto.class))})
    })
    @PostMapping(path = "/todos/{todoId}/items", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public TodoItemDto createTodoItem(@PathVariable("todoId") String todoId, @RequestBody @Valid TodoItemDto todoItemDto) {
        return toDoService.addTodoItem(todoItemDto);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "update todo item in the todo list", description = "return updated todo item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated todo item",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoItemDto.class))})
    })
    @PutMapping(path = "/todos/{todoId}/items/{todoItemId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TodoItemDto updateTodoItem(@PathVariable("todoId") String todoId, @RequestBody @Valid TodoItemDto toDoItemDto) {
        return toDoService.updateTodoItem(toDoItemDto);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "delete todo item", description = "todo item id to delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "todo item is deleted")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/todos/{todoId}/items/{todoItemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteTodoItem(@PathVariable("todoId") @NotNull Long todoId, @PathVariable("todoItemId") @NotNull Long todoItemId) {
        log.info("delete: todoId={}, todoItemId={}", todoId, todoItemId);
        toDoService.deleteTodoItem(todoId, todoItemId);
    }

    // ---------------------------------------------------------
    // todo history
    // ---------------------------------------------------------
    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todo audit history", description = "return todo audit history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the todo history",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoHistoryDto.class))})
    })
    @GetMapping(path = "/todos/{todoId}/history", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public List<TodoHistoryDto> getTodoHistoryById(@PathVariable("todoId") Long todoId) {
        // return auditService.getTodoHistory(todoId);
        return toDoService.getTodoHistory(todoId);
    }

}
