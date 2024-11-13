package ru.rsreu.megafon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.rsreu.megafon.config.KafkaProducerConfig;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({KafkaProducerConfig.class})
public class MegafonApplication {

    public static void main(String[] args) {
        SpringApplication.run(MegafonApplication.class, args);
    }

}

