package pl.betse.beontime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleEntity> findAll() {
        List<RoleEntity> roles = new ArrayList<>();
        roleRepository.findAll().forEach(roles::add);
        return roles;
    }

    @Override
    public RoleEntity findById(Integer roleId) {
        return roleRepository.findById(roleId).get();
    }

    @Override
    public RoleEntity findByName(String roleName) {
        return roleRepository.findByRole(roleName);
    }

    @Override
    public void save(RoleEntity roleEntity) {
        roleRepository.save(roleEntity);
    }

    @Override
    public boolean existsById(Integer roleId) {
        return roleRepository.existsById(roleId);
    }
}
