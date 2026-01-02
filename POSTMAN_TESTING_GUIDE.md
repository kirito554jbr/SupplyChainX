# Testing Keycloak Authentication in Postman - Complete Step-by-Step Guide

## ✅ Pre-Test Checklist

Before starting, verify:
- [ ] Keycloak is running: http://localhost:9090
- [ ] Application is running: http://localhost:8080
- [ ] You have created the realm, client, roles, and users in Keycloak
- [ ] You have the client secret saved
- [ ] `.env` file has `KEYCLOAK_CLIENT_SECRET=your-secret`
- [ ] Application has been restarted after adding the secret

---

## 📥 Part 1: Setup Postman Collection

### Step 1: Open Postman
1. Launch **Postman** application
2. If you don't have it, download from: https://www.postman.com/downloads/

### Step 2: Create New Collection
1. Click **"Collections"** in the left sidebar
2. Click **"+"** or **"Create a collection"**
3. Name it: **"SupplyChainX - Keycloak Auth"**
4. Click **"Create"**

### Step 3: Add Collection Variables
1. Click on your collection **"SupplyChainX - Keycloak Auth"**
2. Click on the **"Variables"** tab
3. Add these variables:

| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| `client_secret` | `vYUvVEkXpi68iw8WZCnmmvXni1SKpFiL` | `vYUvVEkXpi68iw8WZCnmmvXni1SKpFiL` |
| `access_token` | (leave empty) | (leave empty) |
| `keycloak_url` | `http://localhost:9090` | `http://localhost:9090` |
| `app_url` | `http://localhost:8080` | `http://localhost:8080` |

4. Click **"Save"** (Ctrl + S)

---

## 🔑 Part 2: Test Getting Access Token

### Step 4: Create "Get Admin Token" Request

1. **Right-click** on your collection → **"Add request"**
2. Name it: **"1. Get Admin Token"**
3. Change method to: **POST**
4. URL: `{{keycloak_url}}/realms/supplychainx/protocol/openid-connect/token`

### Step 5: Configure Headers
1. Click on **"Headers"** tab
2. Add header:
   - **Key**: `Content-Type`
   - **Value**: `application/x-www-form-urlencoded`

### Step 6: Configure Body
1. Click on **"Body"** tab
2. Select **"x-www-form-urlencoded"**
3. Add these key-value pairs:

| KEY | VALUE |
|-----|-------|
| `client_id` | `supplychainx-client` |
| `client_secret` | `{{client_secret}}` |
| `grant_type` | `password` |
| `username` | `admin` |
| `password` | `admin123` |

### Step 7: Add Auto-Save Token Script
1. Click on **"Tests"** tab (next to Body)
2. Add this JavaScript code:

```javascript
// Parse the response
var jsonData = pm.response.json();

// Test that token was received
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Access token received", function () {
    pm.expect(jsonData.access_token).to.exist;
    pm.expect(jsonData.access_token).to.be.a('string');
});

// Save the access token to collection variable
if (jsonData.access_token) {
    pm.collectionVariables.set("access_token", jsonData.access_token);
    console.log("✅ Token saved to collection variable");
}

// Log token info
console.log("Token expires in: " + jsonData.expires_in + " seconds");
console.log("Token type: " + jsonData.token_type);
```

