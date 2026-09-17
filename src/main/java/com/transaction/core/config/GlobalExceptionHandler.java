package com.transaction.core.config;

import com.transaction.core.dto.response.Error;
import com.transaction.core.exception.AccountNotFoundException;
import com.transaction.core.exception.InsufficientBalanceException;
import com.transaction.core.exception.InvalidTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    ResponseEntity<Error> handleAccountNotFound(AccountNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error("ACCOUNT_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    ResponseEntity<Error> handleInsufficientBalance(InsufficientBalanceException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new Error("INSUFFICIENT_BALANCE", ex.getMessage()));
    }

    @ExceptionHandler(InvalidTransactionException.class)
    ResponseEntity<Error> handleInvalidTransaction(InvalidTransactionException ex) {
        return ResponseEntity
                .badRequest()
                .body(new Error("INVALID_TRANSACTION", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleInvalidTransaction(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );
        return ResponseEntity.badRequest().body(errors);
    }

}
