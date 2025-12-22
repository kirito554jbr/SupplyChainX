package org.example.supplychainx.Controller.Auth;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.AuthResponse;
import org.example.supplychainx.DTO.LoginRequest;
import org.example.supplychainx.DTO.RefreshTokenRequest;
import org.example.supplychainx.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    /**
     * Endpoint de connexion - Authentification par email et mot de passe
     * Retourne un Access Token et un Refresh Token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Endpoint de renouvellement du token
     * Utilise le Refresh Token pour obtenir un nouveau Access Token
     * Applique la rotation du Refresh Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Endpoint de déconnexion
     * Révoque le Refresh Token de l'utilisateur
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return buildErrorResponse(HttpStatus.UNAUTHORIZED, "User not authenticated");
            }
            authService.logout(userDetails.getUsername());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout successful");
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint pour vérifier la validité du token actuel
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("email", userDetails.getUsername());
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Méthode utilitaire pour construire les réponses d'erreur
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);

        return ResponseEntity.status(status).body(errorResponse);
    }
}

