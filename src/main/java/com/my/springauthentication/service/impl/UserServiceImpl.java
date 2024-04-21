package com.my.springauthentication.service.impl;

import com.my.springauthentication.model.User;
import com.my.springauthentication.repository.UserRepository;
import com.my.springauthentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) {
                return userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public Optional<User> getUserDetails(Long id) {
        return userRepository.findById(id);
    }
}
