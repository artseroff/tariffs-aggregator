package ru.rsreu.serov.tariffs.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rsreu.serov.tariffs.entity.User;
import ru.rsreu.serov.tariffs.repository.UserRepository;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class TimeoutSessionListener implements HttpSessionListener {

    @Autowired
    UserRepository userRepository;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("Сессия закончилась");
        User user = (User) event.getSession().getAttribute("user");
        if (user != null) {
            userRepository.updateUserAuthorizationStatus(user.getId(), false);
        }
    }
}
