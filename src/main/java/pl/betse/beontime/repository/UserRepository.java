package pl.betse.beontime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.entity.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    // EXISTS
    boolean existsById(Integer userID);

    boolean existsByEmail(String userEmail);

    boolean existsByGuid(String GUID);

    // SPRING DATA
    List<UserEntity> findByDepartment(DepartmentEntity departmentEntity);

    UserEntity findByGuid(String GUID);

    // CUSTOM QUERIES
    @Query("SELECT user FROM UserEntity user where  lower(user.email)=lower(:userEmail)")
    UserEntity findByEmail(@Param("userEmail") String userEmail);
}
