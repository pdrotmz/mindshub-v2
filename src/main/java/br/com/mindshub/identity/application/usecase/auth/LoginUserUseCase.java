package br.com.mindshub.identity.application.usecase.auth;

import br.com.mindshub.identity.presentation.dto.request.LoginUserRequest;
import br.com.mindshub.identity.presentation.dto.response.auth.LoginUserResponse;

public interface LoginUserUseCase {

    LoginUserResponse execute(LoginUserRequest request);
}
