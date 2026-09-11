package br.com.mindshub.identity.application.usecase;

import br.com.mindshub.identity.presentation.dto.request.LoginUserRequest;
import br.com.mindshub.identity.presentation.dto.response.LoginUserResponse;

public interface LoginUserUseCase {

    LoginUserResponse execute(LoginUserRequest request);
}
