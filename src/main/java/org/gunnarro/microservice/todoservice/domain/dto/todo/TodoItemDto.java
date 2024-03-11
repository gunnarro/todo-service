package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Holds information about a todo item")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class TodoItemDto {
    @Schema(description = "Global unique identifier of the todo item. Should not be set for new todo item.")
    @Pattern(regexp = "[0-9]{1,25}", message = "can only contain integers, min 1 and max 25")
    private String id;
    @Schema(description = "Global unique identifier for the todo that this item belongs to.")
    @Pattern(regexp = "[0-9]{1,25}", message = "can only contain integers, min 1 and max 25")
    private String todoId;
    @Schema(description = "date when todo was created")
    private LocalDateTime createdDate;
    @Schema(description = "date when todo was last modified")
    private LocalDateTime lastModifiedDate;
    @Schema(description = "user that created the todo")
    private String createdByUser;
    @Schema(description = "user that last modified the todo")
    private String lastModifiedByUser;
    @Schema(description = "doc me")
    @Pattern(regexp = "[\\w\\s\\dæÆøØåÅ_-]{1,50}", message = "Name can only contain lower and uppercase alphabetic chars. Min 1 char, max 50 chars.")
    private String name;
    @Schema(description = "description of the task to do")
    @Pattern(regexp = "[\\w\\s\\dæÆøØåÅ_-]{1,100}", message = "Description can only contain lower and uppercase alphabetic chars. Min 1 char, max 100 chars.")
    private String description;
    @Schema(description = "Status of todo, OPEN, IN_PROGRESS or FINISHED")
    private String status;
    @Schema(description = "The action that should be done for this item")
    private String action;
    @Schema(description = "the person that is responsible to follow up and fulfill this task")
    private String assignedTo;
}
