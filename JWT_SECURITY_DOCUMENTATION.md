# Sécurité JWT - Documentation API

## Vue d'ensemble

L'application SupplyChainX utilise JWT (JSON Web Tokens) pour sécuriser ses APIs. Le système implémente deux types de tokens :

- **Access Token** : Token de courte durée (15 minutes) utilisé pour accéder aux endpoints protégés
- **Refresh Token** : Token de longue durée (7 jours) utilisé uniquement pour renouveler l'Access Token

## Architecture de sécurité

### Principes
- ✅ API stateless (sans session serveur)
- ✅ Authentification par email et mot de passe
- ✅ Rotation obligatoire du Refresh Token
- ✅ Révocation des tokens lors du logout
- ✅ Vérification de l'état actif de l'utilisateur

### Stockage
- **Access Token** : Stocké côté client (mémoire ou sessionStorage recommandé)
- **Refresh Token** : Stocké en base de données (table `users`) et côté client (httpOnly cookie recommandé)

## Endpoints d'authentification

### 1. Login (Connexion)

**Endpoint** : `POST /api/auth/login`

**Description** : Authentifie un utilisateur et génère les tokens JWT.

**Request Body** :
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response Success (200 OK)** :
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "accessTokenExpiry": 1703001234567,
  "tokenType": "Bearer",
  "user": {
    "idUser": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "user@example.com",
    "role": "ADMIN"
  }
}
```

**Response Error (401 Unauthorized)** :
```json
{
  "timestamp": "2024-12-15T10:30:45.123",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password"
}
```

**Cas d'erreur** :
- Email ou mot de passe invalide → 401
- Compte utilisateur désactivé → 401

---

### 2. Refresh Token (Renouvellement)

**Endpoint** : `POST /api/auth/refresh`

**Description** : Renouvelle l'Access Token en utilisant le Refresh Token. Applique la rotation du Refresh Token.

**Request Body** :
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response Success (200 OK)** :
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "accessTokenExpiry": 1703001234567,
  "tokenType": "Bearer",
  "user": {
    "idUser": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "user@example.com",
    "role": "ADMIN"
  }
}
```

**Response Error (401 Unauthorized)** :
```json
{
  "timestamp": "2024-12-15T10:30:45.123",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid refresh token"
}
```

**Cas d'erreur** :
- Refresh Token invalide → 401
- Refresh Token expiré → 401
- Refresh Token ne correspond pas → 401
- Utilisateur désactivé → 401

---

### 3. Logout (Déconnexion)

**Endpoint** : `POST /api/auth/logout`

**Description** : Révoque le Refresh Token de l'utilisateur connecté.

**Headers** :
```
Authorization: Bearer {accessToken}
```

**Response Success (200 OK)** :
```json
{
  "message": "Logout successful",
  "timestamp": "2024-12-15T10:30:45.123"
}
```

**Response Error (401 Unauthorized)** :
```json
{
  "timestamp": "2024-12-15T10:30:45.123",
  "status": 401,
  "error": "Unauthorized",
  "message": "User not authenticated"
}
```

---

### 4. Validate Token (Validation)

**Endpoint** : `GET /api/auth/validate`

**Description** : Vérifie la validité de l'Access Token actuel.

**Headers** :
```
Authorization: Bearer {accessToken}
```

**Response Success (200 OK)** :
```json
{
  "valid": true,
  "email": "user@example.com",
  "timestamp": "2024-12-15T10:30:45.123"
}
```

**Response Error (401 Unauthorized)** :
```json
{
  "timestamp": "2024-12-15T10:30:45.123",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired token"
}
```

---

## Utilisation des tokens

### 1. Pour les requêtes protégées

Ajoutez le header `Authorization` avec le préfixe `Bearer` :

```
GET /api/users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 2. Gestion de l'expiration

Quand l'Access Token expire (après 15 minutes) :
1. La requête retourne un 401 Unauthorized
2. Utilisez le Refresh Token pour obtenir un nouveau Access Token via `/api/auth/refresh`
3. Le nouveau Refresh Token remplace l'ancien (rotation)
4. Réessayez la requête avec le nouveau Access Token

### 3. Flux recommandé

```javascript
// 1. Login
const loginResponse = await fetch('/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});
const { accessToken, refreshToken } = await loginResponse.json();

// Stocker les tokens
sessionStorage.setItem('accessToken', accessToken);
localStorage.setItem('refreshToken', refreshToken);

// 2. Faire des requêtes protégées
const response = await fetch('/api/users', {
  headers: { 'Authorization': `Bearer ${accessToken}` }
});

// 3. Si 401, renouveler le token
if (response.status === 401) {
  const refreshResponse = await fetch('/api/auth/refresh', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ refreshToken })
  });
  const { accessToken: newAccessToken, refreshToken: newRefreshToken } = await refreshResponse.json();
  
  // Mettre à jour les tokens
  sessionStorage.setItem('accessToken', newAccessToken);
  localStorage.setItem('refreshToken', newRefreshToken);
  
  // Réessayer la requête
  const retryResponse = await fetch('/api/users', {
    headers: { 'Authorization': `Bearer ${newAccessToken}` }
  });
}

