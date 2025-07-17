package com.andresavilesdev.demo_jwt.demo.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1")
public class DemoController {

    @GetMapping("/demo")
    public String welcome(){
        return "Welcome to SECURE - API";
    }

}
