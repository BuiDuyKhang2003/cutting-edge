package com.exe201.exception;

import com.exe201.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.exe201.exception.BusinessErrorCodes.*;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handlerException(LockedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(ACCOUNT_LOCKED.getCode())
                                .timestamp(DateUtil.formatTimestamp(new Date()))
                                .message(ACCOUNT_LOCKED.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(ACCOUNT_DISABLED.getCode())
                                .timestamp(DateUtil.formatTimestamp(new Date()))
                                .message(ACCOUNT_DISABLED.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(BAD_CREDENTIALS.getCode())
                                .timestamp(DateUtil.formatTimestamp(new Date()))
                                .message(BAD_CREDENTIALS.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Map<String, String> errors = new HashMap<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(400)
                                .timestamp(DateUtil.formatTimestamp(new Date()))
                                .data(errors)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(500)
                                .timestamp(DateUtil.formatTimestamp(new Date()))
                                .message("Internal error, please contact the admin")
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ActivationTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(ActivationTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(400)
                                .timestamp(DateUtil.formatTimestamp(new Date()))
                                .message("Email verification failed")
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(RegistrationAccountExistedException.class)
    public ResponseEntity<ExceptionResponse> handleRegistrationAccountExistedException(RegistrationAccountExistedException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .httpStatus(HttpStatus.CONFLICT.value())
                                .timestamp(DateUtil.formatTimestamp(new Date()))
                                .message("Registration failed")
                                .error(exception.getMessage())
                                .build()
                );
    }

    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex) {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                    ExceptionResponse.builder()
                            .httpStatus(HttpStatus.NOT_FOUND.value())
                            .timestamp(DateUtil.formatTimestamp(new Date()))
                            .message("Not found")
                            .error(ex.getMessage())
                            .build()
            );

    }
}
