package org.example.supplychainx.Controller;

import org.example.supplychainx.DTO.UserResponseDTO;
import org.example.supplychainx.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserResponseDTO userDTO) {
        UserResponseDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(201).body(createdUser);
    }

    public  ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserResponseDTO userDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id , userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("success");
    }
}
