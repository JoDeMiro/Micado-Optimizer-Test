package com.example.filedemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {

    @GetMapping("favicon.ico")
    public ResponseEntity<Void> favicon() {
        // Nincs ikon, de ne legyen 404/403
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}