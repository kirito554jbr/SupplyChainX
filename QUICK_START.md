# 🚀 Quick Start - SupplyChainX

## ✅ All Issues Fixed!

1. ✅ DataInitializer now populates database correctly
2. ✅ PasswordEncoder bean circular dependency resolved
3. ✅ Application starts successfully

---

## 🏃 Start Application (Choose One)

### Option A: Full Docker Stack
```powershell
docker-compose up -d --build
```

### Option B: Database Only + Local App
```powershell
# Terminal 1: Start database
docker-compose up -d db phpmyadmin

# Terminal 2: Start app
.\mvnw.cmd spring-boot:run
```

---

## 🔍 Verify Everything Works

### 1. Check Application is Running
```powershell
curl http://localhost:8080/swagger-ui.html
# or visit in browser
```

### 2. Check Database Has Users
```powershell
.\check-database.ps1
```

### 3. Test in Postman

**Register New User:**
```
POST http://localhost:8080/api/users/register
{
    "firstName": "Aymen",
    "lastName": "jebrane",
    "email": "aymen@gmail.com",
    "password": "0000",
    "role": "ADMIN"
}
```

**Login:**
```
POST http://localhost:8080/api/auth/login
{
    "email": "admin@test.com",
    "password": "0000"
}
```

---

## 📦 Default Test Users

| Email | Password | Role |
|-------|----------|------|
| admin@test.com | 0000 | ADMIN |
| gestionnaire@test.com | 0000 | GESTIONNAIRE_APPROVISIONNEMENT |
| production@test.com | 0000 | CHEF_PRODUCTION |
| logistique@test.com | 0000 | RESPONSABLE_LOGISTIQUE |

---

## 🔧 Useful Commands

```powershell
# View application logs
docker-compose logs -f app

# View database content
docker exec supplychainx-db mysql -uroot -proot -e "USE supplyChainX; SELECT * FROM users;"

# Restart everything
docker-compose restart

# Complete reset (WARNING: deletes all data)
docker-compose down -v
docker-compose up -d --build

# Check container status
docker-compose ps
```

---

## 🌐 Access URLs

- **Application:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **phpMyAdmin:** http://localhost:8089 (root/root)
- **API Docs:** http://localhost:8080/v3/api-docs

---

## 📚 Documentation Files

- `FIXES_SUMMARY.md` - Complete summary of all fixes
- `PASSWORDENCODER_FIX.md` - PasswordEncoder circular dependency fix details
- `DATABASE_INITIALIZATION_GUIDE.md` - DataInitializer troubleshooting guide
- `start-app.ps1` - Automated startup script
- `check-database.ps1` - Database verification script

---

## ⚡ One-Line Complete Setup

```powershell
docker-compose down -v; docker-compose up -d --build; Start-Sleep 30; docker-compose logs app | Select-String "DataInitializer"
```

---

## 🆘 Still Having Issues?

1. ✅ Is Docker running? → `docker ps`
2. ✅ Are all containers healthy? → `docker-compose ps`
3. ✅ Check logs → `docker-compose logs app`
4. ✅ Try complete reset → `docker-compose down -v && docker-compose up -d --build`

---

## 🎯 What Was Fixed

**Problem 1:** DataInitializer didn't populate database
**Solution:** Added `@Transactional` + `saveAndFlush()`

**Problem 2:** PasswordEncoder bean not found
**Solution:** Created separate `PasswordEncoderConfig` + `@Lazy` injection

**Problem 3:** Postman returns "socket hang up"
**Solution:** Now application starts successfully, endpoint responds correctly

---

**Status: ✅ Ready to use!**

