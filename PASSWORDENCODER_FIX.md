# Fix for PasswordEncoder Bean Error

## Problem Solved

**Error Message:**
```
Parameter 2 of constructor in org.example.supplychainx.Service.UserService required a bean of type 
'org.springframework.security.crypto.password.PasswordEncoder' that could not be found.
```

## Root Cause

The issue was caused by a **circular dependency** in Spring's bean initialization:

1. `SecurityConfig` used `@AllArgsConstructor` for constructor injection
2. `SecurityConfig` needs `CustomUserDetailsService` and `JwtAuthenticationFilter`
3. `UserService` needs `PasswordEncoder` (defined in `SecurityConfig`)
4. Spring couldn't determine the proper initialization order, causing the `PasswordEncoder` bean to not be available when `UserService` was being created

## Solution Applied

### 1. Created Separate PasswordEncoder Configuration

Created a new file: `PasswordEncoderConfig.java`
- This ensures the `PasswordEncoder` bean is created early in the Spring context
- Separates security configuration from bean definitions
- Eliminates circular dependency issues

```java
@Configuration
public class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 2. Fixed SecurityConfig Constructor Injection

Modified `SecurityConfig.java`:
- Removed `@AllArgsConstructor` annotation
- Added explicit constructor with `@Lazy` injection
- This breaks the circular dependency by deferring bean initialization

```java
@Autowired
public SecurityConfig(@Lazy CustomUserDetailsService userDetailsService, 
                     @Lazy JwtAuthenticationFilter jwtAuthFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthFilter = jwtAuthFilter;
}
```

### 3. Removed Duplicate PasswordEncoder Bean

- Removed the `passwordEncoder()` method from `SecurityConfig`
- Now only defined in `PasswordEncoderConfig`

## How to Start the Application

### Option 1: Using Docker Compose (Recommended)

1. **Rebuild the application image:**
   ```powershell
   docker-compose build app
   ```

2. **Start all services:**
   ```powershell
   docker-compose up -d
   ```

3. **Check logs to verify startup:**
   ```powershell
   docker-compose logs -f app
   ```

   Look for these success messages:
   - ✅ `Started SupplyChainXApplication`
   - ✅ `🔍 DataInitializer is starting...`
   - ✅ `Admin user created: admin@test.com / 0000`
   - ✅ `🚀 Database initialization complete! Total users: 4`

4. **Verify the application is running:**
   ```powershell
   curl http://localhost:8080/api/auth/health
   ```
   or visit http://localhost:8080/swagger-ui.html

### Option 2: Running Locally with Maven

1. **Start only the database:**
   ```powershell
   docker-compose up -d db phpmyadmin
   ```

2. **Build and run with Maven wrapper:**
   ```powershell
   .\mvnw.cmd clean spring-boot:run
   ```

3. **Watch console output for DataInitializer messages**

## Verify DataInitializer Worked

### Method 1: Check Database via phpMyAdmin
1. Open http://localhost:8089
2. Login: `root` / `root`
3. Select `supplyChainX` database
4. Open `users` table
5. You should see 4 users:
   - admin@test.com
   - gestionnaire@test.com
   - production@test.com
   - logistique@test.com

### Method 2: Using MySQL Command
```powershell
docker exec -it supplychainx-db mysql -uroot -proot -e "USE supplyChainX; SELECT email, role FROM users;"
```

### Method 3: Use the PowerShell script
```powershell
.\check-database.ps1
```

## Test the Register Endpoint

Once the application is running, test registration in Postman:

**Request:**
```
POST http://localhost:8080/api/users/register
Content-Type: application/json

{
    "firstName": "Aymen",
    "lastName": "jebrane",
    "email": "aymen@gmail.com",
    "password": "0000",
    "role": "ADMIN"
}
```

**Expected Response (Success):**
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

## Common Issues After Fix

### Issue: "Cannot connect to database"
**Solution:** Make sure the database container is running and healthy
```powershell
docker-compose ps db
```

### Issue: Still getting PasswordEncoder error
**Solution:** 
1. Clean and rebuild: `docker-compose down -v`
2. Remove old images: `docker rmi supplychainx:latest`
3. Rebuild: `docker-compose build app`
4. Start: `docker-compose up -d`

### Issue: DataInitializer doesn't run
**Check:**
1. Application logs: `docker-compose logs app | Select-String "DataInitializer"`
2. Database connection: `docker exec supplychainx-db mysqladmin ping -uroot -proot`

## Testing Authentication Flow

After the fix, you can:

1. **Register a new user:**
   ```
   POST /api/users/register
   ```

2. **Login to get JWT token:**
   ```
   POST /api/auth/login
   {
       "email": "admin@test.com",
       "password": "0000"
   }
   ```

3. **Use the token to access protected endpoints:**
   ```
   GET /api/users
   Authorization: Bearer <your-jwt-token>
   ```

## Files Modified

1. ✅ `SecurityConfig.java` - Fixed circular dependency
2. ✅ `PasswordEncoderConfig.java` - NEW file for PasswordEncoder bean
3. ✅ `DataInitializer.java` - Previously fixed with @Transactional and saveAndFlush()

## Next Steps

1. ✅ Start the application using docker-compose
2. ✅ Verify DataInitializer populated the database
3. ✅ Test registration endpoint in Postman
4. ✅ Test login endpoint to get JWT token
5. ✅ Test protected endpoints with the JWT token

## Support

If you still encounter issues:
1. Check application logs: `docker-compose logs app`
2. Check database logs: `docker-compose logs db`
3. Verify all containers are healthy: `docker-compose ps`
4. Try a complete reset: `docker-compose down -v && docker-compose up -d --build`

