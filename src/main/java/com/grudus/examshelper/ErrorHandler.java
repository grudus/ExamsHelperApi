package com.grudus.examshelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return toCodes(e.getBindingResult());
    }

    private List<String> toCodes(BindingResult b) {
        return b.getAllErrors().stream().map(ObjectError::getCode).collect(toList());
    }
}