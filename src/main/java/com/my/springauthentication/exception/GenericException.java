package com.my.springauthentication.exception;

import lombok.Getter;

@Getter
public class GenericException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;

    public GenericException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
