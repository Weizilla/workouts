package com.weizilla.workouts.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Workouts {
    @RequestMapping("/")
    public String index() {
        return "Hello world";
    }
}
