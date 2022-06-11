package ru.rsreu.serov.tariffs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rsreu.serov.tariffs.controller.UserController;
import ru.rsreu.serov.tariffs.entity.User;
import ru.rsreu.serov.tariffs.repository.UserRepository;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class TariffsApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Test
    public void contextLoads() {
    }

    @Test
    public void editUser() {
        User user = userRepository.findById(1);
        assertEquals("a1", user.getLogin());
    }


       /* System.out.println(userService.isNewLoginOfUserWithIdUnique("а1", 2));
        System.out.println(userService.isNewLoginOfUserWithIdUnique("а1", 1));
        System.out.println(userService.isNewLoginOfUserWithIdUnique("а", 2));*/

}
