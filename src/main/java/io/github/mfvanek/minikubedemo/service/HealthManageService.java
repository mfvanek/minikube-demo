package io.github.mfvanek.minikubedemo.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HealthManageService {

    private final AtomicLong delayInSeconds = new AtomicLong(0L);

    /**
     * Sets a given {@code newDelayInSeconds} value and returns an old one.
     *
     * @param newDelayInSeconds new delay value
     * @return old delay value
     */
    public long setDelay(final long newDelayInSeconds) {
        log.info("Trying to set new delay to {}", newDelayInSeconds);
        if (newDelayInSeconds < 0) {
            throw new IllegalArgumentException("newDelayInSeconds cannot be negative");
        }
        return delayInSeconds.getAndSet(newDelayInSeconds);
    }

    @SneakyThrows
    public void doDelayBeforeAnswer() {
        final long currentDelay = delayInSeconds.get();
        if (currentDelay > 0) {
            log.debug("Delay of {} seconds", currentDelay);
            TimeUnit.SECONDS.sleep(currentDelay);
            log.debug("Delay is done!");
        }
    }
}
