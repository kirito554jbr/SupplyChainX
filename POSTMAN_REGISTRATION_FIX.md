# Fix for Postman 400 Bad Request - User Registration

## Problem
You got: **400 Bad Request** with message **"401 UNAUTHORIZED [User not authenticated]"**

## Root Cause
You were using the wrong endpoint URL in Postman:
- ❌ **Wrong:** `POST http://localhost:8080/api/users` (requires ADMIN authentication)
- ✅ **Correct:** `POST http://localhost:8080/api/users/register` (public, no auth needed)

## Solution Applied

### 1. Fixed SecurityConfig
Updated the security configuration to be more explicit about which endpoints are protected:
- `/api/users/register` → Public (no authentication required)
- `/api/users` (POST) → Admin only
- `/api/users/**` (GET/PUT/DELETE) → Requires specific roles

### 2. Updated Endpoint URL in Postman

**Change your Postman request from:**
```
POST http://localhost:8080/api/users
```

**To:**
```
POST http://localhost:8080/api/users/register
```

## How to Test

### Step 1: Rebuild and Restart Application
```powershell
docker-compose down
docker-compose up -d --build
```

Wait 30 seconds for startup.

### Step 2: Test Registration (No Auth Required)

**Endpoint:** `POST http://localhost:8080/api/users/register`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "firstName": "Aymen",
    "lastName": "jebrane",
    "email": "aymen@gmail.com",
    "password": "password123",
    "role": "ADMIN"
}
```

**Expected Response (HTTP 201 Created):**
```json
{
    "idUser": 5,
    "firstName": "Aymen",
    "lastName": "jebrane",
    "email": "aymen@gmail.com",
    "role": "ADMIN",
    "enabled": true
}
```

### Step 3: Test Admin Endpoint (Requires Auth)

If you want to use the admin endpoint `POST http://localhost:8080/api/users`, you need to:

1. **Login first:**
   ```
   POST http://localhost:8080/api/auth/login
   
   Body:
   {
       "email": "admin@test.com",
       "password": "0000"
   }
   ```

2. **Copy the accessToken from the response**

3. **Use it in the Authorization header:**
   ```
   POST http://localhost:8080/api/users
   Authorization: Bearer <your-access-token-here>
   Content-Type: application/json
   
   Body:
   {
       "firstName": "Test",
       "lastName": "User",
       "email": "test@test.com",
       "password": "0000",
       "role": "CHEF_PRODUCTION"
   }
   ```

## Summary of User Endpoints

| Endpoint | Method | Auth Required | Role Required | Purpose |
|----------|--------|---------------|---------------|---------|
| `/api/users/register` | POST | ❌ No | None | Public user registration |
| `/api/users` | POST | ✅ Yes | ADMIN | Admin creates user |
| `/api/users` | GET | ✅ Yes | Multiple roles | Get all users |
| `/api/users/{id}` | GET | ✅ Yes | Multiple roles | Get user by ID |
| `/api/users/email?email=xxx` | GET | ✅ Yes | Multiple roles | Get user by email |
| `/api/users/{id}` | PUT | ✅ Yes | Multiple roles | Update user |
| `/api/users/{id}` | DELETE | ✅ Yes | ADMIN | Delete user |

## Quick Fix Checklist

✅ Changed Postman URL to `/api/users/register`
✅ Fixed SecurityConfig to properly distinguish public vs protected endpoints
✅ No authentication header needed for registration
✅ Use `Content-Type: application/json` header

## Common Mistakes

### ❌ Mistake 1: Using wrong URL
```
POST http://localhost:8080/api/users  ← This needs authentication!
```

### ✅ Correct:
```
POST http://localhost:8080/api/users/register  ← Public endpoint
```

### ❌ Mistake 2: Adding Bearer token for registration
You don't need any authorization header for `/api/users/register`

### ✅ Correct:
Just send the JSON body without any authentication

## Testing in Postman

1. **Select:** POST method
2. **URL:** `http://localhost:8080/api/users/register`
3. **Headers tab:**
   - Add: `Content-Type: application/json`
4. **Body tab:**
   - Select: raw
   - Select: JSON
   - Paste your JSON data
5. **Click:** Send

You should get **201 Created** response with the user data!

## If Still Getting 400/401 Error

1. **Check application is running:**
   ```powershell
   docker-compose ps
   ```

2. **Check application logs:**
   ```powershell
   docker-compose logs app | Select-String "Started SupplyChainXApplication"
   ```

3. **Verify the exact URL** - make sure it's:
   ```
   http://localhost:8080/api/users/register
   ```
   NOT:
   ```
   http://localhost:8080/api/users
   ```

4. **Check Content-Type header** is set to `application/json`

5. **Verify JSON body is valid** - use a JSON validator

## Next Steps

After successful registration:
1. ✅ Register your user
2. ✅ Login to get JWT token
3. ✅ Use token to access protected endpoints

Enjoy! 🎉

