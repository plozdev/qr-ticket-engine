package com.backendapi.service.impl;

import com.backendapi.dto.CheckInRequest;
import com.backendapi.dto.CheckInResultResponse;
import com.backendapi.dto.QrPayloadResponse;
import com.backendapi.entity.Ticket;
import com.backendapi.enums.CheckInResultStatus;
import com.backendapi.enums.TicketStatus;
import com.backendapi.exception.InvalidTicketStatusException;
import com.backendapi.exception.TicketNotFoundException;
import com.backendapi.exception.TicketOwnershipException;
import com.backendapi.repository.TicketRepository;
import com.backendapi.service.CheckInLogService;
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
    private final CheckInLogService checkInLogService;

    @Override
    @Transactional(readOnly = true)
    public QrPayloadResponse getDynamicQrPayload(Long ticketId, String reqUserId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Không tìm thấy vé với ID: " + ticketId));

        if (!ticket.getUserId().equals(reqUserId)) {
            throw new TicketOwnershipException("Bạn không có quyền truy cập vé này.");
        }

        if (ticket.getStatus() != TicketStatus.ISSUED) {
            throw new InvalidTicketStatusException("Vé không ở trạng thái hợp lệ để sinh mã QR (Trạng thái hiện tại: " + ticket.getStatus() + ")");
        }

        String totpCode = totpService.generateCurrentTotp(ticket.getSecretKey());
        int remainingSeconds = totpService.getRemainingSeconds();
        String payloadString = ticket.getTicketCode() + ":" + totpCode;

        return new QrPayloadResponse(
                ticket.getTicketCode(),
                totpCode,
                remainingSeconds,
                payloadString
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> findByTicketId(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> findByTicketCode(UUID ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> findAllByUserId(String userId) {
        return ticketRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public CheckInResultResponse checkIn(CheckInRequest req) {
        String ticketCodeStr = req.getTicketCode() != null ? req.getTicketCode().toString() : "UNKNOWN";

        Ticket ticket = ticketRepository.findByTicketCode(req.getTicketCode()).orElse(null);
        if (ticket == null) {
            checkInLogService.recordLog(ticketCodeStr, req.getGateId(), CheckInResultStatus.FAILED_ALREADY_USED, "Vé không tồn tại");
            return buildFailureResponse(ticketCodeStr, null, req.getGateId(), "Vé không tồn tại trên hệ thống");
        }

        if (!totpService.validateTotp(ticket.getSecretKey(), req.getTotpCode())) {
            checkInLogService.recordLog(ticketCodeStr, req.getGateId(), CheckInResultStatus.FAILED_EXPIRED, "Mã TOTP không khớp hoặc đã hết hạn");
            return buildFailureResponse(ticketCodeStr, ticket.getOwnerName(), req.getGateId(), "Mã QR không hợp lệ hoặc đã hết hạn");
        }

        int rows = ticketRepository.executeCheckIn(ticket.getId(), req.getGateId(), Instant.now());
        if (rows == 0) {
            checkInLogService.recordLog(ticketCodeStr, req.getGateId(), CheckInResultStatus.FAILED_ALREADY_USED, "Vé đã được quét trước đó hoặc đã bị thu hồi");
            return buildFailureResponse(ticketCodeStr, ticket.getOwnerName(), req.getGateId(), "Vé đã được sử dụng trước đó");
        }

        checkInLogService.recordLog(ticketCodeStr, req.getGateId(), CheckInResultStatus.SUCCESS, null);
        return CheckInResultResponse.builder()
                .status("SUCCESS")
                .ticketCode(ticketCodeStr)
                .ticketOwnerName(ticket.getOwnerName())
                .gateId(req.getGateId())
                .scanTime(Instant.now())
                .msg("Check-in thành công")
                .build();
    }

    private CheckInResultResponse buildFailureResponse(String ticketCode, String ownerName, Long gateId, String msg) {
        return CheckInResultResponse.builder()
                .status("FAILED")
                .ticketCode(ticketCode)
                .ticketOwnerName(ownerName)
                .gateId(gateId)
                .scanTime(Instant.now())
                .msg(msg)
                .build();
    }
}

