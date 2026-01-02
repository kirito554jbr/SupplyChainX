# Keycloak Setup Checklist

Use this checklist to set up Keycloak authentication for SupplyChainX.

## ☑️ Pre-Setup

- [ ] Docker Desktop is installed and running
- [ ] Docker Compose is available
- [ ] Ports 8080, 9090, 3306, 5432, 8089 are available
- [ ] Project is in: `C:\Users\Youcode\IdeaProjects\SupplyChainX`

## ☑️ Step 1: Start Services

- [ ] Open PowerShell in project directory
- [ ] Run: `.\setup-keycloak.ps1` (automated) OR `docker-compose up -d` (manual)
- [ ] Wait for services to start (~2 minutes)
- [ ] Verify services are running: `docker-compose ps`

**Expected output:** All services should show "healthy" or "Up"

## ☑️ Step 2: Configure Keycloak

### 2.1 Access Keycloak Admin Console
- [ ] Open browser: http://localhost:9090
- [ ] Login with:
  - Username: `admin`
  - Password: `password`

### 2.2 Import Realm
- [ ] Click dropdown in top-left corner (shows "master")
- [ ] Click "Create Realm"
- [ ] Click "Browse" button
- [ ] Select: `keycloak-realm-export.json` from project root
- [ ] Click "Create" button
- [ ] Verify: "supplychainx" realm is created

### 2.3 Verify Realm Configuration
- [ ] Go to "Realm settings" → Check realm name is "supplychainx"
- [ ] Go to "Realm roles" → Verify 10 roles exist:
  - [ ] ADMIN
  - [ ] GESTIONNAIRE_APPROVISIONNEMENT
  - [ ] RESPONSABLE_ACHATS
  - [ ] SUPERVISEUR_LOGISTIQUE
  - [ ] CHEF_PRODUCTION
  - [ ] PLANIFICATEUR
  - [ ] SUPERVISEUR_PRODUCTION
  - [ ] GESTIONNAIRE_COMMERCIAL
  - [ ] RESPONSABLE_LOGISTIQUE
  - [ ] SUPERVISEUR_LIVRAISONS

### 2.4 Verify Client Configuration
- [ ] Go to "Clients"
- [ ] Find "supplychainx-client"
- [ ] Check client is enabled
- [ ] Verify redirect URIs include:
  - [ ] http://localhost:8080/*
  - [ ] http://localhost:3000/*

### 2.5 Get Client Secret
- [ ] Click on "supplychainx-client"
- [ ] Go to "Credentials" tab
- [ ] Copy the "Client Secret" value
- [ ] **Save this value** - you'll need it in the next step

### 2.6 Verify Users
- [ ] Go to "Users"
- [ ] Verify 4 users exist:
  - [ ] admin
  - [ ] production.manager
  - [ ] supply.manager
  - [ ] logistics.supervisor

## ☑️ Step 3: Configure Application

### 3.1 Create Environment File
- [ ] Create file: `.env` in project root
- [ ] Add line: `KEYCLOAK_CLIENT_SECRET=<paste-your-secret-here>`
- [ ] Replace `<paste-your-secret-here>` with actual secret from Step 2.5
- [ ] Save file

### 3.2 Restart Application
- [ ] Run: `docker-compose restart app`
- [ ] Wait for application to restart (~30 seconds)
- [ ] Check logs: `docker-compose logs -f app`
- [ ] Verify no authentication errors in logs

## ☑️ Step 4: Test Authentication

### 4.1 Test Public Endpoint (No Auth Required)
- [ ] Open PowerShell or Terminal
- [ ] Run:
```bash
curl http://localhost:8080/api/auth/public/health
```
- [ ] Expected: `{"status":"UP","message":"Public endpoint - no authentication required"}`

### 4.2 Get Access Token
- [ ] Replace `YOUR_CLIENT_SECRET` with your actual secret in this command:
```bash
curl -X POST "http://localhost:9090/realms/supplychainx/protocol/openid-connect/token" ^
  -H "Content-Type: application/x-www-form-urlencoded" ^
  -d "client_id=supplychainx-client" ^
  -d "client_secret=YOUR_CLIENT_SECRET" ^
  -d "grant_type=password" ^
  -d "username=admin" ^
  -d "password=admin123"
```
- [ ] Run the command
- [ ] **Save the `access_token`** value from response

