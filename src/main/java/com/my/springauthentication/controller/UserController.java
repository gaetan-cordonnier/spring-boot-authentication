package com.my.springauthentication.controller;

import com.my.springauthentication.model.User;
import com.my.springauthentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/details")
    public ResponseEntity<Optional<User>> getUserDetails(@RequestParam Long id) {
        return ResponseEntity.ok(userService.getUserDetails(id));
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
