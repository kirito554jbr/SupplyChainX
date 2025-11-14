package org.example.supplychainx.service.User;

import org.example.supplychainx.DTO.UserRequestDTO;
import org.example.supplychainx.DTO.UserResponseDTO;
import org.example.supplychainx.Mappers.UserMapper;
import org.example.supplychainx.Model.Role;
import org.example.supplychainx.Model.User;
import org.example.supplychainx.Repository.UserRepository;
import org.example.supplychainx.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        // Setup User
        user = new User();
        user.setIdUser(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.ADMIN);

        // Setup UserRequestDTO
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setIdUser(1L);
        userRequestDTO.setFirstName("John");
        userRequestDTO.setLastName("Doe");
        userRequestDTO.setEmail("john.doe@example.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setRole(Role.ADMIN);

        // Setup UserResponseDTO
        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setIdUser(1L);
        userResponseDTO.setFirstName("John");
        userResponseDTO.setLastName("Doe");
        userResponseDTO.setEmail("john.doe@example.com");
        userResponseDTO.setRole(Role.ADMIN);
    }

    // Tests for getUserById
    @Test
    void testGetUserById_Success() throws ClassNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdUser());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testGetUserById_NotFound() throws ClassNotFoundException {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserResponseDTO result = userService.getUserById(999L);

        assertNull(result);
        verify(userRepository, times(1)).findById(999L);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void testGetUserById_Lambda_MapOperation() throws ClassNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testGetUserById_DifferentRole() throws ClassNotFoundException {
        user.setRole(Role.GESTIONNAIRE_APPROVISIONNEMENT);
        userResponseDTO.setRole(Role.GESTIONNAIRE_APPROVISIONNEMENT);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(Role.GESTIONNAIRE_APPROVISIONNEMENT, result.getRole());
        verify(userRepository, times(1)).findById(1L);
    }

    // Tests for getAllUsers
    @Test
    void testGetAllUsers_Success() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testGetAllUsers_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void testGetAllUsers_MultipleUsers_LambdaMapping() {
        User user2 = new User();
        user2.setIdUser(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setRole(Role.RESPONSABLE_ACHATS);

        UserResponseDTO userResponseDTO2 = new UserResponseDTO();
        userResponseDTO2.setIdUser(2L);
        userResponseDTO2.setFirstName("Jane");
        userResponseDTO2.setLastName("Smith");
        userResponseDTO2.setEmail("jane.smith@example.com");
        userResponseDTO2.setRole(Role.RESPONSABLE_ACHATS);

        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);
        when(userMapper.toDto(user2)).thenReturn(userResponseDTO2);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals(Role.ADMIN, result.get(0).getRole());
        assertEquals(Role.RESPONSABLE_ACHATS, result.get(1).getRole());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void testGetAllUsers_DifferentRoles_LambdaMapping() {
        User user2 = new User();
        user2.setIdUser(2L);
        user2.setRole(Role.CHEF_PRODUCTION);

        User user3 = new User();
        user3.setIdUser(3L);
        user3.setRole(Role.SUPERVISEUR_LOGISTIQUE);

        UserResponseDTO responseDTO2 = new UserResponseDTO();
        responseDTO2.setIdUser(2L);
        responseDTO2.setRole(Role.CHEF_PRODUCTION);

        UserResponseDTO responseDTO3 = new UserResponseDTO();
        responseDTO3.setIdUser(3L);
        responseDTO3.setRole(Role.SUPERVISEUR_LOGISTIQUE);

        List<User> users = Arrays.asList(user, user2, user3);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);
        when(userMapper.toDto(user2)).thenReturn(responseDTO2);
        when(userMapper.toDto(user3)).thenReturn(responseDTO3);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Role.ADMIN, result.get(0).getRole());
        assertEquals(Role.CHEF_PRODUCTION, result.get(1).getRole());
        assertEquals(Role.SUPERVISEUR_LOGISTIQUE, result.get(2).getRole());
        verify(userMapper, times(3)).toDto(any(User.class));
    }

    // Tests for createUser
    @Test
    void testCreateUser_Success() {
        when(userMapper.toEntityRequest(userRequestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdUser());
        assertEquals("John", result.getFirstName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());
        verify(userMapper, times(1)).toEntityRequest(userRequestDTO);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testCreateUser_WithDifferentRole() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setFirstName("Alice");
        requestDTO.setLastName("Johnson");
        requestDTO.setEmail("alice@example.com");
        requestDTO.setPassword("pass123");
        requestDTO.setRole(Role.PLANIFICATEUR);

        User newUser = new User();
        newUser.setFirstName("Alice");
        newUser.setLastName("Johnson");
        newUser.setEmail("alice@example.com");
        newUser.setRole(Role.PLANIFICATEUR);

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setFirstName("Alice");
        responseDTO.setLastName("Johnson");
        responseDTO.setEmail("alice@example.com");
        responseDTO.setRole(Role.PLANIFICATEUR);

        when(userMapper.toEntityRequest(requestDTO)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(userMapper.toDto(newUser)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(requestDTO);

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals(Role.PLANIFICATEUR, result.getRole());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testCreateUser_VarKeywordUsage() {
        when(userMapper.toEntityRequest(userRequestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertNotNull(result);
        verify(userMapper, times(1)).toEntityRequest(userRequestDTO);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    // Tests for updateUser
    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toEntityRequest(userRequestDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.updateUser(1L, userRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdUser());
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserResponseDTO result = userService.updateUser(999L, userRequestDTO);

        assertNull(result);
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void testUpdateUser_IdPreserved() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toEntityRequest(userRequestDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            assertEquals(1L, saved.getIdUser());
            return saved;
        });
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.updateUser(1L, userRequestDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_RoleChanged() {
        UserRequestDTO updatedRequestDTO = new UserRequestDTO();
        updatedRequestDTO.setIdUser(1L);
        updatedRequestDTO.setFirstName("John");
        updatedRequestDTO.setLastName("Doe");
        updatedRequestDTO.setEmail("john.doe@example.com");
        updatedRequestDTO.setPassword("newpass");
        updatedRequestDTO.setRole(Role.RESPONSABLE_LOGISTIQUE);

        User updatedUser = new User();
        updatedUser.setIdUser(1L);
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Doe");
        updatedUser.setEmail("john.doe@example.com");
        updatedUser.setRole(Role.RESPONSABLE_LOGISTIQUE);

        UserResponseDTO updatedResponseDTO = new UserResponseDTO();
        updatedResponseDTO.setIdUser(1L);
        updatedResponseDTO.setFirstName("John");
        updatedResponseDTO.setLastName("Doe");
        updatedResponseDTO.setEmail("john.doe@example.com");
        updatedResponseDTO.setRole(Role.RESPONSABLE_LOGISTIQUE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toEntityRequest(updatedRequestDTO)).thenReturn(updatedUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(updatedResponseDTO);

        UserResponseDTO result = userService.updateUser(1L, updatedRequestDTO);

        assertNotNull(result);
        assertEquals(Role.RESPONSABLE_LOGISTIQUE, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_EmailChanged() {
        UserRequestDTO updatedRequestDTO = new UserRequestDTO();
        updatedRequestDTO.setIdUser(1L);
        updatedRequestDTO.setFirstName("John");
        updatedRequestDTO.setLastName("Doe");
        updatedRequestDTO.setEmail("john.newemail@example.com");
        updatedRequestDTO.setPassword("password123");
        updatedRequestDTO.setRole(Role.ADMIN);

        User updatedUser = new User();
        updatedUser.setIdUser(1L);
        updatedUser.setEmail("john.newemail@example.com");

        UserResponseDTO updatedResponseDTO = new UserResponseDTO();
        updatedResponseDTO.setIdUser(1L);
        updatedResponseDTO.setEmail("john.newemail@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toEntityRequest(updatedRequestDTO)).thenReturn(updatedUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(updatedResponseDTO);

        UserResponseDTO result = userService.updateUser(1L, updatedRequestDTO);

        assertNotNull(result);
        assertEquals("john.newemail@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Tests for deleteUser
    @Test
    void testDeleteUser_Success() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NonExistentId() {
        doNothing().when(userRepository).deleteById(999L);

        userService.deleteUser(999L);

        verify(userRepository, times(1)).deleteById(999L);
    }

    @Test
    void testDeleteUser_MultipleDeletes() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(1L);
        userService.deleteUser(2L);
        userService.deleteUser(3L);

        verify(userRepository, times(1)).deleteById(1L);
        verify(userRepository, times(1)).deleteById(2L);
        verify(userRepository, times(1)).deleteById(3L);
        verify(userRepository, times(3)).deleteById(anyLong());
    }

    // Tests for findByUsername
    @Test
    void testFindByUsername_Success() {
        when(userRepository.findByLastName("Doe")).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.findByUsername("Doe");

        assertNotNull(result);
        assertEquals("Doe", result.getLastName());
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).findByLastName("Doe");
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testFindByUsername_NotFound() {
        when(userRepository.findByLastName("NonExistent")).thenReturn(null);
        when(userMapper.toDto(null)).thenReturn(null);

        UserResponseDTO result = userService.findByUsername("NonExistent");

        assertNull(result);
        verify(userRepository, times(1)).findByLastName("NonExistent");
    }

    @Test
    void testFindByUsername_DifferentLastNames() {
        User smithUser = new User();
        smithUser.setIdUser(2L);
        smithUser.setFirstName("Jane");
        smithUser.setLastName("Smith");
        smithUser.setEmail("jane.smith@example.com");
        smithUser.setRole(Role.GESTIONNAIRE_COMMERCIAL);

        UserResponseDTO smithResponseDTO = new UserResponseDTO();
        smithResponseDTO.setIdUser(2L);
        smithResponseDTO.setFirstName("Jane");
        smithResponseDTO.setLastName("Smith");
        smithResponseDTO.setEmail("jane.smith@example.com");
        smithResponseDTO.setRole(Role.GESTIONNAIRE_COMMERCIAL);

        when(userRepository.findByLastName("Smith")).thenReturn(smithUser);
        when(userMapper.toDto(smithUser)).thenReturn(smithResponseDTO);

        UserResponseDTO result = userService.findByUsername("Smith");

        assertNotNull(result);
        assertEquals("Smith", result.getLastName());
        assertEquals("Jane", result.getFirstName());
        verify(userRepository, times(1)).findByLastName("Smith");
    }

    // Tests for findByEmail
    @Test
    void testFindByEmail_Success() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.findByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);
        when(userMapper.toDto(null)).thenReturn(null);

        UserResponseDTO result = userService.findByEmail("nonexistent@example.com");

        assertNull(result);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void testFindByEmail_DifferentEmails() {
        User anotherUser = new User();
        anotherUser.setIdUser(3L);
        anotherUser.setFirstName("Bob");
        anotherUser.setLastName("Wilson");
        anotherUser.setEmail("bob.wilson@example.com");
        anotherUser.setRole(Role.SUPERVISEUR_PRODUCTION);

        UserResponseDTO anotherResponseDTO = new UserResponseDTO();
        anotherResponseDTO.setIdUser(3L);
        anotherResponseDTO.setFirstName("Bob");
        anotherResponseDTO.setLastName("Wilson");
        anotherResponseDTO.setEmail("bob.wilson@example.com");
        anotherResponseDTO.setRole(Role.SUPERVISEUR_PRODUCTION);

        when(userRepository.findByEmail("bob.wilson@example.com")).thenReturn(anotherUser);
        when(userMapper.toDto(anotherUser)).thenReturn(anotherResponseDTO);

        UserResponseDTO result = userService.findByEmail("bob.wilson@example.com");

        assertNotNull(result);
        assertEquals("bob.wilson@example.com", result.getEmail());
        assertEquals("Bob", result.getFirstName());
        assertEquals(Role.SUPERVISEUR_PRODUCTION, result.getRole());
        verify(userRepository, times(1)).findByEmail("bob.wilson@example.com");
    }

    // Edge case tests
    @Test
    void testGetAllUsers_StreamMapping_PreservesOrder() {
        User user1 = new User();
        user1.setIdUser(1L);
        user1.setFirstName("Alice");

        User user2 = new User();
        user2.setIdUser(2L);
        user2.setFirstName("Bob");

        User user3 = new User();
        user3.setIdUser(3L);
        user3.setFirstName("Charlie");

        UserResponseDTO response1 = new UserResponseDTO();
        response1.setIdUser(1L);
        response1.setFirstName("Alice");

        UserResponseDTO response2 = new UserResponseDTO();
        response2.setIdUser(2L);
        response2.setFirstName("Bob");

        UserResponseDTO response3 = new UserResponseDTO();
        response3.setIdUser(3L);
        response3.setFirstName("Charlie");

        List<User> users = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user1)).thenReturn(response1);
        when(userMapper.toDto(user2)).thenReturn(response2);
        when(userMapper.toDto(user3)).thenReturn(response3);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        assertEquals("Bob", result.get(1).getFirstName());
        assertEquals("Charlie", result.get(2).getFirstName());
        assertEquals(1L, result.get(0).getIdUser());
        assertEquals(2L, result.get(1).getIdUser());
        assertEquals(3L, result.get(2).getIdUser());
    }

    @Test
    void testCreateUser_AllRoles() {
        Role[] allRoles = {
            Role.ADMIN,
            Role.GESTIONNAIRE_APPROVISIONNEMENT,
            Role.RESPONSABLE_ACHATS,
            Role.SUPERVISEUR_LOGISTIQUE,
            Role.CHEF_PRODUCTION
        };

        for (Role role : allRoles) {
            UserRequestDTO request = new UserRequestDTO();
            request.setRole(role);

            User userEntity = new User();
            userEntity.setRole(role);

            UserResponseDTO response = new UserResponseDTO();
            response.setRole(role);

            when(userMapper.toEntityRequest(request)).thenReturn(userEntity);
            when(userRepository.save(userEntity)).thenReturn(userEntity);
            when(userMapper.toDto(userEntity)).thenReturn(response);

            UserResponseDTO result = userService.createUser(request);

            assertNotNull(result);
            assertEquals(role, result.getRole());
        }

        verify(userRepository, times(allRoles.length)).save(any(User.class));
    }

    @Test
    void testGetUserById_OrElseNull_Lambda() throws ClassNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_EmptyOptional_ReturnsNull() throws ClassNotFoundException {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserResponseDTO result = userService.getUserById(999L);

        assertNull(result);
        verify(userMapper, never()).toDto(any());
    }
}
