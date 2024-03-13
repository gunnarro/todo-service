package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
    @Schema(description = "Name of the task")
    @Pattern(regexp = "[\\w\\s\\dæÆøØåÅ_-]{2,50}", message = "Name can only contain lower and uppercase alphabetic chars. Min 2 char, max 50 chars.")
    private String name;
    @Pattern(regexp = "[\\w\\s\\dæÆøØåÅ_-]{0,50}", message = "Category that this task belong to. Can be empty or max 50 chars")
    private String category;
    @Schema(description = "description of the task to do")
    @Pattern(regexp = "[\\w\\s\\dæÆøØåÅ_-]{0,100}", message = "Description can only contain lower and uppercase alphabetic chars. Can be empty or max 100 chars.")
    private String description;
    @Schema(description = "Status of todo, OPEN, IN_PROGRESS, ON_HOLD, DONE and CANCELLED")
    @NotNull
    private TodoItemStatus status;
    @Schema(description = "The action that should be done for this item")
    private TaskAction action;
    @Schema(description = "the person that is responsible to follow up and fulfill this task")
    private String assignedTo;
    @Schema(description = "the priority of this task, can be any integer, typically from 1- 10")
    private Integer priority;
    @Schema(description = "the price of this item")
    private Integer price;
    @Schema(description = "number of minutes worked with this task")
    private Integer workedMinutes;
}
