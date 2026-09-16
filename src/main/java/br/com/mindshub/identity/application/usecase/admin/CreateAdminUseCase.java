package br.com.mindshub.identity.application.usecase.admin;

import br.com.mindshub.identity.presentation.dto.request.RegisterAdminRequest;
import br.com.mindshub.identity.presentation.dto.response.auth.RegisterUserResponse;

public interface CreateAdminUseCase {
    RegisterUserResponse execute(RegisterAdminRequest request);
}
