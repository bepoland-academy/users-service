package pl.betse.beontime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.betse.beontime.entity.DepartmentEntity;


@Repository
public interface DepartamentRepository extends JpaRepository<DepartmentEntity, Integer> {

    DepartmentEntity findByName(String departmentName);

    boolean existsById(Integer departmentId);

}
