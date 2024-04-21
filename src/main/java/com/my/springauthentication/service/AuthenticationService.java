package com.my.springauthentication.service;

import com.my.springauthentication.dto.JwtDto;
import com.my.springauthentication.dto.RefreshTokenDto;
import com.my.springauthentication.dto.SignUpDto;
import com.my.springauthentication.dto.SignInDto;
import com.my.springauthentication.model.User;

public interface AuthenticationService {

    User signUp(SignUpDto signUpDto);

    JwtDto signin(SignInDto signinDto);

    JwtDto refreshToken(RefreshTokenDto refreshTokenDto);

    Boolean checkUserExist(String email);

    String forgotPassword(String email);

    String updatePassword(SignInDto signInDto);
}
