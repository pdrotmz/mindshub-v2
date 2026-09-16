package br.com.mindshub.identity.application.usecase.impl.user;

import br.com.mindshub.identity.application.exception.EmailAlreadyRegisteredException;
import br.com.mindshub.identity.application.exception.UsernameAlreadyRegisteredException;
import br.com.mindshub.identity.application.usecase.user.UpdateUsersInfoUseCase;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.request.UpdateUsersInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateUsersInfoUseCaseImpl implements UpdateUsersInfoUseCase {

    private final UserRepository userRepository;

    @Override
    public void execute(String email, UpdateUsersInfoRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User was not found."));

        if (!Objects.equals(user.getUsername(), request.username())
                && userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyRegisteredException("Username already registered.");
        }

        if (!Objects.equals(user.getEmail(), request.email())
                && userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException("Email already registered.");
        }

        user.setUsername(request.username());
        user.setEmail(request.email());

        userRepository.save(user);
    }
}
