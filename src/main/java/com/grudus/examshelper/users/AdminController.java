package com.grudus.examshelper.users;

import com.grudus.examshelper.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole(ROLE_ADMIN)")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/user")
    public User getUserInfo(@RequestParam(value = "id", required = false) Long userId,
                            @RequestParam(value = "username", required = false) String username) {

        return userId == null ? userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username))
                : userService.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"", "/users"})
    public List<User> getUsers() {
        return userService.findAll();
    }

}
