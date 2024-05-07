package com.my.springauthentication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.springauthentication.dto.RefreshTokenDto;
import com.my.springauthentication.dto.SignInDto;
import com.my.springauthentication.dto.SignUpDto;
import com.my.springauthentication.utils.ConstantUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@Transactional
public class AuthenticationServiceImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSignUp() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("noyoveg746@picdv.com");
        signUpDto.setFirstname("Woody");
        signUpDto.setPassword("P@sswOrd");

        mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("noyoveg746@picdv.com"));
    }

    @Test
    public void testSignIn() throws Exception {
        testSignUp();

        SignInDto signInDto = new SignInDto();
        signInDto.setEmail("noyoveg746@picdv.com");
        signInDto.setPassword("P@sswOrd");

        mockMvc.perform(post("/auth/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    public void testRefreshToken() throws Exception {
        MvcResult signInResult = testSignInReturnResult();

        String responseContent = signInResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseContent);

        String refreshToken = jsonNode.get("refreshToken").asText();

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setToken(refreshToken);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    public void testForgotPassword() throws Exception {
        testSignUp();

        String email = "noyoveg746@picdv.com";

        mockMvc.perform(put("/auth/forgot-password")
                        .contentType(APPLICATION_JSON)
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().string(ConstantUtils.USER_PASSWORD_UPDATE_REQUEST));
    }

    @Test
    public void testUpdatePassword() throws Exception {
        MvcResult result = testSignInReturnResult();
        String token = objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();

        SignInDto signInDto = new SignInDto();
        signInDto.setEmail("noyoveg746@picdv.com");
        signInDto.setPassword("NewP@sswOrd");

        mockMvc.perform(put("/auth/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string(ConstantUtils.USER_PASSWORD_UPDATED));
    }

    private MvcResult testSignInReturnResult() throws Exception {
        testSignUp();

        SignInDto signInDto = new SignInDto();
        signInDto.setEmail("noyoveg746@picdv.com");
        signInDto.setPassword("P@sswOrd");

        return mockMvc.perform(post("/auth/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andReturn();
    }
}
