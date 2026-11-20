package com.backendapi.exception;

import org.springframework.http.HttpStatus;

public class InvalidQRCodeException extends AppException {
    public InvalidQRCodeException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_OR_EXPIRED_QR");
    }
}
