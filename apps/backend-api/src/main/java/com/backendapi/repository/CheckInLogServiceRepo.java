package com.backendapi.repository;

import com.backendapi.entity.CheckInLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInLogServiceRepo extends JpaRepository <CheckInLog, Long> {
}
