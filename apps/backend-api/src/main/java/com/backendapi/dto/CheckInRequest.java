package com.backendapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequest {
    @NotNull
    private UUID ticketCode;

    @NotNull
    private String totpCode;

    @NotNull
    private Long gateId;
}
