package com.my.springauthentication.controller;

import com.my.springauthentication.model.User;
import com.my.springauthentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping(path = "/details")
    public ResponseEntity<Optional<User>> getUserDetails(@RequestParam Long id) {
        return ResponseEntity.ok(userService.getUserDetails(id));
    }
}
