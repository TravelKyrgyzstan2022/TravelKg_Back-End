package com.example.benomad.advice;

import com.example.benomad.exception.CustomException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice()
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleNotFoundException(HttpServletRequest request, RuntimeException exception) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(500)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now(ZoneId.of("Asia/Bishkek")))
                .build();
        log.error(exception.getMessage());
        exception.printStackTrace();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(HttpServletRequest request, CustomException exception) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(exception.getStatusCode())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now(ZoneId.of("Asia/Bishkek")))
                .build();
        log.error(exception.getMessage());
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException exception) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(500)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now(ZoneId.of("Asia/Bishkek")))
                .build();
        log.error(exception.getMessage());
        exception.printStackTrace();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<Object> handleInvalidDAAUsageException(HttpServletRequest request, InvalidDataAccessApiUsageException exception) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(400)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now(ZoneId.of("Asia/Bishkek")))
                .build();
        log.error(exception.getMessage());
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(HttpServletRequest request, BadCredentialsException exception) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(401)
                .message("Bad credentials")
                .timestamp(LocalDateTime.now(ZoneId.of("Asia/Bishkek")))
                .build();
        log.error(exception.getMessage());
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(400)
                .message(message)
                .timestamp(LocalDateTime.now(ZoneId.of("Asia/Bishkek")))
                .build();
        log.error(exception.getMessage());
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }
}