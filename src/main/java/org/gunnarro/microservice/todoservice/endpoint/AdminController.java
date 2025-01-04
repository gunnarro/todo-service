package org.gunnarro.microservice.todoservice.endpoint;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.ErrorResponse;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.domain.dto.todo.UserDto;
import org.gunnarro.microservice.todoservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(origins = "https//localhost:9999, http//localhost:3000", maxAge = 3600)
@Tag(name = "Admin Service", description = "Rest API for administration of todo.")
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
@RequestMapping(path = "/adminservice/v1/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    private static final String REST_SERVICE_METRIC_NAME = "todo.admin.service.api";
    private static final int TOKEN_TO_CONSUME = 1;

    @Autowired
    protected AuthenticationFacade authenticationFacade;

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ---------------------------------------------------------
    // todo
    // ---------------------------------------------------------
    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get todos request")
    @Operation(summary = "Get all todos", description = "return all todos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found todos",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoDto.class))})
    })
    @GetMapping(path = "/todos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<TodoDto>> getTodos() {
        return ResponseEntity.ok(adminService.getTodos());
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for delete todo request")
    @Operation(summary = "delete todo", description = "id of todo to be deleted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "todo is deleted")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/todos/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public void deleteTodo(@PathVariable("todoId") @NotNull String todoId) {
        adminService.deleteTodo(Long.valueOf(todoId));
    }

    // ---------------------------------------------------------
    // user
    // ---------------------------------------------------------
    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get users request")
    @Operation(summary = "Get all users", description = "return all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))})
    })
    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(adminService.getUsers());
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for get user request")
    @Operation(summary = "Get specific user", description = "return requested user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))})
    })
    @GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(adminService.getUser(userId));
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for create user request")
    @Operation(summary = "Create new user", description = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))})
    })
    @PostMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public void createUser(@PathVariable("userId") Long userId, UserDto userDto) {
        ResponseEntity.ok(adminService.getUser(userId));
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for update user request")
    @Operation(summary = "Update user", description = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is updated",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))})
    })
    @PutMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public void updateUser(@PathVariable("userId") Long userId, UserDto userDto) {
        ResponseEntity.ok(adminService.getUser(userId));
    }

    @Timed(value = REST_SERVICE_METRIC_NAME, description = "Measure frequency and latency for delete user request")
    @Operation(summary = "delete user", description = "id of user to be deleted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "user is deleted")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public void deleteUser(@PathVariable("userId") @NotNull String userId) {
        adminService.deleteUser(Long.valueOf(userId));
    }
}
