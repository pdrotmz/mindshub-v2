package br.com.mindshub.identity.application.usecase.user;

import br.com.mindshub.identity.presentation.dto.request.UpdateUsersInfoRequest;

public interface UpdateUsersInfoUseCase {
    void execute(String email, UpdateUsersInfoRequest request);
}
