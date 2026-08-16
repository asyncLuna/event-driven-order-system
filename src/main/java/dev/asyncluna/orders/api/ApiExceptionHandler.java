package dev.asyncluna.orders.api;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ApiError notFound(EntityNotFoundException exception) {
    return new ApiError(Instant.now(), 404, "Not Found", exception.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ApiError validation(MethodArgumentNotValidException exception) {
    String details =
        exception.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
    return new ApiError(Instant.now(), 400, "Validation Failed", details);
  }

  record ApiError(Instant timestamp, int status, String error, String message) {}
}
