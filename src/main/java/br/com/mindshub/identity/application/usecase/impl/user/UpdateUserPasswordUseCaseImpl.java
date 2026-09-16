package br.com.mindshub.identity.application.usecase.impl.user;

import br.com.mindshub.identity.application.exception.InvalidCurrentPasswordException;
import br.com.mindshub.identity.application.exception.PasswordAlreadyInUseException;
import br.com.mindshub.identity.application.usecase.user.UpdateUserPasswordUseCase;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.request.UpdateUserPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserPasswordUseCaseImpl implements UpdateUserPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void execute(String email, UpdateUserPasswordRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User was not found.")
                );

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword()
        )) {
            throw new InvalidCurrentPasswordException(
                    "Current password is incorrect."
            );
        }

        if (passwordEncoder.matches(
                request.newPassword(),
                user.getPassword()
        )) {
            throw new PasswordAlreadyInUseException(
                    "New password must be different from current password."
            );
        }

        String encodedPassword =
                passwordEncoder.encode(request.newPassword());

        user.updatePassword(encodedPassword);

        userRepository.save(user);
    }
}
