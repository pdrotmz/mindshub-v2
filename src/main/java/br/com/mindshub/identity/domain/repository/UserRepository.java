package br.com.mindshub.identity.domain.repository;

import br.com.mindshub.identity.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findByUuid(UUID uuid);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
}
