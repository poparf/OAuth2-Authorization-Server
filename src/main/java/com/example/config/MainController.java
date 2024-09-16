package com.example.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/locked")
    public String lockedEndpoint() {
        return "You unlocked it !";
    }
}
