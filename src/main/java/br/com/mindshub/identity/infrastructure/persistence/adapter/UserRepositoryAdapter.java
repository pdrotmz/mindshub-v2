package br.com.mindshub.identity.infrastructure.persistence.adapter;

import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.infrastructure.persistence.entity.UserEntity;
import br.com.mindshub.identity.infrastructure.persistence.jpa.JpaUserRepository;
import br.com.mindshub.identity.infrastructure.persistence.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository userRepository;
    private final UserEntityMapper mapper;

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);

        UserEntity savedEntity = userRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }
}
