package com.backendapi.service;

import com.backendapi.dto.QrPayloadResponse;
import com.backendapi.entity.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketService {
    Optional<QrPayloadResponse> getDynamicQrPayload(Long ticketId, String reqUserId);
    Optional<Ticket> findByTicketCode(UUID ticketCode);
    List<Ticket> findAllByUserId(String userId);
    int executeCheckIn(Long ticketId, Long gateId);
}
