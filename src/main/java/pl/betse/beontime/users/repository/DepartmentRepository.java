package pl.betse.beontime.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.betse.beontime.users.entity.DepartmentEntity;


@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    // EXIST
    boolean existsById(Long departmentId);

    boolean existsByName(String departmentName);

    // SPRING DATA
    DepartmentEntity findByName(String departmentName);
}
