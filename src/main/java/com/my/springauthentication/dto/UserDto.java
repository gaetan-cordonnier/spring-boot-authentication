package com.my.springauthentication.dto;

import com.my.springauthentication.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDto {

    private UUID id;

    private String firstname;

    private String lastname;

    private String email;

    private Role role;

    private String language;

    private String theme;

    private Boolean validated;

    private Integer verification;
}
