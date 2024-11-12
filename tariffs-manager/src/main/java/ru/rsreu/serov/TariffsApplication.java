package ru.rsreu.serov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class TariffsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TariffsApplication.class, args);
    }

}