### Step 8: Send Request and Verify
1. Click **"Send"** button (or press Ctrl + Enter)
2. **Expected Response** (Status: 200 OK):

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6...",
    "expires_in": 300,
    "refresh_expires_in": 1800,
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6...",
    "token_type": "Bearer",
    "not-before-policy": 0,
    "session_state": "abc123...",
    "scope": "profile email"
}
```

3. ✅ Check **"Test Results"** tab - should show 2 passing tests
4. ✅ Check **"Console"** (bottom of Postman) - should show "Token saved"

### Troubleshooting Step 8:
- ❌ **401 Unauthorized**: Check username/password are correct
- ❌ **Invalid client credentials**: Check client_secret matches Keycloak
- ❌ **User not found**: Verify user exists in Keycloak

---

## 🌐 Part 3: Test Public Endpoint (No Authentication)

### Step 9: Create Public Endpoint Request

1. **Add request** to collection
2. Name: **"2. Public Health Check (No Auth)"**
3. Method: **GET**
4. URL: `{{app_url}}/api/auth/public/health`

### Step 10: Send Request
1. Click **"Send"**
2. **Expected Response** (Status: 200 OK):

```json
{
    "status": "UP",
    "message": "Public endpoint - no authentication required"
}
```

✅ **This should work without any token!**

### Troubleshooting Step 10:
- ❌ **Connection refused**: Application is not running
- ❌ **404 Not Found**: Check if AuthController exists

---

## 🔐 Part 4: Test Authenticated Endpoint

### Step 11: Create "Get Current User Info" Request

1. **Add request** to collection
2. Name: **"3. Get Current User Info (Auth Required)"**
3. Method: **GET**
4. URL: `{{app_url}}/api/auth/me`

### Step 12: Add Authorization Header
1. Click on **"Headers"** tab
2. Add header:
   - **Key**: `Authorization`
   - **Value**: `Bearer {{access_token}}`

**Alternative Method (Recommended):**
1. Click on **"Authorization"** tab
2. Type: Select **"Bearer Token"**
3. Token: `{{access_token}}`

### Step 13: Send Request
1. Make sure you ran "Get Admin Token" first!
2. Click **"Send"**
3. **Expected Response** (Status: 200 OK):

```json
{
    "username": "admin",
    "email": "admin@supplychainx.com",
    "roles": ["ADMIN"],
    "sub": "f8e7d6c5-b4a3-9876-5432-abcdef123456",
    "given_name": "Admin",
    "family_name": "User"
}
```

✅ **Success! You're authenticated!**

### Troubleshooting Step 13:
- ❌ **401 Unauthorized**: Token is missing or expired (get new token)
- ❌ **403 Forbidden**: Token is invalid
- ❌ **Token expired**: Run "Get Admin Token" again (tokens expire in 5 min)

---

## 👑 Part 5: Test Admin-Only Endpoint

### Step 14: Create Admin Endpoint Request

1. **Add request** to collection
2. Name: **"4. Admin Only Endpoint (ADMIN role)"**
3. Method: **GET**
4. URL: `{{app_url}}/api/auth/admin`

### Step 15: Add Authorization
1. **Authorization** tab → **Bearer Token**
2. Token: `{{access_token}}`

### Step 16: Send Request
1. Click **"Send"**
2. **Expected Response** (Status: 200 OK):

```json
{
    "message": "Welcome, Admin!",
    "user": "admin"
}
```

✅ **Admin access granted!**

---

## 🏭 Part 6: Test Production Endpoint

### Step 17: Create Production Manager Token Request

1. **Add request** to collection
2. Name: **"5. Get Production Manager Token"**
3. Copy settings from "Get Admin Token"
4. In **Body** tab, change:
   - `username`: `production.manager`
   - `password`: `production123`
5. Keep the **Tests** script (it will save the new token)

### Step 18: Create Production Endpoint Request

1. **Add request** to collection
2. Name: **"6. Production Management (Production Roles)"**
3. Method: **GET**
4. URL: `{{app_url}}/api/auth/production`
5. Authorization: **Bearer Token** → `{{access_token}}`

### Step 19: Test Production User Access

**Test 1: Production endpoint with production user**
1. Run **"5. Get Production Manager Token"**
2. Run **"6. Production Management"**
3. **Expected**: ✅ 200 OK - "Production Management Access"

**Test 2: Admin endpoint with production user**
1. Keep the production.manager token
2. Run **"4. Admin Only Endpoint"**
3. **Expected**: ❌ 403 Forbidden - "Access Denied"

✅ **Perfect! Role-based access control is working!**

---

## 📦 Part 7: Test All Users

### Step 20: Create Token Requests for All Users

Create these additional requests (duplicate "Get Admin Token"):

**A. Supply Manager Token:**
- Name: **"7. Get Supply Manager Token"**
- username: `supply.manager`
- password: `supply123`

**B. Logistics Supervisor Token:**
- Name: **"8. Get Logistics Supervisor Token"**
- username: `logistics.supervisor`
- password: `logistics123`

### Step 21: Create Role-Specific Endpoints

**A. Supply Chain Endpoint:**
- Name: **"9. Supply Chain Management (Supply Roles)"**
- Method: GET
- URL: `{{app_url}}/api/auth/approvisionnement`
- Authorization: Bearer `{{access_token}}`

**B. Logistics Endpoint:**
- Name: **"10. Logistics Management (Logistics Roles)"**
- Method: GET
- URL: `{{app_url}}/api/auth/logistique`
- Authorization: Bearer `{{access_token}}`

**C. Check Role Endpoint:**
- Name: **"11. Check User Roles"**
- Method: GET
- URL: `{{app_url}}/api/auth/check-role`
- Authorization: Bearer `{{access_token}}`

### Step 22: Test Access Matrix

Run this sequence to verify role-based access:

| User | Token Request | Endpoint | Expected Result |
|------|---------------|----------|-----------------|
| admin | #1 | Admin (#4) | ✅ 200 OK |
| admin | #1 | Production (#6) | ✅ 200 OK (Admin has all access) |
| production.manager | #5 | Production (#6) | ✅ 200 OK |
| production.manager | #5 | Admin (#4) | ❌ 403 Forbidden |
| supply.manager | #7 | Supply Chain (#9) | ✅ 200 OK |
| supply.manager | #7 | Admin (#4) | ❌ 403 Forbidden |
| logistics.supervisor | #8 | Logistics (#10) | ✅ 200 OK |
| logistics.supervisor | #8 | Admin (#4) | ❌ 403 Forbidden |

---

## 🎯 Part 8: Advanced Testing

### Step 23: Test Token Expiration

1. Get a token
2. Wait 6 minutes (tokens expire in 5 minutes)
3. Try to use the expired token
4. **Expected**: ❌ 401 Unauthorized - "Token expired"
5. Get a new token
6. **Expected**: ✅ 200 OK

### Step 24: Test Invalid Token

1. In any authenticated request
2. Change token to: `Bearer invalid-token-12345`
3. Send request
4. **Expected**: ❌ 401 Unauthorized

### Step 25: Test Missing Token

1. Remove Authorization header completely
2. Send request to protected endpoint
3. **Expected**: ❌ 401 Unauthorized - "Full authentication is required"

---

## 📊 Part 9: Verify Token Contents

### Step 26: Decode JWT Token

1. Get a token from any token request
2. Copy the `access_token` value
3. Go to **https://jwt.io**
4. Paste token in the left side (Encoded)
5. View the decoded payload on the right

**Expected Payload Structure:**
```json
{
  "exp": 1704228600,
  "iat": 1704228300,
  "jti": "abc-123...",
  "iss": "http://localhost:9090/realms/supplychainx",
  "aud": "account",
  "sub": "f8e7d6c5...",
  "typ": "Bearer",
  "azp": "supplychainx-client",
  "realm_access": {
    "roles": [
      "ADMIN",
      "default-roles-supplychainx"
    ]
  },
  "resource_access": {
    "account": {
      "roles": ["manage-account", "view-profile"]
    }
  },
  "scope": "profile email",
  "email_verified": true,
  "name": "Admin User",
  "preferred_username": "admin",
  "given_name": "Admin",
  "family_name": "User",
  "email": "admin@supplychainx.com"
}
```

✅ **Verify `realm_access.roles` contains your roles!**

---

## 🔄 Part 10: Using Refresh Token

### Step 27: Create Refresh Token Request

1. **Add request**: **"12. Refresh Access Token"**
2. Method: **POST**
3. URL: `{{keycloak_url}}/realms/supplychainx/protocol/openid-connect/token`
4. Body (x-www-form-urlencoded):

| KEY | VALUE |
|-----|-------|
| `client_id` | `supplychainx-client` |
| `client_secret` | `{{client_secret}}` |
| `grant_type` | `refresh_token` |
| `refresh_token` | `{{refresh_token}}` |

5. In "Get Admin Token" Tests script, add this line after saving access_token:

```javascript
pm.collectionVariables.set("refresh_token", jsonData.refresh_token);
```

6. Now you can refresh tokens without re-entering credentials!

---

## 📝 Complete Test Checklist

Run through this checklist to verify everything works:

### Authentication Tests:
- [ ] ✅ Get admin token (200 OK)
- [ ] ✅ Get production manager token (200 OK)
- [ ] ✅ Get supply manager token (200 OK)
- [ ] ✅ Get logistics supervisor token (200 OK)
- [ ] ✅ Token contains access_token field
- [ ] ✅ Token saved to collection variable
- [ ] ✅ Token decoded at jwt.io shows roles

### Public Endpoint Tests:
- [ ] ✅ Public health check works without token
- [ ] ✅ Returns 200 OK

### Authenticated Endpoint Tests:
- [ ] ✅ Get user info with valid token (200 OK)
- [ ] ✅ User info shows correct username
- [ ] ✅ User info shows correct roles
- [ ] ✅ Get user info without token (401 Unauthorized)
- [ ] ✅ Get user info with invalid token (401 Unauthorized)

### Role-Based Access Tests:
- [ ] ✅ Admin user can access admin endpoint
- [ ] ✅ Admin user can access all endpoints
- [ ] ✅ Production user can access production endpoint
- [ ] ✅ Production user CANNOT access admin endpoint (403)
- [ ] ✅ Supply user can access supply chain endpoint
- [ ] ✅ Supply user CANNOT access admin endpoint (403)
- [ ] ✅ Logistics user can access logistics endpoint
- [ ] ✅ Logistics user CANNOT access admin endpoint (403)

### Token Management Tests:
- [ ] ✅ Token expires after 5 minutes
- [ ] ✅ Expired token returns 401
- [ ] ✅ Can get new token after expiration
- [ ] ✅ Refresh token works

---

## 🎉 Success Criteria

✅ **Your Keycloak integration is working if:**

1. ✅ You can get tokens for all users
2. ✅ Tokens contain the correct roles in `realm_access.roles`
3. ✅ Public endpoints work without authentication
4. ✅ Protected endpoints require valid tokens
5. ✅ Role-based endpoints enforce role requirements
6. ✅ Admin user has access to all endpoints
7. ✅ Non-admin users are blocked from admin endpoints
8. ✅ Tokens expire and can be refreshed

---

## 🐛 Common Issues and Solutions

### Issue 1: "Connection Refused" on port 8080
**Solution:**
```bash
# Check if app is running
docker-compose ps app

