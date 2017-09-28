package com.grudus.examshelper.configuration.security.token;

import com.grudus.examshelper.MockitoExtension;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.grudus.examshelper.configuration.security.token.TokenAuthenticationService.AUTH_HEADER_NAME;
import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.Utils.randomUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenAuthenticationServiceTest {

    private static final String SECRET = randAlph(32);

    @Mock
    private UserService userService;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    private Authentication authentication;
    private User user;
    private TokenAuthenticationService tokenAuthenticationService;

    @BeforeEach
    public void init() {
        tokenAuthenticationService = new TokenAuthenticationService(SECRET, userService);
        user = randomUser();
        authentication = new AuthenticatedUser(user);

        doNothing().when(response).setHeader(eq(AUTH_HEADER_NAME), anyString());

    }

    @Test
    public void shouldAddAuthHeaderWhenUserDoNotHaveOne() {
        user.setId(1L);
        doNothing().when(userService).addToken(eq(1L), anyString());

        tokenAuthenticationService.addAuthentication(response, authentication);

        verify(userService).addToken(eq(1L), anyString());
        verify(response).setHeader(eq(AUTH_HEADER_NAME), anyString());
    }

    @Test
    public void shouldSetUserToken() {
        user.setId(1L);
        user.setToken(randAlph(32));

        tokenAuthenticationService.addAuthentication(response, authentication);

        verify(response).setHeader(eq(AUTH_HEADER_NAME), eq(user.getToken()));
        verify(userService, never()).addToken(anyLong(), anyString());
    }

    @Test
    public void shouldReturnProperAuthentication() {
        String token = randAlph(32);
        when(request.getHeader(eq(AUTH_HEADER_NAME))).thenReturn(token);
        when(userService.findEnabledByToken(eq(token))).thenReturn(Optional.of(((AuthenticatedUser)authentication).getUser()));

        AuthenticatedUser auth = (AuthenticatedUser) tokenAuthenticationService.getAuthentication(request);

        User u = ((AuthenticatedUser) authentication).getUser();
        User u2 = auth.getUser();

        assertEquals(u.getUsername(), u2.getUsername());
        assertEquals(u.getPassword(), u2.getPassword());
        assertEquals(u.getEmail(), u2.getEmail());
        assertEquals(u.getState(), u2.getState());
    }

    @Test
    public void shouldReturnNullWhenTokenDoNotExists() {
        when(request.getHeader(eq(AUTH_HEADER_NAME))).thenReturn(null);

        Authentication auth = tokenAuthenticationService.getAuthentication(request);

        assertNull(auth);
    }

    @Test
    public void shouldReturnNullWhenTokenAndUserTokenDoNotMatch() {
        when(request.getHeader(eq(AUTH_HEADER_NAME))).thenReturn(randAlph(32));
        when(userService.findEnabledByToken(anyString())).thenReturn(Optional.empty());

        Authentication auth = tokenAuthenticationService.getAuthentication(request);

        assertNull(auth);
    }

}
