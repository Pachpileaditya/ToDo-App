package com.todoapp.MyApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
// @CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @GetMapping("/manage-users")
    public ResponseEntity<String> manageUsers() {
        return ResponseEntity.ok("Admin can manage users here.");
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getStats() {
        return ResponseEntity.ok("Admin can view app stats here.");
    }
}

