package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "Holds audit log for Todo item.")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class TodoItemHistoryDto implements Serializable {
    // revision info
    @Schema(description = "Unique identifier of the revision.")
    private Long revisionNumber;
    private Integer revisionEndId;
    @Schema(description = "The revision type, update, delete.")
    private String revisionType;
    // end revision info
    @Schema(description = "Global unique identifier of the todo item. Should not be set for new todo item.")
    @Pattern(regexp = "[0-9]{1,25}", message = "can only contain integers, min 1 and max 25")
    private String id;
    @Schema(description = "Global unique identifier for the todo item that this item belongs to.")
    @Pattern(regexp = "[0-9]{1,25}", message = "can only contain integers, min 1 and max 25")
    @NotNull
    private String todoId;
    @Schema(description = "Date when todo item was created.")
    private LocalDateTime createdDate;
    @Schema(description = "Date when todo item was last modified.")
    private LocalDateTime lastModifiedDate;
    @Schema(description = "User that created the todo item.")
    private String createdByUser;
    @Schema(description = "User that last modified the todo item.")
    private String lastModifiedByUser;
    @Schema(description = "doc me")
    @Pattern(regexp = "[\\w\\s\\dæÆøØåÅ_-]{1,50}", message = "Name can only contain lower and uppercase alphabetic chars. Min 1 char, max 50 chars.")
    private String name;
    @Schema(description = "description of the task to do")
    @Pattern(regexp = "[\\w\\s\\dæÆøØåÅ_-]{1,100}", message = "Description can only contain lower and uppercase alphabetic chars. Min 1 char, max 100 chars.")
    private String description;
    @Schema(description = "Status of todo item, OPEN, IN_PROGRESS or FINISHED")
    private String status;
    @Schema(description = "The action that should be done for this item.")
    private String action;
    @Schema(description = "The person that is responsible to follow up and fulfill this task.")
    private String assignedTo;
    @Schema(description = "The priority of this task, can be any integer, typically from 1- 10.")
    private Priority priority;

}
