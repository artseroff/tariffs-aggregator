package ru.rsreu.megafon.config;

import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.validation.annotation.Validated;
import ru.rsreu.megafon.service.sender.SenderService;
import ru.rsreu.megafon.service.sender.dto.TariffsData;
import ru.rsreu.megafon.service.sender.kafka.MegafonQueueProducer;

@Validated
@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
public record KafkaProducerConfig(@NotNull String bootstrapServers, @NotNull String topic) {
    @Bean
    public KafkaTemplate<String, TariffsData> tariffsDataKafkaTemplate() {

        Map<String, Object> props = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        JsonSerializer<TariffsData> jsonSerializer = new JsonSerializer<>();
        jsonSerializer.setAddTypeInfo(false);

        DefaultKafkaProducerFactory<String, TariffsData> producerFactory =
            new DefaultKafkaProducerFactory<>(props, new StringSerializer(), jsonSerializer);
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaAdmin admin() {
        return new KafkaAdmin(Map.of(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers
        ));
    }

    @Bean
    public NewTopic tariffsTopic() {
        return TopicBuilder.name(topic)
            .partitions(1)
            .replicas(1)
            .build();
    }

    @Bean
    public SenderService megafonQueueProducer(KafkaTemplate<String, TariffsData> kafkaTemplate) {
        return new MegafonQueueProducer(topic, kafkaTemplate);
    }
}
