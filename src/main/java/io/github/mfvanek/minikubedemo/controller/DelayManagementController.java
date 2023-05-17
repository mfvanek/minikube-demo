package io.github.mfvanek.minikubedemo.controller;

import io.github.mfvanek.minikubedemo.service.HealthManageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DelayManagementController {

    private final HealthManageService healthManageService;

    @PutMapping(value = "/delay", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> setDelay(@RequestBody final DelayDto delayDto) {
        return ResponseEntity.ok(healthManageService.setDelay(delayDto.getNewDelayInSeconds()));
    }

    @Data
    @SuperBuilder
    @RequiredArgsConstructor
    public static class DelayDto {
        private long newDelayInSeconds;
    }
}
