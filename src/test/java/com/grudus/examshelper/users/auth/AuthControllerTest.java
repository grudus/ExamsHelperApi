package com.grudus.examshelper.users.auth;


import com.grudus.examshelper.AbstractControllerTest;
import com.grudus.examshelper.commons.keys.RestKeys;
import com.grudus.examshelper.emails.EmailSender;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;

import static com.grudus.examshelper.users.roles.RoleName.USER;
import static com.grudus.examshelper.utils.RequestParam.param;
import static com.grudus.examshelper.utils.Utils.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AbstractControllerTest {

    private static final String REDIRECT_URI = randAlph(10);
    private static final String REGISTER_URL = "/api/auth/register?redirect_uri=" + REDIRECT_URI;
    private static final String EXISTENCE_URL = "/api/user/exists?";

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private UserService userService;

    @Before
    public void init() {
        login();
    }

    @Test
    public void shouldReturnFalseWhenCheckingUserExistence() throws Exception {
        checkUserExistence(randomEmail(), false);
    }

    @Test
    public void shouldAddUserToDbAndSendEmailWithRedirect() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        performAddUserRequest(request);

        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        verify(emailSender).sendConfirmationRegister(eq(request.getUsername()), eq(request.getEmail()), url.capture());

        assertThat(url.getValue(), containsString("redirect_uri="+REDIRECT_URI));
        checkUserExistence(request.getEmail(), true);
    }

    @Test
    public void shouldNotSaveUserDueToEmptyPassword() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        request.setPassword(null);

        performAddUserRequestAndAssertError(request, RestKeys.EMPTY_PASSWORD);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString());
    }


    @Test
    public void shouldNotSaveUserDueToEmptyEmail() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        request.setEmail(null);

        performAddUserRequestAndAssertError(request, RestKeys.EMPTY_EMAIL);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString());
    }


    @Test
    public void shouldNotSaveUserDueToInvalidEmail() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        request.setEmail("123455");

        performAddUserRequestAndAssertError(request, RestKeys.INVALID_EMAIL);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString());
    }


    @Test
    public void shouldNotSaveUserDueToEmptyUsername() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        request.setUsername(null);

        performAddUserRequestAndAssertError(request, RestKeys.EMPTY_USERNAME);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString());
    }


    @Test
    public void shouldNotSaveUserWhenUsernameAlreadyExists() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        performAddUserRequest(request);
        reset(emailSender);

        performAddUserRequestAndAssertError(request, RestKeys.USERNAME_EXISTS);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString());
    }

    @Test
    public void shouldNotSaveUserWhenUsernameInDb() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        performAddUserRequest(request);
        User user = randomUser();
        user.setUsername(request.getUsername());
        userService.registerUser(user, USER);
        reset(emailSender);

        performAddUserRequestAndAssertError(request, RestKeys.USERNAME_EXISTS);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString());
    }

    @Test
    public void shouldNotSaveUserWhenEmailAlreadyExists() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        performAddUserRequest(request);
        request.setUsername(randAlph(33));
        reset(emailSender);

        performAddUserRequestAndAssertError(request, RestKeys.EMAIL_EXISTS);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString());
    }

    private void performAddUserRequest(AddUserRequest request) throws Exception {
        post(REGISTER_URL, request)
                .andExpect(status().isOk());
    }


    public void performAddUserRequestAndAssertError(AddUserRequest request, String error) throws Exception {
        post(REGISTER_URL, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0]", is(error)));
    }

    public void checkUserExistence(String email, boolean exists) throws Exception {
        get(EXISTENCE_URL, param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists", is(exists)));
    }

}
