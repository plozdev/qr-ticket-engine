package com.backendapi.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public class TicketAlreadyUsedException extends AppException {
    public TicketAlreadyUsedException(String code, String gate, Instant time) {
        super(String.format("Vé [%s] đã được quét tại cổng [%s] lúc [%s]",
                        code,
                        gate != null ? gate : "N/A",
                        time != null ? time : "N/A"),
                HttpStatus.CONFLICT, "TICKET_ALREADY_USED");
    }
}
