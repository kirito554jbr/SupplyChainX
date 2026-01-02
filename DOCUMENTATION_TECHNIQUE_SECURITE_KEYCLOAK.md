# Documentation Technique - Sécurité SupplyChainX avec Keycloak

## 1. Contexte du Projet

### 1.1 Contexte et Enjeux de Sécurité

Dans le cadre du développement de l'application SupplyChainX, la sécurisation des API constitue un enjeu stratégique afin de garantir la confidentialité, l'intégrité et la disponibilité des données échangées entre les différents composants du système.

L'API manipule des informations sensibles relatives notamment à :
- **L'approvisionnement** et la gestion des relations fournisseurs
- **La production** et la planification des ressources
- **La livraison** et le suivi des produits finis

Une solution de sécurité robuste est donc indispensable pour assurer la fiabilité des échanges et prévenir tout accès non autorisé.

### 1.2 Objectifs de Sécurité

La solution de sécurité mise en place doit permettre :
- ✅ Une **authentification centralisée**, sécurisée et fiable des utilisateurs via Keycloak
- ✅ Une **gestion des autorisations** basée sur les rôles métier (RBAC) définis dans Keycloak
- ✅ La **protection de l'ensemble des API REST** sensibles à l'aide de tokens OAuth 2.0
- ✅ Une **traçabilité complète** des accès et des actions effectuées par les utilisateurs

### 1.3 Principes Généraux

La sécurité de l'application repose sur les principes suivants :

1. **Architecture stateless**, sans gestion de session côté application
2. **Délégation complète** de l'authentification à Keycloak, en tant que serveur d'autorisation centralisé
3. **Toute requête** vers une ressource protégée doit inclure un Access Token JWT valide
4. **Séparation stricte** entre :
   - L'authentification (assurée par Keycloak)
   - L'autorisation (contrôle des rôles côté application)
5. **Support des standards** OAuth 2.0 et OpenID Connect (OIDC)

---

## 2. Architecture Keycloak

### 2.1 Composants Principaux

| Composant | Description | Utilisation dans SupplyChainX |
|-----------|-------------|-------------------------------|
| **Realm** | Espace isolé dédié à la gestion des utilisateurs, rôles, clients et paramètres de sécurité | Un realm spécifique `supplychainx` est créé |
| **Clients** | Applications déclarées dans Keycloak (API backend) | `supplychainx-client` (Backend API) |
| **Utilisateurs** | Comptes utilisateurs gérés de manière centralisée | Gestion des employés et administrateurs |
| **Rôles** | Rôles métier définis au niveau du realm ou des clients | 10 rôles métier définis (voir section 3) |

### 2.2 Architecture Technique

```
┌─────────────────┐         ┌──────────────────┐         ┌─────────────────┐
│                 │         │                  │         │                 │
│  Client Web/    │ ──────> │    Keycloak      │ <────── │   PostgreSQL    │
│  Mobile App     │  Auth   │  (OAuth2/OIDC)   │  Users  │  (User Store)   │
│                 │         │                  │         │                 │
└─────────────────┘         └──────────────────┘         └─────────────────┘
        │                           │
        │  JWT Access Token         │
        │                           │
        v                           │
┌─────────────────┐                 │
│                 │                 │
│  Spring Boot    │ <───────────────┘
│  Application    │   Token Validation
│  (Resource      │   (JWK Set URI)
│   Server)       │
│                 │
└─────────────────┘
        │
        v
┌─────────────────┐
│                 │
│   MySQL DB      │
│  (Business      │
│   Data)         │
│                 │
└─────────────────┘
```

---

## 3. Authentification via Keycloak

### 3.1 Flux d'Authentification (Authorization Code Flow)

Le flux standard OAuth 2.0 / OpenID Connect est orchestré par Keycloak :

