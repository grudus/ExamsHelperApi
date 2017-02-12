package com.grudus.examshelper.users;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole(ROLE_USER, ROLE_ADMIN)")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public User getUserInfo(AuthenticatedUser currentUser) {
        return currentUser.getUser();
    }


    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteUser(AuthenticatedUser currentUser) {
        userService.delete(currentUser.getUser());
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateUser(AuthenticatedUser currentUser) {
        userService.update(currentUser.getUser());
    }

}
