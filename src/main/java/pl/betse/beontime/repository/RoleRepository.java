package pl.betse.beontime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.betse.beontime.entity.RoleEntity;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {



    boolean existsById(Integer roleId);

}
