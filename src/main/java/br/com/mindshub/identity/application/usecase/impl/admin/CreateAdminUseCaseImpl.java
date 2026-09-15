package br.com.mindshub.identity.application.usecase.impl.admin;

import br.com.mindshub.identity.application.event.EmailVerificationRequestedEvent;
import br.com.mindshub.identity.application.exception.EmailAlreadyRegisteredException;
import br.com.mindshub.identity.application.port.EventPublisher;
import br.com.mindshub.identity.application.usecase.admin.CreateAdminUseCase;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.EmailVerificationTokenRepository;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.request.RegisterAdminRequest;
import br.com.mindshub.identity.presentation.dto.response.auth.RegisterUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAdminUseCaseImpl implements CreateAdminUseCase {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    @Override
    public RegisterUserResponse execute(RegisterAdminRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException("User with email: " + request.email() + " already registered.");
        }

        User user = new User();

        user.setUsername(extractUsername(request.email()));
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ADMIN);
        user.setActive(false);

        User savedUser = userRepository.save(user);

        EmailVerificationToken token = EmailVerificationToken.create(savedUser.getUuid());

        emailRepository.save(token);

        eventPublisher.publish(new EmailVerificationRequestedEvent(savedUser.getEmail(), token.getToken()));

        return RegisterUserResponse.from(savedUser);
    }

    public String extractUsername(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
