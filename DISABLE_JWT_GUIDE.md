# JWT Authentication - Disable/Enable Guide

## ⚠️ Current Status: JWT AUTHENTICATION IS DISABLED

This guide explains what has been disabled for Angular frontend development and how to re-enable it for production.

---

## What Was Changed to Disable JWT Authentication

### 1. **SecurityConfig.java** - Main Security Configuration
**Location:** `src/main/java/org/example/supplychainx/Config/SecurityConfig.java`

**Changes Made:**
- ✅ Commented out `JwtAuthenticationFilter` dependency injection
- ✅ Replaced all role-based authorization rules with `.anyRequest().permitAll()`
- ✅ Disabled JWT filter from the security chain
- ✅ Original security configuration preserved in comments

**Current Behavior:**
- All endpoints are publicly accessible
- No authentication required
- No authorization checks
- CORS is still enabled for Angular (localhost:4200)

---

### 2. **AuthorizationAspect.java** - Role-Based Authorization
**Location:** `src/main/java/org/example/supplychainx/aspect/AuthorizationAspect.java`

**Changes Made:**
- ✅ Commented out `@Aspect` and `@Component` annotations
- ✅ Authorization check method now immediately returns without checking roles
- ✅ Original authorization logic preserved in comments

**Current Behavior:**
- `@RequiresRole` annotations are ignored
- No role validation performed
- All users can access all endpoints

---

### 3. **What Was NOT Changed**
The following components remain active and functional:
- ✅ **UserContextInterceptor** - Still active but not enforcing anything
- ✅ **JwtService** - Still available for when you re-enable JWT
- ✅ **AuthController** - Login/register endpoints still work (but JWT tokens are not required)
- ✅ **Database initialization** - DataInitializer still runs
- ✅ **PasswordEncoder bean** - Still available for password hashing

---

## How to Develop Angular Frontend (Current State)

### API Endpoints Are Now Public
You can call any endpoint without authentication:

```typescript
// Angular Service Example
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // No authentication headers needed!
  getUsers() {
    return this.http.get(`${this.baseUrl}/users`);
  }

  getRawMaterials() {
    return this.http.get(`${this.baseUrl}/rawMaterials`);
  }

  getSuppliers() {
    return this.http.get(`${this.baseUrl}/suppliers`);
  }

  // All CRUD operations work without authentication
  createProduct(product: any) {
    return this.http.post(`${this.baseUrl}/products`, product);
  }
}
```

### Available API Endpoints (All Public Now)

#### User Management
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

#### Raw Materials
- `GET /api/rawMaterials` - Get all raw materials
- `GET /api/rawMaterials/{id}` - Get raw material by ID
- `POST /api/rawMaterials` - Create raw material
- `PUT /api/rawMaterials/{id}` - Update raw material
- `DELETE /api/rawMaterials/{id}` - Delete raw material
- `GET /api/rawMaterials/filter/low-stock` - Get low stock items

#### Suppliers
- `GET /api/suppliers` - Get all suppliers
- `GET /api/suppliers/{id}` - Get supplier by ID
- `POST /api/suppliers` - Create supplier
- `PUT /api/suppliers/{id}` - Update supplier
- `DELETE /api/suppliers/{id}` - Delete supplier

#### Supply Orders
- `GET /api/supply-orders` - Get all supply orders
- `GET /api/supply-orders/{id}` - Get supply order by ID
- `POST /api/supply-orders` - Create supply order
- `PUT /api/supply-orders/{id}` - Update supply order
- `DELETE /api/supply-orders/{id}` - Delete supply order

#### Customers
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `GET /api/customers/by-name/{name}` - Get customer by name
- `POST /api/customers` - Create customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

#### Orders
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create order
- `PUT /api/orders/{id}` - Update order
- `DELETE /api/orders/{id}` - Delete order

#### Deliveries
- `GET /api/deliveries` - Get all deliveries
- `GET /api/deliveries/{id}` - Get delivery by ID
- `POST /api/deliveries` - Create delivery
- `PUT /api/deliveries/{id}` - Update delivery
- `DELETE /api/deliveries/{id}` - Delete delivery

#### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

#### Bill of Materials (BOM)
- `GET /api/boms` - Get all BOMs
- `GET /api/boms/{id}` - Get BOM by ID
- `POST /api/boms` - Create BOM
- `PUT /api/boms/{id}` - Update BOM
- `DELETE /api/boms/{id}` - Delete BOM

#### Production Orders
- `GET /api/productionOrders` - Get all production orders
- `GET /api/productionOrders/{id}` - Get production order by ID
- `POST /api/productionOrders` - Create production order
- `PUT /api/productionOrders/{id}` - Update production order
- `DELETE /api/productionOrders/{id}` - Delete production order

---

## How to Re-Enable JWT Authentication (For Production)

### Step 1: Restore SecurityConfig.java