```
┌──────────┐                                           ┌──────────────┐
│          │  1. Accès à l'application                 │              │
│  Client  │ ─────────────────────────────────────────>│  Frontend    │
│          │                                           │              │
└──────────┘                                           └──────────────┘
     │                                                        │
     │  2. Redirection vers Keycloak                         │
     │ <──────────────────────────────────────────────────────
     │
     │  3. Saisie identifiants (email/password)
     v
┌──────────────┐
│              │
│   Keycloak   │
│              │
└──────────────┘
     │
     │  4. Validation et génération code d'autorisation
     v
┌──────────────┐
│              │  5. Échange code contre tokens
│   Frontend   │ ──────────────────────────────────────> Keycloak
│              │ <──────────────────────────────────────
└──────────────┘  6. Access Token + Refresh Token
     │
     │  7. Requête API avec Access Token
     v
┌──────────────┐
│              │  Authorization: Bearer <token>
│   Backend    │ <───────────────────────────────────
│   API        │
│              │
└──────────────┘
```

### 3.2 Étapes Détaillées

1. **L'utilisateur accède** à l'application SupplyChainX
2. **Il est redirigé** vers la page d'authentification Keycloak
3. **L'utilisateur saisit** ses identifiants (email / mot de passe)
4. **Keycloak valide** les informations et génère un code d'autorisation
5. **L'application échange** ce code contre un Access Token et un Refresh Token
6. **L'Access Token est utilisé** pour accéder aux API protégées

### 3.3 Flux Alternatif : Client Credentials Flow

Pour l'authentification des services backend sans utilisateur :

```
┌──────────────┐
│              │  1. Requête token avec client credentials
│   Service    │ ─────────────────────────────────────────> Keycloak
│   Backend    │                                            (client_id, client_secret)
│              │ <─────────────────────────────────────────
└──────────────┘  2. Access Token
```

**Utilisé pour :**
- Communications inter-services
- Tâches planifiées (batch jobs)
- Intégrations système

---

## 4. Access Token (JWT)

### 4.1 Caractéristiques

| Propriété | Valeur |
|-----------|--------|
| **Format** | JWT (JSON Web Token) |
| **Algorithme** | RS256 (RSA avec SHA-256) |
| **Durée de vie** | 15 minutes (configurable) |
| **Transmission** | En-tête HTTP `Authorization: Bearer <token>` |
| **Signature** | Clé privée RSA de Keycloak |

### 4.2 Structure du JWT

#### Header
```json
{
  "alg": "RS256",
  "typ": "JWT",
  "kid": "FJ86GcF3jTbNLOco4NvZkUCIUmfYCqoqtOQeMfbhNlE"
}
```

#### Payload (Claims)
```json
{
  "exp": 1704290700,
  "iat": 1704289800,
  "jti": "a8f2b3c4-d5e6-7f8g-9h0i-1j2k3l4m5n6o",
  "iss": "http://localhost:9090/realms/supplychainx",
  "aud": "account",
  "sub": "f8e7d6c5-b4a3-9876-5432-1234567890ab",
  "typ": "Bearer",
  "azp": "supplychainx-client",
  "session_state": "abc123-def456-ghi789",
  "acr": "1",
  "realm_access": {
    "roles": [
      "ADMIN",
      "default-roles-supplychainx"
    ]
  },
  "resource_access": {
    "supplychainx-client": {
      "roles": ["api-access"]
    },
    "account": {
      "roles": ["manage-account", "view-profile"]
    }
  },
  "scope": "openid profile email",
  "sid": "abc123-def456-ghi789",
  "email_verified": true,
  "name": "Admin User",
  "preferred_username": "admin",
  "given_name": "Admin",
  "family_name": "User",
  "email": "admin@supplychainx.com"
}
```

#### Signature
```
RSASHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  keycloak_private_key
)
```

### 4.3 Claims Standards

| Claim | Description | Exemple |
|-------|-------------|---------|
| `sub` | Identifiant unique de l'utilisateur | `f8e7d6c5-b4a3-9876...` |
| `iss` | Émetteur du token (Keycloak) | `http://localhost:9090/realms/supplychainx` |
| `iat` | Date d'émission (timestamp Unix) | `1704289800` |
| `exp` | Date d'expiration (timestamp Unix) | `1704290700` |
| `azp` | Client autorisé (Authorized Party) | `supplychainx-client` |
| `preferred_username` | Nom d'utilisateur | `admin` |
| `email` | Adresse email | `admin@supplychainx.com` |

