package com.quizly.config;

import com.quizly.exceptions.BadRequestException;
import com.quizly.exceptions.ExceptionResponse;
import com.quizly.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse notFoundHandler(BadRequestException badRequestException) {
        log.info(badRequestException.getMessage());
        return new ExceptionResponse(BAD_REQUEST.value(), badRequestException.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse notFoundHandler(NotFoundException notFoundException) {
        log.info(notFoundException.getMessage());
        return new ExceptionResponse(NOT_FOUND.value(), notFoundException.getMessage());
    }
}
