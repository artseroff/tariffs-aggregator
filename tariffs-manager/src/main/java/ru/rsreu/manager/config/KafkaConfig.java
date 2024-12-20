package ru.rsreu.manager.config;

import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.rsreu.manager.dto.TariffsData;

@ConditionalOnProperty(prefix = "kafka", name = "enable")
@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
public record KafkaConfig(boolean enable,
                          String bootstrapServers,
                          String tariffsTopic,
                          String consumerGroup,
                          String dlqTopic) {

    @Bean
    public KafkaAdmin admin() {
        return new KafkaAdmin(Map.of(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TariffsData> tariffsListenerContainerFactory(
        CommonErrorHandler commonErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, TariffsData> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

        Map<String, Object> propertiesMap = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG, consumerGroup,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        );
        JsonDeserializer<TariffsData> deserializer = new JsonDeserializer<>(TariffsData.class);
//    //    deserializer.addTrustedPackages("ru.rsreu.*");
        deserializer.setUseTypeHeaders(false);
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
            propertiesMap,
            new StringDeserializer(),
            deserializer
        ));
        factory.setCommonErrorHandler(commonErrorHandler);
        return factory;
    }

    @Bean
    public KafkaTemplate<String, String> dlqKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        )));
    }

    @Bean
    public NewTopic errorTopic() {
        return TopicBuilder.name(dlqTopic)
            .partitions(1)
            .replicas(1)
            .build();
    }
}


