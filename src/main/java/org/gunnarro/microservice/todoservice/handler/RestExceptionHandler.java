package org.gunnarro.microservice.todoservice.handler;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.ErrorResponse;
import org.gunnarro.microservice.todoservice.domain.dto.MicroserviceStatus;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.exception.NotFoundException;
import org.gunnarro.microservice.todoservice.exception.RestInputValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class will scan all controller classes with the RestController
 * annotation and handle thrown exceptions.
 * Where http 5xx status codes are logged as ERROR and all other error status codes are logged as WARN
 * <p>
 * Exception should only be logged one place in a application, and the preferred place to log and handle exceptions is here in the RestExceptionHandler class.
 * <p>
 * Generally speaking, the Rest error response should not contain any kind of
 * internal system description of the error. In other word, the exception error
 * message should not be put into the rest error response which is returned back
 * to the calling client.
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Nullable
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        String fieldErrorsMessage = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        String globalErrorsMessage = exception
                .getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        String errorsMessage = Stream.of(fieldErrorsMessage, globalErrorsMessage)
                .filter(message -> !message.isEmpty())
                .collect(Collectors.joining(". "));

        ErrorResponse error = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .httpMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errorCode(MicroserviceStatus.SERVICE_INPUT_VALIDATION_ERROR.getCode())
                .description(MicroserviceStatus.SERVICE_INPUT_VALIDATION_ERROR.getDescription() + ". " + errorsMessage)
                .build();

        logException(exception, error);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class, ApplicationException.class})
    public ResponseEntity<ErrorResponse> handleApplicationException(Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .httpMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .errorCode(MicroserviceStatus.APPLICATION_FAILURE.getCode())
                .description(MicroserviceStatus.APPLICATION_FAILURE.getDescription() + ", " + exception.getMessage())
                .build();
        logException(exception, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN.value())
                .httpMessage(HttpStatus.FORBIDDEN.getReasonPhrase())
                .errorCode(MicroserviceStatus.SERVICE_ACCESS_NOT_ALLOWED.getCode())
                .description(MicroserviceStatus.SERVICE_ACCESS_NOT_ALLOWED.getDescription())
                .build();
        logException(exception, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<ErrorResponse> handleHttpStatusCodeException(final HttpStatusCodeException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder().build();
        logException(exception, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(final Exception exception) {
        HttpClientErrorException ex = (HttpClientErrorException) exception;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(ex.getStatusCode().value())
                .httpMessage(ex.getStatusText())
                .description(ex.getMessage())
                .build();
        logException(exception, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getHttpStatus()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .httpMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
                .description(exception.getMessage())
                .build();
        logException(exception, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class, RestInputValidationException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(final Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .httpMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errorCode(MicroserviceStatus.SERVICE_INPUT_VALIDATION_ERROR.getCode())
                .description(MicroserviceStatus.SERVICE_INPUT_VALIDATION_ERROR.getDescription())
                .build();
        logException(exception, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Common method to log exceptions. All http 4xx errors are logged as warnings and http 5xx is logged as errors
     *
     * @param exception     the exception to be logged
     * @param errorResponse the client error response to be logged
     */
    private void logException(final Exception exception, ErrorResponse errorResponse) {
        Integer errorCode = null;
        String errorMsg = null;
        if (exception instanceof HttpClientErrorException httpClientErrorException) {
            errorCode = httpClientErrorException.getStatusCode().value();
            errorMsg = httpClientErrorException.getResponseBodyAsString();
        } else {
            errorMsg = exception.getMessage();
        }
        if (errorResponse.getHttpStatus() >= 500) {
            log.error("ErrorCode={}, ErrorMsg={}, ErrorResponse: {}", errorCode, errorMsg, errorResponse);
            log.error("Exception: ", exception);
        } else {
            log.warn("ErrorCode={}, ErrorMsg={}, ErrorResponse: {}", errorCode, errorMsg, errorResponse);
            log.error("", exception);
        }
    }
}
