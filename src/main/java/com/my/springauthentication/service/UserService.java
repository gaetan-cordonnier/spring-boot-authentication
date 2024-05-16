package com.my.springauthentication.service;

import com.my.springauthentication.dto.UserDto;
import com.my.springauthentication.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserDetailsService userDetailsService();

    Optional<UserDto> getUserDetails(UUID uuid);

    String deleteUser(UUID uuid);

    Page<User> getAllUsers(Pageable pageable);
}
