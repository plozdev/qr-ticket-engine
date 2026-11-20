package com.backendapi.service;

import com.backendapi.dto.CheckInRequest;
import com.backendapi.dto.CheckInResultResponse;

import java.util.Optional;

public interface CheckInService {
    CheckInResultResponse checkIn(CheckInRequest req);
}
