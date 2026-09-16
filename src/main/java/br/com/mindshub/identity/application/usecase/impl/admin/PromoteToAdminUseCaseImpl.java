package br.com.mindshub.identity.application.usecase.impl.admin;

import br.com.mindshub.identity.application.exception.InvalidUserRoleException;
import br.com.mindshub.identity.application.exception.UserAlreadyAdminException;
import br.com.mindshub.identity.application.exception.UserAlreadyTeacherException;
import br.com.mindshub.identity.application.usecase.admin.CreateAdminUseCase;
import br.com.mindshub.identity.application.usecase.admin.PromoteToAdminUseCase;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.request.RegisterAdminRequest;
import br.com.mindshub.identity.presentation.dto.response.auth.RegisterUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromoteToAdminUseCaseImpl implements PromoteToAdminUseCase {

    private final UserRepository userRepository;

    @Override
    public void execute(UUID userId) {
        User user = userRepository.findByUuid(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found."));

        if (user.getRole() == Role.ADMIN) {
            throw new UserAlreadyAdminException("This user is already promoted to ad  min.");
        }

        user.promoteToAdmin();
        userRepository.save(user);
    }
}
