package ru.rsreu.megafon.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestRetryConfiguration {
    // TODO configuration properties and retry on http codes
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
            .setConnectTimeout(Duration.ofMillis(30000))
            .setReadTimeout(Duration.ofMillis(30000))
            .build();
    }

    @Bean
    public RetryTemplate restRetry() {
        return RetryTemplate.builder()
            .maxAttempts(3)
            .fixedBackoff(1000L)
            .retryOn(HttpServerErrorException.class)
            .build();
    }
}
