package com.backendapi.service;

import com.backendapi.enums.CheckInResultStatus;

public interface CheckInLogService {
    void recordLog(String ticketCode, Long gateId, CheckInResultStatus status, String errorMessage);
}
