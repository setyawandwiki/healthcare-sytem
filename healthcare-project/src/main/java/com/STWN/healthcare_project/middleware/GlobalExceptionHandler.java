package com.STWN.healthcare_project.middleware;

import com.STWN.healthcare_project.common.exception.*;
import com.STWN.healthcare_project.model.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.relation.RoleNotFoundException;
import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
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
            HttpServletRequest request, HttpServletResponse response, Exception e){
        if(e instanceof AccessDeniedException
        || e instanceof SignatureException
        || e instanceof ExpiredJwtException
        || e instanceof AuthenticationException
        || e instanceof InsufficientAuthenticationException){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        log.error("telah terjadi error pada enpoint {}. status code {}. error message: {}",
                request.getRequestURI(),
                HttpStatus.FORBIDDEN,
                e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
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
