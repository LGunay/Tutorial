package org.example.tutorial.exception;

public class TutorialAlreadyExistsException extends RuntimeException {
    public TutorialAlreadyExistsException(String message) {
        super(message);
    }
}