### 4.3 Test Authenticated Endpoint
- [ ] Replace `YOUR_TOKEN` with your access token:
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/api/auth/me
```
- [ ] Expected: User info with username, email, and roles

### 4.4 Test Admin Endpoint
- [ ] Replace `YOUR_TOKEN` with your access token:
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/api/auth/admin
```
- [ ] Expected: `{"message":"Welcome, Admin!","user":"admin"}`

### 4.5 Test Role-Based Endpoint
- [ ] Replace `YOUR_TOKEN` with your access token:
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/api/auth/production
```
- [ ] Expected: Access granted or denied based on role

## ☑️ Step 5: Verify Integration

### 5.1 Access Swagger UI
- [ ] Open browser: http://localhost:8080/swagger-ui/index.html
- [ ] Verify API documentation loads
- [ ] Look for authentication endpoints

### 5.2 Test Different Users
Test with each user to verify roles work:

**Admin User:**
- [ ] Username: `admin`, Password: `admin123`
- [ ] Get token
- [ ] Should access: `/api/auth/admin` ✅

**Production Manager:**
- [ ] Username: `production.manager`, Password: `production123`
- [ ] Get token
- [ ] Should access: `/api/auth/production` ✅
- [ ] Should NOT access: `/api/auth/admin` ❌

**Supply Manager:**
- [ ] Username: `supply.manager`, Password: `supply123`
- [ ] Get token
- [ ] Should access: `/api/auth/approvisionnement` ✅
- [ ] Should NOT access: `/api/auth/admin` ❌

**Logistics Supervisor:**
- [ ] Username: `logistics.supervisor`, Password: `logistics123`
- [ ] Get token
- [ ] Should access: `/api/auth/logistique` ✅
- [ ] Should NOT access: `/api/auth/admin` ❌

## ☑️ Troubleshooting

If something doesn't work, check:

### Services Not Starting
- [ ] Run: `docker-compose logs keycloak` to check Keycloak logs
- [ ] Run: `docker-compose logs app` to check application logs
- [ ] Verify ports are not in use: `netstat -ano | findstr "8080 9090 3306"`

### Authentication Fails
- [ ] Verify Keycloak is accessible: http://localhost:9090
- [ ] Check realm name is exactly: `supplychainx`
- [ ] Verify client secret is correct in `.env` file
- [ ] Check application logs for JWT validation errors

### Token Validation Fails
- [ ] Verify issuer URI in application.properties
- [ ] Check network connectivity between app and Keycloak
- [ ] Decode JWT at https://jwt.io to verify claims
- [ ] Ensure token hasn't expired (default: 5 minutes)

### 403 Forbidden Errors
- [ ] Verify user has the required role in Keycloak
- [ ] Check role names match exactly (case-sensitive)
- [ ] View user's role mapping in Keycloak Admin

## ☑️ Optional: Create Additional Users

If you want to create more users:
- [ ] Go to Keycloak Admin → Users
- [ ] Click "Add user"
- [ ] Fill in details (username, email, etc.)
- [ ] Click "Create"
- [ ] Go to "Credentials" tab → Set password
- [ ] Go to "Role mapping" → Assign roles
- [ ] Test new user authentication

## ☑️ Documentation Review

- [ ] Read: `KEYCLOAK_QUICKSTART.md` for quick reference
- [ ] Review: `KEYCLOAK_SETUP.md` for detailed info
- [ ] Check: `KEYCLOAK_MIGRATION.md` for what changed
- [ ] See: `README.md` for updated project info

## ✅ Setup Complete!

If all items are checked, your Keycloak integration is complete! 🎉

### Quick Reference

**Services:**
- Application: http://localhost:8080
- Keycloak Admin: http://localhost:9090
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- phpMyAdmin: http://localhost:8089

**Default Credentials:**
- Keycloak Admin: `admin` / `password`
- Test User: `admin` / `admin123`

**Documentation:**
- Quick Start: `KEYCLOAK_QUICKSTART.md`
- Full Guide: `KEYCLOAK_SETUP.md`
- Main README: `README.md`

---

**Next Steps:**
- Customize Keycloak theme
- Add more users
- Integrate with frontend
- Enable MFA
- Configure production settings

