package br.com.mindshub.identity.application.usecase.impl.teacher;

import br.com.mindshub.identity.application.exception.InvalidUserRoleException;
import br.com.mindshub.identity.application.exception.UserAlreadyTeacherException;
import br.com.mindshub.identity.application.usecase.teacher.PromoteToTeacherUseCase;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromoteToTeacherUseCaseImpl implements PromoteToTeacherUseCase {

    private final UserRepository userRepository;

    @Override
    public void execute(UUID userId) {
        User user = userRepository.findByUuid(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found."));

        if (user.getRole() == Role.TEACHER) {
            throw new UserAlreadyTeacherException("This user is already promoted to teacher.");
        }

        user.promoteToTeacher();
        userRepository.save(user);
    }
}
