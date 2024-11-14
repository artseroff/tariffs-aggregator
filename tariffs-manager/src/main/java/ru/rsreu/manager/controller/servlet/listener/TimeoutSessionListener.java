package ru.rsreu.manager.controller.servlet.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;
import ru.rsreu.manager.domain.User;
import ru.rsreu.manager.repository.UserRepository;

@Component
public class TimeoutSessionListener implements HttpSessionListener {

    private final UserRepository userRepository;

    public TimeoutSessionListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        User user = (User) event.getSession().getAttribute("user");
        if (user != null) {
            userRepository.updateUserAuthorizationStatus(user.getId(), false);
        }
    }
}
