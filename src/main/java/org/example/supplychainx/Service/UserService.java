package org.example.supplychainx.Service;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.UserRequestDTO;
import org.example.supplychainx.DTO.UserResponseDTO;
import org.example.supplychainx.Mappers.UserMapper;
import org.example.supplychainx.Repository.UserRepository;
import org.example.supplychainx.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElse(null);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        User user = userMapper.toEntityRequest(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserResponseDTO updateUser(Long id ,UserRequestDTO userDTO) {
        var existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            User userToUpdate = userMapper.toEntityRequest(userDTO);
            userToUpdate.setIdUser(existingUser.getIdUser());
            // Encode password if it's being updated
            if (userToUpdate.getPassword() != null && !userToUpdate.getPassword().isEmpty()) {
                userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
            } else {
                // Keep the existing password if not updating
                userToUpdate.setPassword(existingUser.getPassword());
            }
            User updatedUser = userRepository.save(userToUpdate);
            return userMapper.toDto(updatedUser);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponseDTO findByUsername(String username) {
        var user = userRepository.findByLastName(username);
        return userMapper.toDto(user);
    }

    public UserResponseDTO findByEmail(String email) {
        var user = userRepository.findByEmail(email);
        return userMapper.toDto(user);
    }

}
