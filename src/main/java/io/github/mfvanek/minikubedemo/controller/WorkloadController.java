package io.github.mfvanek.minikubedemo.controller;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/workload")
public class WorkloadController {

    @SneakyThrows
    @GetMapping("/now")
    public ResponseEntity<OffsetDateTime> nowWithDelay(@RequestParam("delay") final long delayInMilliseconds) {
        if (delayInMilliseconds >= 0) {
            TimeUnit.MILLISECONDS.sleep(delayInMilliseconds);
        } else {
            log.warn("Skipping negative delay value {}", delayInMilliseconds);
        }
        return ResponseEntity.ok(OffsetDateTime.now());
    }
}
