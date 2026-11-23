package com.backendapi.service.impl;

import com.backendapi.dto.QrPayloadResponse;
import com.backendapi.entity.Ticket;
import com.backendapi.enums.TicketStatus;
import com.backendapi.repository.TicketRepository;
import com.backendapi.service.TicketService;
import com.backendapi.service.TotpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    
    private final TicketRepository ticketRepository;
    private final TotpService totpService;

    @Override
    @Transactional(readOnly = true)
    public Optional<QrPayloadResponse> getDynamicQrPayload(Long ticketId, String reqUserId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket == null ||
            !ticket.getUserId().equals(reqUserId) ||
            ticket.getStatus() != TicketStatus.ISSUED)
            return Optional.empty();

        String totpCode = totpService.generateCurrentTotp(ticket.getSecretKey());
        int remainingSeconds = totpService.getRemainingSeconds();
        String payloadString = ticket.getTicketCode() + ":" + totpCode;

        return Optional.of(new QrPayloadResponse(
                ticket.getTicketCode(),
                totpCode,
                remainingSeconds,
                payloadString
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> findByTicketCode(UUID ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode);
    }

    @Override
    @Transactional
    public int executeCheckIn(Long ticketId, Long gateId) {
        return ticketRepository.executeCheckIn(ticketId, gateId, Instant.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> findAllByUserId(String userId) {
        return ticketRepository.findAllByUserId(userId);
    }

}
