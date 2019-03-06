package pl.betse.beontime.service;

import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.entity.UserEntity;

import java.util.List;

public interface UsersService {

    List<UserEntity> findAll();

    UserEntity findById(Integer userId);

    boolean existsByUserId(Integer userId);

    boolean existsByEmailLogin(String userEmail);

    List<UserEntity> findByDepartmentEntity(DepartmentEntity departmentId);

    UserEntity getUserByEmail(String userEmail);

    void save(UserEntity userEntity);

    void deleteById(Integer userId);



    UserEntity findByGUID(String GUID);

    boolean existsByGUID(String GUID);
}
