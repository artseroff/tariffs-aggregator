package ru.rsreu.serov.tariffs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.serov.tariffs.entity.Role;
import ru.rsreu.serov.tariffs.entity.User;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll();
    Role findById(long id);
    void deleteAll();
}