package br.com.mindshub.identity.application.usecase.impl.user;

import br.com.mindshub.identity.application.usecase.user.GetCurrentUserUseCase;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCurrentUserUseCaseImpl implements GetCurrentUserUseCase {

    private final UserRepository userRepository;

    @Override
    public User execute(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
    }
}
