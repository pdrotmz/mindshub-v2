package br.com.mindshub.identity.application.usecase.user;

import br.com.mindshub.identity.domain.model.User;

public interface GetCurrentUserUseCase {
    User execute(String email);
}
