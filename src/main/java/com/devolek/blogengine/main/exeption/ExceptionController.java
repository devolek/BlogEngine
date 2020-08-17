package com.devolek.blogengine.main.exeption;

import com.devolek.blogengine.main.dto.response.universal.UniversalResponseFactory;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {ExpiredJwtException.class})
    protected ResponseEntity<Object> handleUncaughtException(
            ExpiredJwtException ex,
            WebRequest request
    ) {
        log.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                "",
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED,
                request
        );
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<?> handleUserException(
            UserNotFoundException ex,
            WebRequest request
    ) {
        log.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                "User not found",
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    protected ResponseEntity<?> handleInvalidRequestException(
            InvalidRequestException ex,
            WebRequest request
    ) {
        log.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<?> handleNotFoundException(
            AuthenticationException ex,
            WebRequest request
    ) {
        log.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                "Not AUTHORIZED",
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED,
                request
        );
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<?> handleNotFoundException(
            NotFoundException ex,
            WebRequest request
    ) {
        log.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                "NOT FOUND",
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    protected ResponseEntity<?> handleMaxSizeException(
            MaxUploadSizeExceededException ex,
            WebRequest request
    ) {
        log.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                UniversalResponseFactory.getFileMaxSizeResponse(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<?> handleInvalidLoginException(
            BadCredentialsException ex,
            WebRequest request
    ) {
        log.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                UniversalResponseFactory.getFalseResponse(),
                new HttpHeaders(),
                HttpStatus.OK,
                request
        );
    }
}