1. Open `src/main/java/org/example/supplychainx/Config/SecurityConfig.java`
2. Delete the temporary security configuration (lines ~34-48)
3. Uncomment the large commented block that contains the original security rules
4. Uncomment the JWT filter dependency injection in the constructor
5. The file should look like the original with all role-based rules

**Quick Changes:**
```java
// Change this:
public SecurityConfig(@Lazy CustomUserDetailsService userDetailsService
                     /* @Lazy JwtAuthenticationFilter jwtAuthFilter */) {

// Back to this:
public SecurityConfig(@Lazy CustomUserDetailsService userDetailsService,
                     @Lazy JwtAuthenticationFilter jwtAuthFilter) {
```

### Step 2: Restore AuthorizationAspect.java

1. Open `src/main/java/org/example/supplychainx/aspect/AuthorizationAspect.java`
2. Uncomment `@Aspect` and `@Component` annotations
3. Delete the early return statement in `checkAuthorization` method
4. Uncomment the original authorization logic

**Quick Changes:**
```java
// Change this:
// @Aspect
// @Component
@Slf4j
public class AuthorizationAspect {

// Back to this:
@Aspect
@Component
@Slf4j
public class AuthorizationAspect {
```

### Step 3: Update Angular Frontend

Once JWT is re-enabled, your Angular app will need to:

1. **Login to get JWT token:**
```typescript
login(email: string, password: string) {
  return this.http.post<{accessToken: string, refreshToken: string}>(
    `${this.baseUrl}/auth/login`,
    { email, password }
  );
}
```

2. **Store JWT token:**
```typescript
localStorage.setItem('accessToken', response.accessToken);
localStorage.setItem('refreshToken', response.refreshToken);
```

3. **Add JWT token to all requests:**
```typescript
// Use HttpInterceptor
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';

export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = localStorage.getItem('accessToken');
    
    if (token) {
      const cloned = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(cloned);
    }
    
    return next.handle(req);
  }
}
```

### Step 4: Test Authentication

1. Restart your Spring Boot application
2. Try to access `/api/users` without authentication - should get 401 Unauthorized
3. Login via `/api/auth/login` with credentials
4. Use the returned JWT token in Authorization header
5. Access protected endpoints - should work with valid token

---

## User Roles in the System

When JWT is re-enabled, these roles control access:

- **ADMIN** - Full system access
- **GESTIONNAIRE_APPROVISIONNEMENT** - Supply management
- **RESPONSABLE_ACHATS** - Purchasing management
- **SUPERVISEUR_LOGISTIQUE** - Logistics supervision
- **CHEF_PRODUCTION** - Production management
- **PLANIFICATEUR** - Production planning
- **SUPERVISEUR_PRODUCTION** - Production supervision
- **GESTIONNAIRE_COMMERCIAL** - Sales management
- **RESPONSABLE_LOGISTIQUE** - Logistics management
- **SUPERVISEUR_LIVRAISONS** - Delivery supervision

---

## Testing with Postman (Current State - No Auth)

You can test all endpoints directly without any authentication:

```bash
# Get all users
GET http://localhost:8080/api/users

# Create a raw material
POST http://localhost:8080/api/rawMaterials
Content-Type: application/json

{
  "name": "Steel",
  "quantity": 100,
  "unit": "kg",
  "minThreshold": 20
}
```

---

## Troubleshooting

### If endpoints return 401 Unauthorized:
- JWT authentication might not be fully disabled
- Check that SecurityConfig has `.anyRequest().permitAll()`
- Check that AuthorizationAspect has `@Aspect` and `@Component` commented out

### If CORS errors occur:
- CORS is configured for `http://localhost:4200`
- If using different port, update `application.properties`:
  ```properties
  spring.web.cors.allowed-origins=http://localhost:YOUR_PORT
  ```

### If database is empty:
- Check that DataInitializer is running
- Check database connection in `application.properties`
- Check logs for initialization errors

---

## Important Security Notes

⚠️ **NEVER deploy to production with JWT disabled!**

Before deploying:
1. ✅ Re-enable JWT authentication (follow Step 1 & 2 above)
2. ✅ Change `jwt.secret` in `application.properties` to a secure random value
3. ✅ Test all authentication flows
4. ✅ Verify role-based authorization works correctly
5. ✅ Update CORS origins to production domains only

---

## Summary

**What's Currently Disabled:**
- ✅ JWT token validation
- ✅ All authentication requirements
- ✅ All authorization/role checks
- ✅ Security filter chain enforcement

**What Still Works:**
- ✅ All API endpoints (publicly accessible)
- ✅ Database operations
- ✅ CORS for Angular
- ✅ Data validation
- ✅ Business logic

**When to Re-enable:**
- Before production deployment
- When frontend auth UI is ready
- When you want to test role-based access control

---

**Created:** January 7, 2026
**Purpose:** Angular Frontend Development
**Status:** JWT Authentication Disabled for Development

