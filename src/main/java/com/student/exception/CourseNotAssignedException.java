package com.student.exception;

public class CourseNotAssignedException extends RuntimeException {
    public CourseNotAssignedException(String message) {
        super(message);
    }
}