### 4.4 Claims Personnalisés - Rôles

```json
{
  "realm_access": {
    "roles": [
      "ADMIN",
      "GESTIONNAIRE_APPROVISIONNEMENT"
    ]
  },
  "resource_access": {
    "supplychainx-client": {
      "roles": ["api-access"]
    }
  }
}
```

### 4.5 Transmission du Token

**En-tête HTTP obligatoire :**
```http
GET /api/v1/orders HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MDQyOTA3MDA...
Content-Type: application/json
```

### 4.6 Validation côté Application

L'application Spring Boot valide automatiquement le token via Spring Security OAuth2 Resource Server :

1. ✅ **Vérification de la signature JWT** à l'aide de la clé publique Keycloak (JWK Set)
2. ✅ **Validation de la date d'expiration** (`exp` claim)
3. ✅ **Validation de l'émetteur** (`iss` claim)
4. ✅ **Extraction des rôles** depuis `realm_access.roles`
5. ✅ **Vérification optionnelle** de l'état du compte utilisateur

**Configuration dans `application.properties` :**
```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9090/realms/supplychainx
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:9090/realms/supplychainx/protocol/openid-connect/certs
```

---

## 5. Refresh Token

### 5.1 Caractéristiques

| Propriété | Valeur |
|-----------|--------|
| **Format** | Token opaque (non JWT) |
| **Durée de vie** | 7 jours (configurable) |
| **Usage** | Renouvellement de l'Access Token |
| **Stockage** | Cookie HttpOnly (web) / Secure Storage (mobile) |
| **Rotation** | Automatique activée |

### 5.2 Flux de Renouvellement

```
┌──────────────┐
│              │  1. Access Token expiré
│   Client     │  2. Requête nouveau token avec Refresh Token
│              │ ─────────────────────────────────────────> Keycloak
└──────────────┘                                            POST /token
                                                            grant_type=refresh_token
                                                            refresh_token=<token>
     │
     │ <─────────────────────────────────────────
     │  3. Nouveau Access Token + Nouveau Refresh Token
     v
┌──────────────┐
│              │  4. Requête API avec nouveau Access Token
│   Backend    │ <───────────────────────────────────
│   API        │
│              │
└──────────────┘
```

### 5.3 Requête de Renouvellement

```http
POST /realms/supplychainx/protocol/openid-connect/token HTTP/1.1
Host: localhost:9090
Content-Type: application/x-www-form-urlencoded

client_id=supplychainx-client&
client_secret=<client-secret>&
grant_type=refresh_token&
refresh_token=<refresh-token>
```

### 5.4 Réponse

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 900,
  "refresh_expires_in": 604800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldU...",
  "token_type": "Bearer"
}
```

### 5.5 Rotation Automatique

- ✅ **Chaque renouvellement** génère un nouveau Refresh Token
- ✅ **L'ancien Refresh Token** est immédiatement invalidé
- ✅ **Protection contre** les attaques par rejeu (replay attacks)

---

## 6. Autorisation Basée sur les Rôles (RBAC)

### 6.1 Rôles Métier Définis dans Keycloak

Les rôles suivants sont gérés au niveau du realm `supplychainx` :

| Rôle | Description | Périmètre d'Accès |
|------|-------------|-------------------|
| **ADMIN** | Administrateur système | ✅ Accès complet à toutes les ressources |
| **GESTIONNAIRE_APPROVISIONNEMENT** | Gestionnaire approvisionnement | ✅ Gestion des matières premières et fournisseurs |
| **RESPONSABLE_ACHATS** | Responsable achats | ✅ Validation et gestion des commandes fournisseurs |
| **SUPERVISEUR_LOGISTIQUE** | Superviseur logistique | ✅ Vue globale sur les flux logistiques |
| **CHEF_PRODUCTION** | Chef de production | ✅ Gestion complète de la production |
| **PLANIFICATEUR** | Planificateur production | ✅ Planification et ordonnancement |
| **SUPERVISEUR_PRODUCTION** | Superviseur production | ✅ Supervision atelier et équipes |
| **GESTIONNAIRE_COMMERCIAL** | Gestionnaire commercial | ✅ Gestion clients et commandes |
| **RESPONSABLE_LOGISTIQUE** | Responsable logistique | ✅ Stratégie et optimisation logistique |
| **SUPERVISEUR_LIVRAISONS** | Superviseur livraisons | ✅ Gestion des expéditions |

### 6.2 Contrôle des Autorisations

#### Extraction des Rôles depuis le JWT

**Implémentation Java :**
```java
@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        String username = jwt.getClaimAsString("preferred_username");
        return new JwtAuthenticationToken(jwt, authorities, username);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Collection<String> roles = new ArrayList<>();
        
        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?>) {
            @SuppressWarnings("unchecked")
            Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
            roles = realmRoles;
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }
}
```

#### Vérification des Rôles Requis

**Option 1: Annotations `@PreAuthorize`**
```java
@RestController
@RequestMapping("/api/v1/approvisionnement")
public class ApprovisionnementController {

