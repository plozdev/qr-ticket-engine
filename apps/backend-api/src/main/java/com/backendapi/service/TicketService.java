package com.backendapi.service;

import com.backendapi.dto.CheckInRequest;
import com.backendapi.dto.CheckInResultResponse;
import com.backendapi.dto.QrPayloadResponse;
import com.backendapi.entity.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketService {
    QrPayloadResponse getDynamicQrPayload(Long ticketId, String reqUserId);
    Optional<Ticket> findByTicketId(Long ticketId);
    Optional<Ticket> findByTicketCode(UUID ticketCode);
    List<Ticket> findAllByUserId(String userId);
    CheckInResultResponse checkIn(CheckInRequest req);
}
