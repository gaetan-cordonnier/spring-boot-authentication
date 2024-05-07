package com.my.springauthentication.service.impl;


import com.my.springauthentication.dto.JwtDto;
import com.my.springauthentication.dto.RefreshTokenDto;
import com.my.springauthentication.dto.SignInDto;
import com.my.springauthentication.dto.SignUpDto;
import com.my.springauthentication.exception.GenericException;
import com.my.springauthentication.exception.NotFoundException;
import com.my.springauthentication.model.Role;
import com.my.springauthentication.model.User;
import com.my.springauthentication.repository.UserRepository;
import com.my.springauthentication.service.AuthenticationService;
import com.my.springauthentication.service.EmailService;
import com.my.springauthentication.service.JWTService;
import com.my.springauthentication.utils.ConstantUtils;
import com.my.springauthentication.utils.GenerateCodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final EmailService emailService;

    public User signUp(SignUpDto signUpDto) {
        User user = new User();

        user.setFirstname(signUpDto.getFirstname());
        user.setEmail(signUpDto.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new GenericException(409, ConstantUtils.USER_ALREADY_EXIST);
        }

        return userRepository.save(user);
    }

    public JwtDto signin(SignInDto signinDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinDto.getEmail(), signinDto.getPassword()));

        var user = userRepository.findByEmail(signinDto.getEmail()).orElseThrow(() -> new IllegalArgumentException(ConstantUtils.USER_NOT_FOUND));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtDto jwtDto = new JwtDto();

        jwtDto.setToken(jwt);
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public JwtDto refreshToken(RefreshTokenDto refreshTokenDto) {
        String userEmail = jwtService.extractUsername(refreshTokenDto.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotFoundException(ConstantUtils.USER_NOT_FOUND));
        if (jwtService.isValidToken(refreshTokenDto.getToken(), user)) {
            var jwt = jwtService.generateToken(user);
            var refreshJwt = jwtService.generateRefreshToken(new HashMap<>(), user);

            JwtDto jwtDto = new JwtDto();

            jwtDto.setToken(jwt);
            jwtDto.setRefreshToken(refreshJwt);
            return jwtDto;
        }
        return null;
    }

    public Boolean checkUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public String forgotPassword(String email) {
        Integer verificationCode = GenerateCodeUtils.randomCode();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ConstantUtils.USER_NOT_FOUND));
        user.setVerification(verificationCode);
        userRepository.save(user);

        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("verificationCode", verificationCode);

        emailService.sendEmailWithHtmlTemplate(email, "Modification de mot de passe.", "forgot_password-template", context);

        return ConstantUtils.USER_PASSWORD_UPDATE_REQUEST;
    }

    public String updatePassword(SignInDto signInDto) {
        Date date = new Date();
        User user = userRepository.findByEmail(signInDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("Cet identifiant n'existe pas."));
        user.setPassword(passwordEncoder.encode(signInDto.getPassword()));
        user.setUpdated(date);
        userRepository.save(user);

        return ConstantUtils.USER_PASSWORD_UPDATED;
    }
}
