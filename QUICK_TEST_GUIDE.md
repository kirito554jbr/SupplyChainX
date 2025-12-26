# Quick Test Script for Authentication

## Prerequisites
1. MySQL database is running (via Docker or locally)
2. Application is built: `.\mvnw.cmd clean install -DskipTests`

## Step 1: Start the Application
```powershell
.\mvnw.cmd spring-boot:run
```

**Wait for this log message:**
```
✅ Admin user created: admin@test.com / 0000
✅ Gestionnaire user created: gestionnaire@test.com / 0000
✅ Production user created: production@test.com / 0000
✅ Logistique user created: logistique@test.com / 0000
🚀 Database initialization complete!
Started SupplyChainXApplication in X.XXX seconds
```

## Step 2: Test Login in Postman

### Login Request
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

Body:
{
  "email": "admin@test.com",
  "password": "0000"
}
```

### Expected Success Response (200 OK)
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB0ZXN0LmNvbSIsImlhdCI6MTcwMzM0NTY3OCwiZXhwIjoxNzAzMzQ2NTc4fQ...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB0ZXN0LmNvbSIsImlhdCI6MTcwMzM0NTY3OCwiZXhwIjoxNzAzOTUwNDc4fQ...",
  "expiresAt": 1703346578000,
  "user": {
    "id": 1,
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@test.com",
    "role": "ADMIN"
  }
}
```

## Step 3: Test Register in Postman

### Register Request (No Authorization Header!)
```
POST http://localhost:8080/api/users/register
Content-Type: application/json

Body:
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@test.com",
  "password": "mypassword",
  "role": "GESTIONNAIRE_APPROVISIONNEMENT"
}
```

### Expected Success Response (201 Created)
```json
{
  "idUser": 5,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@test.com",
  "role": "GESTIONNAIRE_APPROVISIONNEMENT"
}
```

## Step 4: Test Protected Endpoint

### Get All Users Request
```
GET http://localhost:8080/api/users
Authorization: Bearer <paste_your_accessToken_here>
```

### Expected Success Response (200 OK)
```json
[
  {
    "idUser": 1,
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@test.com",
    "role": "ADMIN"
  },
  {
    "idUser": 2,
    "firstName": "Gestionnaire",
    "lastName": "Appro",
    "email": "gestionnaire@test.com",
    "role": "GESTIONNAIRE_APPROVISIONNEMENT"
  },
  ...
]
```

## Common Errors and Solutions

### Error: 401 Unauthorized on Login
**Cause:** User doesn't exist in database or password is wrong

**Solution:**
1. Check if DataInitializer ran (look for ✅ messages in console)
2. Verify MySQL is running: `docker ps | Select-String "supplychainx-db"`
3. Check application.properties has correct database URL
4. Restart application

### Error: 401 Unauthorized on Register
**Cause:** You're sending an Authorization header or endpoint is blocked

**Solution:**
1. **Remove Authorization header** from register request in Postman
2. Make sure you're hitting `POST http://localhost:8080/api/users/register`
3. Check SecurityConfig permits `/api/users/register`

### Error: "User not found" or "Invalid credentials"
**Cause:** Database doesn't have test users

**Solution:**
1. Stop the application (Ctrl+C)
2. Restart it with `.\mvnw.cmd spring-boot:run`
3. DataInitializer should create users automatically
4. If users already exist, you'll see no ✅ messages (that's OK)

### Error: Connection refused or Database error
**Cause:** MySQL is not running

**Solution:**
```powershell
# Start Docker containers
docker compose up -d

# Verify MySQL is running
docker ps

# Check MySQL logs
docker logs supplychainx-db
```

### Error: Port 8080 already in use
**Cause:** Another application or previous instance is using port 8080

**Solution:**
```powershell
# Find process using port 8080
Get-NetTCPConnection -LocalPort 8080 | Select-Object -Property OwningProcess

# Kill the process (replace <PID> with actual process ID)
Stop-Process -Id <PID> -Force
```

## Troubleshooting Checklist

- [ ] MySQL is running (`docker ps`)
- [ ] Application built successfully (`.\mvnw.cmd clean install -DskipTests`)
- [ ] Application started without errors
- [ ] DataInitializer created test users (check console logs)
- [ ] No Authorization header on /register request
- [ ] Correct email and password (admin@test.com / 0000)
- [ ] Using POST method for login and register
- [ ] Content-Type is application/json

## Testing with cURL (Alternative to Postman)

### Login
```powershell
curl -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{"email":"admin@test.com","password":"0000"}'
```

### Register
```powershell
curl -X POST http://localhost:8080/api/users/register `
  -H "Content-Type: application/json" `
  -d '{"firstName":"Test","lastName":"User","email":"test@example.com","password":"password123","role":"GESTIONNAIRE_APPROVISIONNEMENT"}'
```

### Get Users (with token)
```powershell
$token = "YOUR_ACCESS_TOKEN_HERE"
curl -X GET http://localhost:8080/api/users `
  -H "Authorization: Bearer $token"
```

## If All Else Fails

1. **Check application logs thoroughly**
2. **Verify database connectivity:**
   ```powershell
   # Connect to MySQL container
   docker exec -it supplychainx-db mysql -uroot -proot supplyChainX
   
   # In MySQL:
   SELECT * FROM users;
   ```

3. **Check SecurityConfig.java** - Verify `/api/auth/**` and `/api/users/register` are in permitAll()

4. **Check JwtAuthenticationFilter.java** - Verify it allows requests without Bearer token through

5. **Restart everything:**
   ```powershell
   # Stop application (Ctrl+C)
   # Stop Docker
   docker compose down
   # Start Docker
   docker compose up -d
   # Start application
   .\mvnw.cmd spring-boot:run
   ```

