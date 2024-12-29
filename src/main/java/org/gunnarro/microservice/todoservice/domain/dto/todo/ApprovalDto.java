package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Holds a participant that have to approve the todo item.")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ApprovalDto {
    @Schema(description = "Unique identifier of the approval.")
    private String id;
    @Schema(description = "Unique identifier of the todo item that must be approved by all participants of the todo list.")
    private String todoItemId;
    @Schema(description = "Unique identifier of the participant that have to approve the todo item.")
    private String participantId;
    @Schema(description = "Set equal to true if approved by the participant, false otherwise.")
    private Boolean approved;
}
