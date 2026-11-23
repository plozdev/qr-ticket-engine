package com.backendapi.service;

import com.backendapi.dto.GateResponse;

import java.util.List;

public interface GateService {
    List<GateResponse> getActiveGates();
}
