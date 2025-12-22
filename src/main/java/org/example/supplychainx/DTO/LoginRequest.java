package org.example.supplychainx.DTO;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class LoginRequest {

    @NotBlank(message = "Password is required")
    private String password;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
}








