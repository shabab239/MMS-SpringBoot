package com.shabab.mezz.security.handler;

import com.shabab.mezz.util.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ApiResponse(
                "Validation failed",
                null,
                errors,
                false
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        }

        ApiResponse response = new ApiResponse(
                "Constraint validation failed",
                null,
                errors,
                false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ApiResponse(
                "Database constraint violation",
                null,
                Map.of("error", "Database constraint violation occurred. " + ex.getMessage()),
                false
        );
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    public ApiResponse handleUnexpectedRollbackException(UnexpectedRollbackException ex) {
        return new ApiResponse(
                "Transaction Rollback",
                null,
                Map.of("error", "The transaction has been rolled back unexpectedly: " + ex.getMessage()),
                false
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse handleNoSuchElementException(NoSuchElementException ex) {
        return new ApiResponse(
                "Not Found",
                null,
                null,
                false
        );
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ApiResponse handleTransactionSystemException(TransactionSystemException ex) {
        Throwable rootCause = ex.getRootCause();
        String errorMessage = rootCause != null ? rootCause.getMessage() : "Transaction error occurred.";

        return new ApiResponse(
                "Transaction Error",
                null,
                Map.of("error", errorMessage),
                false
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception ex) {
        return new ApiResponse(
                ex.getMessage(),
                null,
                null,
                false
        );
    }

}

