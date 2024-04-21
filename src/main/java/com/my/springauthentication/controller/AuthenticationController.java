package com.my.springauthentication.controller;


import com.my.springauthentication.dto.JwtDto;
import com.my.springauthentication.dto.SignUpDto;
import com.my.springauthentication.dto.SigninDto;
import com.my.springauthentication.model.User;
import com.my.springauthentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(authenticationService.signUp(signUpDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> signin(@RequestBody SigninDto signinDto) {
        return ResponseEntity.ok(authenticationService.signin(signinDto));
    }
}
