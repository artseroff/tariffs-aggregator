package ru.rsreu.serov.tariffs;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import ru.rsreu.serov.tariffs.controller.FrontController;
import ru.rsreu.serov.tariffs.entity.User;
import ru.rsreu.serov.tariffs.repository.UserRepository;

import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class TariffsApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FrontController frontController;

    @Test
    public void contextLoads() {
    }

    @Test
    public void editUser() {
        User user = userRepository.findById(1);
        assertEquals("а1", user.getLogin());
    }


       /* System.out.println(userService.isNewLoginOfUserWithIdUnique("а1", 2));
        System.out.println(userService.isNewLoginOfUserWithIdUnique("а1", 1));
        System.out.println(userService.isNewLoginOfUserWithIdUnique("а", 2));*/

}
