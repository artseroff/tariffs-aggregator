package ru.rsreu.megafon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = "ru.rsreu.megafon.config")
public class MegafonApplication {

    public static void main(String[] args) {
        SpringApplication.run(MegafonApplication.class, args);
    }

}

