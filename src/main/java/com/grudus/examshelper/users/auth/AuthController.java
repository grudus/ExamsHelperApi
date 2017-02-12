package com.grudus.examshelper.users.auth;


import com.grudus.examshelper.emails.EmailSender;
import com.grudus.examshelper.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AddUserRequestValidator validator;
    private final UserService userService;

    @Autowired
    public AuthController(AddUserRequestValidator validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @InitBinder("addUserRequest")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void ddd(@Valid @RequestBody AddUserRequest request,  BindingResult result, HttpServletResponse response) throws IOException {
        if (result.hasErrors()) {
            response.sendError(SC_BAD_REQUEST, getMessage(result));
            return;
        }
        handleInvitation(request);

    }

    private void handleInvitation(AddUserRequest request) {
        userService.saveAddUserRequest(request, UUID.randomUUID().toString());
        new EmailSender().send(request);
    }

    private String getMessage(BindingResult result) {
        return result.getAllErrors().stream().map(ObjectError::getCodes)
                .findFirst().orElse(new String[]{"", "Unexpected error"})[1];

    }


}
