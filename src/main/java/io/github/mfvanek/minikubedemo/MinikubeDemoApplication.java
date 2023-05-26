package io.github.mfvanek.minikubedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MinikubeDemoApplication {

    public static void main(String[] args) {
        log.info("availableProcessors {}", Runtime.getRuntime().availableProcessors());
        SpringApplication.run(MinikubeDemoApplication.class, args);
    }
}
