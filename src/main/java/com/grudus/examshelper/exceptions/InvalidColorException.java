package com.grudus.examshelper.exceptions;

public class InvalidColorException extends IllegalArgumentException {
    public InvalidColorException(String message, Object... args) {
        super(String.format(message, args));
    }
}
