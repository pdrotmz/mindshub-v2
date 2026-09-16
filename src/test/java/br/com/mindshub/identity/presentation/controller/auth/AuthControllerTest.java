package br.com.mindshub.identity.presentation.controller.auth;
import br.com.mindshub.identity.presentation.controller.ControllerTestSupport;
import br.com.mindshub.identity.presentation.dto.request.*;
import br.com.mindshub.identity.application.exception.*;
import br.com.mindshub.identity.domain.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import br.com.mindshub.identity.application.usecase.auth.*;
import br.com.mindshub.identity.presentation.dto.response.auth.LoginUserResponse;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTestSupport {
    @MockitoBean private RegisterUserUseCase registerUseCase;
    @MockitoBean private LoginUserUseCase loginUseCase;
    @MockitoBean private VerifyEmailUseCase verifyUseCase;

    @Test
    void shouldRegisterWithoutAuthentication() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest("pedro", "pedro@example.com", "Password@123", "Password@123");
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());
        verify(registerUseCase).execute(request);
    }

    @Test
    void shouldReturnLoginToken() throws Exception {
        LoginUserRequest request = new LoginUserRequest("pedro@example.com", "Password@123");
        when(loginUseCase.execute(request)).thenReturn(new LoginUserResponse("signed-token"));
        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isAccepted())
                .andExpect(jsonPath("token").value("signed-token"));
        verify(loginUseCase).execute(request);
    }

    @Test
    void shouldRejectInvalidCredentials() throws Exception {
        when(loginUseCase.execute(any())).thenThrow(new InvalidCredentialsException("Invalid credentials"));
        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginUserRequest("pedro@example.com", "wrong"))))
                .andExpect(status().isUnauthorized()).andExpect(jsonPath("message").value("Invalid credentials"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"register", "login"})
    void shouldRejectEmptyRequests(String endpoint) throws Exception {
        mvc.perform(post("/api/v1/auth/" + endpoint).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(registerUseCase, loginUseCase);
    }

    @Test
    void shouldRejectMismatchedPasswords() throws Exception {
        var request = new RegisterUserRequest("pedro", "pedro@example.com", "Password@123", "Different@123");
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
        verifyNoInteractions(registerUseCase);
    }

    @Test
    void shouldReturnConflictForRegisteredEmail() throws Exception {
        when(registerUseCase.execute(any())).thenThrow(new EmailAlreadyRegisteredException("Email taken"));
        var request = new RegisterUserRequest("pedro", "pedro@example.com", "Password@123", "Password@123");
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isConflict());
    }

    @Test
    void shouldVerifyEmail() throws Exception {
        mvc.perform(get("/api/v1/auth/verify-email").param("token", "verification-token"))
                .andExpect(status().isNoContent());
        verify(verifyUseCase).execute("verification-token");
    }

    @Test
    void shouldResendVerification() throws Exception {
        mvc.perform(get("/api/v1/auth/resend-verification").param("token", "old-token"))
                .andExpect(status().isOk());
        verify(verifyUseCase).resend("old-token");
    }

    @ParameterizedTest
    @ValueSource(strings = {"verify-email", "resend-verification"})
    void shouldRequireVerificationToken(String endpoint) throws Exception {
        mvc.perform(get("/api/v1/auth/" + endpoint)).andExpect(status().isBadRequest());
        verifyNoInteractions(verifyUseCase);
    }

    @Test
    void shouldReturnNotFoundForUnknownToken() throws Exception {
        doThrow(new EmailVerificationTokenNotFoundException("Token not found")).when(verifyUseCase).execute("missing");
        mvc.perform(get("/api/v1/auth/verify-email").param("token", "missing")).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUnauthorizedForExpiredToken() throws Exception {
        doThrow(new EmailVerificationTokenExpiredException("Expired")).when(verifyUseCase).execute("expired");
        mvc.perform(get("/api/v1/auth/verify-email").param("token", "expired")).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnConflictForVerifiedEmail() throws Exception {
        doThrow(new EmailAlreadyVerifiedException("Verified")).when(verifyUseCase).resend("used");
        mvc.perform(get("/api/v1/auth/resend-verification").param("token", "used")).andExpect(status().isConflict());
    }
}
