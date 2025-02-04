package org.gunnarro.microservice.todoservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Common error response for rest services
 */
@Schema(description = "Holds rest service error description.")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ErrorResponse {
    @Schema(description = "http status code.", example = "404")
    private Integer httpStatus;
    @Schema(description = "Http message.", example = "Not Found")
    private String httpMessage;
    @Schema(description = "Internal error code.", example = "4041001")
    private Integer errorCode;
    @Schema(description = "Description of the error.", example = "Customer not found")
    private String description;
}