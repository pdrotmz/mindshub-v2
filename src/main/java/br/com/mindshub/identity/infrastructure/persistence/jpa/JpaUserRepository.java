package br.com.mindshub.identity.infrastructure.persistence.jpa;

import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUuidAndDeletedAtIsNull(UUID uuid);

    Optional<UserEntity> findByEmailAndDeletedAtIsNull(String email);

    List<UserEntity> findAllByDeletedAtIsNull();

    List<UserEntity> findByIsActiveAndDeletedAtIsNull(boolean isActive);

    List<UserEntity> findByUserRoleAndDeletedAtIsNull(Role role);

    boolean existsByEmailAndDeletedAtIsNull(String email);

    boolean existsByUsernameAndDeletedAtIsNull(String username);
}
