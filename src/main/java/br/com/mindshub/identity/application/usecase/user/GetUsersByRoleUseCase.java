package br.com.mindshub.identity.application.usecase.user;

import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.User;

import java.util.List;

public interface GetUsersByRoleUseCase {
    List<User> execute(Role role);
}
