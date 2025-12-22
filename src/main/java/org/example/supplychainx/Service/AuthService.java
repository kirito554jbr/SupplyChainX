package org.example.supplychainx.Service;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.AuthResponse;
import org.example.supplychainx.DTO.LoginRequest;
import org.example.supplychainx.DTO.UserInfoDTO;
import org.example.supplychainx.Model.User;
import org.example.supplychainx.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            // Authentifier l'utilisateur
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (DisabledException e) {
            throw new RuntimeException("User account is disabled");
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }

        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled");
        }

        // Générer les tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Sauvegarder le refresh token avec rotation
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(LocalDateTime.now().plusSeconds(jwtService.getRefreshTokenExpiration() / 1000));
        userRepository.save(user);

        // Créer le DTO utilisateur
        UserInfoDTO userInfo = new UserInfoDTO(
                user.getIdUser(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name()
        );

        // Retourner la réponse avec les tokens
        return new AuthResponse(
                accessToken,
                refreshToken,
                System.currentTimeMillis() + jwtService.getAccessTokenExpiration(),
                userInfo
        );
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        try {
            // Extraire l'email du refresh token
            String email = jwtService.extractEmail(refreshToken);

            // Récupérer l'utilisateur
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            // Vérifier que le refresh token correspond
            if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
                throw new RuntimeException("Invalid refresh token");
            }

            // Vérifier l'expiration du refresh token
            if (user.getRefreshTokenExpiry() == null || user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
                // Nettoyer le token expiré
                user.setRefreshToken(null);
                user.setRefreshTokenExpiry(null);
                userRepository.save(user);
                throw new RuntimeException("Refresh token expired");
            }

            // Vérifier que l'utilisateur est actif
            if (!user.isEnabled()) {
                throw new RuntimeException("User account is disabled");
            }

            // Générer de nouveaux tokens (rotation du refresh token)
            String newAccessToken = jwtService.generateAccessToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);

            // Sauvegarder le nouveau refresh token
            user.setRefreshToken(newRefreshToken);
            user.setRefreshTokenExpiry(LocalDateTime.now().plusSeconds(jwtService.getRefreshTokenExpiration() / 1000));
            userRepository.save(user);

            // Créer le DTO utilisateur
            UserInfoDTO userInfo = new UserInfoDTO(
                    user.getIdUser(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole().name()
            );

            // Retourner la réponse avec les nouveaux tokens
            return new AuthResponse(
                    newAccessToken,
                    newRefreshToken,
                    System.currentTimeMillis() + jwtService.getAccessTokenExpiration(),
                    userInfo
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh token: " + e.getMessage());
        }
    }

    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Révoquer le refresh token
        user.setRefreshToken(null);
        user.setRefreshTokenExpiry(null);
        userRepository.save(user);
    }
}

