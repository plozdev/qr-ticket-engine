package com.backendapi.exception;

import org.springframework.http.HttpStatus;

public class CryptoOperationException extends AppException {
    public CryptoOperationException(String message, Throwable cause) {
        super(message + (cause != null ? ": " + cause.getMessage() : ""), HttpStatus.INTERNAL_SERVER_ERROR, "CRYPTO_OPERATION_FAILED");
    }
}
