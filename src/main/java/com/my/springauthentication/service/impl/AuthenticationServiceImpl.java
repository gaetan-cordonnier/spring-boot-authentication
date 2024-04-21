package com.my.springauthentication.service.impl;


import com.my.springauthentication.dto.JwtDto;
import com.my.springauthentication.dto.SignUpDto;
import com.my.springauthentication.dto.SigninDto;
import com.my.springauthentication.model.Role;
import com.my.springauthentication.model.User;
import com.my.springauthentication.repository.UserRepository;
import com.my.springauthentication.service.AuthenticationService;
import com.my.springauthentication.service.JWTService;
import com.my.springauthentication.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public User signUp(SignUpDto signUpDto) {
        User user = new User();

        user.setFirstname(signUpDto.getFirstname());
        user.setEmail(signUpDto.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        return userRepository.save(user);
    }

    public JwtDto signin(SigninDto signinDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinDto.getEmail(), signinDto.getPassword()));

        var user = userRepository.findByEmail(signinDto.getEmail()).orElseThrow(()->new IllegalArgumentException(ConstantUtils.USER_NOT_FOUND));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtDto jwtDto = new JwtDto();

        jwtDto.setToken(jwt);
        jwtDto.setRefreshToken(refreshToken);

        return jwtDto;
    }
}
