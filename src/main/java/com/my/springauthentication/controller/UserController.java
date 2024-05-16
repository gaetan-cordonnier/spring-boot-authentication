package com.my.springauthentication.controller;

import com.my.springauthentication.dto.UserDto;
import com.my.springauthentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/details")
    public ResponseEntity<Optional<UserDto>> getUserDetails(@RequestParam UUID uuid) {
        return ResponseEntity.ok(userService.getUserDetails(uuid));
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteUser(@RequestParam UUID uuid) {
        return ResponseEntity.ok(userService.deleteUser(uuid));
    }
}
