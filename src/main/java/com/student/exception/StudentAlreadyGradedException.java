package com.student.exception;

public class StudentAlreadyGradedException extends RuntimeException {
    public StudentAlreadyGradedException(String message) {
        super(message);
    }
}
