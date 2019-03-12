package pl.betse.beontime.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.betse.beontime.users.entity.RoleEntity;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    // EXIST
    boolean existsById(Long roleId);

    boolean existsByName(String name);

    Optional<RoleEntity> findByName(String name);
}
