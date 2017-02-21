package com.grudus.examshelper.users.auth;


import com.grudus.examshelper.emails.EmailSender;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.grudus.examshelper.users.roles.RoleName.USER;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AddUserRequestValidator validator;
    private final UserService userService;
    private final EmailSender emailSender;

    @Autowired
    public AuthController(AddUserRequestValidator validator, UserService userService, EmailSender emailSender) {
        this.validator = validator;
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @InitBinder("addUserRequest")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(value = "/register")
    public void addUserRequest(@Valid @RequestBody AddUserRequest userRequest, BindingResult result, HttpServletResponse response, HttpServletRequest request) throws IOException, MessagingException {
        if (result.hasErrors()) {
            logger.error("Request {} is not proper, because {}", userRequest, getMessage(result));
            response.sendError(SC_BAD_REQUEST, getMessage(result));
            return;
        }

        handleInvitation(userRequest, request.getRequestURL().toString());
    }

    @GetMapping(value = "/register/{token}")
    public void confirmUserRegistration(@PathVariable("token") String token, HttpServletResponse response) throws IOException, MessagingException {
        Optional<User> user = userService.findWaitingByToken(token);
        if (!user.isPresent()) {
            logger.error("Cannot find user with token {}", token);
            response.sendError(400, "Cannot find user with token " + token);
            return;
        }

        userService.registerUser(user.get(), USER);
        logger.info("Enabled user {}", user.get().getUsername());
    }

    private void handleInvitation(AddUserRequest request, String url) throws MessagingException {
        String token = UUID.randomUUID().toString();
        emailSender.sendConfirmationRegister(request.getUsername(), request.getEmail(), token, url);
        userService.saveAddUserRequest(request, token);
        logger.info("Sent invitation to the {}", request);
    }

    private String getMessage(BindingResult result) {
        return result.getAllErrors().stream().map(ObjectError::getCodes)
                .findFirst().orElse(new String[]{"", "Unexpected error"})[1];

    }


}