    @GetMapping("/fournisseurs")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTIONNAIRE_APPROVISIONNEMENT', 'RESPONSABLE_ACHATS')")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        // Logique métier
    }

    @PostMapping("/commandes")
    @PreAuthorize("hasRole('RESPONSABLE_ACHATS')")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        // Logique métier
    }
}
```

**Option 2: Annotation Personnalisée `@RequiresRole`**
```java
@GetMapping("/production/orders")
@RequiresRole({"ADMIN", "CHEF_PRODUCTION", "SUPERVISEUR_PRODUCTION"})
public ResponseEntity<List<ProductionOrder>> getProductionOrders() {
    // Logique métier
}
```

**Option 3: Vérification Programmatique**
```java
public void performSensitiveOperation() {
    if (KeycloakSecurityUtils.hasRole(Role.ADMIN)) {
        // Opération réservée aux administrateurs
    } else {
        throw new AccessDeniedException("Accès refusé");
    }
}
```

### 6.3 Règles d'Accès par Domaine

#### Approvisionnement
```java
@PreAuthorize("hasAnyRole('ADMIN', 'GESTIONNAIRE_APPROVISIONNEMENT', 'RESPONSABLE_ACHATS')")
```

#### Production
```java
@PreAuthorize("hasAnyRole('ADMIN', 'CHEF_PRODUCTION', 'PLANIFICATEUR', 'SUPERVISEUR_PRODUCTION')")
```

#### Livraisons
```java
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_LOGISTIQUE', 'SUPERVISEUR_LOGISTIQUE', 'SUPERVISEUR_LIVRAISONS')")
```

#### Administration
```java
@PreAuthorize("hasRole('ADMIN')")
```

### 6.4 Matrice des Autorisations

| Endpoint | ADMIN | APPRO | ACHATS | PROD | PLAN | LOG | COMM |
|----------|-------|-------|--------|------|------|-----|------|
| GET /api/v1/users | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ |
| GET /api/v1/suppliers | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| POST /api/v1/orders/supply | ✅ | ❌ | ✅ | ❌ | ❌ | ❌ | ❌ |
| GET /api/v1/products | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| POST /api/v1/production/orders | ✅ | ❌ | ❌ | ✅ | ✅ | ❌ | ❌ |
| GET /api/v1/deliveries | ✅ | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ |

---

## 7. Configuration Spring Security

### 7.1 SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints publics
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                // Tous les autres endpoints nécessitent une authentification
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)
                )
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
            );
        
        return http.build();
    }
}
```

### 7.2 Application Properties

```properties
# Keycloak OAuth2 Configuration
spring.security.oauth2.client.registration.keycloak.client-id=supplychainx-client
spring.security.oauth2.client.registration.keycloak.client-secret=${KEYCLOAK_CLIENT_SECRET}
spring.security.oauth2.client.registration.keycloak.scope=openid,profile,email,roles
spring.security.oauth2.client.registration.keycloak.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.keycloak.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}

spring.security.oauth2.client.provider.keycloak.issuer-uri=${KEYCLOAK_ISSUER_URI:http://localhost:9090/realms/supplychainx}
spring.security.oauth2.client.provider.keycloak.user-name-attribute=preferred_username

spring.security.oauth2.resourceserver.jwt.issuer-uri=${KEYCLOAK_ISSUER_URI:http://localhost:9090/realms/supplychainx}
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=${KEYCLOAK_JWK_SET_URI:http://localhost:9090/realms/supplychainx/protocol/openid-connect/certs}
```

