package com.backendapi.service.impl;

import com.backendapi.dto.GateResponse;
import com.backendapi.repository.GateRepository;
import com.backendapi.service.GateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GateServiceImpl implements GateService {

    private final GateRepository gateRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GateResponse> getActiveGates() {
        return gateRepository.findByActiveTrue().stream()
                .map(g -> GateResponse.builder()
                        .id(g.getId())
                        .code(g.getCode())
                        .name(g.getName())
                        .build())
                .toList();
    }
}
