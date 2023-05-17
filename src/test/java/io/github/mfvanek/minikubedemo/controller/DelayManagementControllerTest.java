package io.github.mfvanek.minikubedemo.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mfvanek.minikubedemo.controller.DelayManagementController.DelayDto;
import io.github.mfvanek.minikubedemo.service.HealthManageService;
import io.github.mfvanek.minikubedemo.support.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class DelayManagementControllerTest extends TestBase {

    @Autowired
    private HealthManageService healthManageService;

    @BeforeEach
    void setUpDelay() {
        healthManageService.setDelay(0L);
    }

    @Test
    void setDelayShouldWork() {
        final Long oldDelay = webTestClient.put()
            .uri("/delay")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(DelayDto.builder()
                .newDelayInSeconds(10L)
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody(Long.class)
            .returnResult()
            .getResponseBody();
        assertThat(oldDelay)
            .isZero();
        assertThat(healthManageService.getCurrentDelay())
            .isEqualTo(10L);
    }
}
