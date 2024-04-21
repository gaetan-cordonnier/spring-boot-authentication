package com.my.springauthentication.exception;

public class NotFoundExceptionMessage extends RuntimeException {

    public NotFoundExceptionMessage(String message) {
        super(message);
    }
}
