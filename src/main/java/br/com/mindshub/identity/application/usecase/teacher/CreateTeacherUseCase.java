package br.com.mindshub.identity.application.usecase.teacher;

import br.com.mindshub.identity.presentation.dto.request.RegisterTeacherRequest;
import br.com.mindshub.identity.presentation.dto.response.auth.RegisterUserResponse;

public interface CreateTeacherUseCase {
    RegisterUserResponse execute(RegisterTeacherRequest request);
}
