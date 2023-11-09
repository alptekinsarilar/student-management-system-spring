package com.alptekin.student.exception;

public class StudentAlreadyExistsException extends RuntimeException{
    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}
