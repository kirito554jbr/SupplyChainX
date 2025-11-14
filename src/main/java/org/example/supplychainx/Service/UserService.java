package org.example.supplychainx.Service;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.UserRequestDTO;
import org.example.supplychainx.DTO.UserResponseDTO;
import org.example.supplychainx.Mappers.UserMapper;
import org.example.supplychainx.Model.User;
import org.example.supplychainx.Repository.UserRepository;
import org.example.supplychainx.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserResponseDTO getUserById(Long id) throws ClassNotFoundException {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        User user = userMapper.toEntityRequest(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserResponseDTO updateUser(Long id ,UserRequestDTO userDTO) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            User userToUpdate = userMapper.toEntityRequest(userDTO);
            userToUpdate.setIdUser(existingUser.getIdUser());
            User updatedUser = userRepository.save(userToUpdate);
            return userMapper.toDto(updatedUser);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponseDTO findByUsername(String username) {
        User user = userRepository.findByLastName(username);
        return userMapper.toDto(user);
    }

    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return userMapper.toDto(user);
    }
}
