package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Holds information about a todo list")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TodoDto {
    @Schema(description = "Unique identifier of the todo.")
    @NotNull
    private String id;
    @Schema(description = "date when todo was created")
    private LocalDateTime createdDate;
    @Schema(description = "date when todo was last modified")
    private LocalDateTime lastModifiedDate;
    @Schema(description = "user that created the todo")
    private String createdByUser;
    @Schema(description = "user that last modified the todo")
    private String lastModifiedByUser;
    @Schema(description = "Name of todo")
    @Pattern(regexp = "[\\w\\s]{1,50}", message = "Can only contain lower and uppercase alphabetic chars. Min 1 char, max 50 chars.")
    private String name;
    @Schema(description = "Status of todo, ACTIVE or FINISHED")
    @NotNull
    private String status;
    @Schema(description = "description of todo")
    @Pattern(regexp = "[\\w\\s]{1,100}", message = "Can only contain lower and uppercase alphabetic chars. Min 1 char, max 100 chars.")
    private String description;
    @Schema(description = "List of task/item/action this todo list contains")
    private List<TodoItemDto> toDoItemDtoList;
}
