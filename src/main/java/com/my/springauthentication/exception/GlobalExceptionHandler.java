package com.my.springauthentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({GenericException.class})
    public ResponseEntity<ErrorObject> handleGenericException(GenericException exception) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(exception.getErrorCode());
        errorObject.setMessage(exception.getErrorMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.valueOf(errorObject.getStatusCode()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorObject> handleNotFoundException(NotFoundException exception) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);

    }
}
