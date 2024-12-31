package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Holds information about a user.")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class UserDto {
    @Schema(description = "Global unique identifier of a user.")
    private String id;

    @Schema(description = "The username")
    private String userName;

    @Schema(description = "The user email address")
    private String email;

    @Schema(description = "Is the user enabled or not")
    private Integer enabled;
}