---

## 8. Gestion des Erreurs

### 8.1 Codes HTTP

| Code | Signification | Cause |
|------|---------------|-------|
| **401 Unauthorized** | Non authentifié | Token absent, invalide, expiré ou signature incorrecte |
| **403 Forbidden** | Accès interdit | Utilisateur authentifié mais autorisations insuffisantes |
| **500 Internal Server Error** | Erreur serveur | Problème technique côté application |

### 8.2 Format Standard de Réponse

```json
{
  "timestamp": "2026-01-02T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Access token expiré",
  "path": "/api/v1/orders"
}
```

### 8.3 Implémentation - CustomAuthenticationEntryPoint

```java
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(401)
                .error("Unauthorized")
                .message(authException.getMessage())
                .path(request.getRequestURI())
                .build();
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
```

### 8.4 Implémentation - CustomAccessDeniedHandler

```java
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException {
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(403)
                .error("Forbidden")
                .message("Vous n'avez pas les droits nécessaires pour accéder à cette ressource")
                .path(request.getRequestURI())
                .build();
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
```

### 8.5 Exemples de Réponses d'Erreur

#### Token Absent
```json
{
  "timestamp": "2026-01-02T10:30:00.000Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/v1/products"
}
```

#### Token Expiré
```json
{
  "timestamp": "2026-01-02T10:35:00.000Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "JWT token has expired",
  "path": "/api/v1/orders"
}
```

#### Signature Invalide
```json
{
  "timestamp": "2026-01-02T10:40:00.000Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid JWT signature",
  "path": "/api/v1/suppliers"
}
```

#### Droits Insuffisants
```json
{
  "timestamp": "2026-01-02T10:45:00.000Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Vous n'avez pas les droits nécessaires pour accéder à cette ressource",
  "path": "/api/v1/users"
}
```

---

## 9. Configuration Keycloak

### 9.1 Paramètres du Realm

#### Configuration Générale

| Paramètre | Valeur | Description |
|-----------|--------|-------------|
| **Realm Name** | `supplychainx` | Nom du realm |
| **Enabled** | ✅ Oui | Realm actif |
| **User Registration** | ❌ Non | Enregistrement désactivé (gestion admin uniquement) |
| **Forgot Password** | ✅ Oui | Réinitialisation de mot de passe activée |
| **Remember Me** | ✅ Oui | Mémorisation de la session |
| **Login with Email** | ✅ Oui | Connexion par email autorisée |

#### Durées de Vie des Tokens

```yaml
# Configuration dans Keycloak Admin Console
Realm Settings > Tokens:

Access Token Lifespan: 15 minutes
Access Token Lifespan For Implicit Flow: 15 minutes
Client login timeout: 5 minutes
Login timeout: 30 minutes
Login action timeout: 5 minutes

SSO Session Idle: 30 minutes
SSO Session Max: 10 hours
SSO Session Idle Remember Me: 7 days
SSO Session Max Remember Me: 30 days

Offline Session Idle: 30 days
Offline Session Max Limited: false
Offline Session Max: 60 days

Refresh Token Max Reuse: 0 (rotation activée)
```

#### Configuration Recommandée pour Production

```properties
# Access Token
access.token.lifespan=900                    # 15 minutes
access.token.lifespan.for.implicit.flow=900  # 15 minutes

# Refresh Token
refresh.token.max.reuse=0                    # Rotation activée
sso.session.idle=1800                        # 30 minutes
sso.session.max=36000                        # 10 heures

# Offline Sessions
offline.session.idle=2592000                 # 30 jours
offline.session.max=5184000                  # 60 jours
```

### 9.2 Politiques de Mot de Passe

