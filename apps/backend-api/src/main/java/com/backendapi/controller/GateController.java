package com.backendapi.controller;

import com.backendapi.dto.QrPayloadResponse;
import com.backendapi.entity.Ticket;
import com.backendapi.exception.TicketNotFoundException;
import com.backendapi.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class GateController {
    private final TicketService ticketService;

    @GetMapping("/{ticketId}/qr")
    @Operation(summary = "Sinh mã Dynamic QR 30 giây", description = "Trả về chuỗi ticketCode:totpCode kèm số giây còn lại của chu kỳ.")
    public ResponseEntity<QrPayloadResponse> getDynamicQr(
            @PathVariable Long ticketId,
            @RequestHeader(value = "X-User-Id") String reqUserId
    ) {
        return ticketService.getDynamicQrPayload(ticketId, reqUserId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TicketNotFoundException("Vé không tồn tại hoặc bạn không có quyền sở hữu"));
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Xem danh sách vé của người dùng", description = "Tra cứu toàn bộ các vé mà user đã mua.")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable String userId) {
        List<Ticket> tickets = ticketService.findAllByUserId(userId);
        return ResponseEntity.ok(tickets);
    }
}
