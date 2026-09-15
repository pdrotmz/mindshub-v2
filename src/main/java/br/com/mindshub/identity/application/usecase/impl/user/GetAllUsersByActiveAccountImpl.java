package br.com.mindshub.identity.application.usecase.impl.user;

import br.com.mindshub.identity.application.usecase.user.GetAllUsersByActiveAccount;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.repository.UserRepository;
import br.com.mindshub.identity.presentation.dto.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUsersByActiveAccountImpl implements GetAllUsersByActiveAccount {

    private final UserRepository userRepository;


    @Override
    public List<User> execute(boolean isActive) {
        return userRepository.findByActive(isActive);
    }
}
