package com.my.springauthentication.service;

import com.my.springauthentication.dto.UserDto;
import com.my.springauthentication.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {

    UserDetailsService userDetailsService();

    Optional<User> getUserDetails(Long id);
}
