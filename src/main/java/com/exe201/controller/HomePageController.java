package com.exe201.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

    @RequestMapping("home-page")
    public String loginPage() {
        return "homepage_1";
    }
}
