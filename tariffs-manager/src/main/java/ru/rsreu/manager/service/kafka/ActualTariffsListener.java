package ru.rsreu.manager.service.kafka;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.rsreu.manager.dto.TariffsData;
import ru.rsreu.manager.service.kafka.dlq.DeadLetterQueueProducer;
import ru.rsreu.manager.service.update.TariffsUpdateHandler;

@Component
@EnableKafka
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "kafka", name = "enable")
public class ActualTariffsListener {
    private final TariffsUpdateHandler tariffsUpdateHandler;
    private final Validator validator;
    private final DeadLetterQueueProducer deadLetterQueueProducer;

    @KafkaListener(topics = "${kafka.tariffs-topic}",
                   groupId = "${kafka.consumer-group}",
                   containerFactory = "tariffsListenerContainerFactory",
                   concurrency = "1")
    public void listenMessages(TariffsData tariffsData) {
        Optional<String> constraints = validateTariffsData(tariffsData);
        if (constraints.isPresent()) {
            deadLetterQueueProducer.send(constraints.get());
            return;
        }
        tariffsUpdateHandler.processUpdate(tariffsData);
    }

    private Optional<String> validateTariffsData(TariffsData tariffsData) {
        Set<ConstraintViolation<TariffsData>> violations = validator.validate(tariffsData);
        if (violations.isEmpty()) {
            return Optional.empty();
        }
        StringBuilder sb = new StringBuilder("Ошибки валидации:\n");
        for (ConstraintViolation<TariffsData> constraintViolation : violations) {
            sb.append(constraintViolation.getMessage());
        }
        return Optional.of(sb.toString());
    }
}
