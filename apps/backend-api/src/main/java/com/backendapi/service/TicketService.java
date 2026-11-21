package com.backendapi.service;

import com.backendapi.dto.QrPayloadResponse;
import com.backendapi.entity.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketService {
    Optional<QrPayloadResponse> getDynamicQrPayload(Long ticketId, String reqUserId);
    Optional<Ticket> findByTicketCode(UUID ticketCode);
    int executeCheckIn(Long ticketId, String gateId);
    List<Ticket> findAllByUserId(String userId);
}
