package com.my.springauthentication.service;

import com.my.springauthentication.dto.*;

public interface AuthenticationService {

    UserDto signUp(SignUpDto signUpDto);

    JwtDto signIn(SignInDto signinDto);

    JwtDto refreshToken(RefreshTokenDto refreshTokenDto);

    Boolean checkUserExist(String email);

    String forgotPassword(String email);

    String updatePassword(SignInDto signInDto);
}
