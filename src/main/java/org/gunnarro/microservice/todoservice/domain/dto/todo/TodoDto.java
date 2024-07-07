package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Holds information about a todo")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class TodoDto {
    @Schema(description = "Unique identifier of the todo. Should not be set for new Todo.")
    @Pattern(regexp = "[0-9]{1,25}", message = "can only contain integers, min 1 and max 25")
    private String id;

    @Schema(description = "Unique global identifier of the todo. Used by guest users to access the todo, i.e, unidentified users")
    private String uuid;

    @Schema(description = "date when todo was created")
    private LocalDateTime createdDate;

    @Schema(description = "date when todo was last modified")
    private LocalDateTime lastModifiedDate;

    @Schema(description = "user that created the todo")
    @Pattern(regexp = "[a-zA-Z]{1,50}", message = "can only contain lower and uppercase characters from a-z and A-Z.")
    private String createdByUser;

    @Schema(description = "user that last modified the todo")
    private String lastModifiedByUser;

    @Schema(description = "Name of todo")
    @Pattern(regexp = "[\\w\\s\\dæÆøØåÅ_-]{2,50}", message = "can only contain lower and uppercase alphabetic chars. Min 2 char, max 50 chars.")
    private String name;

    @Schema(description = "Status of todo, OPEN, IN_PROGRESS, ON_HOLD, DONE and CANCELLED")
    @NotNull
    private TodoStatus status;

    @Schema(description = "description of this to do task")
    @Pattern(regexp = "[\\w\\s\\dæÆøØåÅ_-]{0,100}", message = "Description can only contain lower and uppercase alphabetic chars. Can be empty or max 100 chars.")
    private String description;

    @Schema(description = "List of task for the todo list")
    private List<TodoItemDto> todoItemDtoList;

    @Schema(description = "The todo list can have many participants and a participant is unique for a todo list.")
    private List<ParticipantDto> participantDtoList;
}
