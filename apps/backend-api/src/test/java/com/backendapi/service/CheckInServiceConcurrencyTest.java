package com.backendapi.service;

import com.backendapi.dto.CheckInRequest;
import com.backendapi.dto.CheckInResultResponse;
import com.backendapi.entity.Ticket;
import com.backendapi.repository.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CheckInServiceConcurrencyTest {

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TotpService totpService;

    @Test
    @DisplayName("Giả lập 10 cổng quét cùng 1 thời điểm: Chỉ duy nhất 1 cổng thành công, 9 cổng thất bại")
    void testConcurrentCheckIn_ShouldOnlyAllowOneSuccess() throws Exception {
        UUID ticketCode = UUID.randomUUID();
        String base32Secret = "JBSWY3DPEHPK3PXP";
        ticketRepository.save(new Ticket(ticketCode, "USR_1", "Nguyen Van A", base32Secret));
        String validTotp = totpService.generateCurrentTotp(base32Secret);

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final Long gateId = (long) ((i % 4) + 1);
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    CheckInResultResponse response = checkInService.checkIn(
                            new CheckInRequest(ticketCode, validTotp, gateId)
                    );

                    if ("SUCCESS".equals(response.getStatus())) {
                        successCount.incrementAndGet();
                    } else {
                        failedCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(1, successCount.get(), "Chỉ duy nhất 1 cổng được check-in thành công");
        assertEquals(9, failedCount.get(), "9 cổng còn lại phải nhận kết quả FAILED");
    }
}
