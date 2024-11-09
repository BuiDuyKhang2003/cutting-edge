package com.exe201.exception;

public class RegistrationAccountExistedException extends RuntimeException {
    public RegistrationAccountExistedException(String message) {
        super(message);
    }
}
