package com.my.springauthentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExistDto {

    private final boolean emailExist;

    public UserExistDto(boolean emailExists) {
        this.emailExist = emailExists;
    }

}
