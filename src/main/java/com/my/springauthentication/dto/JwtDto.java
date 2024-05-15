package com.my.springauthentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDto {

    private String token;
    private String refreshToken;
}
