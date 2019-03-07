package pl.betse.beontime.service;

import pl.betse.beontime.entity.RoleEntity;

import java.util.List;

public interface RoleService {


    List<RoleEntity> findAll();

    RoleEntity findByName(String roleName);

    RoleEntity findById(Integer roleId);

    boolean existsById(Integer roleId);

    void save(RoleEntity roleEntity);

}
