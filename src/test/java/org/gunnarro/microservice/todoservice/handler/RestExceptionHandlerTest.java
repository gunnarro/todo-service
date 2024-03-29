package org.gunnarro.microservice.todoservice.handler;

import org.gunnarro.microservice.todoservice.domain.dto.ErrorResponse;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.exception.RestInputValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.client.HttpClientErrorException;

class RestExceptionHandlerTest {

    @Test
    void handleApplicationException() {
        RestExceptionHandler handler = new RestExceptionHandler();
        ResponseEntity<ErrorResponse> errorResponse = handler
                .handleApplicationException(new ApplicationException("internal application error!", new RuntimeException("stacktrace for root cause")));
        Assertions.assertEquals(500, errorResponse.getStatusCode().value());
        Assertions.assertEquals(500, errorResponse.getBody().getHttpStatus());
        Assertions.assertEquals("Internal Server Error", errorResponse.getBody().getHttpMessage());
        Assertions.assertEquals("Application Failure, internal application error!", errorResponse.getBody().getDescription());
        Assertions.assertEquals(500100, errorResponse.getBody().getErrorCode());
    }

    @Test
    void handleAccessDeniedException() {
        RestExceptionHandler handler = new RestExceptionHandler();
        ResponseEntity<ErrorResponse> errorResponse = handler.handleAccessDeniedException(new AccessDeniedException("Access denied!"));
        Assertions.assertEquals(403, errorResponse.getStatusCode().value());
        Assertions.assertEquals(403, errorResponse.getBody().getHttpStatus());
        Assertions.assertEquals("Forbidden", errorResponse.getBody().getHttpMessage());
        Assertions.assertEquals("Service Access Not Allowed", errorResponse.getBody().getDescription());
        Assertions.assertEquals(400100, errorResponse.getBody().getErrorCode());
    }

    /**
     * TODO check if we need this when using the @Valid annotation in rest endpoints
     */
    @Test
    void handleBadRequestException() {
        RestExceptionHandler handler = new RestExceptionHandler();
        ResponseEntity<ErrorResponse> errorResponse = handler.handleBadRequestException(new RestInputValidationException("Bad request"));
        Assertions.assertEquals(400, errorResponse.getStatusCode().value());
        Assertions.assertEquals(400, errorResponse.getBody().getHttpStatus());
        Assertions.assertEquals("Bad Request", errorResponse.getBody().getHttpMessage());
        Assertions.assertEquals("Service Input Validation Error", errorResponse.getBody().getDescription());
        Assertions.assertEquals(400200, errorResponse.getBody().getErrorCode());
    }

    @Test
    void handleHttpClientErrorException() {
        RestExceptionHandler handler = new RestExceptionHandler();
        ResponseEntity<ErrorResponse> errorResponse = handler.handleHttpClientErrorException(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertEquals(400, errorResponse.getStatusCode().value());
        Assertions.assertEquals(400, errorResponse.getBody().getHttpStatus());
        Assertions.assertEquals("BAD_REQUEST", errorResponse.getBody().getHttpMessage());
        Assertions.assertEquals("400 BAD_REQUEST", errorResponse.getBody().getDescription());
        Assertions.assertNull(errorResponse.getBody().getErrorCode());
    }
}
