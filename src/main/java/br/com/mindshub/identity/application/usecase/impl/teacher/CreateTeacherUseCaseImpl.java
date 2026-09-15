package br.com.mindshub.identity.application.usecase.impl.teacher;

import br.com.mindshub.identity.application.event.EmailVerificationRequestedEvent;
import br.com.mindshub.identity.application.exception.EmailAlreadyRegisteredException;
import br.com.mindshub.identity.application.port.EventPublisher;
import br.com.mindshub.identity.application.usecase.teacher.CreateTeacherUseCase;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.EmailVerificationTokenRepository;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.request.RegisterTeacherRequest;
import br.com.mindshub.identity.presentation.dto.response.auth.RegisterUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTeacherUseCaseImpl implements CreateTeacherUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenRepository emailRepository;
    private final EventPublisher eventPublisher;


    // TODO: Implements emailDomain to new teacher email

    @Transactional
    @Override
    public RegisterUserResponse execute(RegisterTeacherRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException("User with email: " + request.email() + " already registered.");
        }

        User user = new User();

        user.setUsername(extractUsername(request.email()));
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.TEACHER);
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
