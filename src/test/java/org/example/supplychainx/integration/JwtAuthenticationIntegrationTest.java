package org.example.supplychainx.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.supplychainx.DTO.AuthResponse;
import org.example.supplychainx.DTO.LoginRequest;
import org.example.supplychainx.DTO.RefreshTokenRequest;
import org.example.supplychainx.Model.Role;
import org.example.supplychainx.Model.User;
import org.example.supplychainx.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests d'intégration pour l'authentification JWT
 */
@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private String testEmail = "test@supplychainx.com";
    private String testPassword = "password123";

    @BeforeEach
    void setUp() {
        // Nettoyer la base de données
        userRepository.deleteAll();

        // Créer un utilisateur de test
        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail(testEmail);
        testUser.setPassword(passwordEncoder.encode(testPassword));
        testUser.setRole(Role.ADMIN);
        testUser.setEnabled(true);
        userRepository.save(testUser);
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value(testEmail))
                .andExpect(jsonPath("$.user.role").value("ADMIN"))
                .andReturn();

        // Vérifier que le refresh token est stocké en base
        User user = userRepository.findByEmail(testEmail);
        assertThat(user.getRefreshToken()).isNotNull();
        assertThat(user.getRefreshTokenExpiry()).isNotNull();
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest(testEmail, "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testLoginWithDisabledAccount() throws Exception {
        // Désactiver le compte
        testUser.setEnabled(false);
        userRepository.save(testUser);

        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("User account is disabled"));
    }

    @Test
    void testRefreshToken() throws Exception {
        // D'abord, se connecter pour obtenir les tokens
        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(loginResponse, AuthResponse.class);
        String refreshToken = authResponse.getRefreshToken();

        // Ensuite, utiliser le refresh token pour obtenir un nouveau access token
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest(refreshToken);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));

        // Vérifier que le refresh token a été rotaté (changé)
        User user = userRepository.findByEmail(testEmail);
        assertThat(user.getRefreshToken()).isNotEqualTo(refreshToken);
    }

    @Test
    void testRefreshTokenWithInvalidToken() throws Exception {
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest("invalid.refresh.token");

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testAccessProtectedEndpointWithValidToken() throws Exception {
        // Se connecter pour obtenir un access token
        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(loginResponse, AuthResponse.class);
        String accessToken = authResponse.getAccessToken();

        // Accéder à un endpoint protégé
        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.email").value(testEmail));
    }

    @Test
    void testAccessProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/auth/validate"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccessProtectedEndpointWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogout() throws Exception {
        // Se connecter pour obtenir un access token
        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(loginResponse, AuthResponse.class);
        String accessToken = authResponse.getAccessToken();

        // Se déconnecter
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));

        // Vérifier que le refresh token a été révoqué
        User user = userRepository.findByEmail(testEmail);
        assertThat(user.getRefreshToken()).isNull();
        assertThat(user.getRefreshTokenExpiry()).isNull();
    }

    @Test
    void testValidateTokenReturnsUserInfo() throws Exception {
        // Se connecter
        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(loginResponse, AuthResponse.class);
        String accessToken = authResponse.getAccessToken();

        // Valider le token
        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.email").value(testEmail))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}

