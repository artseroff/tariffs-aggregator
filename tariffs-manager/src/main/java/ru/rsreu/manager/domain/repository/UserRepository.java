package ru.rsreu.manager.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.manager.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(
        type = EntityGraph.EntityGraphType.FETCH,
        attributePaths = "role"
    )
    @Override
    List<User> findAll();

    User getUserByLogin(String login);

    @Transactional
    default User updateUserAuthorizationStatus(User user, boolean status) {
        user.setAuthorized(status);
        return save(user);
    }
}
