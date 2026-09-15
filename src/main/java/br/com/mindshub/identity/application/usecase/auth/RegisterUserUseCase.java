package br.com.mindshub.identity.application.usecase.auth;

import br.com.mindshub.identity.presentation.dto.request.RegisterUserRequest;
import br.com.mindshub.identity.presentation.dto.response.auth.RegisterUserResponse;

public interface RegisterUserUseCase {

    RegisterUserResponse execute(RegisterUserRequest request);
}