#### Configuration dans Keycloak

```
Realm Settings > Security Defenses > Password Policy
```

**Règles recommandées :**

| Règle | Valeur | Description |
|-------|--------|-------------|
| **Minimum Length** | 12 | Longueur minimale de 12 caractères |
| **Uppercase Characters** | 1 | Au moins 1 majuscule |
| **Lowercase Characters** | 1 | Au moins 1 minuscule |
| **Digits** | 1 | Au moins 1 chiffre |
| **Special Characters** | 1 | Au moins 1 caractère spécial |
| **Not Username** | ✅ | Le mot de passe ne peut pas être le nom d'utilisateur |
| **Not Email** | ✅ | Le mot de passe ne peut pas être l'email |
| **Expires** | 90 jours | Expiration tous les 90 jours |
| **Not Recently Used** | 5 | Ne peut pas réutiliser les 5 derniers mots de passe |
| **Hashing Algorithm** | pbkdf2-sha256 | Algorithme de hachage sécurisé |
| **Hashing Iterations** | 27500 | Nombre d'itérations pour le hachage |

### 9.3 Configuration du Client

#### Paramètres Généraux

```yaml
Client ID: supplychainx-client
Name: SupplyChainX Backend API
Description: API backend pour l'application SupplyChainX
Enabled: true
Client Protocol: openid-connect
Access Type: confidential
```

#### Capability Config

```yaml
Standard Flow Enabled: true
Implicit Flow Enabled: false
Direct Access Grants Enabled: true
Service Accounts Enabled: true
Authorization Enabled: false
```

#### Authentication Flow Overrides

```yaml
Browser Flow: browser
Direct Grant Flow: direct grant
```

#### Fine Grain OpenID Connect Configuration

```yaml
Access Token Signature Algorithm: RS256
ID Token Signature Algorithm: RS256
User Info Signed Response Algorithm: RS256
```

#### Advanced Settings

```yaml
Access Token Lifespan: 15 minutes (900 seconds)
Client Session Idle: 30 minutes
Client Session Max: 10 hours
Client Offline Session Idle: 30 days
Client Offline Session Max: 60 days
```

### 9.4 Configuration des Client Scopes

#### Scope: roles

**Mappers:**

1. **realm roles**
   ```yaml
   Name: realm roles
   Mapper Type: User Realm Role
   Token Claim Name: realm_access.roles
   Claim JSON Type: String
   Multivalued: true
   Add to ID token: true
   Add to access token: true
   Add to userinfo: true
   ```

2. **client roles**
   ```yaml
   Name: client roles
   Mapper Type: User Client Role
   Client ID: supplychainx-client
   Token Claim Name: resource_access.${client_id}.roles
   Claim JSON Type: String
   Multivalued: true
   Add to ID token: true
   Add to access token: true
   Add to userinfo: false
   ```

#### Scope: profile

**Mappers:**

1. **full name**
   ```yaml
   Name: full name
   Mapper Type: User Property
   Property: name
   Token Claim Name: name
   Claim JSON Type: String
   Add to ID token: true
   Add to access token: true
   Add to userinfo: true
   ```

2. **given name**
   ```yaml
   Name: given name
   Mapper Type: User Property
   Property: firstName
   Token Claim Name: given_name
   Claim JSON Type: String
   Add to ID token: true
   Add to access token: true
   Add to userinfo: true
   ```

3. **family name**
   ```yaml
   Name: family name
   Mapper Type: User Property
   Property: lastName
   Token Claim Name: family_name
   Claim JSON Type: String
   Add to ID token: true
   Add to access token: true
   Add to userinfo: true
   ```

---

## 10. Traçabilité et Audit

### 10.1 Événements Keycloak

#### Configuration des Événements

```
Realm Settings > Events > Event Listeners
```

**Activer :**
- ✅ `jboss-logging` - Logs dans les fichiers de log
- ✅ Custom event listeners (si nécessaire)

#### Types d'Événements Tracés

