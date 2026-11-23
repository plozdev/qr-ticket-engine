package com.backendapi.entity;

import com.backendapi.enums.CheckInResultStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "check_in_logs", indexes = {
        @Index(name = "idx_log_ticket_code", columnList = "ticket_code"),
        @Index(name = "idx_log_scanned_at", columnList = "scanned_at"),
        @Index(name = "idx_log_gate_id", columnList = "gate_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckInLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 64)
    @NotNull
    @Column(name = "ticket_code", nullable = false, length = 64, updatable = false)
    private String ticketCode;

    @NotNull
    @Column(name = "gate_id", nullable = false, updatable = false)
    private Long gateId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false, length = 32, updatable = false)
    private CheckInResultStatus result;

    @Column(name = "error_message", length = 1000, updatable = false)
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "scanned_at", nullable = false, updatable = false)
    private Instant scannedAt;

    public CheckInLog(String ticketCode, Long gateId, CheckInResultStatus result, String errorMessage) {
        this.ticketCode = ticketCode;
        this.gateId = gateId;
        this.result = result;
        this.errorMessage = errorMessage;
    }
}