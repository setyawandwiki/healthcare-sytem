package com.STWN.healthcare_project.middleware;

import com.STWN.healthcare_project.common.exception.*;
import com.STWN.healthcare_project.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {
    @ExceptionHandler({
            ResourceNotFoundException.class,
            UserNotFoundException.class,
            RoleNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(
            HttpServletRequest request, RuntimeException e){
        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestException(
            HttpServletRequest request, BadRequestException e){
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse genericException(
            HttpServletRequest request, Exception e){
        log.error("telah terjadi error pada enpoint {}. status code {}. error message: {}",
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnAuthorizedException(
            HttpServletRequest request, InvalidPasswordException e){
        return ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler({UsernameAlreadyExist.class, EmailAlreadyExist.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleConflickException(
            HttpServletRequest request, Exception e){
        return ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
