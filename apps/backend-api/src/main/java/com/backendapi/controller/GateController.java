package com.backendapi.controller;

import com.backendapi.dto.CheckInRequest;
import com.backendapi.dto.CheckInResultResponse;
import com.backendapi.dto.GateResponse;
import com.backendapi.service.CheckInService;
import com.backendapi.service.GateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gates")
@RequiredArgsConstructor
@Tag(name = "Gate Check-In", description = "API dành cho thiết bị quét và cổng kiểm soát ra vào")
public class GateController {

    private final CheckInService checkInService;
    private final GateService gateService;

    @GetMapping
    @Operation(summary = "Lấy danh sách các cổng đang mở", description = "Trả về danh mục các cổng hoạt động để nhân viên an ninh chọn trạm trực.")
    public ResponseEntity<List<GateResponse>> getActiveGates() {
        return ResponseEntity.ok(gateService.getActiveGates());
    }

    @PostMapping("/check-in")
    @Operation(summary = "Xác thực và soát vé qua cổng", description = "Thực hiện kiểm tra TOTP và Atomic Update trạng thái vé.")
    public ResponseEntity<CheckInResultResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        CheckInResultResponse response = checkInService.checkIn(request);
        return ResponseEntity.ok(response);
    }
}
