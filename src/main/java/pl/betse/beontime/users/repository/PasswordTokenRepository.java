package pl.betse.beontime.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.betse.beontime.users.entity.PasswordTokenEntity;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordTokenEntity, Long> {

    Optional<PasswordTokenEntity> findByToken(String token);
}
