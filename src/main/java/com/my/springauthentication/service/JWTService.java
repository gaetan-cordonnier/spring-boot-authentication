package com.my.springauthentication.service;

import com.my.springauthentication.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

public interface JWTService {

    String extractUsername(String token);

    String generateToken(UserDetails userDetails);

    Boolean isValidToken(String token, UserDetails userDetails);

    Boolean isTokenExpired(String token);

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);


}
