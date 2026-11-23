package com.backendapi.service.impl;

import com.backendapi.dto.CheckInRequest;
import com.backendapi.dto.CheckInResultResponse;
import com.backendapi.entity.Ticket;
import com.backendapi.enums.CheckInResultStatus;
import com.backendapi.service.CheckInLogService;
import com.backendapi.service.CheckInService;
import com.backendapi.service.TicketService;
import com.backendapi.service.TotpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final TicketService ticketService;
    private final CheckInLogService checkInLogService;
    private final TotpService totpService;

    @Override
    @Transactional
    public CheckInResultResponse checkIn(CheckInRequest req) {
        String ticketCodeStr = req.getTicketCode() != null ? req.getTicketCode().toString() : "UNKNOWN";

        Ticket ticket = ticketService.findByTicketCode(req.getTicketCode()).orElse(null);
        if (ticket == null) {
            checkInLogService.recordLog(ticketCodeStr, req.getGateId(), CheckInResultStatus.FAILED_ALREADY_USED, "Vé không tồn tại");
            return buildFailureResponse(ticketCodeStr, null, req.getGateId(), "Vé không tồn tại trên hệ thống");
        }

        if (!totpService.validateTotp(ticket.getSecretKey(), req.getTotpCode())) {
            checkInLogService.recordLog(ticketCodeStr, req.getGateId(), CheckInResultStatus.FAILED_EXPIRED, "Mã TOTP không khớp hoặc đã hết hạn");
            return buildFailureResponse(ticketCodeStr, ticket.getOwnerName(), req.getGateId(), "Mã QR không hợp lệ hoặc đã hết hạn");
        }

        int rows = ticketService.executeCheckIn(ticket.getId(), req.getGateId());
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
