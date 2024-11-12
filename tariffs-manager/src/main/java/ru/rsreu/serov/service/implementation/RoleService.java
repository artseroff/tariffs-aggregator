package ru.rsreu.serov.service.implementation;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.rsreu.serov.entity.Role;
import ru.rsreu.serov.repository.RoleRepository;
import ru.rsreu.serov.service.EntityService;

@Service
public class RoleService implements EntityService<Role> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role update(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role add(Role role) {
        return roleRepository.save(role);
    }
}
