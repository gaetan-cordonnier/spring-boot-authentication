package com.my.springauthentication.controller;

import com.my.springauthentication.dto.UserDto;
import com.my.springauthentication.model.User;
import com.my.springauthentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping(path = "/details")
    public ResponseEntity<Optional<UserDto>> getUserDetails(@RequestParam UUID uuid) {
        return ResponseEntity.ok(userService.getUserDetails(uuid));
    }

    @DeleteMapping(path = "/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam UUID uuid) {
        return ResponseEntity.ok(userService.deleteUser(uuid));
    }

    @GetMapping(path = "/all-users")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
}
