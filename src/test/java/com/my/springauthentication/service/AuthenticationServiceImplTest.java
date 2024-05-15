package com.my.springauthentication.service;

import com.my.springauthentication.dto.*;
import com.my.springauthentication.exception.GenericException;
import com.my.springauthentication.model.Role;
import com.my.springauthentication.model.User;
import com.my.springauthentication.repository.UserRepository;
import com.my.springauthentication.utils.ConstantUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;
    private SignUpDto signUpDto;
    private SignInDto signInDto;
    private JwtDto jwtDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstname("Woody");
        user.setEmail("noyoveg746@picdv.com");
        user.setRole(Role.USER);
        user.setValidated(true);

        signUpDto = new SignUpDto();
        signUpDto.setFirstname("Woody");
        signUpDto.setEmail("noyoveg746@picdv.com");
        signUpDto.setPassword("P@ssw0rd");

        signInDto = new SignInDto();
        signInDto.setEmail("noyoveg746@picdv.com");
        signInDto.setPassword("P@ssw0rd");

        jwtDto = new JwtDto();
        jwtDto.setToken("jwtToken");
        jwtDto.setRefreshToken("refreshToken");
    }

    @Test
    void testSignUp_UserAlreadyExists() {
        User existingUser = new User();
        existingUser.setEmail("noyoveg746@picdv.com");
        existingUser.setFirstname("Woody");
        existingUser.setRole(Role.USER);
        existingUser.setPassword("P@ssw0rd");
        existingUser.setValidated(true);

        when(userRepository.findByEmail("noyoveg746@picdv.com")).thenReturn(Optional.of(existingUser));

        Throwable thrown = catchThrowable(() -> authenticationService.signUp(signUpDto));
        assertThat(thrown).isNotNull();
        assertThat(thrown).isInstanceOf(GenericException.class);
        GenericException genericException = (GenericException) thrown;
        assertThat(genericException.getErrorMessage()).contains(ConstantUtils.USER_ALREADY_EXIST);

        verify(userRepository, times(1)).findByEmail("noyoveg746@picdv.com");
    }

    @Test
    void testSignUp_Success() {
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        UserDto newUser = authenticationService.signUp(signUpDto);

        assertThat(newUser.getId()).isNotNull();
        assertThat(newUser.getFirstname()).isEqualTo("Woody");

        verify(userRepository, times(1)).findByEmail("noyoveg746@picdv.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("P@ssw0rd");
    }


    @Test
    void testSignIn_UserNotFound() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            authenticationService.signIn(signInDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ConstantUtils.USER_NOT_FOUND);

        verify(userRepository, times(1)).findByEmail("noyoveg746@picdv.com");
    }

    @Test
    void testSignIn_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        when(jwtService.generateRefreshToken(Mockito.<Map<String, Object>>any(), any(User.class)))
                .thenReturn("refreshToken");

        JwtDto jwtResponse = authenticationService.signIn(signInDto);

        assertThat(jwtResponse.getToken()).isEqualTo("jwtToken");
        assertThat(jwtResponse.getRefreshToken()).isEqualTo("refreshToken");

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail("noyoveg746@picdv.com");
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(jwtService, times(1)).generateRefreshToken(Mockito.<Map<String, Object>>any(), any(User.class));
    }

    @Test
    void testCheckUserExist() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        Boolean exists = authenticationService.checkUserExist("noyoveg746@picdv.com");

        assertThat(exists).isTrue();

        verify(userRepository, times(1)).findByEmail("noyoveg746@picdv.com");
    }

    @Test
    void testRefreshToken_Success() {
        when(jwtService.extractUsername(any(String.class))).thenReturn("noyoveg746@picdv.com");
        when(userRepository.findByEmail("noyoveg746@picdv.com")).thenReturn(Optional.of(user));
        when(jwtService.isValidToken(any(String.class), any(User.class))).thenReturn(true);
        when(jwtService.generateToken(any(User.class))).thenReturn("newJwtToken");
        when(jwtService.generateRefreshToken(any(Map.class), any(User.class))).thenReturn("newRefreshToken");

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setToken("oldRefreshToken");

        JwtDto jwtDto = authenticationService.refreshToken(refreshTokenDto);

        assertThat(jwtDto).isNotNull();
        assertThat(jwtDto.getToken()).isEqualTo("newJwtToken");
        assertThat(jwtDto.getRefreshToken()).isEqualTo("newRefreshToken");

        verify(jwtService, times(1)).extractUsername("oldRefreshToken");
        verify(userRepository, times(1)).findByEmail("noyoveg746@picdv.com");
        verify(jwtService, times(1)).isValidToken("oldRefreshToken", user);
        verify(jwtService, times(1)).generateToken(user);
        verify(jwtService, times(1)).generateRefreshToken(Mockito.<Map<String, Object>>any(), eq(user));
    }

    @Test
    void testForgotPassword_Success() {
        when(userRepository.findByEmail("noyoveg746@picdv.com")).thenReturn(Optional.of(user));

        String response = authenticationService.forgotPassword("noyoveg746@picdv.com");

        assertThat(response).isEqualTo(ConstantUtils.USER_PASSWORD_UPDATE_REQUEST);
        verify(userRepository, times(1)).findByEmail("noyoveg746@picdv.com");
        verify(userRepository, times(1)).save(any(User.class));

        verify(emailService, times(1)).sendEmailWithHtmlTemplate(
                eq("noyoveg746@picdv.com"),
                eq("Modification de mot de passe."),
                eq("forgot_password-template"),
                any(Context.class)
        );
    }

    @Test
    void testUpdatePassword_Success() {
        when(userRepository.findByEmail("noyoveg746@picdv.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("P@ssw0rd")).thenReturn("encodedPassword");

        String response = authenticationService.updatePassword(signInDto);

        assertThat(response).isEqualTo(ConstantUtils.USER_PASSWORD_UPDATED);

        verify(userRepository, times(1)).findByEmail("noyoveg746@picdv.com");
        verify(userRepository, times(1)).save(user);

        assertThat(user.getPassword()).isEqualTo("encodedPassword");
    }
}