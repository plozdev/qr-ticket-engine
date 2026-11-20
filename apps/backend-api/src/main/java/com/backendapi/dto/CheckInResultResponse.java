package com.backendapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInResultResponse {
    private String status;
    private String ticketCode;
    private String ticketOwnerName;
    private String gateId;
    private Instant scanTime;
    private String msg;
}
