package com.backendapi.service.impl;

import com.backendapi.entity.CheckInLog;
import com.backendapi.enums.CheckInResultStatus;
import com.backendapi.repository.CheckInLogServiceRepo;
import com.backendapi.service.CheckInLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckInLogServiceImpl implements CheckInLogService {

    private final CheckInLogServiceRepo repo;

    @Override
    public void recordLog(String ticketCode, Long gateId, CheckInResultStatus status, String errorMessage) {
        CheckInLog log = new CheckInLog(ticketCode, gateId, status, errorMessage);
        repo.save(log);
    }
}
