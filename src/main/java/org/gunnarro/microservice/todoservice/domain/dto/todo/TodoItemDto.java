package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Holds information about a todo item")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Builder
public class TodoItemDto {
    @Schema(description = "Unique identifier of the todo item.")
    @NotNull
    private String id;
    @Schema(description = "Unique identifier of the todo that this item belongs to.")
    @NotNull
    private String todoId;
    @Schema(description = "doc me")
    private String name;
    @Schema(description = "doc me")
    private String description;
    @Schema(description = "doc me")
    private String status;
    @Schema(description = "doc me")
    private String action;
    @Schema(description = "doc me")
    private String assignedTo;
}
