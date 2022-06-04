package ru.rsreu.serov.tariffs.service.implementation;

import org.springframework.stereotype.Service;
import ru.rsreu.serov.tariffs.entity.Role;
import ru.rsreu.serov.tariffs.repository.RoleRepository;
import ru.rsreu.serov.tariffs.service.EntityService;

import java.util.List;

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
