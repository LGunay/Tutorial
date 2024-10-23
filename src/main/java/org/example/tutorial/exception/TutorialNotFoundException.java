package org.example.tutorial.exception;

public class TutorialNotFoundException extends RuntimeException {
    public TutorialNotFoundException(String msg) {
        super(msg);
    }
}
