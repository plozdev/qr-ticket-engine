package com.backendapi.controller;

import com.backendapi.dto.CheckInRequest;
import com.backendapi.dto.CheckInResultResponse;
import com.backendapi.dto.QrPayloadResponse;
import com.backendapi.entity.Ticket;
import com.backendapi.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket", description = "API quản lý và truy xuất thông tin vé, mã QR")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{ticketId}")
    @Operation(summary = "Lấy chi tiết vé theo ID", description = "Trả về thông tin chi tiết của vé dựa trên ID số.")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId) {
        return ResponseEntity.of(ticketService.findByTicketId(ticketId));
    }

    @GetMapping("/{ticketId}/qr")
    @Operation(summary = "Lấy mã QR (TOTP) của vé", description = "Tạo mã QR chứa UUID và mã TOTP 30s để người tham dự check-in tại cổng.")
    public ResponseEntity<QrPayloadResponse> getDynamicQrPayload(
            @PathVariable Long ticketId,
            @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
            @RequestParam(value = "userId", required = false) String paramUserId
    ) {
        String userId = (headerUserId != null && !headerUserId.isBlank()) ? headerUserId : paramUserId;
        if (userId == null || userId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ticketService.getDynamicQrPayload(ticketId, userId));
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Lấy danh sách vé của người dùng", description = "Trả về toàn bộ vé đã mua của người dùng theo userId.")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable String userId) {
        return ResponseEntity.ok(ticketService.findAllByUserId(userId));
    }

    @GetMapping("/code/{ticketCode}")
    @Operation(summary = "Tìm vé theo UUID ticketCode", description = "Tra cứu thông tin vé dựa trên mã định danh ticketCode.")
    public ResponseEntity<Ticket> getTicketByCode(@PathVariable UUID ticketCode) {
        return ResponseEntity.of(ticketService.findByTicketCode(ticketCode));
    }

    @PostMapping("/check-in")
    @Operation(summary = "Xác thực và soát vé qua cổng", description = "Thực hiện kiểm tra TOTP và Atomic Update trạng thái vé.")
    public ResponseEntity<CheckInResultResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        CheckInResultResponse response = ticketService.checkIn(request);
        return ResponseEntity.ok(response);
    }
}

