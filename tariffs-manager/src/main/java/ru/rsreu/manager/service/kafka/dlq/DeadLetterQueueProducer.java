package ru.rsreu.manager.service.kafka.dlq;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.rsreu.manager.config.KafkaConfig;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "kafka", name = "enable")
public class DeadLetterQueueProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig config;

    public void send(String message) {
        kafkaTemplate.send(config.dlqTopic(), message);
    }
}
