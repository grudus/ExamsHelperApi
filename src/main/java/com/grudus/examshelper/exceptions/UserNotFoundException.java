package com.grudus.examshelper.exceptions;


import javax.security.sasl.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String detail) {
        super(detail);
    }

    public UserNotFoundException(String detail, Throwable ex) {
        super(detail, ex);
    }
}
