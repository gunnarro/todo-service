package org.gunnarro.microservice.todoservice.endpoint;


import io.github.bucket4j.Bucket;
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
import org.gunnarro.microservice.todoservice.domain.dto.todo.*;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
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
    private static final int TOKEN_TO_CONSUME = 10;

    @Autowired
    protected AuthenticationFacade authenticationFacade;

    private final TodoService todoService;

    private final Bucket bucket;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
        // allowing the API 5 requests per minute. In other words, the API rejects a request if it’s already received 5 requests in a time window of 1 minute.
        this.bucket = Bucket.builder()
                .addLimit(limit -> limit.capacity(50)
                        .refillGreedy(10, Duration.ofSeconds(1))
                        .initialTokens(20))
                .build();
    }

    // ---------------------------------------------------------
    // todo
    // ---------------------------------------------------------

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todos created by user", description = "return todos created by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found todos for user",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoDto.class))})
    })
    @GetMapping(path = "/todos/user/{userName}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<TodoDto>> getTodosByUserName(@PathVariable("userName") @NotNull String userName) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            log.debug("userName={}", userName);
            return ResponseEntity.ok(todoService.getTodosByUserName(userName));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todo", description = "return todo information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the todo",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoDto.class))})
    })
    @GetMapping(path = "/todos/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public @ResponseBody TodoDto getTodoById(@PathVariable("todoId") String todoId) {
        log.info("bucket4j available tokens: {}", bucket.getAvailableTokens());
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return todoService.getTodo(Long.valueOf(todoId));
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
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
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return todoService.createTodo(todoDto);
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
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
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return todoService.updateTodo(todoDto);
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "delete todo", description = "id of deleted todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "todo is deleted")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/todos/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public void deleteTodo(@PathVariable("todoId") @NotNull String todoId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            log.info("delete: todoId={} ", todoId);
            todoService.deleteTodo(Long.valueOf(todoId));
        } else {
            throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
        }
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
    @PostMapping(path = "/todos/{todoId}/items", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public TodoItemDto createTodoItem(@PathVariable("todoId") String todoId, @RequestBody @Valid TodoItemDto todoItemDto) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            TodoItemDto createdTodoItemDto = todoService.createTodoItem(todoItemDto);
            URI resourceUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/todoItemId}").buildAndExpand(createdTodoItemDto.getId()).toUri();
            return createdTodoItemDto;
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "update todo item in the todo list", description = "return updated todo item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated todo item",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoItemDto.class))})
    })
    @PutMapping(path = "/todos/{todoId}/items", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public TodoItemDto updateTodoItem(@PathVariable("todoId") String todoId, @RequestBody @Valid TodoItemDto toDoItemDto) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return todoService.updateTodoItem(toDoItemDto);
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todo item", description = "return todo item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found todo item",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoItemDto.class))})
    })
    @GetMapping(path = "/todos/{todoId}/items/{todoItemId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public TodoItemDto getTodoItem(@PathVariable("todoId") @NotNull String todoId, @PathVariable("todoItemId") @NotNull String todoItemId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return todoService.getTodoItem(Long.valueOf(todoId), Long.valueOf(todoItemId));
        } else {
            throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "delete todo item", description = "todo item id to delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "todo item is deleted")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/todos/{todoId}/items/{todoItemId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public void deleteTodoItem(@PathVariable("todoId") @NotNull String todoId, @PathVariable("todoItemId") @NotNull String todoItemId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            log.info("delete: todoId={}, todoItemId={}", todoId, todoItemId);
            todoService.deleteTodoItem(Long.valueOf(todoId), Long.valueOf(todoItemId));
        } else {
            throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    // ---------------------------------------------------------
    // todo participant
    // ---------------------------------------------------------

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todos participants", description = "return todos participants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found todos participants",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ParticipantDto.class))})
    })
    @GetMapping(path = "/todos/{todoId}/participants", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<ParticipantDto>> getTodoParticipants(@PathVariable("todoId") @NotNull String todoId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return ResponseEntity.ok(todoService.getParticipants(Long.valueOf(todoId)));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Add new participant to the todo list", description = "return created participant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created participant",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ParticipantDto.class))})
    })
    @PostMapping(path = "/todos/{todoId}/participants", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ParticipantDto createParticipant(@PathVariable("todoId") String todoId, @RequestBody @Valid ParticipantDto participantDto) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return todoService.createParticipant(participantDto);
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Update participant to the todo list", description = "return updated participant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Updated participant",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ParticipantDto.class))})
    })
    @PutMapping(path = "/todos/{todoId}/participants", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ParticipantDto updateParticipant(@PathVariable("todoId") String todoId, @RequestBody @Valid ParticipantDto participantDto) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return todoService.updateParticipant(participantDto);
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "delete todo participant", description = "participant to delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "participant is deleted")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/todos/{todoId}/participants/{participantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteParticipant(@PathVariable("todoId") @NotNull String todoId, @PathVariable("participantId") @NotNull String participantId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            log.info("delete: todoId={}, participantId={}", todoId, participantId);
            todoService.deleteParticipant(Long.valueOf(todoId), Long.valueOf(participantId));
        } else {
            throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    // ---------------------------------------------------------
    // todo item approval
    // ---------------------------------------------------------
    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "approve todo item", description = "return approval for todo item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Approved todo item",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApprovalDto.class))})
    })
    @PostMapping(path = "/todos/{todoId}/items/{todoItemId}/approvals", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ApprovalDto> createTodoItemApproval(@PathVariable("todoId") @NotNull String todoId,
                                                              @PathVariable("todoItemId") @NotNull String todoItemId,
                                                              @RequestBody @Valid ApprovalDto ApprovalDto) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return ResponseEntity.ok(todoService.createApproval(ApprovalDto));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "updated todo item approval, options are approved or not.", description = "return updated todo item approval")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated approval for todo item",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApprovalDto.class))})
    })
    @PutMapping(path = "/todos/{todoId}/items/{todoItemId}/approvals", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ApprovalDto> updateTodoItemApproval(@PathVariable("todoId") @NotNull String todoId,
                                                              @PathVariable("todoItemId") @NotNull String todoItemId,
                                                              @RequestBody @Valid ApprovalDto ApprovalDto) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return ResponseEntity.ok(todoService.updateApproval(ApprovalDto));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todo item approvals", description = "return approvals for a todo item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found approvals for todo item",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApprovalDto.class))})
    })
    @GetMapping(path = "/todos/{todoId}/items/{todoItemId}/approvals", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<ApprovalDto>> getTodoItemApprovals(@PathVariable("todoId") @NotNull String todoId, @PathVariable("todoItemId") @NotNull String todoItemId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return ResponseEntity.ok(todoService.getApprovals(Long.valueOf(todoId), Long.valueOf(todoItemId)));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "delete approval for todo item", description = "approval to delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "approval is deleted")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/todos/{todoId}/items/{todoItemId}/approvals/{participantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteApproval(@PathVariable("todoId") @NotNull String todoId, @PathVariable("todoItemId") @NotNull String todoItemId, @PathVariable("participantId") @NotNull String participantId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            log.info("delete: todoId={}, todoItemId={}, participantId={}", todoId, todoItemId, participantId);
            todoService.deleteApproval(Long.valueOf(todoItemId), Long.valueOf(participantId));
        } else {
            throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
        }
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
    public List<TodoHistoryDto> getTodoHistoryById(@PathVariable("todoId") String todoId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return todoService.getTodoHistory(Long.valueOf(todoId));
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todo audit history", description = "return todo audit history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the todo history",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoItemHistoryDto.class))})
    })
    @GetMapping(path = "/todos/{todoId}/items/{todoItemId}/history", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public List<TodoItemHistoryDto> getTodoItemHistoryById(@PathVariable("todoId") String todoId, @PathVariable("todoItemId") String todoItemId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {
            return todoService.getTodoItemHistory(Long.valueOf(todoId), Long.valueOf(todoItemId));
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get subscription request")
    @Operation(summary = "Get todo as pdf", description = "return todo as pdf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found todo pdf",
                    content = {@Content(mediaType = MediaType.APPLICATION_PDF_VALUE,
                            schema = @Schema(implementation = Byte[].class))})
    })
    @GetMapping(path = "/todos/{todoId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getTodoPdf(@PathVariable("todoId") String todoId) {
        if (bucket.tryConsume(TOKEN_TO_CONSUME)) {

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(todoService.getTodoAsPdf(Long.valueOf(todoId)));
        }
        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
    }


    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
       // ImageUploadResponse response = imageDataService.uploadImage(file);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
