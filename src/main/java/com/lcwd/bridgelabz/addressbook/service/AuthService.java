package com.lcwd.bridgelabz.addressbook.service;

import com.lcwd.bridgelabz.addressbook.dto.*;
import com.lcwd.bridgelabz.addressbook.model.User;
import com.lcwd.bridgelabz.addressbook.repository.UserRepository;
import com.lcwd.bridgelabz.addressbook.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    public String register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return "User registered successfully";
    }

    public String forgotPassword(ForgotPasswordDTO request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Email not found");
        }

        User user = userOpt.get();
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);

        emailService.sendResetEmail(user.getEmail(), resetToken);
        return "Password reset link sent to email";
    }

    public String resetPassword(ResetPasswordDTO request) {
        Optional<User> userOpt = userRepository.findByResetToken(request.getToken());
        if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            throw new RuntimeException("New password cannot be null or empty");
        }

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid or expired token");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        userRepository.save(user);

        return "Password reset successfully";
    }


    public AuthResponseDTO login(LoginRequestDTO request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(userOpt.get().getEmail());
        return new AuthResponseDTO(token);
    }


}
