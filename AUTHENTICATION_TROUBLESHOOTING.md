# Authentication Issues - Troubleshooting Guide

## Problem: Login and Register Returning 401

### Root Causes Identified

1. **No test users in database** - The database was empty
2. **Logstash dependency not properly loaded** - Required Maven rebuild

### Solutions Applied

#### 1. Created DataInitializer Component
Created `DataInitializer.java` that automatically creates test users on application startup:

**Test Users Created:**
- **Admin**: `admin@test.com` / `0000` (Role: ADMIN)
- **Gestionnaire**: `gestionnaire@test.com` / `0000` (Role: GESTIONNAIRE_APPROVISIONNEMENT)
- **Production**: `production@test.com` / `0000` (Role: CHEF_PRODUCTION)
- **Logistique**: `logistique@test.com` / `0000` (Role: RESPONSABLE_LOGISTIQUE)

#### 2. Fixed Logstash Configuration
- Updated `logback-spring.xml` to use environment variable for Docker compatibility
- Changed `localhost:5000` to `${LOGSTASH_HOST:-localhost}:5000`
- Added `LOGSTASH_HOST: logstash` to docker-compose.yml

#### 3. Rebuilt Project
```bash
.\mvnw.cmd clean install -DskipTests
```

### How to Test Authentication

#### Test 1: Login with Admin User
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@test.com",
  "password": "0000"
}
```

**Expected Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresAt": 1234567890123,
  "user": {
    "id": 1,
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@test.com",
    "role": "ADMIN"
  }
}
```

#### Test 2: Register New User
```bash
POST http://localhost:8080/api/users/register
Content-Type: application/json

{
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com",
  "password": "password123",
  "role": "GESTIONNAIRE_APPROVISIONNEMENT"
}
```

**Expected Response (201 Created):**
```json
{
  "idUser": 5,
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com",
  "role": "GESTIONNAIRE_APPROVISIONNEMENT"
}
```

#### Test 3: Access Protected Endpoint
```bash
GET http://localhost:8080/api/users
Authorization: Bearer <your_access_token>
```

**Expected Response (200 OK):**
List of all users

#### Test 4: Refresh Token
```bash
POST http://localhost:8080/api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "<your_refresh_token>"
}
```

#### Test 5: Validate Token
```bash
GET http://localhost:8080/api/auth/validate
Authorization: Bearer <your_access_token>
```

#### Test 6: Logout
```bash
POST http://localhost:8080/api/auth/logout
Authorization: Bearer <your_access_token>
```

### Postman Collection

Import the existing `SupplyChainX_JWT_Auth.postman_collection.json` file into Postman for pre-configured requests.

### Common Issues and Solutions

#### Issue: Still getting 401 after restart
**Solution:**
1. Make sure MySQL is running: `docker ps` (check if supplychainx-db container is up)
2. Check if DataInitializer ran: Look for console output `✅ Admin user created: admin@test.com / 0000`
3. Verify database connection in application.properties

#### Issue: Register endpoint returns 401
**Possible Causes:**
1. Application didn't start properly
2. Security configuration blocking the endpoint
3. JWT filter interfering

**Solution:**
1. Restart the application
2. Check that `/api/users/register` is in the permitAll list in SecurityConfig
3. Don't send Authorization header when registering

#### Issue: Token expires too quickly
**Solution:**
Adjust token expiration in `application.properties`:
```properties
# 15 minutes = 900000 ms
jwt.access-token-expiration=900000
# 7 days = 604800000 ms
jwt.refresh-token-expiration=604800000
```

#### Issue: Logback errors on startup
**Solution:**
Rebuild project to ensure logstash-logback-encoder dependency is loaded:
```bash
.\mvnw.cmd clean install -DskipTests
```

### Verification Steps

1. **Start the application:**
   ```bash
   .\mvnw.cmd spring-boot:run
   ```

2. **Check console logs for:**
   - `✅ Admin user created: admin@test.com / 0000`
   - `🚀 Database initialization complete!`
   - `Started SupplyChainXApplication in X.XXX seconds`

3. **Test login in Postman:**
   - Method: POST
   - URL: `http://localhost:8080/api/auth/login`
   - Body (raw JSON):
     ```json
     {
       "email": "admin@test.com",
       "password": "0000"
     }
     ```

4. **Copy the accessToken from response**

5. **Test protected endpoint:**
   - Method: GET
   - URL: `http://localhost:8080/api/users`
   - Headers: `Authorization: Bearer <paste_access_token_here>`

### Security Configuration Summary

**Public Endpoints (No Authentication Required):**
- `/api/auth/**` - All authentication endpoints
- `/api/users/register` - User registration
- `/swagger-ui/**` - Swagger documentation
- `/v3/api-docs/**` - OpenAPI documentation

**Protected Endpoints (Authentication Required):**
- All other endpoints require valid JWT token in Authorization header
- Role-based access control applies based on user role

### Database Schema

The `users` table has the following structure:
```sql
CREATE TABLE users (
  id_user BIGINT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),  -- BCrypt encrypted
  role VARCHAR(50),       -- Enum: ADMIN, GESTIONNAIRE_APPROVISIONNEMENT, etc.
  refresh_token VARCHAR(1000),
  refresh_token_expiry DATETIME,
  enabled BOOLEAN DEFAULT true
);
```

### Next Steps

1. ✅ **Start the application** - `.\mvnw.cmd spring-boot:run`
2. ✅ **Verify test users were created** - Check console logs
3. ✅ **Test login** - Use Postman with admin@test.com / 0000
4. ✅ **Copy access token** - From login response
5. ✅ **Test protected endpoints** - Add Bearer token to headers
6. ✅ **Test other users** - Try different roles to verify authorization

### Contact & Support

If issues persist:
1. Check application logs for detailed error messages
2. Verify MySQL database is accessible
3. Ensure port 8080 is not in use by another application
4. Check that all dependencies are properly installed

---
**Last Updated:** December 23, 2025

