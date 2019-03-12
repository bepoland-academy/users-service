package pl.betse.beontime.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.betse.beontime.users.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // EXISTS
    boolean existsByEmail(String userEmail);

    boolean existsByGuid(String GUID);

    // SPRING DATA
    UserEntity findByGuid(String GUID);

    UserEntity findByEmail(String userEmail);

    void deleteByGuid(String GUID);
}
