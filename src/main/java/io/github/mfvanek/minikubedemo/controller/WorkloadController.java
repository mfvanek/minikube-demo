package io.github.mfvanek.minikubedemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/api/v1/workload")
public class WorkloadController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/now")
    public ResponseEntity<OffsetDateTime> nowWithDelay(@RequestParam("delay") final long delayInMilliseconds) {
        final long requestNum = counter.incrementAndGet();
        try {
            log.info("executing nowWithDelay {}", requestNum);
            if (delayInMilliseconds >= 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(delayInMilliseconds);
                } catch (InterruptedException e) {
                    log.warn("nowWithDelay was interrupted", e);
                    Thread.currentThread().interrupt();
                }
            } else {
                log.warn("Skipping negative delay value {}", delayInMilliseconds);
            }
            return ResponseEntity.ok(OffsetDateTime.now());
        } finally {
            log.info("finishing execution of nowWithDelay {}", requestNum);
        }
    }
}
