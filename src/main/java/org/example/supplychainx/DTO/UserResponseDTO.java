package org.example.supplychainx.DTO;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private long idUser;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private Role role;
}
