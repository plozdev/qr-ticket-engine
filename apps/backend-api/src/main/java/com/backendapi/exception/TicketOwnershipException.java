package com.backendapi.exception;

import org.springframework.http.HttpStatus;

public class TicketOwnershipException extends AppException {
    public TicketOwnershipException(String message) {
        super(message, HttpStatus.FORBIDDEN, "FORBIDDEN_TICKET_ACCESS");
    }
}
