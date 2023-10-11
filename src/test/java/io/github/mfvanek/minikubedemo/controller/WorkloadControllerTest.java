package io.github.mfvanek.minikubedemo.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mfvanek.minikubedemo.support.TestBase;
import java.time.OffsetDateTime;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class WorkloadControllerTest extends TestBase {

    @ParameterizedTest
    @ValueSource(longs = {-10, 0, 10})
    void shouldWork(final long delay) {
        final OffsetDateTime result = webTestClient.get()
            .uri(uriBuilder ->
                uriBuilder.path("/api/v1/workload/now")
                    .queryParam("delayInMilliseconds", delay)
                    .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody(OffsetDateTime.class)
            .returnResult()
            .getResponseBody();
        assertThat(result)
            .isBeforeOrEqualTo(OffsetDateTime.now());
    }
}
