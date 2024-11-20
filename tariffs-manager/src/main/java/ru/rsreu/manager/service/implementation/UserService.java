package ru.rsreu.manager.service.implementation;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.manager.domain.User;
import ru.rsreu.manager.domain.repository.UserRepository;
import ru.rsreu.manager.service.EntityService;
import ru.rsreu.manager.service.exception.AlreadyAuthorizedUserException;
import ru.rsreu.manager.service.exception.EntityNotFoundException;

@Service
public class UserService implements EntityService<User> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElseThrow(() ->
            new EntityNotFoundException("Пользователь с id=%d не найден".formatted(id)));
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
    public boolean isUnique(User user) {
        User foundUser = getByLogin(user.getLogin());
        // Либо такого логина нет у других пользователей,
        // либо пользователь с таким логином найден и это
        // и есть редактируемый пользователь
        return foundUser == null || (foundUser.getId().equals(user.getId()));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean processUpdateTransactional(User user) {
        return processUpdate(user);
    }

    public User getByLogin(String login) {
        return userRepository.getUserByLogin(login);
    }

    private User getByLoginAndPassword(String login, String password) {
        User foundUser = getByLogin(login);
        if (foundUser != null && foundUser.getPassword().equals(password)) {
            return foundUser;
        } else {
            return null;
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User authorizeUserWithLoginAndPassword(String login, String password) {
        User user = getByLoginAndPassword(login, password);
        if (user == null) {
            return null;
        }
        if (user.isAuthorized()) {
            throw new AlreadyAuthorizedUserException("Пользователь с id=%d уже авторизован".formatted(user.getId()));
        }
        return userRepository.updateUserAuthorizationStatus(user, true);
    }

    public void logoutUser(User user) {
        userRepository.updateUserAuthorizationStatus(user, false);
    }
}
