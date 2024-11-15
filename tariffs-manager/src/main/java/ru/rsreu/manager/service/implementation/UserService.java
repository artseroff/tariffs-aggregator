package ru.rsreu.manager.service.implementation;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.rsreu.manager.domain.User;
import ru.rsreu.manager.domain.repository.UserRepository;
import ru.rsreu.manager.service.EntityService;

@Service
public class UserService implements EntityService<User> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User add(User user) {
        return userRepository.save(user);
    }

    public User getByLogin(String login) {
        return userRepository.getUserByLogin(login);
    }

    public void updateUserAuthorizationStatus(long id, boolean status) {
        userRepository.updateUserAuthorizationStatus(id, status);
    }

    public User getByLoginAndPassword(String login, String password) {
        User foundUser = getByLogin(login);
        if (foundUser != null && foundUser.getPassword().equals(password)) {
            return foundUser;
        } else {
            return null;
        }
    }

    public boolean isUnique(User user) {
        User foundUser = getByLogin(user.getLogin());
        // Либо такого логина нет у других пользователей,
        // либо пользователь с таким логином найден и это
        // и есть редактируемый пользователь
        return foundUser == null || (foundUser.getId().equals(user.getId()));
    }
}
