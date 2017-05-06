package com.grudus.examshelper.users.auth;


import com.grudus.examshelper.emails.EmailSender;
import com.grudus.examshelper.exceptions.TokenNotFoundException;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.grudus.examshelper.users.roles.RoleName.USER;
import static java.lang.String.format;
import static java.util.Collections.singletonMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AddUserRequestValidator validator;
    private final UserService userService;
    private final EmailSender emailSender;
    private final String baseUrl;

    @Autowired
    public AuthController(AddUserRequestValidator validator, UserService userService, EmailSender emailSender, @Value("${url.base}") String baseUrl) {
        this.validator = validator;
        this.userService = userService;
        this.emailSender = emailSender;
        this.baseUrl = baseUrl;
    }

    @InitBinder("addUserRequest")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(value = "/register")
    public void addUserRequest(@Valid @RequestBody AddUserRequest userRequest,
                               @RequestParam("redirect_uri") String redirectUri) throws MessagingException {
        handleInvitation(userRequest, baseUrl, redirectUri);
    }

    @GetMapping(value = "/register")
    public void confirmUserRegistration(@RequestParam("token") String token,
                                        @RequestParam("redirect_uri") String redirectUri,
                                        HttpServletResponse response) throws IOException, MessagingException {

        User user = userService.findWaitingByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Cannot find user with token " + token));

        userService.registerUser(user, USER);
        logger.info("Enabled user {}. Send redirect to {}", user.getUsername(), redirectUri);
        response.sendRedirect(redirectUri);
    }

    private void handleInvitation(AddUserRequest request, String host, String redirectUri) throws MessagingException {
        String token = UUID.randomUUID().toString();
        String url = format("%s/api/auth/register?token=%s&redirect_uri=%s", host, token, redirectUri);
        emailSender.sendConfirmationRegister(request.getUsername(), request.getEmail(), url);
        userService.saveAddUserRequest(request, token);
        logger.info("Sent invitation to the {}", request);
    }
}
