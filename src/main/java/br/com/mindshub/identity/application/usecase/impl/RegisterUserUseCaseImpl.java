package br.com.mindshub.identity.application.usecase.impl;

import br.com.mindshub.identity.application.event.EmailVerificationRequestedEvent;
import br.com.mindshub.identity.application.exception.EmailAlreadyRegisteredException;
import br.com.mindshub.identity.application.port.EventPublisher;
import br.com.mindshub.identity.application.usecase.RegisterUserUseCase;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.EmailVerificationTokenRepository;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.request.RegisterUserRequest;
import br.com.mindshub.identity.presentation.dto.response.RegisterUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final EmailVerificationTokenRepository emailRepository;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    @Transactional
    @Override
    public RegisterUserResponse execute(RegisterUserRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException("Email: " + request.email() + "already in use.");
        }

        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.STUDENT);
        user.setActive(false);

        User savedUser = repository.save(user);

        EmailVerificationToken token = EmailVerificationToken.create(savedUser.getUuid());

        emailRepository.save(token);

        eventPublisher.publish(new EmailVerificationRequestedEvent(savedUser.getEmail(), token.getToken()));

        return RegisterUserResponse.from(savedUser);
    }
}
