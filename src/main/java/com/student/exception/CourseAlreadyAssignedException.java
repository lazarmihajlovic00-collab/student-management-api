package com.student.exception;

public class CourseAlreadyAssignedException extends RuntimeException {
    public CourseAlreadyAssignedException(String message) {
        super(message);
    }
}
