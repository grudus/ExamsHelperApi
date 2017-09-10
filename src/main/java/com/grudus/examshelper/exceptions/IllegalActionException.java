package com.grudus.examshelper.exceptions;

public class IllegalActionException extends RuntimeException {
    public IllegalActionException(String message, Object... args) {
        super(String.format(message, args));
    }
}
