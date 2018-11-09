package ua.challenge.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/ping")
    public String ping() {
        return "ping ...";
    }
}
