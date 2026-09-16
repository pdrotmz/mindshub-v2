package br.com.mindshub.identity.application.usecase.impl.user;

import br.com.mindshub.identity.application.usecase.user.GetAllUsersUseCase;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCaseImpl implements GetAllUsersUseCase {

    private final UserRepository userRepository;

    @Override
    public List<User> execute() {
        return userRepository.findAll();
    }
}
