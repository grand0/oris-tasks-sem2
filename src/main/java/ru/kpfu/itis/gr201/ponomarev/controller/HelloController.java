package ru.kpfu.itis.gr201.ponomarev.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/itis")
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello!";
    }
}
