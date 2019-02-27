package pl.betse.beontime.service;


import pl.betse.beontime.entity.DepartmentEntity;

import java.util.List;

public interface DepartmentService {

    DepartmentEntity findByName(String departmentName);

    void save(DepartmentEntity departmentEntity);

    List<DepartmentEntity> findAll();

    boolean existsById(Integer departmentId);

    DepartmentEntity getDepartmentById(Integer departmentId);
}
