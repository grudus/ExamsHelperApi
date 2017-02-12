package com.grudus.examshelper.configuration.security.token;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

@Service
public class TokenAuthenticationService {

    public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private final TokenHandler tokenHandler;
    private final UserService userService;

    @Autowired
    public TokenAuthenticationService(@Value("${token.secret}") String secretToken, UserService userService) {
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secretToken));
        this.userService = userService;
    }

    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication;

        User user = authenticatedUser.getUser();

        String token = user.getToken();

        if (token == null || token.trim().isEmpty()) {
            token = tokenHandler.createTokenForUser(user);
            userService.addToken(user.getId(), token);
        }

        response.setHeader(AUTH_HEADER_NAME, token);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token == null)
            return null;

        User user = userService.findEnabledByToken(token).orElse(null);

        return user == null ? null : new AuthenticatedUser(user);
    }
}