package ru.rsreu.serov.tariffs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.serov.tariffs.entity.User;


import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    void deleteAll();
    User findById(long id);
    User getUserByUsername(String username);

    // @Query(value="UPDATE users SET is_authorized = ?2 where id=?1", nativeQuery = true)

    @Modifying
    @Query("UPDATE User u SET u.isAuthorized = ?2 where u.id=?1")
    void updateUserAuthorizationStatus(long userId, boolean status);
}