package br.com.mindshub.identity.application.usecase.user;

import br.com.mindshub.identity.presentation.dto.request.UpdateUserPasswordRequest;

import java.util.UUID;

public interface UpdateUserPasswordUseCase {
    void execute(String email, UpdateUserPasswordRequest request);
}
