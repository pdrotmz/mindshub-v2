package br.com.mindshub.identity.application.usecase.impl.auth;

import br.com.mindshub.identity.application.exception.InvalidCredentialsException;
import br.com.mindshub.identity.application.usecase.auth.LoginUserUseCase;
import br.com.mindshub.identity.infrastructure.security.jwt.JwtService;
import br.com.mindshub.identity.presentation.dto.request.LoginUserRequest;
import br.com.mindshub.identity.presentation.dto.response.auth.LoginUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCaseImpl implements LoginUserUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginUserResponse execute(LoginUserRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);

            return new LoginUserResponse(token);
        } catch (BadCredentialsException exception) {
            throw new InvalidCredentialsException("Email or password incorrect.");
        }
    }
}
