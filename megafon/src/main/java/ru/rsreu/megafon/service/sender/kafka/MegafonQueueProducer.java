package ru.rsreu.megafon.service.sender.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import ru.rsreu.megafon.service.sender.SenderService;
import ru.rsreu.megafon.service.sender.dto.TariffsData;

public class MegafonQueueProducer implements SenderService {
    private final String topic;
    private final KafkaTemplate<String, TariffsData> kafkaTemplate;

    public MegafonQueueProducer(String topic, KafkaTemplate<String, TariffsData> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(TariffsData tariffsData) {
        kafkaTemplate.send(topic, tariffsData);
    }
}
