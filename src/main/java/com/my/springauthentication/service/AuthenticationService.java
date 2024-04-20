package com.my.springauthentication.service;

import com.my.springauthentication.dto.SignUpDto;
import com.my.springauthentication.model.User;

public interface AuthenticationService {

    User signUp(SignUpDto signUpDto);
}
