package com.employeedashboard.oirs.exception;

public class EmployeeUnauthorizedChangeException extends RuntimeException {

    public EmployeeUnauthorizedChangeException(String message) {
        super(message);
    }
}