package ru.rsreu.serov.tariffs;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.rsreu.serov.tariffs.controller.FrontController;
import ru.rsreu.serov.tariffs.entity.User;
import ru.rsreu.serov.tariffs.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(FrontController.class)
public class ControllerTests {

    @InjectMocks
    private FrontController frontController;

    @Test
    public void editUser() {
//        User user = userRepository.findById(1);
//        Assert.assertEquals(user.getLogin(), "а2");
    }
}
