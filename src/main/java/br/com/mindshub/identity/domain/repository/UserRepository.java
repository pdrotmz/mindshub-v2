package br.com.mindshub.identity.domain.repository;

import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {


    Optional<User> findByUuid(UUID uuid);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    List<User> findByActive(boolean active);
    List<User> findByRole(Role role);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User save(User user);
}
