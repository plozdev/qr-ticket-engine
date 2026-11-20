package com.backendapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequest {
    private UUID ticketCode;
    private String totpCode;
    private String gateId;
}
