package com.grudus.examshelper.configuration.authenticated.stateless;


import com.grudus.examshelper.configuration.authenticated.AuthenticatedUser;
import com.grudus.examshelper.configuration.authenticated.UserAuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_USERNAME_PARAMETER = "username";
    private static final String DEFAULT_PASSWORD_PARAMETER = "password";

    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    private String usernameParameter;
    private String passwordParameter;

    public StatelessLoginFilter(String defaultFilterProcessesUrl, TokenAuthenticationService tokenAuthenticationService, UserAuthenticationProvider userAuthenticationProvider) {
        this(defaultFilterProcessesUrl, tokenAuthenticationService, userAuthenticationProvider, DEFAULT_USERNAME_PARAMETER, DEFAULT_PASSWORD_PARAMETER);
    }

    public StatelessLoginFilter(String defaultFilterProcessesUrl,
                                TokenAuthenticationService tokenAuthenticationService,
                                UserAuthenticationProvider userAuthenticationProvider,
                                String usernameParameter,
                                String passwordParameter) {
        super(defaultFilterProcessesUrl);
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.usernameParameter = usernameParameter;
        this.passwordParameter = passwordParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String username = request.getParameter(usernameParameter);
        String password = request.getParameter(passwordParameter);

        return userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        AuthenticatedUser auth = (AuthenticatedUser) authResult;

        tokenAuthenticationService.addAuthentication(response, auth);
        SecurityContextHolder.getContext().setAuthentication(auth);

    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
    }
}