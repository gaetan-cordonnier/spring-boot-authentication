package com.my.springauthentication.service;

import com.my.springauthentication.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserDetailsService userDetailsService();

    Optional<UserDto> getUserDetails(UUID id);

    String deleteUser(UUID id);
}
