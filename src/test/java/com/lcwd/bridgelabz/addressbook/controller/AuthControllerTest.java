package com.lcwd.bridgelabz.addressbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.bridgelabz.addressbook.dto.*;
import com.lcwd.bridgelabz.addressbook.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void register_ShouldReturnSuccessMessage() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password");

        Mockito.when(authService.register(any())).thenReturn("User registered successfully");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void register_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("invalid-email");
        request.setPassword("password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenPasswordIsBlank() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturnAuthToken() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password");
        AuthResponseDTO response = new AuthResponseDTO("mock-jwt-token");
        Mockito.when(authService.login(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    void login_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("invalid-email");
        request.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturnBadRequest_WhenPasswordIsBlank() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void forgotPassword_ShouldReturnSuccessMessage() throws Exception {
        ForgotPasswordDTO request = new ForgotPasswordDTO();
        request.setEmail("test@example.com");
        Mockito.when(authService.forgotPassword(any())).thenReturn("Password reset link sent");

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset link sent"));
    }

    @Test
    void resetPassword_ShouldReturnSuccessMessage() throws Exception {
        ResetPasswordDTO request = new ResetPasswordDTO();
        request.setToken("token123");
        request.setNewPassword("newPassword");
        Mockito.when(authService.resetPassword(any())).thenReturn("Password reset successful");

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful"));
    }
}
