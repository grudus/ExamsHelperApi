package com.grudus.examshelper.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @RequestMapping(method = RequestMethod.GET)
    public String admin() {
        return "Wow, you're admin!";
    }

}
