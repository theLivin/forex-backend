package com.demo.forexbackend.error;

import com.demo.forexbackend.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            NotFoundException.class,
    })
    public ResponseEntity<ErrorResponseDto> entityNotFoundExceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler({
            NotNullException.class,
    })
    public ResponseEntity<ErrorResponseDto> notNullExceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler({
            ConflictException.class,
    })
    public ResponseEntity<ErrorResponseDto> conflictExceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponseDto.builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponseDto> handleWebClientExceptionHandler(WebClientResponseException exception){
        if (exception.getStatusCode() == HttpStatusCode.valueOf(500)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body
                    (ErrorResponseDto.builder()
                            .success(false)
                            .message(exception.getResponseBodyAsString())
                            .build()
                    );
        }

        return ResponseEntity.badRequest().body(
                ErrorResponseDto.builder()
                        .success(false)
                        .message(exception.getResponseBodyAsString())
                        .build());
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<ErrorResponseDto> invalidBearerTokenExceptionHandler(InvalidBearerTokenException exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ErrorResponseDto.builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build()
                );
    }
}
