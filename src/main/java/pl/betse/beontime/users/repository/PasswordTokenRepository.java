package pl.betse.beontime.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.betse.beontime.users.entity.PasswordTokenEntity;
import pl.betse.beontime.users.entity.UserEntity;

public interface PasswordTokenRepository extends JpaRepository<PasswordTokenEntity, Long> {
    boolean existsByToken(String token);
}
