package org.gunnarro.microservice.todoservice.domain.dto.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

@Schema(description = "Holds audit log for Todo.")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class TodoHistoryDto implements Serializable {
    /**
     * Mapped from hibernate envers int values
     */
    public enum RevisionTypesEnum {
        ADDED(0), MODIFIED(1), DELETED(2);

        private final int type;

        RevisionTypesEnum(int type) {
            this.type = type;
        }

        public static RevisionTypesEnum getByType(int type) {
            return Arrays.stream(RevisionTypesEnum.values()).filter(r -> r.type == type).findFirst().orElse(null);
        }
    }

    // revision info
    @Schema(description = "Unique identifier of the revision.")
    private Long revisionNumber;
    private Integer revisionEndId;
    @Schema(description = "The revision type, update, delete.")
    private String revisionType;
    // end revision info
    @Schema(description = "Unique identifier of the todo. Should not be set for new Todo.")
    @Pattern(regexp = "[0-9]{1,25}", message = "can only contain integers, min 1 and max 25")
    private String id;
    @Schema(description = "Date when todo was created.")
    private LocalDateTime createdDate;
    @Schema(description = "Date when todo was last modified.")
    private LocalDateTime lastModifiedDate;
    @Schema(description = "User that created the todo.")
    @Pattern(regexp = "[a-zA-Z]{1,50}", message = "can only contain lower and uppercase characters from a-z and A-Z.")
    private String createdByUser;
    @Schema(description = "User that last modified the todo.")
    private String lastModifiedByUser;
    @Schema(description = "Name of todo.")
    @Pattern(regexp = "[\\w\\s\\d-_]{1,50}", message = "can only contain lower and uppercase alphabetic chars. Min 1 char, max 50 chars.")
    private String name;
    @Schema(description = "Status of todo, OPEN, IN_PROGRESS or FINISHED")
    @NotNull
    private String status;
    @Schema(description = "Description of this to do task.")
    @Pattern(regexp = "[\\w\\s\\d-_]{1,100}", message = "Description can only contain lower and uppercase alphabetic chars. Min 1 char, max 100 chars.")
    private String description;

}
