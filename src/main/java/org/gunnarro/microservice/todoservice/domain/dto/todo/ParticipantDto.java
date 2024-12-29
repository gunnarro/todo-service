package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Holds information about a todo list participant.")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ParticipantDto {
    @Schema(description = "Global unique identifier of a participant. Should not be set for new todo item.")
    private String id;

    @Schema(description = "Unique identifier of the todo the participant is assigned to.")
    private String todoId;

    @Schema(description = "The participant name")
    private String name;

    @Schema(description = "The participant email address")
    private String email;

    @Schema(description = "Is the participant enabled or not")
    private Integer enabled;
}
