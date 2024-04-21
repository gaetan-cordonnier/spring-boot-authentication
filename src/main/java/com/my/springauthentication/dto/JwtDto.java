package com.my.springauthentication.dto;

import lombok.Data;

@Data
public class JwtDto {

    private String token;
    private String refreshToken;
}
