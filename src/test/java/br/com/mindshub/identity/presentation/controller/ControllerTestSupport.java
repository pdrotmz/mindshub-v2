package br.com.mindshub.identity.presentation.controller;

import br.com.mindshub.identity.infrastructure.security.config.*;
import br.com.mindshub.identity.infrastructure.security.jwt.*;
import br.com.mindshub.shared.presentation.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@Import({SecurityConfig.class, PasswordConfig.class, JwtAuthenticationFilter.class, JwtService.class,
        CustomAccessDeniedHandler.class, CustomAuthorityEntryPoint.class, GlobalExceptionHandler.class})
@ActiveProfiles("test")
public abstract class ControllerTestSupport {
    @Autowired protected MockMvc mvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected JwtService jwtService;
    @MockitoBean protected UserDetailsService userDetailsService;
}
