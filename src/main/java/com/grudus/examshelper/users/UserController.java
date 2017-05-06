package com.grudus.examshelper.users;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Collections.singletonMap;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public UserDto getUserInfo(AuthenticatedUser currentUser) {
        return currentUser.getUser().toDto();
    }


    @DeleteMapping
    public void deleteUser(AuthenticatedUser currentUser) {
        userService.delete(currentUser.getUser());
    }

    @PutMapping
    public void updateUser(AuthenticatedUser currentUser) {
        userService.update(currentUser.getUser());
    }

}
