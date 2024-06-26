package com.my.springauthentication.controller;


import com.my.springauthentication.dto.*;
import com.my.springauthentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(path = "/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto) {
        UserDto newUser = authenticationService.signUp(signUpDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(newUser);
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<JwtDto> signin(@RequestBody SignInDto signinDto) {
        return ResponseEntity.ok(authenticationService.signIn(signinDto));
    }

    @PostMapping(path = "/refresh")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<JwtDto> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        return new ResponseEntity<>(authenticationService.refreshToken(refreshTokenDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/check-email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserExistDto> checkUserExist(@RequestParam String email) {
        Boolean emailExist = authenticationService.checkUserExist(email);
        UserExistDto response = new UserExistDto(emailExist);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<>(authenticationService.forgotPassword(email), HttpStatus.OK);
    }

    @PutMapping(path = "/update-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updatePassword(@RequestBody SignInDto signInDto) {
        return new ResponseEntity<>(authenticationService.updatePassword(signInDto), HttpStatus.OK);
    }
}
