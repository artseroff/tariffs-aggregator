package ru.rsreu.manager.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.manager.domain.User;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    User findById(long id);

    User getUserByLogin(String login);

    @Modifying
    @Query("UPDATE User u SET u.isAuthorized = ?2 where u.id=?1")
    void updateUserAuthorizationStatus(long userId, boolean status);
}
