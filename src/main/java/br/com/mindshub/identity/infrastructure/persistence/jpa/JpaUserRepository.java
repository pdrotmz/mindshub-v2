package br.com.mindshub.identity.infrastructure.persistence.jpa;

import br.com.mindshub.identity.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUuid(UUID uuid);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

}
