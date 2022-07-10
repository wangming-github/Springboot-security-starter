package com.example.jwtserver.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author maizi
 */
@RestController
@Slf4j
public class HelloController {

    @PostMapping("/hello")
    public String hello() {
        return "hello world!";
    }

}
