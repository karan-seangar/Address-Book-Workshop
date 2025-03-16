package com.lcwd.bridgelabz.addressbook.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    @NotBlank(message = "Reset token is required")
    private String token;

    @NotBlank(message = "New password cannot be empty")
    private String newPassword;
}
