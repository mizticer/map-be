package com.employeedashboard.oirs.exception.handler;

import com.employeedashboard.oirs.exception.*;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeAlreadyExistException.class)
    public ResponseEntity handleEmployeeAlreadyExistException(EmployeeAlreadyExistException exc) {
        return ResponseEntity.badRequest().body(new EmployeeAlreadyExistError(exc.getFirstName(), exc.getLastName(), "EMPLOYEE ALREADY EXISTS"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        Map<String, List<String>> body = new HashMap<>();
        body.put("ERRORS :", errors);
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity handleEmployeeNotFoundException(EmployeeNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exc.getMessage());
    }

    @ExceptionHandler(EmployeeUnauthorizedChangeException.class)
    public ResponseEntity handleEmployeeUnauthorizedChangeException(EmployeeUnauthorizedChangeException exc) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exc.getMessage());
    }

    @ExceptionHandler(SquadAlreadyExistException.class)
    public ResponseEntity handleSquadAlreadyExistException(SquadAlreadyExistException exc) {
        return ResponseEntity.badRequest().body(new SquadAlreadyExistError(exc.getName(), "SQUAD ALREADY EXISTS"));
    }

    @ExceptionHandler(DepartmentAlreadyExistException.class)
    public ResponseEntity handleSquadAlreadyExistException(DepartmentAlreadyExistException exc) {
        return ResponseEntity.badRequest().body(new DepartmentAlreadyExistError(exc.getName(), "DEPARTMENT ALREADY EXISTS"));
    }

    @ExceptionHandler(EmailErrorSendException.class)
    public ResponseEntity handleEmailErrorSendException(EmailErrorSendException exc) {
        return ResponseEntity.badRequest().body(new EmailSendError(exc.getCode()));
    }

    @Value
    @Builder
    public static class EmployeeAlreadyExistError {
        private String firstName;
        private String lastName;
        private String code;
    }

    @Value
    @Builder
    public static class SquadAlreadyExistError {
        private String name;
        private String code;
    }

    @Value
    @Builder
    public static class DepartmentAlreadyExistError {
        private String name;
        private String code;
    }

    @Value
    @Builder
    public static class EmailSendError {
        private String code;
    }
}
