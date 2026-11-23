package com.backendapi.repository;

import com.backendapi.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketCode(UUID ticketCode);

    List<Ticket> findAllByUserId(String userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Ticket t 
        SET t.status = 'CHECKED_IN', 
            t.checkedInGateId = :gateId, 
            t.checkedInAt = :now,
            t.version = t.version + 1
        WHERE t.id = :id AND t.status = 'ISSUED'
    """)
    int executeCheckIn(
            @Param("id") Long id,
            @Param("gateId") Long gateId,
            @Param("now") Instant now
    );

}
