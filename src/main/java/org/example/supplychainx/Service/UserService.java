package org.example.supplychainx.Service;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.UserRequestDTO;
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

    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        var user = userMapper.toEntityRequest(userDTO);
        var savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserResponseDTO updateUser(Long id ,UserRequestDTO userDTO) {
        var existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            var userToUpdate = userMapper.toEntityRequest(userDTO);
            userToUpdate.setIdUser(userDTO.getIdUser());
            var updatedUser = userRepository.save(userToUpdate);
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