// 4. Logout
await fetch('/api/auth/logout', {
  method: 'POST',
  headers: { 'Authorization': `Bearer ${accessToken}` }
});

// Supprimer les tokens
sessionStorage.removeItem('accessToken');
localStorage.removeItem('refreshToken');
```

---

## Codes d'erreur HTTP

| Code | Description | Cas d'usage |
|------|-------------|-------------|
| 200 | OK | Requête réussie |
| 400 | Bad Request | Données de validation invalides |
| 401 | Unauthorized | Token invalide/expiré ou authentification échouée |
| 403 | Forbidden | Accès interdit (rôle insuffisant) |
| 500 | Internal Server Error | Erreur serveur |

---

## Structure du Access Token (JWT Payload)

```json
{
  "sub": "user@example.com",
  "role": "ADMIN",
  "userId": 1,
  "firstName": "John",
  "lastName": "Doe",
  "iat": 1703001234,
  "exp": 1703002134
}
```

---

## Rôles et autorisations

Les rôles disponibles dans l'application :
- `ADMIN` : Accès complet
- `GESTIONNAIRE_APPROVISIONNEMENT` : Gestion approvisionnement
- `RESPONSABLE_ACHATS` : Gestion des achats
- `SUPERVISEUR_LOGISTIQUE` : Supervision logistique
- `CHEF_PRODUCTION` : Chef de production
- `PLANIFICATEUR` : Planification
- `SUPERVISEUR_PRODUCTION` : Supervision production
- `GESTIONNAIRE_COMMERCIAL` : Gestion commerciale
- `RESPONSABLE_LOGISTIQUE` : Responsable logistique
- `SUPERVISEUR_LIVRAISONS` : Supervision livraisons

Chaque endpoint a ses propres restrictions de rôles (voir SecurityConfig.java).

---

## Configuration

Les paramètres JWT sont configurables dans `application.properties` :

```properties
# Secret key (minimum 256 bits pour HS256)
jwt.secret=VotreSecretTresSecurise...

# Durée de vie Access Token (15 minutes)
jwt.access-token-expiration=900000

# Durée de vie Refresh Token (7 jours)
jwt.refresh-token-expiration=604800000
```

⚠️ **IMPORTANT** : En production, utilisez une clé secrète forte et stockez-la de manière sécurisée (variables d'environnement, vault, etc.).

---

## Sécurité et bonnes pratiques

### ✅ Implémenté
- Tokens JWT signés avec HS256
- Rotation du Refresh Token
- Révocation des tokens au logout
- Vérification de l'état actif de l'utilisateur
- Session stateless
- Expiration des tokens
- Validation stricte des tokens

### 🔒 Recommandations production
1. Utiliser HTTPS uniquement
2. Stocker le Refresh Token dans un httpOnly cookie
3. Stocker l'Access Token en mémoire ou sessionStorage
4. Changer la clé secrète JWT et la stocker dans des variables d'environnement
5. Implémenter un système de blacklist pour les tokens révoqués (Redis)
6. Activer CORS de manière restrictive
7. Ajouter rate limiting sur les endpoints d'authentification
8. Logger les tentatives de connexion échouées
9. Implémenter 2FA pour les comptes sensibles
10. Utiliser des tokens à durée de vie encore plus courte si possible

---

## Tests avec Postman/cURL

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@supplychainx.com","password":"admin123"}'
```

### Accéder à un endpoint protégé
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer {accessToken}"
```

### Refresh Token
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"{refreshToken}"}'
```

### Logout
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer {accessToken}"
```

---

## Traçabilité

Pour la traçabilité complète des accès :
- Utilisez les aspects AOP existants dans l'application
- Loggez les tentatives de connexion (succès/échec)
- Loggez les renouvellements de tokens
- Loggez les déconnexions
- Implémentez un audit trail pour les actions sensibles

Exemple d'aspect à ajouter :
```java
@Aspect
@Component
public class AuthenticationAuditAspect {
    @AfterReturning(pointcut = "execution(* AuthService.login(..))", returning = "result")
    public void logSuccessfulLogin(JoinPoint joinPoint, AuthResponse result) {
        // Log successful login
    }
    
    @AfterThrowing(pointcut = "execution(* AuthService.login(..))", throwing = "error")
    public void logFailedLogin(JoinPoint joinPoint, Throwable error) {
        // Log failed login attempt
    }
}
```

