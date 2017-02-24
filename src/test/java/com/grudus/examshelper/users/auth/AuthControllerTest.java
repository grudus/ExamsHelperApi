package com.grudus.examshelper.users.auth;


import com.grudus.examshelper.AbstractControllerTest;
import com.grudus.examshelper.commons.keys.RestKeys;
import com.grudus.examshelper.emails.EmailSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.grudus.examshelper.Utils.randomAddUserRequest;
import static com.grudus.examshelper.Utils.randomEmail;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AbstractControllerTest {

    public static final String REGISTER_URL = "/api/auth/register";

    @Autowired
    private EmailSender emailSender;

    @Test
    public void shouldReturnFalseWhenCheckingUserExistence() throws Exception {
        checkUserExistence(randomEmail(), false);
    }

    @Test
    public void shouldAddUserToDbAndSendEmail() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(emailSender).sendConfirmationRegister(eq(request.getUsername()), eq(request.getEmail()), anyString(), anyString());
        checkUserExistence(request.getEmail(), true);
    }

    @Test
    public void shouldNotSaveUserDueToEmptyPassword() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        request.setPassword(null);

        performAddUserRequestAndAssertError(request, RestKeys.EMPTY_PASSWORD);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString(), anyString());
    }


    @Test
    public void shouldNotSaveUserDueToEmptyEmail() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        request.setEmail(null);

        performAddUserRequestAndAssertError(request, RestKeys.EMPTY_EMAIL);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString(), anyString());
    }


    @Test
    public void shouldNotSaveUserDueToInvalidEmail() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        request.setEmail("123455");

        performAddUserRequestAndAssertError(request, RestKeys.INVALID_EMAIL);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString(), anyString());
    }


    @Test
    public void shouldNotSaveUserDueToEmptyUsername() throws Exception {
        AddUserRequest request = randomAddUserRequest();
        request.setUsername(null);

        performAddUserRequestAndAssertError(request, RestKeys.EMPTY_USERNAME);
        verify(emailSender, never()).sendConfirmationRegister(anyString(), anyString(), anyString(), anyString());
    }


    public void performAddUserRequestAndAssertError(AddUserRequest request, String error) throws Exception {
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0]", is(error)));
    }

    public void checkUserExistence(String email, boolean exists) throws Exception {
        mockMvc.perform(get("/api/auth/exists").param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists", is(exists)));
    }
//    @Test
//    public void shouldReturnTrueWhenUserInDbAndCheckingExistence() throws Exception {
//        User user = randomUser();
//        addUserRequest(user);
//    }
//
//    private void addUserRequest(User user) throws Exception {
//        AddUserRequest request = new AddUserRequest(user.getUsername(), user.getPassword(), user.getEmail());
//        mockMvc.perform(post("/api/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(toJson(request)))
//                .andExpect(status().isOk());
//    }

}
