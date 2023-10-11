package io.github.mfvanek.minikubedemo;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class LoadSimulation extends Simulation {

    private static final HttpProtocolBuilder HTTP_PROTOCOL = http
            .acceptHeader("application/json")
            .userAgentHeader("gatling")
            .disableCaching()
            .disableWarmUp()
            .disableFollowRedirect();

    private static final FeederBuilder<Object> DELAY_FEEDER = listFeeder(List.of(
            Map.of("delay", 1000L),
            Map.of("delay", 2000L),
            Map.of("delay", 2500L),
            Map.of("delay", 3000L),
            Map.of("delay", 500L)
    )).random();

    ScenarioBuilder scn = scenario("MinikubeDemoLoadSimulation")
            .feed(DELAY_FEEDER)
            .exec(session -> {
                final String hostName = System.getProperty("hostName", "http://localhost:8080");
                return session.set("hostName", hostName);
            })
            .exec(
                    http("Get the current date and time")
                            .get("#{hostName}/api/v1/workload/now?delay=#{delay}")
            );

    public LoadSimulation() {
        final int rps = Integer.getInteger("rps", 1);
        final long durationSec = Long.getLong("durationSec", 10L);
        this.setUp(scn.injectOpen(
                        constantUsersPerSec(rps)
                                .during(durationSec)))
                .protocols(HTTP_PROTOCOL);
    }
}
