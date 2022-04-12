package ru.rsreu.serov.tariffs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.serov.tariffs.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}