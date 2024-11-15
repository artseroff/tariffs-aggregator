package ru.rsreu.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@ConfigurationPropertiesScan(basePackages = "ru.rsreu.manager.config")
public class TariffsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TariffsApplication.class, args);
    }

}
