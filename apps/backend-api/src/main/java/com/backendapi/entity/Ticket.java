package com.backendapi.entity;

import com.backendapi.enums.TicketStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tickets", indexes = {
        @Index(name = "idx_ticket_code", columnList = "ticket_code", unique = true),
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "ticket_code", nullable = false, updatable = false)
    private UUID ticketCode;

    @Size(max = 64)
    @NotNull
    @Column(name = "user_id", nullable = false, length = 64, updatable = false)
    private String userId;

    @Size(max = 255)
    @NotNull
    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Size(max = 255)
    @NotNull
    @Column(name = "secret_key", nullable = false, updatable = false)
    private String secretKey;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ISSUED'")
    @Column(name = "status", nullable = false, length = 20)
    private TicketStatus status = TicketStatus.ISSUED;

    @Size(max = 64)
    @Column(name = "checked_in_gate", length = 64)
    private String checkedInGate;

    @Column(name = "checked_in_at")
    private Instant checkedInAt;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "version", nullable = false)
    @Version
    private Long version;

    @NotNull
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Ticket(UUID ticketCode, String userId, String ownerName, String secretKey) {
        this.ticketCode = ticketCode;
        this.userId = userId;
        this.ownerName = ownerName;
        this.secretKey = secretKey;
    }
}
