package ru.rsreu.serov.tariffs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.rsreu.serov.tariffs.entity.User;
import ru.rsreu.serov.tariffs.repository.UserRepository;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class TariffsApplication {

    private static final Logger log = LoggerFactory.getLogger(TariffsApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(TariffsApplication.class, args);
        openHomePage();

    }

    private static void openHomePage() {

        try {
            Runtime.getRuntime().exec("cmd /c start http://localhost:8888");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public CommandLineRunner demo(UserRepository repository) {
        return (args) -> {
            /*repository.deleteAll();
            repository.save(new User("чел1", "чел1"));
            repository.save(new User("чел2", "чел2"));*/

            // fetch all customers
            log.info("User found with findAll():");
            log.info("-------------------------------");
            for (User user : repository.findAll()) {
                log.info(user.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            User user = repository.findById(1L);
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            log.info(user.toString());
            log.info("");
        };
    }

}
