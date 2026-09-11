package br.com.mindshub.identity.application.usecase.impl;

import br.com.mindshub.identity.application.event.EmailVerificationRequestedEvent;
import br.com.mindshub.identity.application.exception.EmailVerificationTokenNotFoundException;
import br.com.mindshub.identity.application.port.EventPublisher;
import br.com.mindshub.identity.application.usecase.VerifyEmailUseCase;
import br.com.mindshub.identity.application.exception.EmailAlreadyVerifiedException;
import br.com.mindshub.identity.domain.exception.EmailVerificationTokenExpiredException;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.EmailVerificationToken;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.EmailVerificationTokenRepository;
import br.com.mindshub.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyEmailUseCaseImpl implements VerifyEmailUseCase {

    private final EmailVerificationTokenRepository emailRepository;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    @Override
    public void execute(String token) {

        EmailVerificationToken verificationToken = emailRepository.findByToken(token)
                .orElseThrow(() -> new EmailVerificationTokenNotFoundException("Verification token not found."));

        if (verificationToken.isUsed()) {
            throw new EmailAlreadyVerifiedException("Email already verified");
        }

        if (verificationToken.isExpired()) {
            throw new EmailVerificationTokenExpiredException("Verification token expired");
        }

        User user = userRepository.findByUuid(verificationToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.activate();
        verificationToken.markAsUsed();

        userRepository.save(user);
        emailRepository.save(verificationToken);
    }

    @Override
    public void resend(String token) {

        EmailVerificationToken oldToken = emailRepository.findByToken(token)
                .orElseThrow(() -> new EmailVerificationTokenNotFoundException("Verification token not found."));

        User user = userRepository.findByUuid(oldToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (user.isActive()) {
            throw new EmailAlreadyVerifiedException(
                    "Email is already verified."
            );
        }

        oldToken.markAsUsed();
        emailRepository.save(oldToken);

        EmailVerificationToken newToken = EmailVerificationToken.create(user.getUuid());

        emailRepository.save(newToken);

        eventPublisher.publish(new EmailVerificationRequestedEvent(user.getEmail(), newToken.getToken()));
    }
}
