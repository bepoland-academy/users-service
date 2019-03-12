package pl.betse.beontime.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.betse.beontime.users.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String userEmail);

    Optional<UserEntity> findByGuid(String GUID);

    Optional<UserEntity> findByEmail(String userEmail);

}
