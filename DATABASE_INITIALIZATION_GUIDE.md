# Database Initialization Guide

## Problem: DataInitializer Not Populating Database

The DataInitializer has been fixed with the following improvements:

### Changes Made:

1. **Added @Transactional annotation to the run() method**
   - This ensures that all database operations are committed in a single transaction
   - Previously, the transaction might not have been committed properly

2. **Changed `save()` to `saveAndFlush()`**
   - This forces immediate write to the database instead of waiting for transaction commit
   - Ensures data is persisted before the method completes

3. **Enhanced error logging**
   - Better visibility of any issues during initialization

## How to Start the Application and Initialize Data

### Option 1: Using Docker Compose (Recommended)

1. **Start the database first:**
   ```powershell
   docker-compose up -d db
   ```

2. **Wait for database to be healthy (about 10-15 seconds)**
   ```powershell
   docker-compose ps
   ```
   Look for "healthy" status for the db container

3. **Build and start the application:**
   ```powershell
   docker-compose up -d app
   ```

4. **Check logs to see if DataInitializer ran:**
   ```powershell
   docker-compose logs app | Select-String "DataInitializer"
   ```

### Option 2: Running Locally (Without Docker for the app)

1. **Start only the database with Docker:**
   ```powershell
   docker-compose up -d db phpmyadmin
   ```

2. **Build the application:**
   ```powershell
   mvn clean package -DskipTests
   ```

3. **Run the application locally:**
   ```powershell
   mvn spring-boot:run
   ```
   OR
   ```powershell
   java -jar target/SupplyChainX-0.0.1-SNAPSHOT.jar
   ```

4. **Look for DataInitializer output in console:**
   You should see:
   ```
   ================================================================================
   🔍 DataInitializer is starting...
   🔍 Database connection test: Starting data initialization
   Current user count in database: 0
   ✅ Admin user created: admin@test.com / 0000
   ✅ Gestionnaire user created: gestionnaire@test.com / 0000
   ✅ Production user created: production@test.com / 0000
   ✅ Logistique user created: logistique@test.com / 0000
   🚀 Database initialization complete! Total users: 4
   ================================================================================
   ```

## Verifying Data in Database

### Using phpMyAdmin:
1. Open browser: http://localhost:8089
2. Login with:
   - Server: db
   - Username: root
   - Password: root
3. Select `supplyChainX` database
4. Check the `users` table

### Using MySQL Command Line:
```powershell
docker exec -it db mysql -uroot -proot -e "USE supplyChainX; SELECT email, firstName, lastName, role FROM users;"
```

## Default Users Created

| Email | Password | Role |
|-------|----------|------|
| admin@test.com | 0000 | ADMIN |
| gestionnaire@test.com | 0000 | GESTIONNAIRE_APPROVISIONNEMENT |
| production@test.com | 0000 | CHEF_PRODUCTION |
| logistique@test.com | 0000 | RESPONSABLE_LOGISTIQUE |

## Troubleshooting

### Issue: Database is empty after startup

**Check 1: Is the application actually running?**
```powershell
netstat -ano | findstr :8080
```

**Check 2: Is the database running?**
```powershell
docker ps | findstr db
```

**Check 3: Check application logs for errors**
```powershell
docker-compose logs app
```

**Check 4: Verify database connection**
- Ensure MySQL/MariaDB is running on port 3306
- Test connection: `docker exec -it db mysql -uroot -proot -e "SHOW DATABASES;"`

### Issue: DataInitializer runs but users are not persisted

This was the original problem. The fix:
- Added `@Transactional` annotation to ensure transaction is committed
- Changed `save()` to `saveAndFlush()` to force immediate database write

### Issue: Database connection refused

**Solution:**
1. Make sure the db container is fully started and healthy
2. Wait 15-20 seconds after `docker-compose up -d db`
3. Check health: `docker-compose ps`

### Issue: "Table 'users' doesn't exist"

**Solution:**
1. Check `spring.jpa.hibernate.ddl-auto` is set to `update` in application.properties
2. Restart the application to create tables
3. Or manually create tables using schema DDL

## Force Re-initialization

If you want to clear the database and re-run the DataInitializer:

### Method 1: Drop and recreate database
```powershell
docker exec -it db mysql -uroot -proot -e "DROP DATABASE IF EXISTS supplyChainX; CREATE DATABASE supplyChainX;"
docker-compose restart app
```

### Method 2: Clear only users table
```powershell
docker exec -it db mysql -uroot -proot -e "USE supplyChainX; TRUNCATE TABLE users;"
docker-compose restart app
```

### Method 3: Complete reset (nuclear option)
```powershell
docker-compose down -v
docker-compose up -d
```

## Additional Configuration

### For Production:
- Change `spring.jpa.hibernate.ddl-auto` from `update` to `validate`
- Use proper database credentials (not root/root)
- Disable or remove DataInitializer or add conditional logic:
  ```java
  @Profile("dev")
  public class DataInitializer implements CommandLineRunner {
  ```

### For Development:
- Keep current settings
- DataInitializer checks if users exist before creating them
- Safe to run multiple times

## Next Steps

1. Start the database: `docker-compose up -d db`
2. Wait 15 seconds for database to initialize
3. Start the application: `docker-compose up -d app` or `mvn spring-boot:run`
4. Verify users in phpMyAdmin: http://localhost:8089
5. Test login with admin@test.com / 0000

## Support

If issues persist:
1. Check all logs: `docker-compose logs`
2. Verify database schema: Check if `users` table exists
3. Manually test database connection
4. Review application startup logs for exceptions

