package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Holds all participants that have approved the todo item")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ApprovalDto {
    @Schema(description = "Global unique identifier of a participant. Should not be set for new todo item.")
    private String id;
    @Schema(description = "Unique identifier of the todo item that is approved.")
    private String todoItemId;
    @Schema(description = "Unique identifier of the participant that have approved the todo item.")
    private String participantId;
    @Schema(description = "Name of the participant that have approved the todo item.")
    private String name;
    @Schema(description = "Set equal to true if approved, false otherwise.")
    private Boolean approved;
}
