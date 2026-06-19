package com.student.exception;

public class StudentStatusChangeNotAllowedException extends RuntimeException {
    public StudentStatusChangeNotAllowedException(String message) {
        super(message);
    }
}
