package br.com.mindshub.identity.application.usecase;

import br.com.mindshub.identity.presentation.dto.request.RegisterUserRequest;
import br.com.mindshub.identity.presentation.dto.response.RegisterUserResponse;

public interface RegisterUserUseCase {

    RegisterUserResponse execute(RegisterUserRequest request);
}
