package com.exe201.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping("/profile")
    public String userprofilePage(){
        return "profile";
    }
}
