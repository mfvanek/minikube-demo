package io.github.mfvanek.minikubedemo.service;

import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagableHealthIndicator implements HealthIndicator {

    private final HealthManageService healthManageService;
    private final AtomicLong counter = new AtomicLong(0L);

    /**
     * {@inheritDoc}
     */
    @Override
    public Health health() {
        final long tryNumber = counter.incrementAndGet();
        log.info("Custom health indicator is called. Try {}", tryNumber);
        healthManageService.doDelayBeforeAnswer();
        return Health.up().build();
    }
}
