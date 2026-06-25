package com.student.exception;

public class CourseAssignmentNotAllowedException extends RuntimeException {
    public CourseAssignmentNotAllowedException(String message) {
        super(message);
    }
}
