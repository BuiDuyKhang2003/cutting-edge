package com.exe201.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReviewController {

    @RequestMapping("/post")
    public String creataPostPage(){
        return "create_post";
    }

    @RequestMapping("/category")
    public String categoryPage(){
        return "category";
    }

    @RequestMapping("/review-post")
    public String reviewPostPage(){
        return "reviews";
    }
}
