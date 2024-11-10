package com.exe201.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @RequestMapping(value = {"/", "/login"})
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/register")
    public String registerPage() {
        return "sign_up";
    }

    @RequestMapping("/activate-account")
    public String activateAccountPage() {
        return "activate-account";
    }
}

