package br.com.mindshub.identity.presentation.controller.auth;

import br.com.mindshub.identity.application.usecase.auth.LoginUserUseCase;
import br.com.mindshub.identity.application.usecase.auth.RegisterUserUseCase;
import br.com.mindshub.identity.application.usecase.auth.VerifyEmailUseCase;
import br.com.mindshub.identity.presentation.dto.request.LoginUserRequest;
import br.com.mindshub.identity.presentation.dto.request.RegisterUserRequest;
import br.com.mindshub.identity.presentation.dto.response.auth.LoginUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Identity - Authentication", description = "Endpoints to user authenticate")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;

    @Operation(summary = "Sign up user", description = "Where user register on mindshub.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User signed up mindshub"),
            @ApiResponse(responseCode = "400", description = "Response has a bad request, user not signed in."),
            @ApiResponse(responseCode = "409", description = "Email has been registered.")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterUserRequest request) {
        registerUserUseCase.execute(request);
    }

    @Operation(summary = "Sign in user", description = "Where user login on mindshub.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "User sign in mindshub."),
            @ApiResponse(responseCode = "400", description = "Response has a bad request, user not signed in.")
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LoginUserResponse login(@Valid @RequestBody LoginUserRequest request) {
        return loginUserUseCase.execute(request);
    }

    @Operation(summary = "Verify email user", description = "Where user email is verify.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Email successfully verified."),
            @ApiResponse(responseCode = "404", description = "Token not found."),
            @ApiResponse(responseCode = "409", description = "Email already verified.")
    })
    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        verifyEmailUseCase.execute(token);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resend-verification")
    public ResponseEntity<Void> resendEmail(@RequestParam String token) {
        verifyEmailUseCase.resend(token);

        return ResponseEntity.ok().build();
    }
}
