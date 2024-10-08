package org.example.exceptions.controller;

import org.example.exceptions.ClientNotFound;
import org.example.exceptions.InvalidPinCode;
import org.example.exceptions.NotEnoughBalance;
import org.example.exceptions.factory.ErrorMessage;
import org.example.exceptions.factory.ErrorMessageFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.security.auth.login.AccountNotFoundException;

@RestControllerAdvice
public class UserControllerAdvice {

    private final ErrorMessageFactory errorMessageFactory;

    public UserControllerAdvice(ErrorMessageFactory errorMessageFactory) {
        this.errorMessageFactory = errorMessageFactory;
    }

    @ExceptionHandler({
            ClientNotFound.class,
            InvalidPinCode.class,
            NotEnoughBalance.class
    })
    public ResponseEntity<ErrorMessage> handleException(Exception ex, WebRequest request) {
        return errorMessageFactory.createErrorMessage(ex, request);
    }
}
