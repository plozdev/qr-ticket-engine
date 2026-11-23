package com.backendapi.controller;

import com.backendapi.dto.GateResponse;
import com.backendapi.service.GateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gates")
@RequiredArgsConstructor
@Tag(name = "Gate", description = "API dành cho truy xuất thông tin cửa quét")
public class GateController {

    private final GateService gateService;

    @GetMapping
    @Operation(summary = "Lấy danh sách các cổng đang mở", description = "Trả về danh mục các cổng hoạt động để nhân viên an ninh chọn trạm trực.")
    public ResponseEntity<List<GateResponse>> getActiveGates() {
        return ResponseEntity.ok(gateService.getActiveGates());
    }
}
