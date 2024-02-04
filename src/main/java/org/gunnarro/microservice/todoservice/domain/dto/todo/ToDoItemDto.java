package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Schema(description = "Holds information about a todo item")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Builder
public class ToDoItemDto {
    @Schema(description = "Unique identifier of the todo item.")
    @NotNull
    private UUID uuid;
    @Schema(description = "doc me")
    private String location;
    @Schema(description = "doc me")
    private String name;
    @Schema(description = "doc me")
    private String status;
    @Schema(description = "doc me")
    private String action;
    @Schema(description = "doc me")
    private String participant1;
    @Schema(description = "doc me")
    private String participant2;
    @Schema(description = "doc me")
    private String participant3;
}
