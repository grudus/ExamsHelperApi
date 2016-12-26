package com.grudus.examshelper.exceptions;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Cannot find user");
    }

    public UserNotFoundException(String username) {
        super("Cannot find user: " + username);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String detail, Throwable ex) {
        super(detail, ex);
    }

}
