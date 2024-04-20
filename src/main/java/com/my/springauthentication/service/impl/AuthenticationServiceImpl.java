package com.my.springauthentication.service.impl;


import com.my.springauthentication.dto.SignUpDto;
import com.my.springauthentication.model.Role;
import com.my.springauthentication.model.User;
import com.my.springauthentication.repository.UserRepository;
import com.my.springauthentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User signUp(SignUpDto signUpDto) {
        User user = new User();

        user.setFirstname(signUpDto.getFirstname());
        user.setEmail(signUpDto.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        return userRepository.save(user);
    }
}
