package com.grudus.examshelper.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Collections.singletonMap;

@RequestMapping("/api/user/exists")
@RestController
public class UserExistenceController {

    private final UserService userService;

    @Autowired
    public UserExistenceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Map<String, Boolean> exists(@RequestParam(value = "email", required = false) String email,
                                       @RequestParam(value = "username", required = false) String username) {
        return singletonMap("exists", userService.findByEmailOrUsername(email, username).isPresent());
    }
}
