package com.grudus.examshelper.configuration.security.filters;


import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.configuration.security.UserAuthenticationProvider;
import com.grudus.examshelper.configuration.security.token.TokenAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    public static final Logger logger = LoggerFactory.getLogger(StatelessLoginFilter.class);

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
        logger.info("User {} successfully logged", auth.getUser().getUsername());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        logger.warn("Cannot authenticate user", failed);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
    }
}