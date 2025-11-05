package org.example.supplychainx.Service;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.UserResponseDTO;
import org.example.supplychainx.Mappers.UserMapper;
import org.example.supplychainx.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

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

    public UserResponseDTO createUser(UserResponseDTO userDTO) {
        var user = userMapper.toEntity(userDTO);
        var savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserResponseDTO updateUser(Long id ,UserResponseDTO userDTO) {
        var existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            var userToUpdate = userMapper.toEntity(userDTO);
            userToUpdate.setIdUser(userDTO.getIdUser());
            var updatedUser = userRepository.save(userToUpdate);
            return userMapper.toDto(updatedUser);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