**Login Events:**
- LOGIN - Connexion réussie
- LOGIN_ERROR - Échec de connexion
- LOGOUT - Déconnexion
- REFRESH_TOKEN - Renouvellement de token
- CODE_TO_TOKEN - Échange code d'autorisation
- REGISTER - Nouvel enregistrement

**Admin Events:**
- CREATE - Création d'une ressource
- UPDATE - Mise à jour d'une ressource
- DELETE - Suppression d'une ressource
- ACTION - Action administrative

### 10.2 Configuration des Logs

```properties
# Activer les événements
Events > Config > Save Events: ON
Events > Config > Expiration: 365 days
Events > Config > Event Listeners: jboss-logging
```

### 10.3 Audit Logging côté Application

**Implémentation avec AOP :**

```java
@Aspect
@Component
@Slf4j
public class AuditLoggingAspect {

    @Around("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public Object logAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        String username = KeycloakSecurityUtils.getCurrentUsername();
        String method = joinPoint.getSignature().toShortString();
        
        log.info("User [{}] accessing method [{}]", username, method);
        
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        
        log.info("User [{}] completed method [{}] in {}ms", 
                 username, method, (endTime - startTime));
        
        return result;
    }
}
```

### 10.4 Exemple de Log

```
2026-01-02 10:30:15.123 INFO  [AuditLoggingAspect] User [admin] accessing method [ProductController.getAllProducts()]
2026-01-02 10:30:15.456 INFO  [AuditLoggingAspect] User [admin] completed method [ProductController.getAllProducts()] in 333ms
```

---

## 11. Tests

### 11.1 Tests Unitaires avec MockMvc

```java
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminCanAccessAllProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "GESTIONNAIRE_APPROVISIONNEMENT")
    void testSupplyManagerCanAccessProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk());
    }

    @Test
    void testUnauthenticatedUserCannotAccessProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }
}
```

