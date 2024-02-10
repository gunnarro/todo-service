package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Holds information about a todo item")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TodoItemDto {
    @Schema(description = "Global unique identifier of the todo item. Should not be set for new todo item.")
    private Long id;
    @Schema(description = "Global unique identifier for the todo that this item belongs to.")
    @NotNull
    private Long todoId;
    @Schema(description = "date when todo was created")
    private LocalDateTime createdDate;
    @Schema(description = "date when todo was last modified")
    private LocalDateTime lastModifiedDate;
    @Schema(description = "user that created the todo")
    private String createdByUser;
    @Schema(description = "user that last modified the todo")
    private String lastModifiedByUser;
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
