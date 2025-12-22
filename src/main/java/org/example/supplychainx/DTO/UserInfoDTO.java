package org.example.supplychainx.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private long idUser;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}

