package com.grudus.examshelper.exceptions;

import org.springframework.security.core.AuthenticationException;

public class CannotFindUserException extends AuthenticationException {

    public CannotFindUserException(String msg) {
        super(msg);
    }
}
