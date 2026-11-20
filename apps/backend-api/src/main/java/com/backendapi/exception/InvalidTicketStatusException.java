package com.backendapi.exception;

import org.springframework.http.HttpStatus;

public class InvalidTicketStatusException extends AppException {
    public InvalidTicketStatusException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_TICKET_STATUS");
    }
}