# Restart if needed
docker-compose restart app

# Check logs
docker-compose logs -f app
```

### Issue 2: "Connection Refused" on port 9090
**Solution:**
```bash
# Check if Keycloak is running
docker-compose ps keycloak

# Restart if needed
docker-compose restart keycloak

# Check logs
docker-compose logs -f keycloak
```

### Issue 3: "Invalid client credentials"
**Solution:**
- Verify `client_secret` variable matches Keycloak
- Go to Keycloak → Clients → supplychainx-client → Credentials
- Copy the secret again
- Update Postman variable

### Issue 4: "User not found"
**Solution:**
- Verify user exists in Keycloak
- Check username spelling (case-sensitive)
- Verify user is Enabled
- Check password is not Temporary

### Issue 5: Token doesn't contain roles
**Solution:**
- In Keycloak: Client scopes → roles → Mappers
- Verify "realm-roles" mapper exists
- Check user has roles assigned
- Decode token at jwt.io to verify

### Issue 6: 403 Forbidden when should have access
**Solution:**
- Decode token at jwt.io
- Check `realm_access.roles` contains required role
- Verify role name matches exactly (case-sensitive)
- Check @PreAuthorize annotation in code

### Issue 7: Token expired
**Solution:**
- Tokens expire in 5 minutes by default
- Just run "Get Token" request again
- Or use refresh token

### Issue 8: Application returns 404
**Solution:**
- Verify AuthController exists
- Check endpoint paths match exactly
- Verify application compiled successfully
- Check application logs

---

## 📥 Import Pre-Built Collection

Want to skip manual setup? Import this JSON:

Save as `SupplyChainX-Keycloak.postman_collection.json` and import to Postman:

```json
{
  "info": {
    "name": "SupplyChainX - Keycloak Auth",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {
      "key": "client_secret",
      "value": "vYUvVEkXpi68iw8WZCnmmvXni1SKpFiL"
    },
    {
      "key": "access_token",
      "value": ""
    },
    {
      "key": "keycloak_url",
      "value": "http://localhost:9090"
    },
    {
      "key": "app_url",
      "value": "http://localhost:8080"
    }
  ],
  "item": [
    {
      "name": "1. Get Admin Token",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "var jsonData = pm.response.json();",
              "pm.test(\"Status code is 200\", function () {",
              "    pm.response.to.have.status(200);",
              "});",
              "pm.test(\"Access token received\", function () {",
              "    pm.expect(jsonData.access_token).to.exist;",
              "});",
              "if (jsonData.access_token) {",
              "    pm.collectionVariables.set(\"access_token\", jsonData.access_token);",
              "    console.log(\"✅ Token saved\");",
              "}"
            ]
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/x-www-form-urlencoded"}],
        "body": {
          "mode": "urlencoded",
          "urlencoded": [
            {"key": "client_id", "value": "supplychainx-client"},
            {"key": "client_secret", "value": "{{client_secret}}"},
            {"key": "grant_type", "value": "password"},
            {"key": "username", "value": "admin"},
            {"key": "password", "value": "admin123"}
          ]
        },
        "url": {
          "raw": "{{keycloak_url}}/realms/supplychainx/protocol/openid-connect/token",
          "host": ["{{keycloak_url}}"],
          "path": ["realms", "supplychainx", "protocol", "openid-connect", "token"]
        }
      }
    },
    {
      "name": "2. Public Health Check",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{app_url}}/api/auth/public/health",
          "host": ["{{app_url}}"],
          "path": ["api", "auth", "public", "health"]
        }
      }
    },
    {
      "name": "3. Get Current User Info",
      "request": {
        "method": "GET",
        "header": [],
        "auth": {
          "type": "bearer",
          "bearer": [{"key": "token", "value": "{{access_token}}"}]
        },
        "url": {
          "raw": "{{app_url}}/api/auth/me",
          "host": ["{{app_url}}"],
          "path": ["api", "auth", "me"]
        }
      }
    },
    {
      "name": "4. Admin Only Endpoint",
      "request": {
        "method": "GET",
        "header": [],
        "auth": {
          "type": "bearer",
          "bearer": [{"key": "token", "value": "{{access_token}}"}]
        },
        "url": {
          "raw": "{{app_url}}/api/auth/admin",
          "host": ["{{app_url}}"],
          "path": ["api", "auth", "admin"]
        }
      }
    }
  ]
}
```

**To Import:**
1. Open Postman
2. Click **Import**
3. Drag and drop the JSON file
4. Update `client_secret` variable
5. Start testing!

---

## 🎯 Summary

You've learned how to:
- ✅ Set up Postman collection with variables
- ✅ Get access tokens from Keycloak
- ✅ Test public endpoints (no auth)
- ✅ Test authenticated endpoints (with token)
- ✅ Test role-based access control
- ✅ Handle token expiration
- ✅ Decode and verify JWT tokens
- ✅ Troubleshoot common issues

**Your Keycloak authentication is fully functional! 🎉**

---

## 📚 Next Steps

1. ✅ Test all your existing API endpoints with authentication
2. ✅ Add @PreAuthorize annotations to other controllers
3. ✅ Create frontend integration
4. ✅ Set up environment-specific collections (dev, staging, prod)
5. ✅ Configure automated tests with Newman (Postman CLI)
6. ✅ Enable MFA for production users

**Happy Testing! 🚀**

