package com.backendapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrPayloadResponse {
    private UUID ticketCode;
    private String totpCode;
    private int remainingSeconds;
    private String payloadString;
}
