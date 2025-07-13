package com.blogdirectorio.affiliate.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class MainController {

    @GetMapping("/{path:[^\\.]*}")
    public String forward() {
        return "forward:/";
    }
}
