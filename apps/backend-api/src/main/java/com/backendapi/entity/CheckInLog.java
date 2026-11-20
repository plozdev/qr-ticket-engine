package com.backendapi.entity;

import com.backendapi.enums.CheckInResultStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "check_in_logs", indexes = {
        @Index(name = "idx_log_ticket_code", columnList = "ticket_code"),
        @Index(name = "idx_log_scanned_at", columnList = "scanned_at")
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
    @Column(name = "ticket_code", nullable = false, length = 64)
    private String ticketCode;

    @Size(max = 64)
    @NotNull
    @Column(name = "gate_id", nullable = false, length = 64)
    private String gateId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false, length = 32)
    private CheckInResultStatus result;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "scanned_at", nullable = false)
    private Instant scannedAt = Instant.now();

    public CheckInLog(String ticketCode, String gateId, CheckInResultStatus result, String errorMessage) {
        this.ticketCode = ticketCode;
        this.gateId = gateId;
        this.result = result;
        this.errorMessage = errorMessage;
        this.scannedAt = Instant.now();
    }
}