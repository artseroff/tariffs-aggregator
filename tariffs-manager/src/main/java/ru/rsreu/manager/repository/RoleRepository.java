package ru.rsreu.manager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rsreu.manager.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll();

    Role findById(long id);

    void deleteAll();
}
