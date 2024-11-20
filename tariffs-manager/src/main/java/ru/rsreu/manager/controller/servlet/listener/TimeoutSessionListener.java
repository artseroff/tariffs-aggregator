package ru.rsreu.manager.controller.servlet.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;
import ru.rsreu.manager.domain.User;
import ru.rsreu.manager.service.implementation.UserService;

@Component
public class TimeoutSessionListener implements HttpSessionListener {

    private final UserService userService;

    public TimeoutSessionListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        User user = (User) event.getSession().getAttribute("user");
        if (user != null) {
            userService.logoutUser(user);
        }
    }
}
