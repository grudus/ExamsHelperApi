package com.grudus.examshelper;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@ResponseBody
public class ErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(NOT_FOUND)
    public void noSuchElementException(NoSuchElementException e) {
        logger.warn("Element not found", e);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<String> bindExceptionException(BindException e) {
        return toCodes(e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void accessDenied(HttpServletResponse response, HttpServletRequest request, AuthenticatedUser user) throws IOException {
        logger.warn("Access denied for user [{}] for resource {}", user.getUser().getUsername(), request.getRequestURI());
        response.sendError(403);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return toCodes(e.getBindingResult());
    }

    private List<String> toCodes(BindingResult b) {
        return b.getAllErrors().stream().map(ObjectError::getCode).collect(toList());
    }
}