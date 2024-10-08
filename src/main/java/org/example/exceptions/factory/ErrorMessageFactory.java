package org.example.exceptions.factory;

import org.example.exceptions.factory.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Component
public class ErrorMessageFactory {

    public ResponseEntity<ErrorMessage> createErrorMessage(Exception ex, WebRequest request) {
        HttpStatus httpStatus = getHttpStatus(ex);
        ErrorMessage errorMessage = new ErrorMessage(
                httpStatus.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
        return ResponseEntity.status(httpStatus).body(errorMessage);
    }

    public HttpStatus getHttpStatus (Exception ex) {
        return switch (ex.getClass().getSimpleName()) {
            case "ClientNotFound" -> HttpStatus.NOT_FOUND;
            case "InvalidPinCode" -> HttpStatus.CONFLICT;
            case "NotEnoughBalance" -> HttpStatus.BAD_GATEWAY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
