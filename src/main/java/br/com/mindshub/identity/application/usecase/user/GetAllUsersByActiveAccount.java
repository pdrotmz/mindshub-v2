package br.com.mindshub.identity.application.usecase.user;

import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.presentation.dto.response.user.UserResponse;

import java.util.List;

public interface GetAllUsersByActiveAccount {
    List<User> execute(boolean isActive);
}
