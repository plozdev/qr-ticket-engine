package com.backendapi.exception;

import org.springframework.http.HttpStatus;

public class TicketNotFoundException extends AppException {
    public TicketNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "TICKET_NOT_FOUND");
    }
}
