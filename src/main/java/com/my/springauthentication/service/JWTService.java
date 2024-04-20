package com.my.springauthentication.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    String extractUsername(String token);

    String generateToken(UserDetails userDetails);

    Boolean isValidToken(String token, UserDetails userDetails);

    Boolean isTokenExpired(String token);
}
