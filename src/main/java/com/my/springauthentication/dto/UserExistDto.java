package com.my.springauthentication.dto;

import lombok.Data;

@Data
public class UserExistDto {

    private boolean emailExist;

    public UserExistDto(boolean emailExists) {
        this.emailExist = emailExists;
    }

}
