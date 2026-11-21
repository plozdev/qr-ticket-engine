package com.backendapi.controller;

import com.backendapi.dto.CheckInRequest;
import com.backendapi.dto.CheckInResultResponse;
import com.backendapi.service.CheckInService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gates")
@RequiredArgsConstructor
public class TicketController {
    private final CheckInService checkInService;

    @PostMapping("/check-in")
    @Operation(summary = "Xác thực và soát vé qua cổng", description = "Thực hiện kiểm tra TOTP và Atomic Update trạng thái vé.")
    public ResponseEntity<CheckInResultResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        CheckInResultResponse response = checkInService.checkIn(request);
        return ResponseEntity.ok(response);
    }
}
