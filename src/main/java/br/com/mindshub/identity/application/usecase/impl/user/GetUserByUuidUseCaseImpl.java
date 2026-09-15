package br.com.mindshub.identity.application.usecase.impl.user;

import br.com.mindshub.identity.application.usecase.user.GetUserByUuidUseCase;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserByUuidUseCaseImpl implements GetUserByUuidUseCase {

    private final UserRepository userRepository;

    @Override
    public User execute(UUID userId) {
        User user = userRepository.findByUuid(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found."));

        return user;
    }
}
