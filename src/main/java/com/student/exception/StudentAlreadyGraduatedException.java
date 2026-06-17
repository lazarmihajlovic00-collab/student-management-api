package com.student.exception;

public class StudentAlreadyGraduatedException extends RuntimeException {
    public StudentAlreadyGraduatedException(String message) {
        super(message);
    }
}