### 11.2 Tests d'Intégration avec Keycloak

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class KeycloakIntegrationTest {

    @LocalServerPort
    private int port;

    private String getAccessToken(String username, String password) {
        // Obtenir un vrai token depuis Keycloak
        RestTemplate restTemplate = new RestTemplate();
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", "supplychainx-client");
        params.add("client_secret", clientSecret);
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);
        
        TokenResponse response = restTemplate.postForObject(
            "http://localhost:9090/realms/supplychainx/protocol/openid-connect/token",
            params,
            TokenResponse.class
        );
        
        return response.getAccessToken();
    }

    @Test
    void testRealKeycloakAuthentication() {
        String token = getAccessToken("admin", "admin123");
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/api/v1/products",
            HttpMethod.GET,
            entity,
            String.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

---

## 12. Déploiement en Production

### 12.1 Checklist de Sécurité

- [ ] ✅ **HTTPS activé** pour Keycloak et l'application
- [ ] ✅ **Certificats SSL valides** (Let's Encrypt ou autre CA)
- [ ] ✅ **Client Secret stocké** dans un gestionnaire de secrets (Vault, AWS Secrets Manager)
- [ ] ✅ **Base de données externe** pour Keycloak (pas dev mode)
- [ ] ✅ **Politiques de mot de passe** renforcées
- [ ] ✅ **Durées de vie des tokens** configurées selon les besoins
- [ ] ✅ **Rotation des tokens** activée
- [ ] ✅ **Événements Keycloak** activés et archivés
- [ ] ✅ **Logs applicatifs** centralisés (ELK, Splunk)
- [ ] ✅ **Rate limiting** activé (protection DDoS)
- [ ] ✅ **CORS** configuré strictement
- [ ] ✅ **Monitoring** des accès et des erreurs
- [ ] ✅ **Backup** régulier de la base Keycloak

### 12.2 Configuration Docker Compose Production

```yaml
version: '3.8'

services:
  keycloak:
    image: quay.io/keycloak/keycloak:25.0.5
    environment:
      KC_DB: postgres
      KC_DB_URL: jdbc:postgresql://postgres:5432/keycloak
      KC_DB_USERNAME: keycloak
      KC_DB_PASSWORD: ${KEYCLOAK_DB_PASSWORD}
      KC_HOSTNAME: keycloak.supplychainx.com
      KC_HOSTNAME_STRICT: true
      KC_HTTPS_CERTIFICATE_FILE: /opt/keycloak/conf/server.crt.pem
      KC_HTTPS_CERTIFICATE_KEY_FILE: /opt/keycloak/conf/server.key.pem
      KC_HTTP_ENABLED: false
      KC_HTTPS_ENABLED: true
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: ${KEYCLOAK_ADMIN_PASSWORD}
    command:
      - start
      - --optimized
    volumes:
      - ./certs:/opt/keycloak/conf
    ports:
      - "8443:8443"
    depends_on:
      - postgres
    networks:
      - keycloak-network

  app:
    image: supplychainx:production
    environment:
      SPRING_PROFILES_ACTIVE: production
      KEYCLOAK_ISSUER_URI: https://keycloak.supplychainx.com/realms/supplychainx
      KEYCLOAK_JWK_SET_URI: https://keycloak.supplychainx.com/realms/supplychainx/protocol/openid-connect/certs
      KEYCLOAK_CLIENT_SECRET: ${KEYCLOAK_CLIENT_SECRET}
    ports:
      - "8080:8080"
    depends_on:
      - keycloak
    networks:
      - keycloak-network
```

### 12.3 Variables d'Environnement Production

```bash
# .env.production
KEYCLOAK_DB_PASSWORD=<strong-password>
KEYCLOAK_ADMIN_PASSWORD=<admin-strong-password>
KEYCLOAK_CLIENT_SECRET=<generated-secret>
SPRING_DATASOURCE_PASSWORD=<db-password>
```

---

## 13. Annexes

### 13.1 Endpoints de Test

| Méthode | Endpoint | Rôle Requis | Description |
|---------|----------|-------------|-------------|
| GET | `/api/auth/public/health` | Aucun | Health check public |
| GET | `/api/auth/me` | Authentifié | Informations utilisateur courant |
| GET | `/api/auth/admin` | ADMIN | Test accès admin |
| GET | `/api/auth/production` | CHEF_PRODUCTION, SUPERVISEUR_PRODUCTION | Test accès production |
| GET | `/api/auth/approvisionnement` | GESTIONNAIRE_APPROVISIONNEMENT, RESPONSABLE_ACHATS | Test accès approvisionnement |
| GET | `/api/auth/logistique` | SUPERVISEUR_LOGISTIQUE, RESPONSABLE_LOGISTIQUE | Test accès logistique |

### 13.2 Commandes Utiles

#### Obtenir un Token (cURL)
```bash
curl -X POST 'http://localhost:9090/realms/supplychainx/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'client_id=supplychainx-client' \
  -d 'client_secret=<client-secret>' \
  -d 'grant_type=password' \
  -d 'username=admin' \
  -d 'password=admin123'
```

#### Tester un Endpoint (cURL)
```bash
curl -H "Authorization: Bearer <access-token>" \
  http://localhost:8080/api/v1/products
```

#### Décoder un JWT Token
```bash
# Copier le token et le coller sur https://jwt.io
# Ou utiliser jq et base64:
echo "<token>" | cut -d. -f2 | base64 -d | jq
```

### 13.3 Références

- **Keycloak Documentation**: https://www.keycloak.org/documentation
- **OAuth 2.0 RFC**: https://tools.ietf.org/html/rfc6749
- **OpenID Connect Core**: https://openid.net/specs/openid-connect-core-1_0.html
- **JWT RFC**: https://tools.ietf.org/html/rfc7519
- **Spring Security OAuth2**: https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html

---

## 14. Support et Contact

Pour toute question concernant la sécurité de l'application :

- **Documentation technique**: `KEYCLOAK_SETUP.md`, `KEYCLOAK_QUICKSTART.md`
- **Guide de test**: `POSTMAN_TESTING_GUIDE.md`
- **Troubleshooting**: `TROUBLESHOOTING_CONNECTION.md`
- **Équipe DevSecOps**: devsecops@supplychainx.com

---

**Document rédigé le 2 janvier 2026**  
**Version: 1.0**  
**Statut: Production Ready** ✅

