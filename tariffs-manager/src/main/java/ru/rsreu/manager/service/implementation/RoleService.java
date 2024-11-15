package ru.rsreu.manager.service.implementation;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.rsreu.manager.domain.Role;
import ru.rsreu.manager.domain.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

}
