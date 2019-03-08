package pl.betse.beontime.service;

import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.entity.UserEntity;

import java.util.List;

public interface UsersService {

    List<UserEntity> findAll();

    UserEntity findById(Integer userId);

    boolean existsById(Integer userId);

    boolean existsByEmail(String userEmail);

    List<UserEntity> findByDepartment(DepartmentEntity departmentId);

    UserEntity getUserByEmail(String userEmail);

    void save(UserEntity userEntity);

    void deleteById(Integer userId);



    UserEntity findByGuid(String GUID);

    boolean existsByGuid(String GUID);
}
