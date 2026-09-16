package br.com.mindshub.identity.application.usecase.impl.user;

import br.com.mindshub.identity.application.usecase.user.DeleteUserUseCase;
import br.com.mindshub.identity.domain.exception.UserNotFoundException;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void execute(UUID userId) {

        User user = userRepository.findByUuid(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User was not found."
                        )
                );

        user.delete();

        userRepository.save(user);
    }
}
